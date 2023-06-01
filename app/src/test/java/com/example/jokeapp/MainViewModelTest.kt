package com.example.jokeapp

import kotlinx.coroutines.flow.callbackFlow
import org.junit.Assert.*
import org.junit.Test

//теперь тесты делать легко , потому что всё у нас в интерфейсах
//можно создавать фейковые классы - удобно и дёргать нужные методы
class MainViewModelTest {

    @Test
    fun test_success() {
        val model = FakeModel()
        model.returnSuccess = true
        val viewModel = MainViewModel(model)
        viewModel.init(object : TextCallback {
            override fun provideText(text: String) {
                assertEquals("fake joke 1" + "\n" + "punchline", text)
            }
        })
        viewModel.getJoke()
        //хорошо бы проверить отдкльно отписку от всего - метод клиар
        //viewModel.clear()
    }

    @Test
    fun test_error() {
        val model = FakeModel()
        model.returnSuccess = false
        val viewModel = MainViewModel(model)
        viewModel.init(object : TextCallback {
            override fun provideText(text: String) {
                assertEquals("fake error message", text)
            }
        })
        viewModel.getJoke()
    }

}

//прямо тут создаёшь фейк модель для передачи в к-р класса мэйнвьюмод
private class FakeModel : Model<Joke, Error> {

    var returnSuccess = true
    private var callback: ResultCallback<Joke, Error>? = null

    override fun fetch() {
        if (returnSuccess) {
            callback?.provideSuccess(Joke("fake joke 1", "punchline"))
        } else
            callback?.provideError(FakeError())
    }

    override fun clear() {
        callback = null
    }

    override fun init(resultCallback: ResultCallback<Joke, Error>) {
        callback = resultCallback
    }
}

private class FakeError : Error {
    override fun message(): String {
        return "fake error message"
    }
}