package com.example.there.flextube.groups

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.flextube.R
import com.example.there.flextube.groups.group.GroupFragment
import com.example.there.flextube.groups.list.GroupsListFragment
import com.example.there.flextube.model.UiGroup


class GroupsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_groups, container, false)
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
