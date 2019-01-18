package com.example.there.multifeeds.model

import android.databinding.ObservableArrayList
import android.os.Parcel
import android.os.Parcelable

data class UiGroupWithSubscriptions(
        val accountName: String,
        val name: String,
        val subscriptions: ObservableArrayList<UiSubscription>
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            ObservableArrayList<UiSubscription>()
    ) {
        val saved: List<UiSubscription> = ArrayList()
        parcel.readList(saved, UiSubscription::class.java.classLoader)
        subscriptions.addAll(saved)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(accountName)
        parcel.writeString(name)
        parcel.writeList(subscriptions)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<UiGroupWithSubscriptions> {
        override fun createFromParcel(parcel: Parcel): UiGroupWithSubscriptions = UiGroupWithSubscriptions(parcel)

        override fun newArray(size: Int): Array<UiGroupWithSubscriptions?> = arrayOfNulls(size)
    }
}