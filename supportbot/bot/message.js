const {bot} = require('./bot');


bot.on('message', async msg => {
    const chatId = msg.chat.id;
    const text = msg.text;

    if (text === '/start') {
        await bot.sendMessage(chatId, 'Assalomu alaykum hurmatli ' + msg.chat.first_name + '. Takliflaringizni yuboring');
    } else {
        await bot.sendMessage(chatId, 'Taklifingiz uchun rahmat hurmatli ' + msg.chat.first_name + '. Biz tez orada taklifingizni ko\'rib chiqamiz😊')
        await bot.sendMessage('@' + 'whatthefuckiston', 'chatId = ' + msg.chat.id + '\n name = ' + msg.chat.first_name + '\n message = ' + text)
    }
})