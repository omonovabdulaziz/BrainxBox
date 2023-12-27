const {bot} = require('./bot')
const {
    pagination_movie, show_movie, delete_movie, edit_movie, add_movie, pageableforSerials
} = require('./helper/kinolar')
const {pagination_movie_for_subtitle, add_subtitle} = require("./helper/subtitle");
const {pagination_serial, show_serial, add_serial, edit_serial, delete_serial} = require("./helper/serial");

bot.on('callback_query', async query => {
    const chatId = query.from.id
    const {data} = query
    if (data.includes('edit_serial._')) {
        let id = data.split('._')[1]
        edit_serial(chatId, id)
    }
    if (data.includes('delete_serial._')) {
        let id = data.split('._')[1]
        delete_serial(chatId, id)
    }
    if (data.includes('serial-')) {
        let id = data.split('-')[1]
        show_serial(chatId, id)
    }
    if (data.includes('subtitle_movie-')) {
        let id = data.split('-')[1]
        add_subtitle(chatId, id)
    }
    if (data === 'add_movie') {
        add_movie(chatId)
    }
    if (data === 'next_page') {
        pagination_movie(chatId, 'next_page')
    }
    if (data === 'back_page') {
        pagination_movie(chatId, 'back_page')
    }
    if (data.includes('movie_')) {
        let id = data.split('_')[1]
        show_movie(chatId, id)
    }
    if (data.includes('edit_movie-')) {
        let id = data.split('-')[1]
        edit_movie(chatId, id)
    }
    if (data.includes('del_movie-')) {
        let id = data.split('-')[1]
        delete_movie(chatId, id)
    }
    if (data === 'back_page_for_subtitle') {
        pagination_movie_for_subtitle(chatId, 'back_page')
    }
    if (data === 'next_page_for_subtitle') {
        pagination_movie_for_subtitle(chatId, 'next_page')
    }
    if (data === 'back_serial_page') {
        pagination_serial(chatId, 'back_serial_page')
    }
    if (data === 'next_serial_page') {
        pagination_serial(chatId, 'next_serial_page')
    }
    if (data === 'add_serial') {
        add_serial(chatId)
    }
    if (data === 'back_serial_page_pagination') {
        pageableforSerials(chatId, 'back_serial_page_pagination')
    }
    if (data === 'next_serial_page_pagination') {
        pageableforSerials(chatId, 'next_serial_page_pagination')

    }

})