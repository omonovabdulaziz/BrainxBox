import json
import re
from collections import Counter
from fastapi import FastAPI, UploadFile, Form, File
from fastapi.responses import JSONResponse
from deep_translator import GoogleTranslator
from tqdm import tqdm
from langdetect import detect
import concurrent.futures

app = FastAPI()


def is_english_word(word):
    try:
        language = detect(word)
        return language in ['en', 'eng']
    except:
        return False


def count_word_occurrences(text):
    characters_to_remove = r'[!?<>,-:->\d]'
    text_cleaned = re.sub(characters_to_remove, '', text)
    words = text_cleaned.split()
    word_count = Counter(words)
    return {"word_count": word_count}


def remove_non_english_words(data):
    word_count_dict = data.get("word_count", {})
    english_word_count = {word: count for word, count in word_count_dict.items() if is_english_word(word)}
    data["word_count"] = english_word_count
    return data


class TranslationEntry:
    def __init__(self, word, count, translation):
        self.word = word
        self.count = count
        self.translation = translation


def translate_word(word, count):
    translation = GoogleTranslator(source='en', target='uz').translate(word)
    return TranslationEntry(word, count, translation)


def translate_word_threaded(word, count):
    translation = GoogleTranslator(source='en', target='uz').translate(word)
    return TranslationEntry(word, count, translation)


def translate(data):
    print('Translating...')
    translated_entries = {}
    translated_words = set()

    with tqdm(total=len(data['word_count'])) as pbar:
        with concurrent.futures.ThreadPoolExecutor() as executor:
            futures = [executor.submit(translate_word_threaded, word, count) for word, count in
                       data['word_count'].items()]

            for future in concurrent.futures.as_completed(futures):
                entry = future.result()
                if entry.word not in translated_words:
                    translated_entries[entry.word] = entry
                    translated_words.add(entry.word)
                else:
                    # If the word is already translated, update the count
                    translated_entries[entry.word].count += entry.count
                pbar.update(1)

    return list(translated_entries.values())


def write_to_file(content, file_name):
    with open(f'{file_name}.json', 'w', encoding='utf-8') as json_file:
        json.dump(content, json_file, ensure_ascii=False, indent=4)


@app.post("/uploadSubtitle")
async def upload_subtitle(
        movie_id: int = Form(...),
        language_id: int = Form(...),
        subtitle_file: UploadFile = File(...),
):
    try:
        content = await subtitle_file.read()

        cleaned_text = content.decode('utf-8')

        word_count_data = count_word_occurrences(cleaned_text)
        english_word_count_data = remove_non_english_words(word_count_data)
        translated_data = translate(english_word_count_data)

        translated_data_dict = [entry.__dict__ for entry in translated_data]

        return JSONResponse(content={"result": translated_data_dict})

    except Exception as e:
        return JSONResponse(content={"message": f"Error processing file: {str(e)}"}, status_code=500)
