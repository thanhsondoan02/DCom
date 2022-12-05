package com.example.dcom.repo

import android.content.Context
import android.content.Intent

interface ICommunicationRepo {
    fun speak(text: String, context: Context)
    fun listen(context: Context): String
}
