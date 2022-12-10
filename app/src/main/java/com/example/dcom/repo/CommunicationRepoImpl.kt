package com.example.dcom.repo

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.*

class CommunicationRepoImpl : ICommunicationRepo,
    TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    var resultSpeech: String? = null
    var callback: ICommunicationRepoCallback? = null
    var text: String? = null


    override fun speak(text: String, context: Context, callback: ICommunicationRepoCallback) {
        this.callback = callback
        this.text = text
        if (tts == null) {
            tts = TextToSpeech(context, this)
        } else {
            tts?.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID
            )
        }

    }

    override fun listen(context: Context): String {
        Thread.sleep(5000)
        return resultSpeech.toString()
    }

    override fun onInit(p0: Int) {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                callback?.onSpeakStart()
            }

            override fun onDone(utteranceId: String?) {
                callback?.onSpeakSuccess()
            }

            override fun onError(p0: String?) {
            }

        })
        tts?.language = Locale("vi", "VN")
        tts?.speak(this.text, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID)
    }

}
