package com.example.there.presentation.subfeed

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.there.domain.usecase.impl.GetUserSubscriptions
import javax.inject.Inject

class SubFeedViewModel @Inject constructor(private val getUserSubscriptions: GetUserSubscriptions): ViewModel() {

    fun loadUserSubscriptions(accessToken: String) {
        getUserSubscriptions.execute(accessToken).subscribe({
            Log.e("SUBS", it.joinToString(",") { it.title })
        }, {
            Log.e("ERROR", it.message ?: "fuck you")
        })
    }
}