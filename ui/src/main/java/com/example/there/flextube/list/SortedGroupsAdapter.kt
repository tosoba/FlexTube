package com.example.there.flextube.list

import android.databinding.ObservableArrayList
import android.support.v7.util.SortedList
import android.support.v7.widget.util.SortedListAdapterCallback
import com.example.there.domain.model.Group
import com.example.there.flextube.base.BaseBindingViewHolder
import com.example.there.flextube.base.BaseObservableSortedListAdapter
import com.example.there.flextube.databinding.GroupItemBinding
import io.reactivex.subjects.PublishSubject

class SortedGroupsAdapter(
        items: ObservableArrayList<Group>,
        itemLayoutId: Int
) : BaseObservableSortedListAdapter<Group, GroupItemBinding>(items, itemLayoutId) {

    override fun onBindViewHolder(holder: BaseBindingViewHolder<GroupItemBinding>?, position: Int) {
        val group = sortedItems[position]
        holder?.binding?.group = group
        holder?.binding?.root?.setOnClickListener {
            groupClicked.onNext(group)
        }
    }

    override val sortedItems: SortedList<Group> = SortedList<Group>(Group::class.java, object : SortedListAdapterCallback<Group>(this) {
        override fun areItemsTheSame(item1: Group, item2: Group): Boolean = item1 == item2

        override fun compare(o1: Group, o2: Group): Int = o2.name.compareTo(o1.name)

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean = oldItem == newItem
    })

    val groupClicked: PublishSubject<Group> = PublishSubject.create()
}