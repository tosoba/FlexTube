package com.example.there.flextube.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.flextube.R
import com.example.there.flextube.base.BaseHostFragment


class HomeHostFragment : BaseHostFragment() {
    override val backStackLayoutId: Int
        get() = R.id.home_back_stack_layout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showHomeFragment()
    }

    private fun showHomeFragment() {
        childFragmentManager.beginTransaction()
                .replace(R.id.home_back_stack_layout, HomeFragment())
                .commit()
    }
}
