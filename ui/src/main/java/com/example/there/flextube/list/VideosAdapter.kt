package com.example.there.flextube.list

import android.databinding.ObservableArrayList
import android.support.v7.util.SortedList
import android.support.v7.widget.util.SortedListAdapterCallback
import com.example.there.domain.model.PlaylistItem
import com.example.there.flextube.base.BaseBindingViewHolder
import com.example.there.flextube.base.BaseObservableSortedListAdapter
import com.example.there.flextube.databinding.VideoItemBinding
import io.reactivex.subjects.PublishSubject

class VideosAdapter(
        items: ObservableArrayList<PlaylistItem>,
        itemLayoutId: Int
) : BaseObservableSortedListAdapter<PlaylistItem, VideoItemBinding>(items, itemLayoutId) {

    override fun onBindViewHolder(holder: BaseBindingViewHolder<VideoItemBinding>?, position: Int) {
        val video = sortedItems[position]
        holder?.binding?.video = video
        holder?.binding?.root?.setOnClickListener {
            videoClicked.onNext(video.videoId)
        }
    }

    override val sortedItems: SortedList<PlaylistItem> = SortedList<PlaylistItem>(PlaylistItem::class.java, object : SortedListAdapterCallback<PlaylistItem>(this) {
        override fun areItemsTheSame(item1: PlaylistItem, item2: PlaylistItem): Boolean = item1 == item2

        override fun compare(o1: PlaylistItem, o2: PlaylistItem): Int = o2.publishedAt.compareTo(o1.publishedAt)

        override fun areContentsTheSame(oldItem: PlaylistItem, newItem: PlaylistItem): Boolean = oldItem == newItem
    })

    val videoClicked: PublishSubject<String> = PublishSubject.create()
}