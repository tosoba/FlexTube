package com.example.there.flextube.groups.list

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.there.flextube.list.SortedGroupsAdapter

data class GroupsListView(
        val state: GroupsListViewState,
        val adapter: SortedGroupsAdapter,
        val itemDecoration: RecyclerView.ItemDecoration,
        val onNewGroupBtnClickListener: View.OnClickListener
)