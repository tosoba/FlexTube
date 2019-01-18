package com.example.there.multifeeds.mapper

import com.example.there.domain.model.VideoCategory
import com.example.there.multifeeds.R
import com.example.there.multifeeds.model.UiVideoCategory

object UiVideoCategoryMapper : UiMapper<VideoCategory, UiVideoCategory> {
    override fun toUi(domain: VideoCategory): UiVideoCategory = UiVideoCategory(
            id = domain.id,
            title = domain.title,
            imageResourceId = icons[domain.id] ?: R.drawable.category_default
    )

    override fun toDomain(ui: UiVideoCategory): VideoCategory = VideoCategory(id = ui.id, title = ui.title)

    private val icons = mapOf(
            "1" to R.drawable.film,
            "2" to R.drawable.autos,
            "10" to R.drawable.music,
            "15" to R.drawable.pets,
            "17" to R.drawable.sports,
            "18" to R.drawable.short_movies,
            "19" to R.drawable.travel,
            "20" to R.drawable.gaming,
            "21" to R.drawable.videoblogging,
            "22" to R.drawable.people_and_blogs,
            "23" to R.drawable.comedy,
            "24" to R.drawable.entertainment,
            "25" to R.drawable.news_and_politics,
            "26" to R.drawable.howto_and_style,
            "27" to R.drawable.education,
            "28" to R.drawable.science_and_technology,
            "29" to R.drawable.nonprofits_and_activism,
            "30" to R.drawable.movies,
            "31" to R.drawable.anime_animation,
            "32" to R.drawable.action_adventure,
            "33" to R.drawable.classics,
            "34" to R.drawable.comedy,
            "35" to R.drawable.documentary,
            "36" to R.drawable.drama,
            "37" to R.drawable.family,
            "38" to R.drawable.foreign,
            "39" to R.drawable.horror,
            "40" to R.drawable.fantasy_sci_fi,
            "41" to R.drawable.thriller,
            "42" to R.drawable.short_movies,
            "43" to R.drawable.shows,
            "44" to R.drawable.trailers
    )
}