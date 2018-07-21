package com.example.there.cache.util

import com.example.there.cache.model.CachedGroup
import com.example.there.data.model.GroupData

val CachedGroup.toData: GroupData
    get() = GroupData(name, accountName)

val GroupData.toCache: CachedGroup
    get() = CachedGroup(name, accountName)