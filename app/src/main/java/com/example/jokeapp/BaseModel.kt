package com.example.jokeapp

class BaseModel(
    private val jokeService: JokeService,
    private val manageResources: ManageResources
) : Model<Joke, Error> {

    //ба лэйзи - это синглтон
    //создал объект - его не трогает никто больше. не стирает
    //это поле НЕ ЗАЧИЩАЕТСЯ
    private val  noConnection by lazy {
        Error.NoConnection(manageResources) }

    private val serviceError by lazy {
        Error.ServiceUnavailable(manageResources) }

    private var callback: ResultCallback<Joke, Error>? = null

    override fun fetch() {
        jokeService.joke(object :ServiceCallback{
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
        })
    }

    override fun clear() {
        callback = null
    }

    override fun init(resultCallback: ResultCallback<Joke, Error>) {
        callback = resultCallback
    }
}