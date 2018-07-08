package com.example.there.flextube.subfeed

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.there.domain.model.Subscription
import com.example.there.flextube.R

class SubFeedSubscriptionsAdapter : RecyclerView.Adapter<SubFeedSubscriptionsAdapter.ViewHolder>() {
    val subscriptions: ArrayList<Subscription> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.subscription_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = subscriptions.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val thumbnailUrl = subscriptions[position].thumbnailUrl
        Glide.with(holder?.itemView)
                .load(thumbnailUrl)
                .apply(RequestOptions()
                        .circleCrop()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round))
                .into(holder?.thumbnailImageView)
    }

    fun addSubscriptions(newSubscriptions: List<Subscription>) {
        subscriptions.addAll(newSubscriptions)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnailImageView: ImageView = itemView.findViewById(R.id.sub_image_view)
    }
}