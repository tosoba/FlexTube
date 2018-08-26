package com.example.there.flextube.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class UiSubscription(
        val id: String,
        val publishedAt: Date,
        val title: String,
        val description: String,
        val channelId: String,
        val thumbnailUrl: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            Date(parcel.readLong()),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeLong(publishedAt.time)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(channelId)
        parcel.writeString(thumbnailUrl)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<UiSubscription> {
        override fun createFromParcel(parcel: Parcel): UiSubscription = UiSubscription(parcel)

        override fun newArray(size: Int): Array<UiSubscription?> = arrayOfNulls(size)
    }
}