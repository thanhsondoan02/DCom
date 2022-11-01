package com.example.dcom.presentation.main.history

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R

class ConversationHistoryActivity : AppCompatActivity() {

    private lateinit var tvActionBarTitle: TextView
    private lateinit var btnGoBack: ImageButton
    private lateinit var rvListMessage: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.conversation_history_activity)

        initView()
        initRecyclerView()

        btnGoBack.setOnClickListener { onBackPressed() }
    }

    private fun initView() {
        tvActionBarTitle = findViewById(R.id.tvActionBarTitle)
        btnGoBack = findViewById(R.id.btnActionBarGoBack)
        rvListMessage = findViewById(R.id.rvConversationHistory)

        tvActionBarTitle.text = getString(R.string.conversation_history)
    }

    private fun initRecyclerView() {
        rvListMessage.adapter = ConversationHistoryAdapter(this@ConversationHistoryActivity)
        rvListMessage.layoutManager = LinearLayoutManager(this@ConversationHistoryActivity)
    }
}
