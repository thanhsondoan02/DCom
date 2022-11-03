package com.example.dcom.presentation.main.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.dcom.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var bnvNavigator: BottomNavigationView
    private lateinit var vpHomePager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        initView()

        setNavigatorListener()
    }

    private fun initView() {
        bnvNavigator = findViewById(R.id.bnvHomeNavigator)
        vpHomePager = findViewById(R.id.vpHomePager)
    }

    private fun setNavigatorListener() {
        bnvNavigator.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.itmCommunication -> {
                    return@setOnItemSelectedListener true
                }
                R.id.itmConversationHistory -> {
                    return@setOnItemSelectedListener true
                }
                R.id.itmFastCom -> {
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }
    }

}
