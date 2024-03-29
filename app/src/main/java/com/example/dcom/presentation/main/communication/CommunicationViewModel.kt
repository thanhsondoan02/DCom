package com.example.dcom.presentation.main.communication

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dcom.base.event.ConversationEvent
import com.example.dcom.base.event.EventBusManager
import com.example.dcom.database.AppDatabase
import com.example.dcom.database.conversation.Conversation
import com.example.dcom.database.message.Message
import com.example.dcom.database.note.Note
import com.example.dcom.extension.loading
import com.example.dcom.extension.success
import com.example.dcom.repo.ICommunicationRepoCallback
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
    private val mainHandler: Handler = Handler(Looper.getMainLooper())


    lateinit var database: AppDatabase
    var createdTime: Long = 0

    fun textToSpeech(text: String, context: Context, target: CommunicationFragment) {
        val rv = TextToSpeechUseCase.TextToSpeechRV(text, context, callback = object : ICommunicationRepoCallback{
            override fun onSpeakStart(){
                mainHandler.post {
                    target.stopRecognition()
                }
            }
            override fun onSpeakSuccess() {
                mainHandler.post {
                    target.startRecognition()
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

    fun deleteTempConversation() {
        database.iConversationDao().deleteMessageInConversation(-1)
    }

    fun saveTempConversation(list: List<Message>) {
        deleteTempConversation()
        database.iConversationDao().insertMessages(list)
    }

    fun getTempConversation(): List<Message> {
        return database.iConversationDao().getAllMessageInConversation(-1)
    }

    fun saveNewConversation(list: List<Message>, name: String) {
        deleteTempConversation()
        database.iConversationDao().apply {
            insertConversation(Conversation(name, createdTime!!))
            val id = getLatestConversation().id
            list.forEach {
                it.conversationId = id
            }
            insertMessages(list)
            EventBusManager.instance?.postPending(ConversationEvent(ConversationEvent.STATUS.ADD, -1, id))
        }
    }

    fun setCreateTime() {
        createdTime = System.currentTimeMillis()
    }

    fun searchNote(keyword: String): List<Note> {
        return database.iNoteDao().search(keyword)
    }
}
