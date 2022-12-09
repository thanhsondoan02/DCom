package com.example.dcom.presentation.main.history

import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.base.event.ComversationEvent
import com.example.dcom.base.event.EventBusManager
import com.example.dcom.base.event.IEvent
import com.example.dcom.base.event.IEventHandler
import com.example.dcom.database.AppDatabase
import com.example.dcom.presentation.common.BaseFragment
import com.example.dcom.presentation.main.history.conversation.ConversationActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HistoryFragment : BaseFragment(R.layout.history_fragment), IEventHandler {

    private lateinit var rvContent: RecyclerView
    private val historyAdapter by lazy { HistoryAdapter() }
    private val database by lazy {
        AppDatabase.getInstance(requireContext())
    }

    override fun onStart() {
        super.onStart()
        EventBusManager.instance?.register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBusManager.instance?.unregister(this)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    override fun onEvent(event: IEvent) {
        when (event) {
            is ComversationEvent -> {
                when (event.status) {
                    ComversationEvent.STATUS.ADD -> {
                        historyAdapter.addItem(HistoryAdapter.ConversationDisplay(database.iConversationDao().getConversationById(event.id)))
                    }
                    ComversationEvent.STATUS.EDIT -> {
                    }
                    ComversationEvent.STATUS.DELETE -> {
                    }
                }
                EventBusManager.instance?.removeSticky(event)
            }
        }
    }

    override fun onInitView() {
        super.onInitView()

        setUpVariables()
        setUpOnClick()
        setUpRecyclerView()
    }

    private fun setUpVariables() {
        rvContent = requireView().findViewById(R.id.rvHistoryContent)
    }

    private fun setUpOnClick() {
        historyAdapter.listener = object: HistoryAdapter.IListener {
            override fun onClickConversation(id: Int?, name: String?) {
                if (id != null) {
                    startActivity(Intent(requireContext(), ConversationActivity::class.java).apply {
                        putExtra(ConversationActivity.CONVERSATION_ID, id)
                        putExtra(ConversationActivity.CONVERSATION_NAME, name)
                    })
                } else {
                    Toast.makeText(requireContext(), getString(R.string.cant_find_conversation), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        rvContent.layoutManager = LinearLayoutManager(requireContext())
        rvContent.adapter = historyAdapter

        getConversationFromDatabase()
    }

    private fun getConversationFromDatabase() {
        val listConversationDisplay = mutableListOf<HistoryAdapter.ConversationDisplay>()
        database.iConversationDao().getAll().forEach { conversation ->
            val message = database.iConversationDao().getLatestMessageInConversation(conversation.id)
            listConversationDisplay.add(HistoryAdapter.ConversationDisplay(conversation, message))
        }
        historyAdapter.addItems(listConversationDisplay)
    }

    private fun checkData() {
        val list = database.iConversationDao().getAll()
        val listConversationDisplay = mutableListOf<HistoryAdapter.ConversationDisplay>()
        if (list.size > historyAdapter.itemCount) {
            // add new data
            list.subList(historyAdapter.itemCount, list.size - 1).forEach { conversation ->
                val message = database.iConversationDao().getLatestMessageInConversation(conversation.id)
                listConversationDisplay.add(HistoryAdapter.ConversationDisplay(conversation, message))
            }
        }
        historyAdapter.addItems(listConversationDisplay)
    }

}
