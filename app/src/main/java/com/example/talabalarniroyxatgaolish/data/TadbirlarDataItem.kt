package com.example.talabalarniroyxatgaolish.data

import android.os.Parcel
import android.os.Parcelable

data class TadbirlarDataItem(
    val description: String,
    val id: Long,
    val image_base64: String? = null,
    val image_name: String,
    val image_path: String,
    val meeting_place: String,
    val name: String,
    val time: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeLong(id)
        parcel.writeString(image_base64)
        parcel.writeString(image_name)
        parcel.writeString(image_path)
        parcel.writeString(meeting_place)
        parcel.writeString(name)
        parcel.writeString(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TadbirlarDataItem> {
        override fun createFromParcel(parcel: Parcel): TadbirlarDataItem {
            return TadbirlarDataItem(parcel)
        }

        override fun newArray(size: Int): Array<TadbirlarDataItem?> {
            return arrayOfNulls(size)
        }
    }

}