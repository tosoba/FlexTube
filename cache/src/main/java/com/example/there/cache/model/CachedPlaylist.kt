package com.example.there.cache.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import com.example.there.cache.db.Tables

@Entity(
        tableName = Tables.PLAYLISTS,
        indices = [Index(value = ["channel_id"])]
)
data class CachedPlaylist(
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: String,

        @ColumnInfo(name = "channel_id")
        val channelId: String,

        @ColumnInfo(name = "next_page_token")
        var nextPageToken: String? = null
)