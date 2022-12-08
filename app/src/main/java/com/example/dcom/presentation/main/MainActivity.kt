package com.example.dcom.presentation.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.dcom.R
import com.example.dcom.extension.gone
import com.example.dcom.extension.show
import com.example.dcom.presentation.SettingActivity
import com.example.dcom.presentation.common.BaseFragment
import com.example.dcom.presentation.common.BaseView
import com.example.dcom.presentation.main.communication.CommunicationFragment
import com.example.dcom.presentation.main.favorite.FavoriteFragment
import com.example.dcom.presentation.main.history.HistoryFragment
import com.example.dcom.presentation.main.setting.SettingFragment
import com.example.dcom.presentation.widget.CustomViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity(), BaseView {

    private lateinit var cvpHomePager: CustomViewPager
    private lateinit var bnvMenu: BottomNavigationView
    private lateinit var constTopBar: ConstraintLayout
    private lateinit var tvTitle: TextView
    private lateinit var btnSetting: MaterialButton
    private lateinit var tabSelectBar: MaterialToolbar
    private lateinit var ablNormalTopBar: AppBarLayout
    private lateinit var btnOptions: Button

    private lateinit var pagerAdapter: MainViewPagerAdapter
    private val fragmentList = mutableListOf<BaseFragment>()
    private var communicationFragment = CommunicationFragment()
    private var favoriteFragment = FavoriteFragment()
    private var historyFragment = HistoryFragment()
    private var settingFragment = SettingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        onInitView()

        initBottomNavigation()
    }

    override fun onInitView() {
        setUpVariables()
        setUpOnClick()
        initViewPager()
        initBottomNavigation()
    }

    fun hideBottomNav() {
        bnvMenu.gone()
    }

    fun showBottomNav() {
        bnvMenu.show()
    }

    fun hideTopBar() {
        constTopBar.gone()
    }

    fun showTopBar() {
        constTopBar.show()
    }

    fun getMainTopBar(): AppBarLayout {
        return ablNormalTopBar
    }

    fun getSelectBar(): MaterialToolbar {
        return tabSelectBar
    }

    private fun setUpVariables() {
        cvpHomePager = findViewById(R.id.cvpMainPager)
        bnvMenu = findViewById(R.id.bnvMainMenu)
        constTopBar = findViewById(R.id.constMainTopBar)
        tvTitle = findViewById(R.id.tvMainTitle)
        btnSetting = findViewById(R.id.btnMainSetting)
        tabSelectBar = findViewById(R.id.mtbMainSelectTopBar)
        ablNormalTopBar = findViewById(R.id.ablMainNormalTopBar)
        btnOptions = findViewById(R.id.btnMainOptions)
    }

    private fun setUpOnClick() {
        btnSetting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        btnOptions.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
        btnOptions.setOnLongClickListener {
            // show content description

            true
        }
    }

    private fun initBottomNavigation() {
        bnvMenu.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.itmCommunication -> {
                    cvpHomePager.setCurrentItem(0, false)
                    tvTitle.text = getString(R.string.communication)
                    true
                }
                R.id.itmFavorite -> {
                    cvpHomePager.setCurrentItem(1, false)
                    tvTitle.text = getString(R.string.favorite)
                    true
                }
                R.id.itmHistory -> {
                    cvpHomePager.setCurrentItem(2, false)
                    tvTitle.text = getString(R.string.history)
                    true
                }
                else -> throw IllegalArgumentException("Invalid item id")
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
