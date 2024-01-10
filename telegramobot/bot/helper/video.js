const axios = require('axios');
const {bot} = require("../bot");

async function getUserInput(chatId) {
    if (process.env.ADMINCHATID == chatId || process.env.ADMINCHATID2 == chatId) {
        await bot.sendMessage(chatId, "Iltimos, video linkini yuboring:");
        bot.once("text", async (msg) => {
            const link = msg.text;
            await bot.sendMessage(chatId, "Iltimos, rasm linkini yuboring:");
            bot.once("text", async (msg) => {
                const imageLink = msg.text;
                const video = {
                    link: link,
                    imageLink: imageLink,
                };
                await add_video(chatId, video);
            });
        });
    } else {
        await bot.sendMessage(chatId, 'Damingni ol')
    }
}

async function add_video(chatId, video) {
    try {
        const response = await axios.post(process.env.MAINAPI + '/api/v1/video/addVideo', video, {
            headers: {
                Authorization: `Bearer ${process.env.BEKENDTOKEN}`,
            },
        });
        await bot.sendMessage(chatId, response.status === 200 ? "Video qo'shildi" : "Xatolik yuz berdi");
    } catch (error) {
        console.error(error);
        await bot.sendMessage(chatId, "Xatolik yuz berdi");
    }
}

module.exports = {getUserInput};
