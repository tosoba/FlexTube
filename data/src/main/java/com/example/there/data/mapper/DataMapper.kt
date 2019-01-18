package com.example.there.data.mapper

interface OneWayDataMapper<DT, DO> {
    fun toDomain(data: DT): DO
}

interface TwoWayDataMapper<DT, DO> : OneWayDataMapper<DT, DO> {
    fun toData(domain: DO): DT
}