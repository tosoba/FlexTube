package com.example.there.flextube.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.flextube.R
import com.example.there.flextube.base.BaseHostFragment
import com.example.there.flextube.groups.group.GroupFragment
import com.example.there.flextube.groups.list.GroupsListFragment
import com.example.there.flextube.model.UiGroup


class GroupsHostFragment : BaseHostFragment() {

    override val backStackLayoutId: Int
        get() = R.id.groups_back_stack_layout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_groups_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showGroupListFragment()
    }

    private fun showGroupListFragment() {
        childFragmentManager.beginTransaction()
                .replace(R.id.groups_back_stack_layout, GroupsListFragment())
                .commit()
    }

    fun showGroupFragment(group: UiGroup) {
        childFragmentManager.beginTransaction()
                .replace(R.id.groups_back_stack_layout, GroupFragment.newInstance(group))
                .addToBackStack(null)
                .commit()
    }
}
