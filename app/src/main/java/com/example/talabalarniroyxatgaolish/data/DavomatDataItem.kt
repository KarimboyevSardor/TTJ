package com.example.talabalarniroyxatgaolish.data

data class DavomatDataItem(
    val date: String = "",
    val id: Long = 0,
    var is_there: Boolean = false,
    val room_id: Long = 0,
    val student_id: Long = 0,
    val name: String = "",
    val course: String = "",
    val room_count: String = "",
    val course_count: Long = 0
)