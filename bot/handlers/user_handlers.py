import os
from pathlib import Path
from aiogram import Bot, Router
from aiogram.filters import CommandStart
from aiogram.types import Message, File
from lexicon.lexicon_RU import ANSWERS_LEXICON
from aiogram import F
from services.recognition_services import voice_to_text
from services.data_generation import data_generation
from services.server import post_ans
from wordToNum.extractor import NumberExtractor
from services.neural import get_neural_answer
from services.get_from_db import get_loaded
from datetime import datetime

main_router: Router = Router()

@main_router.message(CommandStart())
async def process_start_command(message: Message):
    await message.answer(text=ANSWERS_LEXICON['start'])


async def handle_file(file: File, file_name: str, path: str, bot: Bot):
    Path(f"{path}").mkdir(parents=True, exist_ok=True)

    await bot.download_file(file_path=file.file_path, destination=f"{path}/{file_name}")


@main_router.message(F.voice)
async def process_recognition_command(message: Message, bot: Bot):
    await message.reply('Чуть-чуть подождите...')
    voice_id = message.voice.file_id
    file = await bot.get_file(voice_id)

    path = "files/voices"

    await handle_file(file=file, file_name="voice.mp3", path=path, bot=bot)

    voice_to_text('files/voices/voice.mp3', 'files/voices/text.txt')
    text = ''.join(open('files/voices/text.txt', 'r').readlines())
    extractor = NumberExtractor()
    extext = extractor.replace_groups(text)
    data = data_generation(extext)
    print(text)
    os.remove('files/voices/text.txt')
    await message.answer("Нейросеть обрабатывает запрос...")
    date = datetime(year=data['year'], month=data['month'], day=data['day'])
    answer = 0.0
    if date.year >= 2024:
        if date <= datetime.now():
            answer = get_loaded(data['station'], str(data['month']) + '/' + \
                                str(data['day']) + '/' + str(data['year']))
        #post_ans(data)
        #neural_ans = get_neural_answer(data)
        if data['station'] == '':
            await message.answer('Пожалуйста, повторите запрос!' + '\n' + 'You said: ' + text)
        else:
            await message.answer(f"""Дата: {data['year']}-{data['month'] // 10}{data['month'] % 10}-{data['day'] // 10}{data['day'] % 10}
Станция: {data['station']}\n""" + f'Загруженность: {answer}' +'\n\n' + 'Текст: ' + text)
    else:
        await message.answer('Некорректные данные!')
@main_router.message()
async def process_message_command(message: Message):
    text = message.text
    extractor = NumberExtractor()
    text = extractor.replace_groups(text)
    data = data_generation(text)
    await message.answer("Нейросеть обрабатывает запрос...")
    date = datetime(year=data['year'], month=data['month'], day=data['day'])
    answer = 0.0
    if date.year >= 2024:
        if date <= datetime.now():
            answer = get_loaded(data['station'], str(data['month']) + '/' + \
                                str(data['day']) + '/' + str(data['year']))
        #post_ans(data)
        #neural_ans = get_neural_answer(data)
        if data['station'] == '':
            await message.answer('Пожалуйста, повторите запрос!' + '\n' + 'You said: ' + text)
        else:
            await message.answer(f"""Дата: {data['year']}-{data['month'] // 10}{data['month'] % 10}-{data['day'] // 10}{data['day'] % 10}
Станция: {data['station']}\n""" + f'Загруженность: {answer}' +'\n\n' + 'Текст: ' + text)
    else:
        await message.answer('Некорректные данные!')

