package com.example.there.cache.dao.impl

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.example.there.cache.dao.base.InsertReplaceDao
import com.example.there.cache.db.Tables
import com.example.there.cache.model.CachedPlaylist
import io.reactivex.Single

@Dao
interface PlaylistDao : InsertReplaceDao<CachedPlaylist> {
    @Query("SELECT * FROM ${Tables.PLAYLISTS} WHERE id = :id")
    fun getById(id: String): Single<CachedPlaylist>

    @Query("SELECT * FROM ${Tables.PLAYLISTS} WHERE channel_id = :channelId")
    fun getByChannelId(channelId: String): Single<CachedPlaylist>

    @Query("UPDATE ${Tables.PLAYLISTS} SET next_page_token = :nextPageToken WHERE id = :id")
    fun updateNextPageToken(id: String, nextPageToken: String?)
}