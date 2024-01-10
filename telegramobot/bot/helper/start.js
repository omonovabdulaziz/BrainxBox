const {bot} = require('../bot')

const start = async (chatId) => {
    if (process.env.ADMINCHATID == chatId || process.env.ADMINCHATID2 == chatId) {
        bot.sendMessage(chatId, 'Choose from menu Admin', {
            reply_markup: {
                keyboard: [[{
                    text: 'Kinolar'
                }, {
                    text: 'Subtitlelar'
                }], [{
                    text: 'Seriallar'
                }], [{
                    text: 'DeveloperModeEditor'
                }, {
                    text: 'Add Video'
                }]], resize_keyboard: true
            }
        })
    } else {
        console.log(chatId)
        await bot.sendMessage(chatId, 'You are not an admin. please do not log in')
    }
}


module.exports = {start}