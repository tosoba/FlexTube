package com.example.there.data.mapper

interface DataMapper<DT, DO> {
    fun toDomain(data: DT): DO
    fun toData(domain: DO): DT
}