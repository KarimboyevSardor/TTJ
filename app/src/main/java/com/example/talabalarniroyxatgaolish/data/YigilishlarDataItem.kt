package com.example.talabalarniroyxatgaolish.data

import android.os.Parcel
import android.os.Parcelable

data class YigilishlarDataItem(
    val description: String = "",
    val id: Long = 0,
    val image_base64: String = "",
    val image_name: String = "",
    val image_path: String = "",
    val meeting_place: String = "",
    val name: String = "",
    val time: String = ""
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

    companion object CREATOR : Parcelable.Creator<YigilishlarDataItem> {
        override fun createFromParcel(parcel: Parcel): YigilishlarDataItem {
            return YigilishlarDataItem(parcel)
        }

        override fun newArray(size: Int): Array<YigilishlarDataItem?> {
            return arrayOfNulls(size)
        }
    }

}