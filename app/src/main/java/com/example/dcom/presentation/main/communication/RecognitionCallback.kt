package com.example.dcom.presentation.main.communication

import android.os.Bundle

interface RecognitionCallback {
    fun onPrepared(status: RecognitionStatus)
    fun onBeginningOfSpeech()
    fun onKeywordDetected()
    fun onReadyForSpeech(params: Bundle)
    fun onBufferReceived(buffer: ByteArray)
    fun onRmsChanged(rmsdB: Float)
    fun onPartialResults(results: List<String>)
    fun onResults(results: String)
    fun onError(errorCode: Int)
    fun onEvent(eventType: Int, params: Bundle)
    fun onEndOfSpeech()
}