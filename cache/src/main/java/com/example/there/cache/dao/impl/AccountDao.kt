package com.example.there.cache.dao.impl

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.example.there.cache.dao.base.DeleteDao
import com.example.there.cache.dao.base.InsertIgnoreDao
import com.example.there.cache.db.Tables
import com.example.there.cache.model.CachedAccount
import io.reactivex.Maybe

@Dao
interface AccountDao : DeleteDao<CachedAccount>, InsertIgnoreDao<CachedAccount> {
    @Query("SELECT * FROM ${Tables.ACCOUNTS} WHERE name = :name")
    fun getByName(name: String): Maybe<CachedAccount>
}