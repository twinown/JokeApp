package com.example.jokeapp

import java.util.TimerTask

//тестовая(фейковая) моделька чиста для проверки рабоыт кода, в оригинале - с серваком
class FakeModel : Model<Any, Any> {

    private var callback: ResultCallback<Any, Any>? = null

    private var count = 0

    override fun fetch() {
        //имитация работы сервера!
        //таймер работает на своём потоке
        java.util.Timer().schedule(object : TimerTask() {
            override fun run() {
                if (++count % 2 == 1) {
                    //колбэк на юай - потоке
                    //потому в активити надо исп метод раонюайтред
                    //чтоб переключился на работу в мэнтред
                    //если просто написать тред слип, то ты блокируешь юай поток
                    //что не круто
                    callback?.provideSuccess("")
                } else {
                    callback?.provideError("")
                }
            }
        }, 2000)
    }

    override fun clear() {
        callback = null
    }

    //вмка подписывается на колбэки модели
    override fun init(resultCallback: ResultCallback<Any, Any>) {
        callback = resultCallback
    }
}