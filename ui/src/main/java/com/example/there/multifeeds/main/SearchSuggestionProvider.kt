package com.example.there.multifeeds.main

import android.content.SearchRecentSuggestionsProvider

class SearchSuggestionProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.example.there.multifeeds.main.SearchSuggestionProvider"
        const val MODE = DATABASE_MODE_QUERIES
    }
}