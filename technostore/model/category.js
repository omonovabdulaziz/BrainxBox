const {Schema, model} = require('mongoose')


const Category = new Schema({
    title: String,
    status: {
        type: Boolean,
        default: true
    }
})

module.exports = model('Category', Category)