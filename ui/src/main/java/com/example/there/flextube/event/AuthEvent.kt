package com.example.there.flextube.event

sealed class AuthEvent {
    data class Successful(
            val accessToken: String,
            val accountName: String
    ) : AuthEvent()

    object Failure : AuthEvent()
}