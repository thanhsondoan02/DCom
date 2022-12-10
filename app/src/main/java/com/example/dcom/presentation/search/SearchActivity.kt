package com.example.dcom.presentation.search

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.base.event.*
import com.example.dcom.database.AppDatabase
import com.example.dcom.database.conversation.Conversation
import com.example.dcom.database.note.Note
import com.example.dcom.extension.IViewListener
import com.example.dcom.extension.handleUiState
import com.example.dcom.presentation.common.BaseView
import com.example.dcom.presentation.conversation.ConversationActivity
import com.example.dcom.presentation.note.NoteActivity
import com.google.android.material.appbar.MaterialToolbar
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SearchActivity : AppCompatActivity(), BaseView, IEventHandler {

    private lateinit var mtbToolbar: MaterialToolbar
    private lateinit var edtSearch: EditText
    private lateinit var rvContent: RecyclerView

    private lateinit var database: AppDatabase
    private val adapter by lazy {
        SearchAdapter()
    }

    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        database = AppDatabase.getInstance(this)

        onPrepareInitView()
        onInitView()
        onObserverViewModel()
    }

    override fun onInitView() {
        setUpVariables()
        setUpListener()
        setUpRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        EventBusManager.instance?.register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBusManager.instance?.unregister(this)
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        lifecycleScope.launchWhenCreated {
            viewModel.textToSpeechState.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        Toast.makeText(this@SearchActivity, getString(R.string.speak_success), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    override fun onEvent(event: IEvent) {
        when (event) {
            is NoteEvent -> {
                when (event.status) {
                    NoteEvent.STATUS.ADD -> {
                    }
                    NoteEvent.STATUS.EDIT -> {
                        adapter.updateById(event.noteId!!, SearchAdapter.SearchDisplay(note = database.iNoteDao().get(event.noteId)))
                    }
                    NoteEvent.STATUS.DELETE -> {
                        adapter.removeById(event.noteId!!)
                    }
                }
                EventBusManager.instance?.removeSticky(event)
            }
            is ConversationEvent -> {
                when (event.status) {
                    ConversationEvent.STATUS.ADD -> {
                    }
                    ConversationEvent.STATUS.EDIT -> {
                        adapter.updateById(event.id, SearchAdapter.SearchDisplay(conversation = database.iConversationDao().getConversationById(event.id)))
                    }
                    ConversationEvent.STATUS.DELETE -> {
                        adapter.removeById(event.id)
                    }
                }
                EventBusManager.instance?.removeSticky(event)
            }
        }
    }

    private fun setUpVariables() {
        mtbToolbar = findViewById(R.id.mtbSearchTopBar)
        edtSearch = findViewById(R.id.edtSearch)
        rvContent = findViewById(R.id.rvSearchContent)
    }

    private fun setUpListener() {
        mtbToolbar.setNavigationOnClickListener { finish() }

        mtbToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.itmSearchClear -> {
                    edtSearch.text.clear()
                    true
                }
                else -> false
            }
        }

        edtSearch.doOnTextChanged { text, start, before, count ->
            mtbToolbar.menu.findItem(R.id.itmSearchClear).isVisible = text.toString().isNotEmpty()
            showSearchResult(text.toString())
        }
    }

    private fun setUpRecyclerView() {
        rvContent.adapter = adapter.apply {
            listener = object : SearchAdapter.IListener {
                override fun onConversationClick(conversation: Conversation, position: Int) {
                    startActivity(Intent(this@SearchActivity, ConversationActivity::class.java).apply {
                        putExtra(ConversationActivity.CONVERSATION_ID, conversation.id)
                    })
                }

                override fun onNoteClick(note: Note?, position: Int) {
                    speak(note?.content)
                }

                override fun onNoteLongClick(note: Note?, position: Int) {
                    startActivity(Intent(this@SearchActivity, NoteActivity::class.java).apply {
                        putExtra(NoteActivity.NOTE_ID, note?.id)
                        putExtra(NoteActivity.NOTE_POSITION, position)
                    })
                }
            }
        }
        rvContent.layoutManager = LinearLayoutManager(this)
        showSearchResult("")
    }

    private fun speak(text: String?) {
        if (text != null) {
            viewModel.textToSpeech(text, this)
        } else {
            Toast.makeText(this, "Text is null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSearchResult(keyword: String) {
        val notes = database.iNoteDao().search(keyword)
        val conversations = database.iConversationDao().searchConversationByName(keyword)

        val searchDisplay = mutableListOf<SearchAdapter.SearchDisplay>()

        notes.forEach { note ->
            searchDisplay.add(SearchAdapter.SearchDisplay(note = note))
        }

        conversations.forEach { conversation ->
            searchDisplay.add(SearchAdapter.SearchDisplay(conversation = conversation))
        }

        adapter.clearItems()
        adapter.addItems(searchDisplay)
    }

}
