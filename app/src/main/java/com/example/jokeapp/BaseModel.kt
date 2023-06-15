package com.example.jokeapp

import retrofit2.Call
import retrofit2.Response
import java.net.UnknownHostException

class BaseModel(
    private val jokeService: JokeService,
    private val manageResources: ManageResources
) : Model<Joke, Error> {

    //ба лэйзи - это синглтон
    //создал объект - его не трогает никто больше. не стирает
    //это поле НЕ ЗАЧИЩАЕТСЯ
    private val noConnection by lazy {
        Error.NoConnection(manageResources)
    }

    private val serviceError by lazy {
        Error.ServiceUnavailable(manageResources)
    }

    private var callback: ResultCallback<Joke, Error>? = null

    override fun fetch() {

        //с ретрофитом
        //энкью - чтоб запрос начал идти
        //избегаем: новый тред не делаем трай-кэтч, не берем стримы, не работаем напрямую с гсоном
        //не парсим из текста в объект , всё это под капотом делает ретрофит
        jokeService.joke().enqueue(object : retrofit2.Callback<JokeCloud> {
            //код 200-300 - успешно
            //код >400 неуспешно
            override fun onResponse(call: Call<JokeCloud>, response: Response<JokeCloud>) {
                if (response.isSuccessful){
                    callback?.provideSuccess(response.body()!!.toJoke())
                } else{
                    callback?.provideError(serviceError)
                }
            }

            override fun onFailure(call: Call<JokeCloud>, t: Throwable) {
                if (t is UnknownHostException || t is java.net.ConnectException){
                    callback?.provideError(noConnection)
                }else{
                    callback?.provideError(serviceError)
                }
            }
        })


        //без ретрофита
        /*  jokeService.joke(object :ServiceCallback{
              //раньше приходил просто стринг - потом сделали джоукклауд
              override fun returnSuccess(data: JokeCloud) {
                  //callback?.provideSuccess(Joke(data,""))
                  callback?.provideSuccess(data.toJoke())
              }

              override fun returnError(errorType: ErrorType) {
                  when(errorType){
                      ErrorType.NO_CONNECTION->callback?.provideError(noConnection)
                      ErrorType.OTHER->callback?.provideError(serviceError)
                  }
              }
          })*/
    }

    override fun clear() {
        callback = null
    }

    override fun init(resultCallback: ResultCallback<Joke, Error>) {
        callback = resultCallback
    }
}