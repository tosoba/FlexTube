package com.example.there.flextube.base

import android.support.v4.app.Fragment
import com.example.there.flextube.search.SearchFragment
import com.example.there.flextube.util.ext.mainToolbar
import com.example.there.flextube.util.ext.resetTitle

abstract class BaseHostFragment : Fragment() {

    abstract val backStackLayoutId: Int

    fun showSearchFragment(query: String) {
        mainToolbar?.resetTitle(query)

        childFragmentManager.beginTransaction()
                .replace(backStackLayoutId, SearchFragment.newInstance(query))
                .addToBackStack(null)
                .commit()
    }
}