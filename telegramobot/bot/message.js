const { bot } = require('./bot');
const { start } = require("./helper/start");
const { get_all_movies } = require("./helper/kinolar");
const { get_all_movie_for_subtitle } = require('./helper/subtitle');
const { get_all_serials } = require("./helper/serial");
const { editOnOf } = require("./helper/developer");

bot.on('message', async msg => {
    const chatId = msg.chat.id;
    const text = msg.text;

    const messages = await bot.getChatMessages(chatId);
    if (messages.length > 2) {
        for (let i = 0; i < messages.length - 2; i++) {
            await bot.deleteMessage(chatId, messages[i].message_id);
        }
    }

    if (text === '/start') {
        await start(chatId);
    }
    if (text === 'Kinolar') {
        await get_all_movies(chatId);
    }
    if (text === 'Subtitlelar') {
        await get_all_movie_for_subtitle(chatId);
    }
    if (text === 'Seriallar') {
        await get_all_serials(chatId);
    }
    if (text === 'DeveloperModeEditor') {
        await editOnOf(chatId);
    }
});
