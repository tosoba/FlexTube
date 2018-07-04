package com.example.there.flextube.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.there.flextube.groups.GroupsFragment
import com.example.there.flextube.home.HomeFragment
import com.example.there.flextube.subfeed.SubFeedFragment

class MainViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val fragments = arrayOf(HomeFragment(), SubFeedFragment(), GroupsFragment())

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size
}