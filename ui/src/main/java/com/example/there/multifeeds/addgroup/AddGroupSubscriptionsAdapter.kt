package com.example.there.multifeeds.addgroup

import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.example.there.multifeeds.base.list.adapter.BaseObservableListAdapter
import com.example.there.multifeeds.base.list.viewholder.BaseBindingViewHolder
import com.example.there.multifeeds.databinding.SubscriptionToChooseItemBinding
import com.example.there.multifeeds.model.UiSubscriptionToChoose
import com.example.there.multifeeds.util.view.ObservableSortedList


class AddGroupSubscriptionsAdapter(
        items: ObservableSortedList<UiSubscriptionToChoose>,
        layoutId: Int
) : BaseObservableListAdapter<UiSubscriptionToChoose, SubscriptionToChooseItemBinding>(items, layoutId), Filterable {

    private var filteredItems = items

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): BaseBindingViewHolder<SubscriptionToChooseItemBinding> = super.onCreateViewHolder(
            parent,
            viewType
    ).apply {
        binding.chosenSubscriptionCheckbox.setOnCheckedChangeListener { _, isChecked ->
            binding.chosenSubscription?.isChosen?.set(isChecked)
        }
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<SubscriptionToChooseItemBinding>?, position: Int) {
        val subscription = filteredItems[position]
        holder?.binding?.chosenSubscription = subscription
    }

    override fun getItemCount(): Int = filteredItems.size

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterString = constraint.toString()
            if (filterString.trim().isEmpty()) {
                return FilterResults().apply {
                    values = items
                    count = items.size
                }
            }
            val filteredItems = items.filter { it.subscription.title.contains(filterString, ignoreCase = true) }
            return FilterResults().apply {
                values = ObservableSortedList<UiSubscriptionToChoose>(
                        UiSubscriptionToChoose::class.java,
                        UiSubscriptionToChoose.observableSortedListCallback
                ).apply { addAll(filteredItems) }
                count = filteredItems.size
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            results?.values?.let {
                val newItems = it as ObservableSortedList<UiSubscriptionToChoose>
                val diffResult = DiffUtil.calculateDiff(DiffUtilCallback(newItems, filteredItems))
                diffResult.dispatchUpdatesTo(this@AddGroupSubscriptionsAdapter)
                filteredItems = it
            }
        }
    }

    class DiffUtilCallback(
            private val newItems: List<UiSubscriptionToChoose>,
            private val oldItems: List<UiSubscriptionToChoose>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
        ): Boolean = oldItems[oldItemPosition].subscription.id == newItems[newItemPosition].subscription.id

        override fun areContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
        ): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]
            return oldItem.subscription.id == newItem.subscription.id && oldItem.isChosen.get() == newItem.isChosen.get()
        }
    }
}