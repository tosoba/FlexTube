package com.example.there.flextube.event

sealed class AuthEvent {
    object Successful: AuthEvent()
    object Failure: AuthEvent()
}