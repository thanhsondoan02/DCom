package com.example.dcom.repo

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*

class CommunicationRepoImpl: ICommunicationRepo {

    private var tts: TextToSpeech? = null

    override fun speak(text: String, context: Context) {
        // TODO
        if(tts == null){
            tts = TextToSpeech(context) { status ->
                if (status != TextToSpeech.ERROR) {
                    tts?.language = Locale("vi", "VN")
                    tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        } else {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }

    }

    override fun listen(context: Context): String {
        // TODO
        Thread.sleep(5000)
        val listString =  listOf("Hello", "How are you", "I am fine", "Thank you")
        return listString.random()
    }
}
