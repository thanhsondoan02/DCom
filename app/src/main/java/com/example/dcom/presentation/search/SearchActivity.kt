package com.example.dcom.presentation.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dcom.R
import com.example.dcom.database.AppDatabase
import com.example.dcom.presentation.common.BaseView

class SearchActivity : AppCompatActivity(), BaseView {

    companion object {
        const val NOTE_ID = "NOTE_ID"
        const val NOTE_POSITION = "NOTE_POSITION"
    }

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)

        database = AppDatabase.getInstance(this)

        onPrepareInitView()
        onInitView()
    }

    override fun onInitView() {
        setUpVariables()
        setUpListener()
    }

    private fun setUpVariables() {
    }

    private fun setUpListener() {
    }

}
