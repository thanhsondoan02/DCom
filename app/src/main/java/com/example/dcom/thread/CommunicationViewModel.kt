package com.example.dcom.thread

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dcom.extension.loading
import com.example.dcom.extension.success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CommunicationViewModel: ViewModel() {

    private val _textToSpeechState = MutableStateFlow(FlowResult.newInstance<Unit>())
    val textToSpeechState = _textToSpeechState.asStateFlow()

    private val _speechToTextState = MutableStateFlow(FlowResult.newInstance<String>())
    val speechToTextState = _speechToTextState.asStateFlow()


    fun textToSpeech(text: String, context: Context) {
        val rv = TextToSpeechUseCase.TextToSpeechRV(text, context)
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
