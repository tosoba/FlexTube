package com.example.there.flextube.addgroup

import android.databinding.ObservableArrayList
import android.widget.Filter
import android.widget.Filterable
import com.example.there.flextube.base.BaseBindingViewHolder
import com.example.there.flextube.base.BaseObservableListAdapter
import com.example.there.flextube.databinding.SubscriptionToChooseItemBinding
import com.example.there.flextube.model.UiSubscriptionToChoose


class AddGroupSubscriptionsAdapter(
        items: ObservableArrayList<UiSubscriptionToChoose>,
        layoutId: Int
) : BaseObservableListAdapter<UiSubscriptionToChoose, SubscriptionToChooseItemBinding>(items, layoutId), Filterable {

    private var filteredItems = items

    override fun onBindViewHolder(holder: BaseBindingViewHolder<SubscriptionToChooseItemBinding>?, position: Int) {
        val subscription = filteredItems[position]
        holder?.binding?.chosenSubscription = subscription
        holder?.binding?.chosenSubscriptionCheckbox?.setOnCheckedChangeListener { _, isChecked ->
            subscription.isChosen.set(isChecked)
        }
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
                values = ObservableArrayList<UiSubscriptionToChoose>().apply { addAll(filteredItems) }
                count = filteredItems.size
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            results?.values?.let {
                filteredItems = it as ObservableArrayList<UiSubscriptionToChoose>
                notifyDataSetChanged()
            }
        }
    }
}