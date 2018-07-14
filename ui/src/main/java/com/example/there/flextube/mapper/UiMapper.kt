package com.example.there.flextube.mapper

interface UiMapper<DO, UI> {
    fun toDomain(ui: UI): DO
    fun toUi(domain: DO): UI
}