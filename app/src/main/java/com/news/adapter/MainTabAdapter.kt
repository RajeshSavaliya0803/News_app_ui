package com.news.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.news.fragment.TodayFragment
import com.news.fragment.WorldFragment


class MainTabAdapter(fm: FragmentManager, var totalTabs: Int) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                TodayFragment()
            }
            1 -> {
                WorldFragment()
            }
            2 -> {
                WorldFragment()
            }
            3 -> {
                WorldFragment()
            }
            4 -> {
                WorldFragment()
            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}