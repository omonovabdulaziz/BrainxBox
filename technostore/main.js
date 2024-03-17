const express = require("express")
const {mongo} = require("mongoose");
const mongoose = require("mongoose");
const app = express()
require('dotenv').config()
app.use(express.json)

require('./bot/bot')

async function dev() {
    try {
        await mongoose.connect(process.env.MONGO_URL)
            .then(() => {
                console.log("Mongo db start")
            }).catch((error) => {
                console.log(error)
            })
        app.listen(process.env.PORT, () => {
            console.log('server is running')
        })
    } catch (error) {
        console.log(error)
    }
}

dev()