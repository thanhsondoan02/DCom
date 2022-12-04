package com.example.dcom.repo

class CommunicationRepoImpl: ICommunicationRepo {
    override fun speak(text: String) {
        // TODO
    }

    override fun listen(): String {
        // TODO
        Thread.sleep(2000)
        val listString =  listOf("Hello", "How are you", "I am fine", "Thank you")
        return listString.random()
    }
}
