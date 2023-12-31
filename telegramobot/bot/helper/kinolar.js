const axios = require('axios');
const {bot} = require('../bot');

let pageCalc = 0;
let deleteCounter = '';
let pageAbleCalc = 0;
const askForAddingMovie = async (chatId) => {
    const questions = [
        'Ism:',
        'Description:',
        'Narxi:',
        'Daraja: <code>INTERMEDIATE</code> <code>BEGINNER</code> <code>ELEMENTRY</code> <code>UPPER_INTERMEDIATE</code>',
        'Yosh chegarasi:',
        'Serial Tanlang',
    ];

    let answers = [];
    let lastMessageId = null;

    for (let i = 0; i < questions.length; i++) {
        if (lastMessageId) {
            await bot.deleteMessage(chatId, lastMessageId).catch(e => console.log('Error deleting message:', e));
        }

        const question = questions[i];
        const response = await bot.sendMessage(chatId, question, { parse_mode: 'HTML' });
        lastMessageId = response.message_id;

        if (i === questions.length - 1) {
            const selectedSerialId = await get_all_serials(chatId);
            answers.push(selectedSerialId);
        } else {
            const answer = await new Promise((resolve) => {
                bot.once('text', (msg) => resolve(msg.text));
            });
            answers.push(answer);
        }
    }

    return {
        name: answers[0],
        description: answers[1],
        price: parseFloat(answers[2]),
        level: answers[3],
        belongAge: parseInt(answers[4]),
        serialId: answers[5],
    };
};


const get_all_serials = async (chatId, page = 0) => {
    return new Promise(async (resolve) => {
        if (chatId == process.env.ADMINCHATID) {
            try {
                const response = await axios.get(process.env.MAINAPI + '/api/v1/serial/getSerialsPage', {
                    params: {
                        page: page,
                        size: 10,
                    },
                    headers: {
                        'Authorization': `Bearer ${process.env.BEKENDTOKEN}`,
                    },
                });
                const allResponse = response.data;
                const serials = allResponse.content;
                const serialButtons = serials.map(serial => [{ text: serial.name, callback_data: `serial_${serial.id}` }]);
                serialButtons.push([{ text: 'Serial tanlanmadi', callback_data: 'no_serial_selection' }]);

                bot.sendMessage(chatId, 'Seriallar ro`yxati', {
                    reply_markup: {
                        inline_keyboard: [
                            ...serialButtons,
                            [
                                { text: 'Ortga', callback_data: page === 0 ? '0' : 'back_serial_page_pagination' },
                                { text: page, callback_data: '0' },
                                { text: 'Keyingi', callback_data: allResponse.totalPages === pageCalc ? '0' : 'next_serial_page_pagination' },
                            ],
                        ],
                    },
                }).then(() => {
                    bot.once('callback_query', (query) => {
                        const data = query.data.split('_');
                        if (data[0] === 'serial') {
                            resolve(data[1]);
                        } else if (data[0] === 'no' && data[1] === 'serial') {
                            resolve(null);
                        }
                    });
                });
            } catch (error) {
                console.error('Error fetching serials:', error);
                bot.sendMessage(chatId, 'Error fetching serials. Please try again later.');
            }
        } else {
            await bot.sendMessage(chatId, 'Damingni ol');
            resolve(null); // Unauthorized access
        }
    });
};


const pageableforSerials = async (chatId, action) => {
    if (action === 'next_serial_page_pagination') {
        pageAbleCalc++;
        get_all_serials(chatId, pageAbleCalc);
    }
    if (action === 'back_serial_page_pagination') {
        pageAbleCalc--;
        get_all_serials(chatId, pageAbleCalc);
    }
};


