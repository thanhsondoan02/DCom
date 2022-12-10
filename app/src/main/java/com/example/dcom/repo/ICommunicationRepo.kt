package com.example.dcom.repo

import android.content.Context

interface ICommunicationRepo {
    fun speak(text: String, context: Context, callback: ICommunicationRepoCallback)
    fun listen(context: Context): String
}
