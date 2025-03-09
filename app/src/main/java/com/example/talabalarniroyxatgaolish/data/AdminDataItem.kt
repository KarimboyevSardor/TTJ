package com.example.talabalarniroyxatgaolish.data

import android.os.Parcel
import android.os.Parcelable

data class AdminDataItem(
    var auth_id: Long = 0,
    val id: Long = 0,
    var name: String = "",
    var login: String = "",
    var password: String = "",
    var role: String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(auth_id)
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(login)
        parcel.writeString(password)
        parcel.writeString(role)
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