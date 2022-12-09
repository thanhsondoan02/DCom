package com.example.dcom.repo

import android.content.Context
import com.example.dcom.presentation.main.communication.CommunicationFragment

interface ICommunicationRepo {
    fun speak(text: String, context: Context, target: CommunicationFragment)
    fun listen(context: Context): String
}
