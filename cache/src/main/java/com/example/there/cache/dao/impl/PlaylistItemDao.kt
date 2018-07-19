package com.example.there.cache.dao.impl

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.example.there.cache.dao.base.InsertIgnoreDao
import com.example.there.cache.db.Tables
import com.example.there.cache.model.CachedPlaylistItem
import io.reactivex.Flowable

@Dao
interface PlaylistItemDao : InsertIgnoreDao<CachedPlaylistItem> {
    @Query("SELECT * FROM ${Tables.PLAYLIST_ITEMS} WHERE playlist_id = :playlistId")
    fun getAllByPlaylistId(playlistId: String): Flowable<List<CachedPlaylistItem>>

    @Query("SELECT * FROM ${Tables.PLAYLIST_ITEMS} WHERE channel_id = :channelId")
    fun getAllByChannelId(channelId: String): Flowable<List<CachedPlaylistItem>>
}