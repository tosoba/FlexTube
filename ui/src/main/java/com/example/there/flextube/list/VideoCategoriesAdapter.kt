package com.example.there.flextube.list

import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import com.example.there.flextube.base.list.adapter.BaseObservableListAdapter
import com.example.there.flextube.base.list.viewholder.BaseBindingViewHolder
import com.example.there.flextube.databinding.VideoCategoryItemBinding
import com.example.there.flextube.model.UiVideoCategory
import io.reactivex.subjects.PublishSubject

class VideoCategoriesAdapter(
        items: ObservableList<UiVideoCategory>,
        itemLayoutId: Int
) : BaseObservableListAdapter<UiVideoCategory, VideoCategoryItemBinding>(items, itemLayoutId) {

    val categoryClicked: PublishSubject<String> = PublishSubject.create()

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun scrollToTop() {
        recyclerView?.scrollToPosition(0)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<VideoCategoryItemBinding>?, position: Int) {
        val category = items[position]
        holder?.binding?.category = category
        holder?.binding?.root?.setOnClickListener {
            if (position != 0) {
                items[0].isSelected.set(false)
                observableItems.removeAt(position)
                category.isSelected.set(true)
                observableItems.add(0, category)
                notifyItemRangeChanged(0, position + 1)
                categoryClicked.onNext(category.id)
            }
        }
    }
}