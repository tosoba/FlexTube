package com.example.there.multifeeds.model

import android.os.Parcel
import android.os.Parcelable

data class UiGroup(val accountName: String, val name: String) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString()!!, parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(accountName)
        parcel.writeString(name)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<UiGroup> {
        override fun createFromParcel(parcel: Parcel): UiGroup = UiGroup(parcel)

        override fun newArray(size: Int): Array<UiGroup?> = arrayOfNulls(size)
    }
}