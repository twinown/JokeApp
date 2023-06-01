package com.example.jokeapp

import java.util.TimerTask

//тестовая(фейковая) моделька чиста для проверки рабоыт кода, в оригинале - с серваком
//было сначала Any,Any в дженерике, потом поменяли на то, что сейчас
class FakeModel (
     manageResources: ManageResources
    ) : Model<Joke, Error> {

    private var noConnection = Error.NoConnection(manageResources)
    private var serviceUnavailable = Error.ServiceUnavailable(manageResources)
    private var callback: ResultCallback<Joke, Error>? = null

    private var count = 0

    override fun fetch() {
        //имитация работы сервера!
        //таймер работает на своём потоке
        java.util.Timer().schedule(object : TimerTask() {
            override fun run() {
                if (count % 2 == 1) {
                    //колбэк на юай - потоке
                    //потому в активити надо исп метод раонюайтред
                    //чтоб переключился на работу в мэнтред
                    //если просто написать тред слип, то ты блокируешь юай поток
                    //что не круто
                    callback?.provideSuccess(Joke("fake joke $count","punchline"))
                } else if (count%3==0) {
                    callback?.provideError(noConnection)
                }else{
                    callback?.provideError(serviceUnavailable)
                }
                count++
            }
        }, 2000)
    }

    override fun clear() {
        callback = null
    }

    //вмка подписывается на колбэки модели
    override fun init(resultCallback: ResultCallback<Joke, Error>) {
        callback = resultCallback
    }
}