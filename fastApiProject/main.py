import re
from collections import Counter

import uvicorn
from deep_translator import GoogleTranslator
from fastapi import FastAPI, UploadFile, File
from fastapi.responses import JSONResponse
from langdetect import detect
from tqdm import tqdm

app = FastAPI()

EXCLUDED_WORDS = {"the", "to", "of", "you", "and", "this", "that's", "it's", "THE", "TO", "OF", "YOU", "AND", "THIS",
                  "THAT'S", "IT'S", "THAT", "that", "You", "The", "To", "Of", "And", "This", "That", "That's", "It's",
                  "It", "WHAT", "What", "what"}


def is_english_word(word):
    if word in EXCLUDED_WORDS:
        return False
    try:
        language = detect(word)
        if language in ['en', 'eng']:
            return True
        # 1% e'timol tekshirish qo'shilgan qismi
        total_chars = len(word)
        english_chars = sum(1 for char in word if char.isalpha() and char.isascii())
        english_ratio = english_chars / total_chars
        return english_ratio >= 0.01
    except:
        return False


def count_word_occurrences(text):
    characters_to_remove = r'[!?<>,-:->\d]'
    text_cleaned = re.sub(characters_to_remove, '', text)
    words = text_cleaned.split()
    words = [word for word in words if is_english_word(word)]  # Qo'shilgan qismi
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
        for word, count in data['word_count'].items():
            if word not in translated_words and is_english_word(word):
                entry = translate_word_threaded(word, count)
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
