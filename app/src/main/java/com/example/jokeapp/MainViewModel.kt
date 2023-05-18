package com.example.jokeapp

import android.widget.EditText


class MainViewModel {

    private var textCallback:TextCallback? = null
    //здесь просиходит запрос к серваку
    //получив данные, возвращаем их в коллбэк
    fun getJoke() {
    }

    fun init(textCallback: TextCallback) {

    }

    //зачищеник колбэка
    fun clear() {
        TODO("Not yet implemented")
    }
}

interface TextCallback{
    fun provideText(text: String)
}