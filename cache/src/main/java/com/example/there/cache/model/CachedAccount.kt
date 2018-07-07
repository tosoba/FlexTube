package com.example.there.cache.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.example.there.cache.db.Tables

@Entity(tableName = Tables.ACCOUNTS)
data class CachedAccount(
        @PrimaryKey
        val name: String
)