const axios = require('axios');
const {bot} = require('../bot');


const API_URL = process.env.MAINAPI + '/api/v1/auth/onOrOfDebug';

const editOnOf = async (chatId) => {
    try {
        const response = await axios.post(API_URL);
        const isDebugModeOn = response.data;
        const message = isDebugModeOn ? 'Debug mode is ON' : 'Debug mode is OFF';
        await bot.sendMessage(chatId, message);
    } catch (error) {
        console.error('Error while making the API request:', error);
    }
};


module.exports = {editOnOf}