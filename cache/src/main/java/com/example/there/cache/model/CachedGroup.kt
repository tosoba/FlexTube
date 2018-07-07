package com.example.there.cache.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import com.example.there.cache.db.Tables

@Entity(
        tableName = Tables.GROUPS,
        primaryKeys = ["name", "account_name"],
        foreignKeys = [
            ForeignKey(
                    entity = CachedAccount::class,
                    parentColumns = ["name"],
                    childColumns = ["account_name"],
                    onDelete = ForeignKey.CASCADE
            )
        ],
        indices = [
            Index(value = ["name", "account_name"], unique = true),
            Index(value = ["account_name"])
        ]
)
data class CachedGroup(
        @ColumnInfo(name = "name")
        val name: String,

        @ColumnInfo(name = "account_name")
        val accountName: String
)