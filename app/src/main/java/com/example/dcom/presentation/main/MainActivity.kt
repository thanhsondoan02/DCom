package com.example.dcom.presentation.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.example.dcom.R
import com.example.dcom.extension.gone
import com.example.dcom.extension.hide
import com.example.dcom.extension.show
import com.example.dcom.presentation.common.BaseFragment
import com.example.dcom.presentation.common.BaseView
import com.example.dcom.presentation.main.communication.CommunicationFragment
import com.example.dcom.presentation.main.favorite.FavoriteFragment
import com.example.dcom.presentation.main.history.HistoryFragment
import com.example.dcom.presentation.main.setting.SettingFragment
import com.example.dcom.presentation.widget.CustomViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.materialswitch.MaterialSwitch

class MainActivity : AppCompatActivity(), BaseView {

    var isRecordGranted = false

    private lateinit var cvpHomePager: CustomViewPager
    private lateinit var bnvMenu: BottomNavigationView
    private lateinit var constTopBar: ConstraintLayout
    private lateinit var tvTitle: TextView
    private lateinit var btnRight1: Button
    private lateinit var btnRight2: Button
    private lateinit var btnRight3: MaterialSwitch

    private lateinit var pagerAdapter: MainViewPagerAdapter
    private val fragmentList = mutableListOf<BaseFragment>()
    private var communicationFragment = CommunicationFragment()
    private var favoriteFragment = FavoriteFragment()
    private var historyFragment = HistoryFragment()
    private var settingFragment = SettingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        askRecordAudioPermission()

        onInitView()

        initBottomNavigation()
    }

    override fun onInitView() {
        setUpVariables()
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

    private fun setUpVariables() {
        cvpHomePager = findViewById(R.id.cvpMainPager)
        bnvMenu = findViewById(R.id.bnvMainMenu)
        constTopBar = findViewById(R.id.constMainTopBar)
        tvTitle = findViewById(R.id.tvMainTitle)
        btnRight1 = findViewById(R.id.btnMainRight)
        btnRight2 = findViewById(R.id.btnMainRight2)
        btnRight3 = findViewById(R.id.btnMainRight3)
    }

    private fun initBottomNavigation() {
        bnvMenu.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.itmCommunication -> {
                    cvpHomePager.setCurrentItem(0, false)
                    tvTitle.text = getString(R.string.communication)
                    btnRight1.show()
                    btnRight2.show()
                    btnRight3.show()
                    true
                }
                R.id.itmFavorite -> {
                    cvpHomePager.setCurrentItem(1, false)
                    tvTitle.text = getString(R.string.favorite)
                    btnRight1.hide()
                    btnRight2.hide()
                    btnRight3.hide()
                    true
                }
                R.id.itmHistory -> {
                    cvpHomePager.setCurrentItem(2, false)
                    tvTitle.text = getString(R.string.history)
                    btnRight1.hide()
                    btnRight2.hide()
                    btnRight3.hide()
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

    private fun askRecordAudioPermission() {
        if (!isRecordAudioPermissionGranted()) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
        }
        isRecordGranted = isRecordAudioPermissionGranted()
    }

    fun isRecordAudioPermissionGranted(): Boolean {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED
        )
    }
}
