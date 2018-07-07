package com.example.there.cache.dao.base

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy

@Dao
interface InsertIgnoreDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(t: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMany(vararg t: T): Array<Long>
}