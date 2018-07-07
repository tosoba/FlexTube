package com.example.there.cache.dao.base

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy

@Dao
interface InsertReplaceDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMany(vararg t: T): Array<Long>
}