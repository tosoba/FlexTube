package com.example.there.flextube.groups.list

import com.example.there.domain.model.Group
import com.example.there.flextube.util.view.ObservableSortedList

data class GroupsListViewState(
        val groups: ObservableSortedList<Group> = ObservableSortedList(
                Group::class.java,
                object : ObservableSortedList.Callback<Group> {
                    override fun compare(o1: Group, o2: Group): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())

                    override fun areItemsTheSame(item1: Group, item2: Group): Boolean = item1 == item2

                    override fun areContentsTheSame(oldItem: Group?, newItem: Group?): Boolean = oldItem == newItem
                }
        )
)