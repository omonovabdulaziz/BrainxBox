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
    try {
        const messageIdsToDelete = [];
        for (let i = 1; i < messageId; i++) {
            messageIdsToDelete.push(i);
        }
        await Promise.all(messageIdsToDelete.map(id => bot.telegram.deleteMessage(chatId, id)));
    } catch (error) {
        console.error('Error deleting messages:', error);
    }
}

bot.on('message', async msg => {
    const chatId = msg.chat.id;
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

    // Admin buttons pressed, clear previous messages
    if (adminKeyboard.some(row => row.some(btn => btn.text === text))) {
        clearChatBefore(chatId, messageId);
    }
});

module.exports = { adminKeyboard };
