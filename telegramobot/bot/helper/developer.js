const axios = require('axios');
const {bot} = require('../bot');


const API_URL = 'http://137.184.14.168:8080/api/v1/auth/onOrOfDebug';

const editOnOf = async () => {
    try {
        const response = await axios.post(API_URL);
        const isDebugModeOn = response.data;
        const message = isDebugModeOn ? 'Debug mode is ON' : 'Debug mode is OFF';
        await bot.sendMessage(message);
    } catch (error) {
        console.error('Error while making the API request:', error);
    }
};


module.exports = {editOnOf}