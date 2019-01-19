package com.example.there.multifeeds.list

import android.support.v7.widget.GridLayoutManager
import android.view.ViewGroup
import com.example.there.multifeeds.R
import com.example.there.multifeeds.base.list.adapter.BaseObservableListAdapter
import com.example.there.multifeeds.base.list.viewholder.BaseBindingViewHolder
import com.example.there.multifeeds.databinding.GroupItemBinding
import com.example.there.multifeeds.groups.list.item.GroupItemView
import com.example.there.multifeeds.groups.list.item.GroupItemViewState
import com.example.there.multifeeds.model.UiGroupWithSubscriptions
import com.example.there.multifeeds.util.view.ObservableSortedList
import io.reactivex.subjects.PublishSubject

class SortedGroupsAdapter(
        items: ObservableSortedList<UiGroupWithSubscriptions>,
        itemLayoutId: Int,
        itemsOffset: Int = 0
) : BaseObservableListAdapter<UiGroupWithSubscriptions, GroupItemBinding>(items, itemLayoutId, itemsOffset) {

    val groupClicked: PublishSubject<UiGroupWithSubscriptions> = PublishSubject.create()

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): BaseBindingViewHolder<GroupItemBinding> = super.onCreateViewHolder(parent, viewType).apply {
        binding.groupSubscriptionsRecyclerView.layoutManager = GridLayoutManager(
                binding.root.context,
                2,
                GridLayoutManager.HORIZONTAL,
                false
        )
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<GroupItemBinding>?, position: Int) {
        val group = items[position]
        holder?.binding?.groupItemView =
                GroupItemView(GroupItemViewState(group), SubscriptionsAdapter(group.subscriptions, R.layout.subscription_item))
        holder?.binding?.root?.setOnClickListener { groupClicked.onNext(group) }
    }
}