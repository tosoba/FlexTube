package com.example.there.data.mapper

import com.example.there.data.model.GroupData
import com.example.there.domain.model.Group

object GroupMapper: TwoWayDataMapper<GroupData, Group> {
    override fun toData(domain: Group): GroupData = GroupData(domain.name, domain.accountName)

    override fun toDomain(data: GroupData): Group = Group(data.name, data.accountName)
}