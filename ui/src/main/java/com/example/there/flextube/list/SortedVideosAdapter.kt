package com.example.there.flextube.list

import com.example.there.domain.model.PlaylistItem
import com.example.there.flextube.base.BaseBindingViewHolder
import com.example.there.flextube.base.BaseObservableListAdapter
import com.example.there.flextube.databinding.VideoItemBinding
import com.example.there.flextube.util.view.ObservableSortedList
import io.reactivex.subjects.PublishSubject

class SortedVideosAdapter(
        items: ObservableSortedList<PlaylistItem>,
        itemLayoutId: Int
) : BaseObservableListAdapter<PlaylistItem, VideoItemBinding>(items, itemLayoutId) {

    override fun onBindViewHolder(holder: BaseBindingViewHolder<VideoItemBinding>?, position: Int) {
        val video = items[position]
        holder?.binding?.video = video
        holder?.binding?.root?.setOnClickListener {
            videoClicked.onNext(video.videoId)
        }
    }

    val videoClicked: PublishSubject<String> = PublishSubject.create()
}