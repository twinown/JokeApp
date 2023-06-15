package com.example.jokeapp

import android.app.Application
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

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

        val retrofit = Retrofit.Builder()
            .baseUrl("https://official-joke-api.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        viewModel = MainViewModel(BaseModel(
            retrofit.create(JokeService::class.java)
            ,ManageResources.Base(this)))

        //было так_старый способ
        //viewModel = MainViewModel(BaseModel(JokeService.Base(Gson()),ManageResources.Base(this)))

    }
}