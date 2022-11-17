package com.example.dcom.presentation.main.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dcom.R
import com.example.dcom.presentation.widget.CustomBottomNavigation
import com.example.dcom.presentation.widget.CustomViewPager

class MainActivity : AppCompatActivity() {

    private lateinit var bnvNavigator: CustomBottomNavigation
    private lateinit var cvpHomePager: CustomViewPager

    private lateinit var pagerAdapter: MainViewPagerAdapter
    private val fragmentList = mutableListOf<BaseFragment>()
    private var communicationFragment = CommunicationFragment()
    private var fastComFragment = FastComFragment()
    private var history = HistoryFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        initView()

        setNavigatorListener()
    }

    private fun initView() {
        bnvNavigator = findViewById(R.id.cbnHomeNavigation)
        cvpHomePager = findViewById(R.id.cvpHomePager)

        initViewPager()
    }

    private fun setNavigatorListener() {

    }

    private fun initViewPager() {
        pagerAdapter = MainViewPagerAdapter(supportFragmentManager)


    }
}
