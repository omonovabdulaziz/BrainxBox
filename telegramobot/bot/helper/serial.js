const {bot} = require('../bot');
const axios = require('axios');

let pageCalc = 0;
let deleteCounter = ''

const add_serial = async (chatId) => {
    if (chatId == process.env.ADMINCHATID) {
        await bot.sendMessage(chatId, 'Yangi serial nomini kiriting :');
        bot.onText(/^(.*)$/, async (msg, match) => {
            const serialName = match[1];
            const apiUrl = process.env.MAINAPI + '/api/v1/serial/addSerial';
            const headers = {
                Authorization: `Bearer ${process.env.BEKENDTOKEN}`, 'Content-Type': 'application/json',
            };
            try {
                const response = await axios.post(apiUrl, {name: serialName}, {headers});
                bot.sendMessage(chatId, 'Yangi serial qo`shildi.');
                get_all_serials(chatId);
            } catch (error) {
                console.error('Error adding serial:', error.response.data);
                bot.sendMessage(chatId, 'Xatolik yuz berdi. Iltimos, keyinroq urinib ko`ring.');
            }
            bot.removeTextListener(/^(.*)$/);
        });
    } else {
        await bot.sendMessage(chatId, 'Damingni ol');
    }
}

const get_all_serials = async (chatId, page = 0) => {
    if (chatId == process.env.ADMINCHATID) {
        try {
            const response = await axios.get(process.env.MAINAPI + '/api/v1/serial/getSerialsPage', {
                params: {
                    page: page, size: 10
                }, headers: {
                    'Authorization': `Bearer ${process.env.BEKENDTOKEN}`
                }
            });
            const allResponse = response.data
            const serials = allResponse.content;
            const serialButtons = serials.map(serial => [{text: serial.name, callback_data: 'serial-' + serial.id}]);
            bot.sendMessage(chatId, 'Seriallar ro`yxati', {
                reply_markup: {
                    inline_keyboard: [...serialButtons, [{
                        text: 'Ortga', callback_data: page === 0 ? '0' : 'back_serial_page'
                    }, {
                        text: page, callback_data: '0'
                    }, {
                        text: 'Keyingi', callback_data: allResponse.totalPages === pageCalc ? '0' : 'next_serial_page'
                    }], [{
                        text: 'Yangi Serial', callback_data: 'add_serial'
                    }]]
                }
            });

        } catch (error) {
            console.error('Error fetching serials:', error);
            bot.sendMessage(chatId, 'Error fetching serials. Please try again later.');
        }
    } else {
        await bot.sendMessage(chatId, 'Damingni ol');
    }
}
const pagination_serial = async (chatId, action) => {
    if (action === 'next_serial_page') {
        pageCalc++
    }
    if (action === 'back_serial_page') {
        pageCalc--
    }
    get_all_serials(chatId, pageCalc)
}
const show_serial = async (chatId, id) => {
    if (chatId == process.env.ADMINCHATID) {
        deleteCounter = ''
        try {
            const response = await axios.get(process.env.MAINAPI + `/api/v1/serial/getSerialById/${id}`, {
                headers: {
                    'Authorization': `Bearer ${process.env.BEKENDTOKEN}`
                }
            });

            const serialData = response.data;
            const messageText = `
                    Serial id: ${serialData.id}  Name: ${serialData.name}`;
            await bot.sendMessage(chatId, messageText, {
                reply_markup: {
                    remove_keyboard: true, inline_keyboard: [[{
                        text: 'Serialni tahrirlash', callback_data: `edit_serial._${serialData.id}`
                    }, {
                        text: 'Serialni o`chirish', callback_data: `delete_serial._${serialData.id}`
                    }]]
                }
            });
        } catch (error) {
            console.log(error);
        }
    }
}

const edit_serial = async (chatId, id) => {
    if (chatId == process.env.ADMINCHATID) {
        await bot.sendMessage(chatId, 'Ushbu serial uchun yangi nom kiriting :');
        bot.onText(/^(.*)$/, async (msg, match) => {
            const serialName = match[1];
            const apiUrl = process.env.MAINAPI + '/api/v1/serial/updateSerial/' + id;
            const headers = {
                Authorization: `Bearer ${process.env.BEKENDTOKEN}`, 'Content-Type': 'application/json',
            };
            try {
                const response = await axios.put(apiUrl, {name: serialName}, {headers});
                bot.sendMessage(chatId, 'Serial yangilandi');
                get_all_serials(chatId);
            } catch (error) {
                bot.sendMessage(chatId, 'Xatolik yuz berdi. Iltimos, keyinroq urinib ko`ring.');
            }
            bot.removeTextListener(/^(.*)$/);
        });
    } else {
        await bot.sendMessage(chatId, 'Damingni ol');
    }
}

const delete_serial = async (chatId, id) => {
    if (chatId == process.env.ADMINCHATID) {
        if (deleteCounter === 'delete_serial') {
            deleteCounter = ''
            try {
                const response = await axios.delete(process.env.MAINAPI + `/api/v1/serial/deleteSerial/${id}`, {
                    headers: {
                        'Authorization': `Bearer ${process.env.BEKENDTOKEN}`
                    }
                });
                const statusCode = response.status;
                if (statusCode === 200) {
                    bot.sendMessage(chatId, 'Ushbu serial o`chirildi')
                }
            } catch (error) {
                bot.sendMessage(chatId, `Xatolik : Ushbu serialga Bog'langan kinolar mavjud`);
            }
        } else {
            deleteCounter = 'delete_serial'
            bot.sendMessage(chatId, `Siz shu serialni o'chirmoqchisiz. Qaroringiz qatiymi ? `, {
                reply_markup: {
                    inline_keyboard: [[{
                        text: 'Bekor qilish', callback_data: `serial-${id}`
                    }, {
                        text: 'O`chirish', callback_data: `delete_serial._${id}`
                    }]]
                }
            });
        }
    } else {
        bot.sendMessage(chatId, 'Damingni ol')
    }
}
module.exports = {get_all_serials, pagination_serial, show_serial, add_serial, edit_serial, delete_serial}