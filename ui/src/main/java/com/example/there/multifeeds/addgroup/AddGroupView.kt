package com.example.there.multifeeds.addgroup

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.View

data class AddGroupView(
        val state: AddGroupViewState,
        val adapter: AddGroupSubscriptionsAdapter,
        val itemDecoration: RecyclerView.ItemDecoration,
        val onAddGroupBtnClickListener: View.OnClickListener,
        val onSearchTextChangeListener: SearchView.OnQueryTextListener
)