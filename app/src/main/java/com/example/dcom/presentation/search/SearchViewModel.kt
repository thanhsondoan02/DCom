package com.example.dcom.presentation.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dcom.extension.loading
import com.example.dcom.extension.success
import com.example.dcom.repo.ICommunicationRepoCallback
import com.example.dcom.thread.FlowResult
import com.example.dcom.thread.TextToSpeechUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {

    private val _textToSpeechState = MutableStateFlow(FlowResult.newInstance<Unit>())
    val textToSpeechState = _textToSpeechState.asStateFlow()

    fun textToSpeech(text: String, context: Context) {
        val rv = TextToSpeechUseCase.TextToSpeechRV(text, context, callback = object :
            ICommunicationRepoCallback {
            override fun onSpeakStart(){

            }
            override fun onSpeakSuccess() {

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

}
