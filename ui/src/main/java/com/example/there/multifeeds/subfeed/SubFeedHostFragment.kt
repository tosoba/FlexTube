package com.example.there.multifeeds.subfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.multifeeds.R
import com.example.there.multifeeds.base.fragment.BaseHostFragment

class SubFeedHostFragment : BaseHostFragment() {

    override val backStackLayoutId: Int
        get() = R.id.sub_feed_back_stack_layout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sub_feed_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSubFeedFragment()
    }

    private fun showSubFeedFragment() {
        childFragmentManager.beginTransaction()
                .replace(R.id.sub_feed_back_stack_layout, SubFeedFragment())
                .commit()
    }
}
