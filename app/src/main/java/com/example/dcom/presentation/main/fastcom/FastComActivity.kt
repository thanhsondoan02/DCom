package com.example.dcom.presentation.main.fastcom

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dcom.R

class FastComActivity : AppCompatActivity() {

    private lateinit var tvActionBarTitle: TextView
    private lateinit var btnGoBack: ImageButton
    private lateinit var rvListText: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fast_com_activity)

        initView()
        initRecyclerView()

        btnGoBack.setOnClickListener { onBackPressed() }
    }

    private fun initView() {
        tvActionBarTitle = findViewById(R.id.tvActionBarTitle)
        btnGoBack = findViewById(R.id.btnActionBarGoBack)
        rvListText = findViewById(R.id.rvFastCom)

        tvActionBarTitle.text = getString(R.string.fast_communication)
    }

    private fun initRecyclerView() {
        rvListText.adapter = FastComAdapter(this)
        rvListText.layoutManager = LinearLayoutManager(this)
    }

}
