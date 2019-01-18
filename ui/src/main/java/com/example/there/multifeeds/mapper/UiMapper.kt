package com.example.there.multifeeds.mapper

interface UiMapper<DO, UI> {
    fun toDomain(ui: UI): DO
    fun toUi(domain: DO): UI
}