package com.example.there.flextube.list

import com.example.there.domain.model.Group
import com.example.there.flextube.base.list.adapter.BaseObservableListAdapter
import com.example.there.flextube.base.list.viewholder.BaseBindingViewHolder
import com.example.there.flextube.databinding.GroupItemBinding
import com.example.there.flextube.util.view.ObservableSortedList
import io.reactivex.subjects.PublishSubject

class SortedGroupsAdapter(
        items: ObservableSortedList<Group>,
        itemLayoutId: Int
) : BaseObservableListAdapter<Group, GroupItemBinding>(items, itemLayoutId) {

    override fun onBindViewHolder(holder: BaseBindingViewHolder<GroupItemBinding>?, position: Int) {
        val group = items[position]
        holder?.binding?.group = group
        holder?.binding?.root?.setOnClickListener {
            groupClicked.onNext(group)
        }
    }

    val groupClicked: PublishSubject<Group> = PublishSubject.create()
}