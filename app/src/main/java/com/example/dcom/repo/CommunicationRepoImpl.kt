package com.example.dcom.repo

import android.content.Context
import android.speech.tts.TextToSpeech
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.example.dcom.presentation.main.MainActivity
import java.util.*

class CommunicationRepoImpl: ICommunicationRepo {

    private var tts: TextToSpeech? = null
    var resultSpeech: String? = null


    override fun speak(text: String, context: Context) {
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
        Thread.sleep(5000)
        return resultSpeech.toString()
    }

}
