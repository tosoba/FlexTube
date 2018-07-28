package com.example.there.cache.dao.impl

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.example.there.cache.dao.base.DeleteDao
import com.example.there.cache.dao.base.InsertIgnoreDao
import com.example.there.cache.db.Tables
import com.example.there.cache.model.CachedSubscription
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface SubscriptionDao : DeleteDao<CachedSubscription>, InsertIgnoreDao<CachedSubscription> {
    @Query("SELECT * FROM ${Tables.SUBSCRIPTIONS} WHERE account_name = :accountName ORDER BY title")
    fun getAllByAccountName(accountName: String): Single<List<CachedSubscription>>

    @Query("SELECT * FROM ${Tables.SUBSCRIPTIONS} WHERE account_name = :accountName ORDER BY title")
    fun getAllFlowableByAccountName(accountName: String): Flowable<List<CachedSubscription>>

    @Query("SELECT ${Tables.SUBSCRIPTIONS}.* " +
            "FROM ${Tables.SUBSCRIPTIONS} JOIN ${Tables.SUBSCRIPTIONS_GROUPS} " +
            "ON ${Tables.SUBSCRIPTIONS}.id=${Tables.SUBSCRIPTIONS_GROUPS}.subscription_id " +
            "WHERE group_name = :groupName AND ${Tables.SUBSCRIPTIONS_GROUPS}.account_name = :accountName " +
            "ORDER BY LOWER(title)")
    fun getAllByGroup(accountName: String, groupName: String): Flowable<List<CachedSubscription>>

    @Query("SELECT ${Tables.SUBSCRIPTIONS}.* " +
            "FROM ${Tables.SUBSCRIPTIONS} LEFT JOIN ${Tables.SUBSCRIPTIONS_GROUPS} " +
            "ON ${Tables.SUBSCRIPTIONS}.id=${Tables.SUBSCRIPTIONS_GROUPS}.subscription_id " +
            "WHERE (group_name != :groupName OR group_name IS NULL) AND ${Tables.SUBSCRIPTIONS}.account_name = :accountName " +
            "ORDER BY LOWER(title)")
    fun getAllNotFromGroup(accountName: String, groupName: String): Flowable<List<CachedSubscription>>
}