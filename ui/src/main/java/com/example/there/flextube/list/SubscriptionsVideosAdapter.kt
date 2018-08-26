package com.example.there.flextube.list

import android.databinding.ObservableList
import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.there.domain.model.PlaylistItem
import com.example.there.flextube.R
import com.example.there.flextube.base.list.adapter.TracksInitialScroll
import com.example.there.flextube.base.list.viewholder.BaseBindingViewHolder
import com.example.there.flextube.base.list.viewholder.BaseViewHolder
import com.example.there.flextube.databinding.SubscriptionListItemBinding
import com.example.there.flextube.databinding.VideoItemBinding
import com.example.there.flextube.model.UiSubscription
import com.example.there.flextube.util.ext.bindToItems
import com.example.there.flextube.util.ext.makeBinding
import io.reactivex.subjects.PublishSubject

class SubscriptionsVideosAdapter(
        private val videos: ObservableList<PlaylistItem>,
        private val subscriptions: ObservableList<UiSubscription>,
        @LayoutRes private val videoItemLayoutId: Int,
        @LayoutRes private val subscriptionListItemLayoutId: Int,
        @LayoutRes private val subscriptionItemLayoutId: Int,
        @LayoutRes private val loadingItemLayoutId: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), TracksInitialScroll {

    init {
        bindToItems(videos, 1)
    }

    var loadingInProgress: Boolean = false

    override var userHasScrolled: Boolean = false

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> SUBSCRIPTION_LIST_VIEW_TYPE
        in 1..videos.size -> VIDEO_VIEW_TYPE
        videos.size + 1 -> LOADING_VIEW_TYPE
        else -> throw IllegalStateException("Position $position out of range.")
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        return when (viewType) {
            LOADING_VIEW_TYPE -> BaseViewHolder(inflater.inflate(loadingItemLayoutId, parent, false))
            VIDEO_VIEW_TYPE -> BaseBindingViewHolder<VideoItemBinding>(parent!!.makeBinding(videoItemLayoutId))
            SUBSCRIPTION_LIST_VIEW_TYPE ->
                BaseBindingViewHolder(parent!!.makeBinding<SubscriptionListItemBinding>(subscriptionListItemLayoutId).apply {
                    adapter = SubscriptionsAdapter(subscriptions, subscriptionItemLayoutId)
                    subscriptionListItemRecyclerView.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
                })
            else -> throw IllegalStateException("Unknown viewType: $viewType")
        }
    }

    override fun getItemCount(): Int = videos.size + 2

    val videoClicked: PublishSubject<String> = PublishSubject.create()

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (position) {
            0 -> return
            in 1..videos.size -> {
                val videoViewHolder = holder as? BaseBindingViewHolder<VideoItemBinding>
                videoViewHolder?.let {
                    val video = videos[position - 1]
                    it.binding.video = video
                    it.binding.root.setOnClickListener { _ -> videoClicked.onNext(video.videoId) }
                }
            }
            videos.size + 1 -> {
                val progressBar = holder?.itemView?.findViewById<ProgressBar>(R.id.loading_item_progress_bar)
                if (loadingInProgress) progressBar?.visibility = View.VISIBLE
                else progressBar?.visibility = View.GONE
            }
            else -> throw IllegalStateException("Position $position out of range.")
        }
    }

    companion object {
        private const val SUBSCRIPTION_LIST_VIEW_TYPE = 0
        private const val VIDEO_VIEW_TYPE = 1
        private const val LOADING_VIEW_TYPE = 2
    }
}