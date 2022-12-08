package com.example.dcom.presentation.main.history

import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.database.AppDatabase
import com.example.dcom.presentation.common.BaseFragment
import com.example.dcom.presentation.main.history.conversation.ConversationActivity

class HistoryFragment : BaseFragment(R.layout.history_fragment) {

    private lateinit var rvContent: RecyclerView
    private val historyAdapter by lazy { HistoryAdapter() }
    private val database by lazy {
        AppDatabase.getInstance(requireContext())
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
        historyAdapter.addData(listConversationDisplay)
    }

}
