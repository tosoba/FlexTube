package com.example.there.cache.model

import android.arch.persistence.room.*
import com.example.there.cache.converter.DateTypeConverters
import com.example.there.cache.db.Tables
import java.util.*

@Entity(
        tableName = Tables.SUBSCRIPTIONS,
        foreignKeys = [
            ForeignKey(
                    entity = CachedAccount::class,
                    childColumns = ["account_name"],
                    parentColumns = ["name"],
                    onDelete = ForeignKey.CASCADE
            )
        ],
        indices = [
            Index(value = ["account_name"])
        ]
)
data class CachedSubscription(
        @PrimaryKey
        val id: String,

        @TypeConverters(DateTypeConverters::class)
        @ColumnInfo(name = "published_at")
        val publishedAt: Date,

        val title: String,

        val description: String,

        @ColumnInfo(name = "channel_id")
        val channelId: String,

        @ColumnInfo(name = "thumbnail_url")
        val thumbnailUrl: String,

        @ColumnInfo(name = "account_name")
        val accountName: String
)