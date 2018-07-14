package com.example.there.remote.model

data class VideoCategoriesResponse(
        val items: List<ApiVideoCategory>
)

data class ApiVideoCategory(
        val id: String,
        val snippet: VideoCategorySnippet
)

data class VideoCategorySnippet(
        val title: String
)