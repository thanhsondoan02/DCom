package com.example.dcom.presentation.main.history.conversation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.database.AppDatabase
import com.example.dcom.presentation.common.BaseView

class ConversationActivity : AppCompatActivity(), BaseView {

    companion object {
        const val CONVERSATION_ID = "conversation_id"
    }

    private var id: Int? = null
    private lateinit var rvListMessage: RecyclerView
    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }
    private val conversationAdapter by lazy {
        ConversationAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onPrepareInitView()
        setContentView(R.layout.conversation_activity)
        onInitView()
    }

    override fun onPrepareInitView() {
        super.onPrepareInitView()
        id = intent.getIntExtra(CONVERSATION_ID, -1)
    }

    override fun onInitView() {
        setUpVariables()
        setUpOnClick()
        setUpRecyclerView()
    }

    private fun setUpVariables() {
        rvListMessage = findViewById(R.id.rvConversationHistory)
    }

    private fun setUpOnClick() {

    }

    private fun setUpRecyclerView() {
        rvListMessage.adapter = conversationAdapter
        rvListMessage.layoutManager = LinearLayoutManager(this)

        getConversationData()
    }

    private fun getConversationData() {
        if (id != null && id != -1) {
            conversationAdapter.addList(database.iConversationDao().getAllMessageInConversation(id!!))
        }
    }

}
