package com.example.jokeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.jokeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //лэйт инит, потому ко вью модели нужен доступ из двух методов:
    //OnCreate()(что -то делаю тут) & OnDestroy() (зачищаю вмку)
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //здесь это не совсем правильно, так как нарушается ООП в джокапп
        //правильнее - через фабрику моделей
        viewModel = (application as JokeApp).viewModel

        //байндинг-для удобной иниц-ции вьюх
        //при билде проекта айдишки вьюх заносятся в класс статично, откуда
        //получаешь к ним доступ напрямую
        binding.actionButton.setOnClickListener {
            binding.actionButton.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE
            //выз-ся метод во вмке, в  которой
            // происходит получение данных с сервака
            //здесь этого не делается !!!
            //из активити говорю вмке дай мне данные
            viewModel.getJoke()
        }

        //инициализируем колбэки//

        //вмка грит, мол, хорошо, получи данные в текстовый колбэк
        //просто скажи, куда их вернуть, ведь это
        //асинхронный процесс
        //(кстати, вмка делает то же самое с моделью)
        //асинхронный ответ от сервака получаем в коллбэк
        //колбэк здесь является типо объектом(типо - потому что аноним кл) класса активити
        // (держит ссылку на него,на родителя, на активити)
        //потому что он в нём лежит вот здесь сейчас (в майнактивити)
        val textCallback = object : TextCallback {
            override fun provideText(text: String) = runOnUiThread() {
                binding.actionButton.isEnabled = true
                binding.progressBar.visibility = View.INVISIBLE
                //текст, который прилетает в интерфейс коллбэка, ставим в поле
                binding.textView.text = text
            }
        }

        //на самом деле, пробрасываем объект активити, который может
        //умирать раньше, чем вмка (вмка порожда-ся внутри аппликэйшена)
        //вмка живёт дольше активити !
        //(активити подписывается на колбэки вмки
        viewModel.init(textCallback)





       //можно записать так
        /*viewModel.init(object :TextCallback{
            override fun provideText(text: String) {
                binding.actionButton.isEnabled = true
                binding.progressBar.visibility = View.INVISIBLE
                //текст, который прилетает в интерфейс коллбэка, ставим в поле
                binding.textView.text = text
            }
        })*/




    }

    //"отписываемся" от изменений
    override fun onDestroy() {
        super.onDestroy()
        //эта ф - ция зачищает колбэк
        //отвязываем активити от вмки,(отписывается от вмки))
        //чтобы не хранили тот объект, который должен умереть
        //чтоб смогли в ините новый объект создть
        //если не зачистить и новые данные вернутся в коллбэк, то они
        //могут вернуться в момент, когда ондестрой ВЫЗЫВАЕТСЯ, а
        //а он криате ЕЩЁ НЕ вызван
        //то есть данные будет некуда выводить, активити умерла тут уже
        viewModel.clear()
    }

}