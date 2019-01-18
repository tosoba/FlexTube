package com.example.there.multifeeds.base.fragment

import android.support.v4.app.Fragment
import com.example.there.multifeeds.search.SearchFragment
import com.example.there.multifeeds.util.ext.mainActivity
import com.example.there.multifeeds.util.ext.mainToolbar
import com.example.there.multifeeds.util.ext.resetTitle

abstract class BaseHostFragment : Fragment() {

    abstract val backStackLayoutId: Int

    fun showSearchFragment(query: String) {
        mainToolbar?.resetTitle(query)
        mainActivity?.addBackNavigationToToolbar()

        childFragmentManager.beginTransaction()
                .replace(backStackLayoutId, SearchFragment.newInstance(query))
                .addToBackStack(null)
                .commit()
    }
}