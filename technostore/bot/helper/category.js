const {bot} = require('../bot')
const User = require('../../model/user')
const Category = require('../../model/category')

const get_all_categories = async (msg) => {
    const chatId = msg.from.id
    let user = await User.findOne({chatId}).lean()
    let categories = await Category.find().lean()
    let list = categories.map(category =>
        [
            {
                text: category.title,
                callback_data: `category_${category._id}`
            }
        ]
    )
    bot.sendMessage(chatId, `Kategoriyalar ro'yxati`, {
        reply_markup: {
            remove_keyboard: true,
            inline_keyboard: [
                ...list,
                [
                    {
                        text: `Ortga`,
                        callback_data: 'back_category'
                    },
                    {
                        text: 1,
                        callback_data: '0'
                    },
                    {
                        text: 'Keyingi',
                        callback_data: 'next_category'
                    }
                ],
                user.admin ? [
                    {
                        text: 'Yangi Kategoriya',
                        callback_data: 'add_category'
                    }
                ] : []
            ]
        }
    })
}



const add_category = async (chatId) => {
    let user = await User.findOne({chatId}).lean()
    if (user.admin) {
        await User.findByIdAndUpdate(user._id, {...user, action: 'add_category'}, {new: true})
        bot.sendMessage(chatId, 'Yangi kategoriya nomini kiriting')
    } else {
        bot.sendMessage(chatId, 'Sizga bunday sorov mumkin emas')
    }
}




const new_category = async (msg) => {
    const chatId = msg.from.id
    const text = msg.text
    const user = await User.findOne({chatId}).lean()
    if (user.admin && user.action === 'add_category') {
        let newCategory = new Category({title: text})
        await newCategory.save()
        await User.findOneAndUpdate(user._id, {
            ...user,
            action: 'category'
        })
        get_all_categories(msg)
    } else {
        bot.sendMessage(chatId, 'Siz admin emassiz')
    }
}




module.exports = {get_all_categories, add_category, new_category}