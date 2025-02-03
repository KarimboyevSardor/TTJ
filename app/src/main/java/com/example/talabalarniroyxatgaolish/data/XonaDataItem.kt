package com.example.talabalarniroyxatgaolish.data

import android.os.Parcel
import android.os.Parcelable

data class XonaDataItem(
    val id: Long = 0,
    val room_count: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(room_count)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<XonaDataItem> {
        override fun createFromParcel(parcel: Parcel): XonaDataItem {
            return XonaDataItem(parcel)
        }

        override fun newArray(size: Int): Array<XonaDataItem?> {
            return arrayOfNulls(size)
        }
    }
}