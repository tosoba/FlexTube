package com.example.there.cache.dao.impl

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.example.there.cache.dao.base.DeleteDao
import com.example.there.cache.dao.base.InsertIgnoreDao
import com.example.there.cache.db.Tables
import com.example.there.cache.model.CachedGroup
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface GroupDao : DeleteDao<CachedGroup>, InsertIgnoreDao<CachedGroup> {
    @Query("SELECT * FROM ${Tables.GROUPS} WHERE name = :name")
    fun getByName(name: String): Maybe<CachedGroup>

    @Query("SELECT * FROM ${Tables.GROUPS} WHERE account_name = :accountName")
    fun getAllByAccountName(accountName: String): Single<List<CachedGroup>>
}