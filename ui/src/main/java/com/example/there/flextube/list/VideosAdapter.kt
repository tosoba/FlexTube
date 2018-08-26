package com.example.there.flextube.list

import android.databinding.ObservableField
import android.databinding.ObservableList
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.there.domain.model.PlaylistItem
import com.example.there.flextube.base.list.adapter.TracksInitialScroll
import com.example.there.flextube.base.list.viewholder.BaseBindingViewHolder
import com.example.there.flextube.databinding.LoadingItemBinding
import com.example.there.flextube.databinding.VideoItemBinding
import com.example.there.flextube.util.ext.bindToItems
import com.example.there.flextube.util.ext.makeBinding
import io.reactivex.subjects.PublishSubject

class VideosAdapter(
        private val items: ObservableList<PlaylistItem>,
        @LayoutRes private val itemLayoutId: Int,
        @LayoutRes private val loadingItemLayoutId: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), TracksInitialScroll {

    init {
        bindToItems(items)
    }

    val loadingInProgress: ObservableField<Boolean> = ObservableField(false)

    override fun getItemViewType(
            position: Int
    ): Int = if (position == items.size) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        VIEW_TYPE_LOADING -> BaseBindingViewHolder(parent!!.makeBinding<LoadingItemBinding>(loadingItemLayoutId).apply {
            viewState = LoadingItemViewState(loadingInProgress)
        })
        VIEW_TYPE_ITEM -> BaseBindingViewHolder<VideoItemBinding>(parent!!.makeBinding(itemLayoutId))
        else -> throw IllegalStateException("${javaClass.name}: Unknown viewType: $viewType")
    }

    override fun getItemCount(): Int = items.size + 1

    override var userHasScrolled: Boolean = false

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (position == items.size) {

        } else {
            val itemViewHolder = holder as? BaseBindingViewHolder<VideoItemBinding>
            itemViewHolder?.let {
                if (position < items.size) {
                    val video = items[position]
                    it.binding.video = video
                    it.binding.root.setOnClickListener { _ ->
                        videoClicked.onNext(video.videoId)
                    }
                }
            }
        }

    }

    val videoClicked: PublishSubject<String> = PublishSubject.create()

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_ITEM = 1
    }
}