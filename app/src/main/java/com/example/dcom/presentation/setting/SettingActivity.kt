package com.example.dcom.presentation.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dcom.R
import com.example.dcom.presentation.common.BaseView
import com.google.android.material.appbar.MaterialToolbar

class SettingActivity : AppCompatActivity(), BaseView {

    private lateinit var mtbTopBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_activity)
        onInitView()
    }

    override fun onInitView() {
        setUpVariables()
        setUpOnClick()
        initRecyclerView()
    }

    private fun setUpVariables() {
        mtbTopBar = findViewById(R.id.mtbSettingTopBar)
    }

    private fun setUpOnClick() {
        mtbTopBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initRecyclerView() {

    }
}
