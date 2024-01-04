const { bot } = require('./bot');
const { start } = require("./helper/start");
const { get_all_movies } = require("./helper/kinolar");
const { get_all_movie_for_subtitle } = require('./helper/subtitle');
const { get_all_serials } = require("./helper/serial");
const { editOnOf } = require("./helper/developer");

// Admin keyboard buttons
const adminKeyboard = [
    [
        {
            text: 'Kinolar'
        },
        {
            text: 'Subtitlelar'
        }
    ],
    [
        {
            text: 'Seriallar'
        }
    ],
    [
        {
            text: 'DeveloperModeEditor'
        }
    ]
];

async function clearChatBefore(chatId, messageId) {
    const options = {
        chat_id: chatId,
        message_id: messageId - 1, // Birortaga oldingi xabarlar
    };
    try {
        await bot.telegram.deleteMessage(chatId, messageId);
        await bot.telegram.deleteMessage(chatId, options.message_id);
    } catch (error) {
        console.error('Xabar o\'chirishda xato:', error);
    }
}

bot.on('message', async msg => {
    const chatId = msg.from.id;
    const text = msg.text;
    const messageId = msg.message_id;

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

    // Admin tugmalari bosilganda, xabarlar to'plamini o'chirib tashlash
    if (adminKeyboard.some(row => row.some(btn => btn.text === text))) {
        clearChatBefore(chatId, messageId);
    }
});

module.exports = { adminKeyboard };
