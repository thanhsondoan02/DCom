package com.example.dcom.presentation.main.communication

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dcom.database.AppDatabase
import com.example.dcom.database.conversation.Conversation
import com.example.dcom.database.message.Message
import com.example.dcom.extension.loading
import com.example.dcom.extension.success
import com.example.dcom.thread.FlowResult
import com.example.dcom.thread.SpeechToTextUseCase
import com.example.dcom.thread.TextToSpeechUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CommunicationViewModel: ViewModel() {

    private val _textToSpeechState = MutableStateFlow(FlowResult.newInstance<Unit>())
    val textToSpeechState = _textToSpeechState.asStateFlow()

    private val _speechToTextState = MutableStateFlow(FlowResult.newInstance<String>())
    val speechToTextState = _speechToTextState.asStateFlow()

    var database: AppDatabase? = null

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

    fun saveTempConversation(list: List<Message>) {
        database?.iConversationDao()?.insertMessages(list)
    }

    fun saveNewConversation(list: List<Message>, name: String) {
        database?.iConversationDao()?.insertMessages(list)
        database?.iConversationDao()?.insertConversation(Conversation(name, list.last().id))
    }

    fun saveExistConversation(list: List<Message>, id: Int) {
        list.forEach {
            it.conversationId = id
        }
        database?.iConversationDao()?.insertMessages(list)
        database?.iConversationDao()?.updateConversation(id, list.last().id)
    }
}
