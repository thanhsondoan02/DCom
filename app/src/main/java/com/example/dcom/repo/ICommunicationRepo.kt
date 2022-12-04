package com.example.dcom.repo

interface ICommunicationRepo {
    fun speak(text: String)
    fun listen(): String
}
