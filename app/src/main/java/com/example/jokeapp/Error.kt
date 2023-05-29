package com.example.jokeapp

import androidx.annotation.StringRes
import java.security.MessageDigest

interface Error {

    fun message(): String

    abstract class Abstract(
        private val manageResources: ManageResources,
        @StringRes private val messageId: Int
    ) : Error {

        @Override
        override fun message() = manageResources.string(messageId)

    }

    class NoConnection(manageResources: ManageResources) :
        Abstract(manageResources, R.string.no_connection_message)

    class ServiceUnavailable(manageResources: ManageResources) :
        Abstract(manageResources, R.string.service_unavailable_message)


    //принцип драй нарушается - повторяется код, потому перепишем , см выше
    /*  class NoConnection(private val manageResources: ManageResources) : Error {
          override fun message(): String {
              return manageResources.string(R.string.no_connection_message)
          }
      }

      class ServiceUnavailable(private val manageResources: ManageResources) : Error {
          override fun message(): String {
              return manageResources.string(R.string.R.string.service_unavailable_message)
          }
      }*/
}