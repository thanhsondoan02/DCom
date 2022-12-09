package com.example.dcom.repo

import android.content.Context
import com.example.dcom.presentation.main.communication.CommunicationFragment
import com.example.dcom.presentation.main.communication.RecognitionManager

interface ICommunicationRepo {
    fun speak(text: String, context: Context, callback: ICommunicationRepoCallback)
    fun listen(context: Context): String
}
