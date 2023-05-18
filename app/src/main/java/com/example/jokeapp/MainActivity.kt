package com.example.jokeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.jokeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //лэйт инит, потому к вью модели нужен доступ из двух методов:
    //OnCreate()(что -то делаю тут) & OnDestroy() (зачищаю вмку)
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //байндинг-для удобной иниц-ции вьюх
        //при билде проекта айдишки вьюх заносятся в класс статично, откуда
        //получаешь к ним доступ напрямую

        binding.actionButton.setOnClickListener {
            binding.actionButton.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE
            //выз-ся метод во вмке, в  котором
            // происходит получение данных с сервака
            //здесь это не делается !!!
            viewModel.getJoke()
        }

        //асинхронный ответ от сервака получаем в коллбэк
        //инициализируем колбэк
        viewModel.init(object : TextCallback{
            override fun provideText(text:String){
                binding.actionButton.isEnabled = true
                binding.progressBar.visibility = View.INVISIBLE
                //текст, который прилетает в интерфейс коллбэка, ставим в поле
                binding.textView.text = text
            }
        })
    }

    //"отписываемся" от изменений
    override fun onDestroy() {
        super.onDestroy()
        //эта ф - ция зачищает колбэк
        viewModel.clear()
    }
}