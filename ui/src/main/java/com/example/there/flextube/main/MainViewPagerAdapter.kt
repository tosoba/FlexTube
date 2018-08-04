package com.example.there.flextube.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import com.example.there.flextube.groups.GroupsHostFragment
import com.example.there.flextube.home.HomeHostFragment
import com.example.there.flextube.subfeed.SubFeedHostFragment
import com.example.there.flextube.util.ext.mainActivity


class MainViewPagerAdapter(
        fm: FragmentManager
) : FragmentStatePagerAdapter(fm) {
    private val fragments = arrayOf(HomeHostFragment(), SubFeedHostFragment(), GroupsHostFragment())

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    var currentFragment: Fragment? = null
        private set

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        currentFragment = `object` as? Fragment

        currentFragment?.let { it.mainActivity?.updateTitle(it) }

        super.setPrimaryItem(container, position, `object`)
    }
}