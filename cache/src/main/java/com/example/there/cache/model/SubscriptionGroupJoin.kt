package com.example.there.cache.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import com.example.there.cache.db.Tables

@Entity(
        tableName = Tables.SUBSCRIPTIONS_GROUPS,
        primaryKeys = ["subscription_id", "group_name", "account_name"],
        foreignKeys = [
            ForeignKey(
                    entity = CachedGroup::class,
                    parentColumns = ["name", "account_name"],
                    childColumns = ["group_name", "account_name"],
                    onDelete = ForeignKey.CASCADE
            ),
            ForeignKey(
                    entity = CachedSubscription::class,
                    parentColumns = ["id"],
                    childColumns = ["subscription_id"],
                    onDelete = ForeignKey.CASCADE
            )
        ],
        indices = [
            Index(value = ["group_name", "account_name"])
        ]
)
data class SubscriptionGroupJoin(
        @ColumnInfo(name = "subscription_id")
        val subscriptionId: String,

        @ColumnInfo(name = "group_name")
        val groupName: String,

        @ColumnInfo(name = "account_name")
        val accountName: String
)