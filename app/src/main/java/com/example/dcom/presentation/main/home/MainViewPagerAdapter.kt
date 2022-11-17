@file:Suppress("DEPRECATION")

package com.example.dcom.presentation.main.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MainViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private var fragmentList = mutableListOf<BaseFragment>()

    override fun getCount() = fragmentList.size

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addListFragment(fragmentList: List<BaseFragment>) {
        this.fragmentList.clear()
        this.fragmentList.addAll(fragmentList)
    }
}
