package com.example.jokeapp

import android.content.Context
import androidx.annotation.StringRes

interface ManageResources {

    fun string(@StringRes resourceId: Int): String


    //утечка памяти - контекст, который прилетает умирает раньше ,  чем сам объект
    //утечки памяти не будет
    //потому что контекст у нас будет из аппликейшена (из джоук аппа) , а не из активити
    class Base(private val context: Context) : ManageResources {
        override fun string(resourceId: Int): String {
           return context.getString(resourceId)
        }
    }
}