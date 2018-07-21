package com.example.there.cache.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.example.there.cache.converter.DateTypeConverters
import com.example.there.cache.dao.impl.*
import com.example.there.cache.model.*

@Database(
        entities = [
            CachedAccount::class,
            CachedGroup::class,
            CachedSubscription::class,
            SubscriptionGroupJoin::class,
            CachedPlaylistItem::class,
            CachedPlaylist::class
        ],
        version = 1,
        exportSchema = false
)
@TypeConverters(value = [DateTypeConverters::class])
abstract class FlexTubeDb: RoomDatabase() {

    abstract fun accountDao(): AccountDao

    abstract fun groupDao(): GroupDao

    abstract fun subscriptionDao(): SubscriptionDao

    abstract fun playlistItemDao(): PlaylistItemDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun subscriptionGroupsJoinDao(): SubscriptionGroupJoinDao
}