package com.example.there.flextube.base

import android.support.v4.app.Fragment
import com.example.there.flextube.search.SearchFragment

abstract class BaseHostFragment: Fragment() {
    abstract val backStackLayoutId: Int

    fun showSearchFragment(query: String) {
        childFragmentManager.beginTransaction()
                .replace(backStackLayoutId, SearchFragment.newInstance(query))
                .addToBackStack(null)
                .commit()
    }
}