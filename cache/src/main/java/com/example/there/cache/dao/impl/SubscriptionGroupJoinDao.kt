package com.example.there.cache.dao.impl

import android.arch.persistence.room.Dao
import com.example.there.cache.dao.base.DeleteDao
import com.example.there.cache.dao.base.InsertIgnoreDao
import com.example.there.cache.model.SubscriptionGroupJoin

@Dao
interface SubscriptionGroupJoinDao : DeleteDao<SubscriptionGroupJoin>, InsertIgnoreDao<SubscriptionGroupJoin>