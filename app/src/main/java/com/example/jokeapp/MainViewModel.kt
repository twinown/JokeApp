package com.example.jokeapp

import android.widget.EditText


//актитвити и вмка живут разными жизнями
//активити умирает каждый раз
//вмка умирает, если прям смахнуть приложение
//смерть аппликашна = смерть вмки (и наоборот)
class MainViewModel (private val model: Model<Any, Any>) {

    private var textCallback: TextCallback = TextCallback.Empty();

    //здесь просиходит запрос к серваку
    //получив данные, возвращаем их в коллбэк
    fun getJoke() {
        //= get
        model.fetch()
      //  textCallback.provideText("result")
    }

    fun init(textCallback: TextCallback) {
        // в он криайте просто сохраняеям активити во вмку (грубо говоря)
        //(в он криайте активити) привязываем вмку к активити (активити подписывается на колбэки вмки)
        //
        this.textCallback = textCallback

        // типо того . что и в активити
        //анонимный объект держит ссылку на вмку (на родителя)
        //вмка подписывается на колбэки модели
        model.init(object :ResultCallback<Any, Any>{
            override fun provideSuccess(data: Any) {
                //в текст-коллбэк отдаёшь текст здесь, типо сетишь
                textCallback.provideText("success")
            }

            override fun provideError(error: Any) {
                textCallback.provideText("error")
            }

        })
    }

    //зачистка колбэка
    fun clear() {
        //отвязываем активити от вмки =
        //зачищаем вмку от  активити (АКТИВИТИ отписывается от колбэков вмки)
        textCallback = TextCallback.Empty()
        //вмка отписывается от колбэков модели
        model.clear()
    }
}

interface TextCallback {
    //передача в текст колбэк текста
    fun provideText(text: String)

    //смарт-контракт = создание эмпти еласса, чтоб не было нуллов
    class Empty : TextCallback {
        override fun provideText(text: String) = Unit
    }
}