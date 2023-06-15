package com.example.jokeapp

import com.google.gson.Gson
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.net.UnknownHostException


interface JokeService {

    fun joke(callback: ServiceCallback)

    //без ретрофита!!!
    //получение шутки из сети
    //как делалось по-старому..до корутин и ретрофита
    //вложенный класс
    class Base(
        //получаем гсон в констр, чтоб потом можно было бы получать кастомный гсон
        private val gson: Gson
    ) : JokeService {
        override fun joke(callback: ServiceCallback) {
            Thread{
                var  connection : HttpURLConnection? = null
                try {
                    val url = URL(URL)
                    connection = url.openConnection() as HttpURLConnection
                    //use автоматичски открывает поток и закрывает
                    InputStreamReader(BufferedInputStream(connection.inputStream)).use {
                        val text  = it.readText()
                        //тут раньше в метод приходил текст, что выше
                        //сырую текстовку преобразовавыем в сырок джоукклауд объект с помощью гсона
                        callback.returnSuccess(gson.fromJson(text, JokeCloud::class.java))
                    }
                } catch (e:Exception){
            if (e is UnknownHostException|| e is java.net.ConnectException){
                callback.returnError(ErrorType.NO_CONNECTION )
            } else{
                callback.returnError(ErrorType.OTHER)
            }
                }finally {
                    //коннекшн обязательно закрывать
        connection?.disconnect()
                }
            }.start()
        }

        //Companion Object - Такому объекту можно не указывать имя, а к его компонентам можно обращаться через
        // имя класса, в котором он находится. Как правило объекты-компаньоны используются
        // для объявления переменных и функций, к которым требуется обращаться без создания
        // экземпляра класса
        companion object {
            private const val URL = "https://official-joke-api.appspot.com/random_joke"
        }
    }
}

interface ServiceCallback {

    fun returnSuccess(data: JokeCloud)
    fun returnError(errorType: ErrorType)
}

enum class ErrorType {
    NO_CONNECTION,
    OTHER
}
