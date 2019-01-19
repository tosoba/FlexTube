package com.example.there.multifeeds.groups.list

import android.view.View
import com.example.there.multifeeds.list.SortedGroupsAdapter

class GroupsListView(
        val state: GroupsListViewState,
        val adapter: SortedGroupsAdapter,
        val onNewGroupBtnClickListener: View.OnClickListener
)