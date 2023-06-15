package com.example.jokeapp

import android.app.Application
import com.google.gson.Gson

class JokeApp : Application() {

    //вмка живёт дольше, чем активити, потому что в аппликэйшене

    lateinit var viewModel: MainViewModel
    override fun onCreate() {
        super.onCreate()
        //объект мэнэдж ресурсес
        //это объект будет держать ссылку на аппликешн
        //а сам будет лежать также в аппликашне
        //его прокинем дальше во вью-модель
        //у манадж ресурса и у аппликэшна будет один лайф-сайкл- будут умирать вместе

        viewModel = MainViewModel(BaseModel(JokeService.Base(Gson()),ManageResources.Base(this)))

    }
}