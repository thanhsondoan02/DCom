package com.example.dcom.presentation.main.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dcom.R
import com.example.dcom.presentation.widget.CustomViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var bnvNavigator: BottomNavigationView
    private lateinit var cvpHomePager: CustomViewPager

    private lateinit var adapter: HomeAdapter
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
        bnvNavigator = findViewById(R.id.bnvHomeNavigator)
        cvpHomePager = findViewById(R.id.cvpHomePager)

        initViewPager()
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

    private fun initViewPager() {
        adapter = HomeAdapter(supportFragmentManager)

        cvpHomePager.setPagingEnabled(false)
        fragmentList.add(communicationFragment)
        fragmentList.add(fastComFragment)
        adapter.addListFragment(fragmentList)
        cvpHomePager.adapter = adapter
        binding.vpMain.offscreenPageLimit = adapter.count
        binding.vpMain.currentItem = 0

    }
}
