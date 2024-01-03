import re
from collections import Counter
import uvicorn
from fastapi import FastAPI, UploadFile, File
from fastapi.responses import JSONResponse
from deep_translator import GoogleTranslator
from tqdm import tqdm
from langdetect import detect_langs
import concurrent.futures

app = FastAPI()

EXCLUDED_WORDS = {"the", "to", "of", "you", "and", "this", "that's", "it's", "THE", "TO", "OF", "YOU", "AND", "THIS",
                  "THAT'S", "IT'S", "THAT", "that", "You", "The", "To", "Of", "And", "This", "That", "That's", "It's",
                  "It"}

# Set a confidence threshold for language detection
CONFIDENCE_THRESHOLD = 0.9


def is_english_word(word):
    if word in EXCLUDED_WORDS:
        return False

    # Check if the word is exactly 3 letters long or occurs as an individual word
    if len(word) == 3 or word in text_cleaned.split():
        return False

    detected_languages = detect_langs(word)

    for lang_info in detected_languages:
        if lang_info.lang in ['en', 'eng'] and lang_info.prob >= CONFIDENCE_THRESHOLD:
            return True
    return False


def count_word_occurrences(text):
    # Remove all characters except the single quote ('), letters, and spaces
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
    print('Translating...')
    translated_entries = {}
    translated_words = set()

    with tqdm(total=len(data['word_count'])) as pbar:
        with concurrent.futures.ThreadPoolExecutor() as executor:
            futures = [executor.submit(translate_word_threaded, word, count) for word, count in
                       data['word_count'].items() if word not in translated_words]

            for future in concurrent.futures.as_completed(futures):
                entry = future.result()
                if entry.word not in translated_words:
                    # Check if the word is English (you can adjust the condition as needed)
                    if is_english_word(entry.word):
                        # Remove 'i' at the beginning and end if it's followed by uppercase letters
                        if re.match(r'^i[A-Z]+i$', entry.word):
                            entry.word = entry.word[1:-1]
                        # Convert the word to uppercase
                        entry.word = entry.word.upper()
                    # Convert the first letter of the translated word to lowercase
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
            return file_content.decode(encoding)
        except UnicodeDecodeError:
            continue
    raise UnicodeDecodeError("Could not decode with any of the provided encodings.")


@app.post("/uploadSubtitle")
async def upload_subtitle(
        subtitle_file: UploadFile = File(...),
):
    try:
        content = await subtitle_file.read()
        text_cleaned = try_different_encodings(content)

        word_count_data = count_word_occurrences(text_cleaned)
        english_word_count_data = remove_non_english_words(word_count_data)
        translated_data = translate(english_word_count_data)

        translated_data_dict = [entry.__dict__ for entry in translated_data]

        if not translated_data_dict:
            # Handle the case when no English words are found
            return JSONResponse(content={"message": "No English words found in the text."})

        return JSONResponse(content={"result": translated_data_dict})

    except Exception as e:
        return JSONResponse(content={"message": f"Error processing file: {str(e)}"}, status_code=500)


if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
