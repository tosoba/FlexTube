package com.example.there.flextube.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.flextube.R
import com.example.there.flextube.base.fragment.BaseHostFragment
import com.example.there.flextube.groups.group.GroupFragment
import com.example.there.flextube.groups.list.GroupsListFragment
import com.example.there.flextube.model.UiGroup
import com.example.there.flextube.util.ext.mainActivity
import com.example.there.flextube.util.ext.mainToolbar
import com.example.there.flextube.util.ext.resetTitle


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
        val groupsListFragment = GroupsListFragment()
        mainToolbar?.resetTitle(groupsListFragment.title)
        childFragmentManager.beginTransaction()
                .replace(R.id.groups_back_stack_layout, groupsListFragment)
                .commit()
    }

    fun showGroupFragment(group: UiGroup) {
        val groupFragment = GroupFragment.newInstance(group)
        mainToolbar?.resetTitle(groupFragment.title)

        mainActivity?.addBackNavigationToToolbar()

        childFragmentManager.beginTransaction()
                .replace(R.id.groups_back_stack_layout, groupFragment)
                .addToBackStack(null)
                .commit()
    }
}
