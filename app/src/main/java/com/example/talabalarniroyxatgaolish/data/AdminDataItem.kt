package com.example.talabalarniroyxatgaolish.data

import android.os.Parcel
import android.os.Parcelable

data class AdminDataItem(
    val auth_id: Long = 0,
    val id: Long = 0,
    var name: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(auth_id)
        parcel.writeLong(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdminDataItem> {
        override fun createFromParcel(parcel: Parcel): AdminDataItem {
            return AdminDataItem(parcel)
        }

        override fun newArray(size: Int): Array<AdminDataItem?> {
            return arrayOfNulls(size)
        }
    }
}