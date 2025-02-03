package com.example.talabalarniroyxatgaolish.data

import android.os.Parcel
import android.os.Parcelable

data class StudentDataItem(
    val auth_id: Long = 0,
    val course: String = "",
    val course_count: Int = 0,
    val id: Long = 0,
    val name: String = "",
    var room_id: Long = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(auth_id)
        parcel.writeString(course)
        parcel.writeInt(course_count)
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeLong(room_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudentDataItem> {
        override fun createFromParcel(parcel: Parcel): StudentDataItem {
            return StudentDataItem(parcel)
        }

        override fun newArray(size: Int): Array<StudentDataItem?> {
            return arrayOfNulls(size)
        }
    }
}