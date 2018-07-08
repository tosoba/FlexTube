package com.example.there.flextube.list

import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.util.SortedListAdapterCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.there.domain.model.PlaylistItem
import com.example.there.flextube.R
import io.reactivex.subjects.PublishSubject

class VideosAdapter : RecyclerView.Adapter<VideosAdapter.ViewHolder>() {

    private val videos = SortedList<PlaylistItem>(PlaylistItem::class.java, object : SortedListAdapterCallback<PlaylistItem>(this) {
        override fun areItemsTheSame(item1: PlaylistItem, item2: PlaylistItem): Boolean = item1 == item2

        override fun compare(o1: PlaylistItem, o2: PlaylistItem): Int = o2.publishedAt.compareTo(o1.publishedAt)

        override fun areContentsTheSame(oldItem: PlaylistItem, newItem: PlaylistItem): Boolean = oldItem == newItem
    })

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.video_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = videos.size()

    val videoClicked: PublishSubject<String> = PublishSubject.create()

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val video = videos[position]

        Glide.with(holder?.itemView)
                .load(video.thumbnailUrl)
                .apply(RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round))
                .into(holder?.thumbnailImageView)

        holder?.itemView?.setOnClickListener {
            videoClicked.onNext(video.videoId)
        }

        holder?.titleTextView?.text = video.title
    }

    fun addVideos(newVideos: List<PlaylistItem>) {
        videos.addAll(newVideos)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnailImageView: ImageView = itemView.findViewById(R.id.video_thumbnail_image_view)
        val titleTextView: TextView = itemView.findViewById(R.id.video_title_text_view)
    }
}