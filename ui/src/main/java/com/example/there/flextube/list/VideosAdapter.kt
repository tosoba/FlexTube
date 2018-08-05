package com.example.there.flextube.list

import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import com.example.there.domain.model.PlaylistItem
import com.example.there.flextube.base.list.adapter.BaseObservableListLoadingAdapter
import com.example.there.flextube.base.list.viewholder.BaseBindingViewHolder
import com.example.there.flextube.databinding.VideoItemBinding
import io.reactivex.subjects.PublishSubject

open class VideosAdapter(
        items: ObservableList<PlaylistItem>,
        itemLayoutId: Int,
        loadingItemLayoutId: Int
) : BaseObservableListLoadingAdapter<PlaylistItem, VideoItemBinding>(items, itemLayoutId, loadingItemLayoutId) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        super.onBindViewHolder(holder, position)
        val itemViewHolder = holder as? BaseBindingViewHolder<VideoItemBinding>
        itemViewHolder?.let {
            if (position < items.size) {
                val video = items[position]
                it.binding.video = video
                it.binding.root.setOnClickListener {
                    videoClicked.onNext(video.videoId)
                }
            }
        }
    }

    val videoClicked: PublishSubject<String> = PublishSubject.create()
}