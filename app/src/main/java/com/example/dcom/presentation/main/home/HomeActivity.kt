package com.example.dcom.presentation.main.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R
import com.example.dcom.presentation.main.emergency.EmergencyActivity
import com.example.dcom.presentation.main.fastcom.FastComActivity
import com.example.dcom.presentation.main.history.ConversationHistoryActivity
import com.example.dcom.presentation.main.speechtotext.SpeechToTextActivity
import com.example.dcom.presentation.main.texttospeech.TextToSpeechActivity

class HomeActivity : AppCompatActivity() {

    interface IListener {
        fun onTextToSpeechClick()
        fun onSpeechToTextClick()
        fun onConversationHistoryClick()
        fun onFastCommunicationClick()
        fun onEmergencyClick()
    }

    lateinit var listener: IListener

    private lateinit var rvHome: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        initView()
        initRecyclerView()
        initListener()
    }

    private fun initView() {
        rvHome = findViewById(R.id.rvHomeContent)
    }

    private fun initRecyclerView() {
        rvHome.adapter = HomeAdapter(this@HomeActivity)
        rvHome.layoutManager = LinearLayoutManager(this)
    }

    private fun initListener() {
        listener = object : IListener {
            override fun onTextToSpeechClick() {
                startActivity(Intent(this@HomeActivity, TextToSpeechActivity::class.java))
            }

            override fun onSpeechToTextClick() {
                startActivity(Intent(this@HomeActivity, SpeechToTextActivity::class.java))
            }

            override fun onConversationHistoryClick() {
                startActivity(Intent(this@HomeActivity, ConversationHistoryActivity::class.java))
            }

            override fun onFastCommunicationClick() {
                startActivity(Intent(this@HomeActivity, FastComActivity::class.java))
            }

            override fun onEmergencyClick() {
                startActivity(Intent(this@HomeActivity, EmergencyActivity::class.java))
            }
        }
    }
}
