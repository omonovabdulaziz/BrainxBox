import re
from collections import Counter
import uvicorn
from fastapi import FastAPI, UploadFile, File
from fastapi.responses import JSONResponse
from deep_translator import GoogleTranslator
from tqdm import tqdm
from langdetect import detect
import concurrent.futures

app = FastAPI()

EXCLUDED_WORDS = {
    "the", "to", "of", "and", "in", "it", "is", "for", "on", "with",
    "that", "this", "at", "from", "by", "as", "an", "but", "or",
    "he", "she", "i", "me", "my", "mine", "you",
    "his", "her", "hers", "its", "we", "us", "our", "ours",
    "your", "yours", "their", "theirs",
}


def is_english_word(word):
    if word.lower() in {excluded_word.lower() for excluded_word in EXCLUDED_WORDS}:
        return False
    try:
        language = detect(word)
        return language in ['en', 'eng']
    except:
        return False


def count_word_occurrences(text):
    text_cleaned = re.sub(r"[^A-Za-z\s']", '', text)
    words = text_cleaned.split()
    word_count = Counter(words)
    return {"word_count": word_count}


def remove_non_english_words(data):
    word_count_dict = data.get("word_count", {})
    english_word_count = {word: count for word, count in word_count_dict.items() if is_english_word(word)}
    data["word_count"] = english_word_count
    return data


class TranslationEntry:
    def __init__(self, word, count, translation_en, translation_ru):
        self.word = word
        self.count = count
        self.translation_en = translation_en
        self.translation_ru = translation_ru


def translate_word_threaded(word, count):
    translation_en = GoogleTranslator(source='en', target='uz').translate(word)
    translation_ru = GoogleTranslator(source='en', target='ru').translate(word)
    return TranslationEntry(word, count, translation_en, translation_ru)


def translate(data):
    translated_entries = {}
    translated_words = set()

    with tqdm(total=len(data['word_count'])) as pbar:
        with concurrent.futures.ThreadPoolExecutor() as executor:
            futures = [executor.submit(translate_word_threaded, word, count) for word, count in
                       data['word_count'].items() if word not in translated_words]

            for future in concurrent.futures.as_completed(futures):
                entry = future.result()
                if entry.word not in translated_words:
                    if is_english_word(entry.word):
                        entry.word = entry.word.upper()
                    entry.translation_en = entry.translation_en[0].lower() + entry.translation_en[1:]
                    entry.translation_ru = entry.translation_ru[0].lower() + entry.translation_ru[1:]
                    translated_entries[entry.word] = entry
                    translated_words.add(entry.word)
                pbar.update(1)

    return list(translated_entries.values())


def try_different_encodings(file_content):
    encodings = ['utf-8', 'windows-1252', 'ISO-8859-1']
    for encoding in encodings:
        try:
            decoded_content = file_content.decode(encoding)
            cleaned_content = re.sub(r'<i>', '', decoded_content)  # Remove <i> tags
            return cleaned_content
        except UnicodeDecodeError:
            continue
    raise UnicodeDecodeError("Could not decode with any of the provided encodings.")


@app.post("/uploadSubtitle")
async def upload_subtitle(subtitle_file: UploadFile = File(...)):
    try:
        content = await subtitle_file.read()
        cleaned_text = try_different_encodings(content)

        word_count_data = count_word_occurrences(cleaned_text)
        english_word_count_data = remove_non_english_words(word_count_data)
        translated_data = translate(english_word_count_data)

        translated_data_dict = [entry.__dict__ for entry in translated_data]

        return JSONResponse(content={"result": translated_data_dict})

    except Exception as e:
        return JSONResponse(content={"message": f"Error processing file: {str(e)}"}, status_code=500)


if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
