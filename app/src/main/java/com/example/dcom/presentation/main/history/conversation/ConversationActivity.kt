package com.example.dcom.presentation.main.history.conversation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R

class ConversationActivity : AppCompatActivity() {

    private lateinit var rvListMessage: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.conversation_activity)

        initView()
        initRecyclerView()
    }

    private fun initView() {
        rvListMessage = findViewById(R.id.rvConversationHistory)
    }

    private fun initRecyclerView() {
        rvListMessage.adapter = ConversationAdapter()
        rvListMessage.layoutManager = LinearLayoutManager(this)
    }
}
