package com.example.there.data.mapper

import com.example.there.data.model.VideoCategoryData
import com.example.there.domain.model.VideoCategory

object VideoCategoryMapper : OneWayDataMapper<VideoCategoryData, VideoCategory> {
    override fun toDomain(data: VideoCategoryData): VideoCategory = VideoCategory(
            id = data.id,
            title = data.title
    )
}