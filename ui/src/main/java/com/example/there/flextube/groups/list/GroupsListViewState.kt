package com.example.there.flextube.groups.list

import com.example.there.flextube.model.UiGroupWithSubscriptions
import com.example.there.flextube.util.view.ObservableSortedList

data class GroupsListViewState(
        val groups: ObservableSortedList<UiGroupWithSubscriptions> = ObservableSortedList(
                UiGroupWithSubscriptions::class.java,
                object : ObservableSortedList.Callback<UiGroupWithSubscriptions> {
                    override fun compare(
                            o1: UiGroupWithSubscriptions,
                            o2: UiGroupWithSubscriptions
                    ): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())

                    override fun areItemsTheSame(
                            item1: UiGroupWithSubscriptions,
                            item2: UiGroupWithSubscriptions
                    ): Boolean = item1.accountName == item2.accountName && item1.name == item2.name

                    override fun areContentsTheSame(
                            oldItem: UiGroupWithSubscriptions?,
                            newItem: UiGroupWithSubscriptions?
                    ): Boolean = oldItem?.accountName == newItem?.accountName && oldItem?.name == newItem?.name
                }
        )
)