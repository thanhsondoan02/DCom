package com.example.dcom.thread

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dcom.extension.loading
import com.example.dcom.extension.success
import com.example.dcom.presentation.main.communication.CommunicationFragment
import com.example.dcom.repo.ICommunicationRepoCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CommunicationViewModel: ViewModel() {

    private val _textToSpeechState = MutableStateFlow(FlowResult.newInstance<Unit>())
    val textToSpeechState = _textToSpeechState.asStateFlow()

    private val _speechToTextState = MutableStateFlow(FlowResult.newInstance<String>())
    val speechToTextState = _speechToTextState.asStateFlow()
    private val mainHandler: Handler = Handler(Looper.getMainLooper())



    fun textToSpeech(text: String, context: Context, target: CommunicationFragment) {
        val rv = TextToSpeechUseCase.TextToSpeechRV(text, context, callback = object :
            ICommunicationRepoCallback {
            override fun onSpeakStart(){
                mainHandler.post {
                    target.getRecognitionManager().stopRecognition()
                }
            }
            override fun onSpeakSuccess() {
                mainHandler.post {
                    target.getRecognitionManager().startRecognition()
                }
            }
        })
        viewModelScope.launch {
            TextToSpeechUseCase().invoke(rv)
                .onStart {
                    _textToSpeechState.loading()
                }.collect {
                    _textToSpeechState.success(it)
                }
        }
    }

    fun speechToText(context: Context) {
        viewModelScope.launch {
            val rv = SpeechToTextUseCase.SpeechToTextRV(context)
            SpeechToTextUseCase().invoke(rv)
                .onStart {
                    _speechToTextState.loading()
                }.collect {
                    _speechToTextState.success(it)
                }
        }
    }

}
