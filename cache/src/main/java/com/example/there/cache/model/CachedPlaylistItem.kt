package com.example.there.cache.model

import android.arch.persistence.room.*
import com.example.there.cache.converter.DateTypeConverters
import com.example.there.cache.db.Tables
import java.util.*

@Entity(
        tableName = Tables.PLAYLIST_ITEMS,
        indices = [
            Index(value = ["channel_id"]),
            Index(value = ["playlist_id"])
        ],
        foreignKeys = [
            ForeignKey(
                    entity = CachedPlaylist::class,
                    parentColumns = ["id"],
                    childColumns = ["playlist_id"],
                    onDelete = ForeignKey.CASCADE
            )
        ]
)
data class CachedPlaylistItem(
        @PrimaryKey
        @ColumnInfo(name = "video_id")
        val videoId: String,

        @ColumnInfo(name = "channel_id")
        val channelId: String,

        val title: String,

        val description: String,

        @ColumnInfo(name = "thumbnail_url")
        val thumbnailUrl: String,

        @TypeConverters(DateTypeConverters::class)
        @ColumnInfo(name = "published_at")
        val publishedAt: Date,

        @ColumnInfo(name = "playlist_id")
        val playlistId: String
)