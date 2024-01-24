const axios = require('axios');
const {bot} = require('../bot');
const FormData = require('form-data');

let pageCalc = 0;
let deleteCounter = '';
let pageAbleCalc = 0;
const askForAddingMovie = async (chatId) => {
    const questions = ['Ism:', 'Daraja: <code>INTERMEDIATE</code> <code>BEGINNER</code> <code>ELEMENTARY</code> <code>UPPER_INTERMEDIATE</code>', 'Yosh chegarasi:', 'Serial Tanlang',];


    let answers = [];
    let lastMessageId = null;

    for (let i = 0; i < questions.length; i++) {
        if (lastMessageId) {
            await bot.deleteMessage(chatId, lastMessageId).catch(e => console.log('Error deleting message:', e));
        }

        const question = questions[i];
        const response = await bot.sendMessage(chatId, question, {parse_mode: 'HTML'});
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
        name: answers[0], level: answers[1], belongAge: parseInt(answers[2]), serialId: answers[3],
    };
};


const get_all_serials = async (chatId, page = 0) => {
    return new Promise(async (resolve) => {
        if (process.env.ADMINCHATID == chatId || process.env.ADMINCHATID2 == chatId) {
            try {
                const response = await axios.get(process.env.MAINAPI + '/api/v1/serial/getSerialsPage', {
                    params: {
                        page: page, size: 10,
                    }, headers: {
                        'Authorization': `Bearer ${process.env.BEKENDTOKEN}`,
                    },
                });
                const allResponse = response.data;
                const serials = allResponse.content;
                const serialButtons = serials.map(serial => [{
                    text: serial.name, callback_data: `serial_${serial.id}`
                }]);
                serialButtons.push([{text: 'Serial tanlanmadi', callback_data: 'no_serial_selection'}]);

                bot.sendMessage(chatId, 'Seriallar ro`yxati', {
                    reply_markup: {
                        inline_keyboard: [...serialButtons, [{
                            text: 'Ortga', callback_data: page === 0 ? '0' : 'back_serial_page_pagination'
                        }, {text: page, callback_data: '0'}, {
                            text: 'Keyingi',
                            callback_data: allResponse.totalPages === pageCalc ? '0' : 'next_serial_page_pagination'
                        },],],
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
    if (process.env.ADMINCHATID == chatId || process.env.ADMINCHATID2 == chatId) {
        try {
            const movieInfo = await askForAddingMovie(chatId);
            const apiUrl = process.env.MAINAPI + '/api/v1/movie/addMovie';
            const headers = {
                Authorization: `Bearer ${process.env.BEKENDTOKEN}`, 'Content-Type': 'application/json',
            };
            const response = await axios.post(apiUrl, movieInfo, {headers});

            if (response.status === 200) {
                console.log(response.data);
                const id = response.data.object; // Get the ID from the response
                await bot.sendMessage(chatId, 'Iltimos subtitleni yuklang');
                bot.once('document', async (msg) => {


                    const documentId = msg.document.file_id;
                    const file = await bot.getFile(documentId);

                    const response = await axios({
                        method: 'get',
                        url: `https://api.telegram.org/file/bot${process.env.TOKEN}/${file.file_path}`,
                        responseType: 'arraybuffer',
                    });
                    const formData = new FormData();
                    formData.append('file', response.data, {filename: 'subtitle_file.txt'});

                    const headers = {
                        'Authorization': `Bearer ${process.env.BEKENDTOKEN}`, ...formData.getHeaders(),
                    };

                    const requestData = {
                        languageId: 1,
                    };

                    try {
                        await axios.post(`${process.env.MAINAPI}/api/v1/subtitleWords/addSubtitle/${id}`, formData, {
                            headers, params: requestData,
                        });

                         bot.sendMessage(chatId, 'Subtitle uploaded successfully to ' + movieInfo.name, {
                            reply_markup: {
                                inline_keyboard: [[{
                                    text: 'New Movie', callback_data: `add_movie`
                                }]]
                            }
                        });

                    } catch (error) {
                        console.error('Error uploading subtitle:', error);
                    }
                });
            } else {
                await bot.sendMessage(chatId, response.data.message);
            }
        } catch (error) {
            console.error(error);
            await bot.sendMessage(chatId, 'An error occurred while processing your request.');
        }
    } else {
        await bot.sendMessage(chatId, 'Damingni ol');
    }
};


const get_all_movies = async (chatId, page = 0) => {
    if (process.env.ADMINCHATID == chatId || process.env.ADMINCHATID2 == chatId) {
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
    if (process.env.ADMINCHATID == chatId || process.env.ADMINCHATID2 == chatId) {
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
    if (process.env.ADMINCHATID == chatId || process.env.ADMINCHATID2 == chatId) {
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


const edit_movie = async (chatId, id) => {
    if (process.env.ADMINCHATID == chatId || process.env.ADMINCHATID2 == chatId) {
        const questions = ['Ism:', 'Tavsif:', 'Daraja: <code>INTERMEDIATE</code> <code>BEGINNER</code> <code>ELEMENTARY</code> <code>UPPER_INTERMEDIATE</code>', 'Yosh chegarasi:', 'Rasm URL manzili:', 'Rasm Janri : <code>ACTION</code> <code>ADVENTURE</code>  <code>ANIMATION</code> <code>COMEDY</code>   <code>CRIME</code> <code>DRAMA</code>  <code>FANTASY</code> <code>HISTORICAL</code>', 'Serial Tanlang'];

        let answers = [];
        let lastMessageId = null;

        for (let i = 0; i < questions.length; i++) {
            if (lastMessageId) {
                await bot.deleteMessage(chatId, lastMessageId).catch(e => console.log('Error deleting message:', e));
            }

            const question = questions[i];
            const response = await bot.sendMessage(chatId, question, {parse_mode: 'HTML'});
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

        try {
            const editInfo = {
                name: answers[0],
                description: answers[1],
                level: answers[2],
                belongAge: parseInt(answers[3]),
                serialId: answers[6],
                updateImageUrl: answers[4],
                updateImageGenre: answers[5]
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