const add_movie = async (chatId) => {
    if (process.env.ADMINCHATID == chatId) {
        try {
            const movieInfo = await askForAddingMovie(chatId);
            const apiUrl = process.env.MAINAPI + '/api/v1/movie/addMovie';
            const headers = {
                Authorization: `Bearer ${process.env.BEKENDTOKEN}`, 'Content-Type': 'application/json',
            };
            const response = await axios.post(apiUrl, movieInfo, {headers});
            if (response.status === 200) {
                console.log(response.data)
                bot.sendMessage(chatId, 'Kino qo`shildi');
                get_all_movies(chatId);
            } else {
                bot.sendMessage(chatId, 'Kino qo`shishda xatolik');
            }
        } catch (error) {
            console.error(error);
            bot.sendMessage(chatId, 'An error occurred while processing your request.');
        }
    } else {
        bot.sendMessage(chatId, 'Damingni ol');
    }
};


const get_all_movies = async (chatId, page = 0) => {
    if (chatId == process.env.ADMINCHATID) {
        try {
            const response = await axios.get(process.env.MAINAPI + '/api/v1/movie/getAllMoviePage', {
                params: {
                    page: page, size: 10
                }, headers: {
                    'Authorization': `Bearer ${process.env.BEKENDTOKEN}`
                }
            });
            const allResponse = response.data;
            const movies = allResponse.content;
            const movieButtons = movies.map(movie => [{text: movie.name, callback_data: 'movie_' + movie.id}]);
            bot.sendMessage(chatId, 'Kinolar ro`yxati', {
                reply_markup: {
                    inline_keyboard: [...movieButtons, [{
                        text: 'Ortga', callback_data: page === 0 ? '0' : 'back_page'
                    }, {
                        text: page, callback_data: '0'
                    }, {
                        text: 'Keyingi', callback_data: allResponse.totalPages === pageCalc ? '0' : 'next_page'
                    }], [{
                        text: 'Yangi Kino', callback_data: 'add_movie'
                    }]]
                }
            });

        } catch (error) {
            console.error('Error fetching movies:', error);
            bot.sendMessage(chatId, 'Error fetching movies. Please try again later.');
        }
    } else {
        bot.sendMessage(chatId, 'Damingni ol');
    }
};


const show_movie = async (chatId, id) => {
    if (chatId == process.env.ADMINCHATID) {
        deleteCounter = ''
        try {
            const response = await axios.get(process.env.MAINAPI + `/api/v1/movie/getMovie/${id}`, {
                headers: {
                    'Authorization': `Bearer ${process.env.BEKENDTOKEN}`
                }
            });

            const movieData = response.data;
            const messageText = `
                    Movie ID: ${movieData.id}
                    Name: ${movieData.name}
                    Description: ${movieData.description}
                    Price: ${movieData.price}
                    Genre: ${movieData.genre}
                    ImageUrl:${movieData.avatarUrl}
                    Is Bought: ${movieData.isBought ? 'Yes' : 'No'}
                    Level: ${movieData.level}
                    Belong Age: ${movieData.belongAge} 
                    Is Serial: ${movieData.serial === null ? 'No' : 'Yes'}
                `;
            bot.sendMessage(chatId, messageText, {
                reply_markup: {
                    remove_keyboard: true, inline_keyboard: [[{
                        text: 'Kinoni tahrirlash', callback_data: `edit_movie-${movieData.id}`
                    }, {
                        text: 'Kinoni o`chirish', callback_data: `del_movie-${movieData.id}`
                    }]]
                }
            });
        } catch (error) {
            console.log(error);
        }
    }
};


const pagination_movie = async (chatId, action) => {
    if (action === 'next_page') {
        pageCalc++
    }
    if (action === 'back_page') {
        pageCalc--
    }
    get_all_movies(chatId, pageCalc)
}


const delete_movie = async (chatId, id) => {
    console.log(id)
    if (chatId == process.env.ADMINCHATID) {
        if (deleteCounter === 'delete_movie') {
            deleteCounter = ''
            try {
                const response = await axios.delete(process.env.MAINAPI + `/api/v1/movie/deleteMovie/${id}`, {
                    headers: {
                        'Authorization': `Bearer ${process.env.BEKENDTOKEN}`
                    }
                });
                const statusCode = response.status;
                if (statusCode === 200) {
                    bot.sendMessage(chatId, 'Ushbu kino o`chirildi')
                }
            } catch (error) {
                bot.sendMessage(chatId, `Error: ${error.response.status}\n${error.response.data}`);
            }
        } else {
            deleteCounter = 'delete_movie'
            bot.sendMessage(chatId, `Siz shu kinoni o'chirmoqchisiz. Qaroringiz qatiymi ? `, {
                reply_markup: {
                    inline_keyboard: [[{
                        text: 'Bekor qilish', callback_data: `movie_${id}`
                    }, {
                        text: 'O`chirish', callback_data: `del_movie-${id}`
                    }]]
                }
            });
        }
    } else {
        bot.sendMessage(chatId, 'Damingni ol')
    }
}


