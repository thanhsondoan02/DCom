package com.example.dcom.presentation.main.communication

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.core.content.getSystemService

class RecognitionManager(
    private val context: Context,
    private val shouldMute: Boolean = false,
    private val callback: RecognitionCallback? = null
) : RecognitionListener {
    private var isActivated: Boolean = true
    private val speech: SpeechRecognizer by lazy { SpeechRecognizer.createSpeechRecognizer(context) }
    private val audioManager: AudioManager? = context.getSystemService()

    private val recognizerIntent by lazy {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi")
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
        }
    }

    fun createRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speech.setRecognitionListener(this)
            callback?.onPrepared(RecognitionStatus.SUCCESS)
        } else {
            callback?.onPrepared(RecognitionStatus.UNAVAILABLE)
        }
    }

    fun destroyRecognizer() {
        muteRecognition(false)
        speech.destroy()
    }

    fun startRecognition() {
        isActivated = true
        speech.startListening(recognizerIntent)
    }

    fun stopRecognition() {
        isActivated = false
        speech.stopListening()

    }

    fun cancelRecognition() {
        speech.cancel()
    }

    @Suppress("DEPRECATION")
    private fun muteRecognition(mute: Boolean) {
//        audioManager?.let {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                val flag = if (mute) AudioManager.ADJUST_MUTE else AudioManager.ADJUST_UNMUTE
//                it.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, flag, 0)
//                it.adjustStreamVolume(AudioManager.STREAM_ALARM, flag, 0)
////                it.adjustStreamVolume(AudioManager.STREAM_MUSIC, flag, 0)
//                it.adjustStreamVolume(AudioManager.STREAM_RING, flag, 0)
//                it.adjustStreamVolume(AudioManager.STREAM_SYSTEM, flag, 0)
//            } else {
//                it.setStreamMute(AudioManager.STREAM_NOTIFICATION, mute)
//                it.setStreamMute(AudioManager.STREAM_ALARM, mute)
////                it.setStreamMute(AudioManager.STREAM_MUSIC, mute)
//                it.setStreamMute(AudioManager.STREAM_RING, mute)
//                it.setStreamMute(AudioManager.STREAM_SYSTEM, mute)
//            }
//        }
    }

    override fun onBeginningOfSpeech() {
        callback?.onBeginningOfSpeech()
    }

    override fun onReadyForSpeech(params: Bundle) {
        muteRecognition(shouldMute)
        callback?.onReadyForSpeech(params)
    }

    override fun onBufferReceived(buffer: ByteArray) {
        callback?.onBufferReceived(buffer)
    }

    override fun onRmsChanged(rmsdB: Float) {
        callback?.onRmsChanged(rmsdB)
    }

    override fun onEndOfSpeech() {
        callback?.onEndOfSpeech()
    }

    override fun onError(errorCode: Int) {
        callback?.onError(errorCode)
        if(isActivated) {
            startRecognition()
        }
    }

    override fun onEvent(eventType: Int, params: Bundle) {
        callback?.onEvent(eventType, params)
    }

    override fun onPartialResults(partialResults: Bundle) {
        val matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (matches != null) {
            callback?.onPartialResults(matches)
        }
    }

    override fun onResults(results: Bundle) {
        val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (matches != null) {
//            println(callback)
            callback?.onResults(matches[0])
//            println("onResults: ${matches[0]}")
            if(isActivated) {
                startRecognition()
            }
        }
    }

}