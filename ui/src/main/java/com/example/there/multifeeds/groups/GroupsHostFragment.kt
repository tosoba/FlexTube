package com.example.there.multifeeds.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.multifeeds.R
import com.example.there.multifeeds.base.fragment.BaseHostFragment
import com.example.there.multifeeds.groups.group.GroupFragment
import com.example.there.multifeeds.groups.list.GroupsListFragment
import com.example.there.multifeeds.model.UiGroupWithSubscriptions
import com.example.there.multifeeds.util.ext.mainActivity
import com.example.there.multifeeds.util.ext.mainToolbar
import com.example.there.multifeeds.util.ext.resetTitle


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

    fun showGroupFragment(group: UiGroupWithSubscriptions) {
        val groupFragment = GroupFragment.newInstance(group)
        mainToolbar?.resetTitle(groupFragment.title)

        mainActivity?.addBackNavigationToToolbar()

        childFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom)
                .replace(R.id.groups_back_stack_layout, groupFragment)
                .addToBackStack(null)
                .commit()
    }

    private fun showGroupListFragment() {
        val groupsListFragment = GroupsListFragment()
        mainToolbar?.resetTitle(groupsListFragment.title)
        childFragmentManager.beginTransaction()
                .replace(R.id.groups_back_stack_layout, groupsListFragment)
                .commit()
    }
}
