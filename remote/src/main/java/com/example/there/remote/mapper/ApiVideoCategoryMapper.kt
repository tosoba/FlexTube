package com.example.there.remote.mapper

import com.example.there.data.model.VideoCategoryData
import com.example.there.remote.model.ApiVideoCategory

object ApiVideoCategoryMapper : ApiMapper<ApiVideoCategory, VideoCategoryData> {
    override fun toData(api: ApiVideoCategory): VideoCategoryData = VideoCategoryData(
            id = api.id,
            title = api.snippet.title
    )
}