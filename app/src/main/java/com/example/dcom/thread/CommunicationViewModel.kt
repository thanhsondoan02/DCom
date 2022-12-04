package com.example.dcom.thread

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


    fun textToSpeech(text: String) {
        val rv = TextToSpeechUseCase.SendGiftRV(text)
        viewModelScope.launch {
            TextToSpeechUseCase().invoke(rv)
                .onStart {
                    _textToSpeechState.loading()
                }.collect {
                    _textToSpeechState.success(it)
                }
        }
    }

    fun speechToText() {
        viewModelScope.launch {
            SpeechToTextUseCase().invoke(BaseUseCase.VoidRequest())
                .onStart {
                    _speechToTextState.loading()
                }.collect {
                    _speechToTextState.success(it)
                }
        }
    }
}
