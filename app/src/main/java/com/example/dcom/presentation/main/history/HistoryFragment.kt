package com.example.dcom.presentation.main.history

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.presentation.common.BaseFragment
import com.example.dcom.presentation.main.history.conversation.ConversationActivity

class HistoryFragment : BaseFragment(R.layout.history_fragment) {

    private lateinit var rvContent: RecyclerView
    private val historyAdapter by lazy { HistoryAdapter() }

    override fun onInitView() {
        super.onInitView()

        setUpVariables()
        setUpOnClick()
        setUpRecyclerView()

        // fake data
        historyAdapter.addData(mockHistoryData())
    }

    private fun setUpVariables() {
        rvContent = requireView().findViewById(R.id.rvHistoryContent)
    }

    private fun setUpOnClick() {
        historyAdapter.listener = object: HistoryAdapter.IListener {
            override fun onClickConversation(id: String?) {
                if (id != null) {
                    val intent = Intent(requireContext(), ConversationActivity::class.java)
                    intent.putExtra("id", id)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        rvContent.layoutManager = LinearLayoutManager(requireContext())
        rvContent.adapter = historyAdapter
    }

    private fun mockHistoryData(): MutableList<Any> {
        val ans = mutableListOf<Any>()

        // common
        val listCommonConversation = mutableListOf<HistoryAdapter.ConversationData>()
        for (i in 0 until 10) {
            listCommonConversation.add(randomConversation())
        }
        ans.add(listCommonConversation)

        // conversation
        for (i in 0 until 10) {
            ans.add(randomConversation())
        }

        return ans
    }

    private fun randomConversation(): HistoryAdapter.ConversationData {
        return HistoryAdapter.ConversationData().apply {
            id = "temp"
            avatar = ColorDrawable(Color.rgb(randomColor(), randomColor(), randomColor()))
            title = "Title"
            latestMessage = "Son: shut the fuck up"
        }
    }

    private fun randomColor(): Int {
        return (0 until 256).random()
    }

}
