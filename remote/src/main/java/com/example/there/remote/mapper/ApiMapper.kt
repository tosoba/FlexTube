package com.example.there.remote.mapper

interface ApiMapper<AP, DT> {
    fun toData(api: AP): DT
}