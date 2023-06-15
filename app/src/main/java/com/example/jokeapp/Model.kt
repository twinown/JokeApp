package com.example.jokeapp

//было так
interface Model<S, E> {

    fun fetch()
    fun clear()
    fun init(resultCallback: ResultCallback<S, E>)

}

//этот коллбэк отдаёт данные из модельки в вмку

interface ResultCallback<S, E> {
    fun provideSuccess(data:S)
    fun provideError(error:E)

}

//теперь стало так
