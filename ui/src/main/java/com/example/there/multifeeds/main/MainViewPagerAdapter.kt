package com.example.there.multifeeds.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import com.example.there.multifeeds.groups.GroupsHostFragment
import com.example.there.multifeeds.home.HomeHostFragment
import com.example.there.multifeeds.subfeed.SubFeedHostFragment
import com.example.there.multifeeds.util.ext.mainActivity


class MainViewPagerAdapter(
        fm: FragmentManager
) : FragmentStatePagerAdapter(fm) {

    private val fragments = arrayOf(HomeHostFragment(), SubFeedHostFragment(), GroupsHostFragment())

    var currentFragment: Fragment? = null
        private set

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        currentFragment = `object` as? Fragment

        currentFragment?.let {
            with(it.mainActivity) {
                this?.updateToolbarTitle(it)
                this?.updateToolbarBackNavigation(it)
            }
        }

        super.setPrimaryItem(container, position, `object`)
    }
}