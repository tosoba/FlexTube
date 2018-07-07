package com.example.there.cache.dao.base

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete

@Dao
interface DeleteDao<in T> {
    @Delete
    fun delete(t: T)

    @Delete
    fun deleteMany(vararg t: T)
}