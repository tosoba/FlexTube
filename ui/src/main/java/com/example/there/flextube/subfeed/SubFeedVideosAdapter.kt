package com.example.there.flextube.subfeed

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.there.domain.model.PlaylistItem
import com.example.there.flextube.R
import java.util.*

class SubFeedVideosAdapter : RecyclerView.Adapter<SubFeedVideosAdapter.ViewHolder>() {
    private val videos: SortedSet<PlaylistItem> = TreeSet(Comparator { i1, i2 -> i2.publishedAt.compareTo(i1.publishedAt) })

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.video_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val video = videos.elementAt(position)
        val thumbnailUrl = video.thumbnailUrl
        val thumbnail = holder?.itemView?.findViewById<ImageView>(R.id.video_thumbnail_image_view)
        Glide.with(holder?.itemView)
                .load(thumbnailUrl)
                .apply(RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round))
                .into(thumbnail)

        holder?.itemView?.findViewById<TextView>(R.id.video_title_text_view)?.text = video.title
    }

    fun addVideos(newVideos: List<PlaylistItem>) {
        videos.addAll(newVideos)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}