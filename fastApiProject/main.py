import re
from collections import Counter
import uvicorn
from fastapi import FastAPI, UploadFile, File
from fastapi.responses import JSONResponse
from deep_translator import GoogleTranslator
from tqdm import tqdm
from langdetect import detect
import concurrent.futures
import html2text

app = FastAPI()

EXCLUDED_WORDS = {
    "the", "to", "of", "and", "in", "it", "is", "for", "on", "with",
    "that", "this", "at", "from", "by", "as", "an", "but", "or",
    "he", "she", "i", "me", "my", "mine", "you",
    "his", "her", "hers", "its", "we", "us", "our", "ours",
    "your", "yours", "their", "theirs",
}

translated_words = set()  # Qo'shilgan so'zlarni saqlash uchun to'plam


class TranslationEntry:
    def __init__(self, word, count, translation_en, translation_ru):
        self.word = word
        self.count = count
        self.translation_en = translation_en
        self.translation_ru = translation_ru


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


def translate_word_threaded(word, count):
    if word.lower() in translated_words:  # Qo'shilgan so'zlarni tekshirish
        return None
    translation_en = GoogleTranslator(source='en', target='uz').translate(word)
    translation_ru = GoogleTranslator(source='en', target='ru').translate(word)
    translated_words.add(word.lower())  # So'zni qo'shgan ekanini saqlash
    return TranslationEntry(word.upper(), count, translation_en.capitalize(), translation_ru.capitalize())


def translate(data):
    translated_entries = {}

    with tqdm(total=len(data['word_count'])) as pbar:
        with concurrent.futures.ThreadPoolExecutor() as executor:
            futures = [executor.submit(translate_word_threaded, word, count) for word, count in
                       data['word_count'].items()]

            for future in concurrent.futures.as_completed(futures):
                entry = future.result()
                if entry:
                    translated_entries[entry.word] = entry
                pbar.update(1)

    return list(translated_entries.values())


def try_different_encodings(file_content):
    encodings = ['utf-8', 'windows-1252', 'ISO-8859-1']
    for encoding in encodings:
        try:
            decoded_content = file_content.decode(encoding)
            cleaned_content = eliminate_patterns(decoded_content)
            return cleaned_content
        except UnicodeDecodeError:
            continue
    raise UnicodeDecodeError("Could not decode with any of the provided encodings.")


def eliminate_patterns(text):
    html_converter = html2text.HTML2Text()
    html_converter.ignore_links = True
    html_converter.ignore_images = True
    html_converter.ignore_emphasis = True

    cleaned_text = html_converter.handle(text)

    additional_patterns_to_eliminate = [
        r'\[', r'\]', r'\.',
        r'<i>', r'\.<i>', r'\,</i>', r'\.</i>', r'\,</i>', r'</i>', r'.</i>', r',</i>',
    ]

    additional_pattern = '|'.join(re.escape(p) for p in additional_patterns_to_eliminate)

    cleaned_text = re.sub(additional_pattern, '', cleaned_text)

    return cleaned_text


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
