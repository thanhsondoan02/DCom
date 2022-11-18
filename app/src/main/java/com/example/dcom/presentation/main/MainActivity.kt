package com.example.dcom.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dcom.R
import com.example.dcom.presentation.common.BaseFragment
import com.example.dcom.presentation.common.BaseView
import com.example.dcom.presentation.main.communication.CommunicationFragment
import com.example.dcom.presentation.main.favorite.FavoriteFragment
import com.example.dcom.presentation.main.history.HistoryFragment
import com.example.dcom.presentation.main.setting.SettingFragment
import com.example.dcom.presentation.widget.CustomBottomNavigation
import com.example.dcom.presentation.widget.CustomViewPager

class MainActivity : AppCompatActivity(), BaseView {

    private lateinit var cvpHomePager: CustomViewPager
    private lateinit var bnvNavigator: CustomBottomNavigation

    private lateinit var pagerAdapter: MainViewPagerAdapter
    private val fragmentList = mutableListOf<BaseFragment>()
    private var communicationFragment = CommunicationFragment()
    private var favoriteFragment = FavoriteFragment()
    private var historyFragment = HistoryFragment()
    private var settingFragment = SettingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        onInitView()

        setNavigationListener()
    }

    override fun onInitView() {
        setUpVariables()
        initViewPager()
        setNavigationListener()
    }

    private fun setUpVariables() {
        cvpHomePager = findViewById(R.id.cvpHomePager)
        bnvNavigator = findViewById(R.id.cbnHomeNavigation)
    }

    private fun setNavigationListener() {
        bnvNavigator.listener = object : CustomBottomNavigation.IListener {
            override fun onClickItem(index: Int) {
                cvpHomePager.currentItem = index
            }
        }
    }

    private fun initViewPager() {
        pagerAdapter = MainViewPagerAdapter(supportFragmentManager)

        fragmentList.add(communicationFragment)
        fragmentList.add(favoriteFragment)
        fragmentList.add(historyFragment)
        fragmentList.add(settingFragment)
        pagerAdapter.addListFragment(fragmentList)

        cvpHomePager.apply {
            setPagingEnabled(false)
            adapter = pagerAdapter
            offscreenPageLimit = pagerAdapter.count
            currentItem = 0
        }
    }
}
