const {bot} = require('./bot')
const {start, requestContact} = require('./helper/start')
const User = require('../model/user')
const {get_all_users} = require("./helper/user");
const {get_all_categories, new_category} = require("./helper/category");

bot.on('message', async msg => {
    const chatId = msg.from.id
    const text = msg.text
    const user = await User.findOne({chatId}).lean()
    if (text === '/start') {
        start(msg)
    }
    if (user) {
        if (user.action === 'request_contact' && !user.phone) {
            requestContact(msg)
        }
        if (text === 'Foydalanuvchilar') {
            get_all_users(msg)
        }
        if (text === 'Katalog') {
            get_all_categories(msg)
        }
        if (user.action === 'add_category') {
            new_category(msg)
        }
    }
})