const askForEditInformation = async (chatId) => {
    const questions = [
        'Ism:',
        'Tavsif:',
        'Narxi:',
        'Daraja: <code>INTERMEDIATE</code> <code>BEGINNER</code> <code>ELEMENTRY</code> <code>UPPER_INTERMEDIATE</code>',
        'Yosh chegarasi:',
        'Serial ID:',
        'Rasm URL manzili:',
        'Kino janri:',
    ];

    let answers = [];
    let lastMessageId = null;

    for (const question of questions) {
        if (lastMessageId) {
            await bot.deleteMessage(chatId, lastMessageId).catch(e => console.log('Error deleting message:', e));
        }

        const response = await bot.sendMessage(chatId, question);
        lastMessageId = response.message_id;

        const answer = await new Promise(resolve => {
            bot.once('text', (msg) => resolve(msg.text));
        });
        answers.push(answer);
    }

    return {
        name: answers[0],
        description: answers[1],
        price: parseFloat(answers[2]),
        level: answers[3],
        belongAge: parseInt(answers[4]),
        serialId: parseInt(answers[5]) || null,
        updateImageUrl: answers[6],
        updateImageGenre: answers[7]
    };
};

const edit_movie = async (chatId, id) => {
    if (process.env.ADMINCHATID == chatId) {
        const questions = [
            'Ism:',
            'Tavsif:',
            'Narxi:',
            'Daraja: <code>INTERMEDIATE</code> <code>BEGINNER</code> <code>ELEMENTRY</code> <code>UPPER_INTERMEDIATE</code>',
            'Yosh chegarasi:',
            'Serial Tanlang',
            'Rasm URL manzili:'
        ];

        let answers = [];
        let lastMessageId = null;

        for (let i = 0; i < questions.length; i++) {
            if (lastMessageId) {
                await bot.deleteMessage(chatId, lastMessageId).catch(e => console.log('Error deleting message:', e));
            }

            const question = questions[i];
            const response = await bot.sendMessage(chatId, question, { parse_mode: 'HTML' });
            lastMessageId = response.message_id;

            if (i === questions.length - 2) {
                const selectedSerialId = await get_all_serials(chatId);
                answers.push(selectedSerialId);
            } else {
                const answer = await new Promise((resolve) => {
                    bot.once('text', (msg) => resolve(msg.text));
                });
                answers.push(answer);
            }
        }

        try {
            const editInfo = {
                name: answers[0],
                description: answers[1],
                price: parseFloat(answers[2]),
                level: answers[3],
                belongAge: parseInt(answers[4]),
                serialId: answers[5],
                imageUrl: answers[6]
            };

            const response = await axios.put(`${process.env.MAINAPI}/api/v1/movie/updateMovie/${id}`, editInfo, {
                headers: {'Authorization': `Bearer ${process.env.BEKENDTOKEN}`}
            });

            if (response.status === 200) {
                await bot.sendMessage(chatId, 'Kino muvaffaqiyatli tahrirlandi.');
            } else {
                await bot.sendMessage(chatId, 'Kino tahrirlashda xatolik yuz berdi.');
            }
        } catch (error) {
            console.error('Error updating movie:', error.message);
            await bot.sendMessage(chatId, 'Tahrirlash jarayonida xatolik yuz berdi.');
        }
    } else {
        await bot.sendMessage(chatId, 'Sizda bu amalni bajarish huquqi yo\'q.');
    }
};




module.exports = {
    get_all_movies, pagination_movie, show_movie, delete_movie, edit_movie, add_movie, pageableforSerials
};
