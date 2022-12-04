package com.example.dcom.repo

import android.content.Context

class CommunicationRepoImpl: ICommunicationRepo {
    override fun speak(text: String, context: Context) {
        // TODO
    }

    override fun listen(context: Context): String {
        // TODO
        Thread.sleep(2000)
        val listString =  listOf("Hello", "How are you", "I am fine", "Thank you")
        return listString.random()
    }
}
