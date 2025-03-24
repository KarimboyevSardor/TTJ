package com.example.talabalarniroyxatgaolish.data

data class AddedStudentDataItem(
    val course: String = "",
    val course_count: Int = 0,
    val id: Long = 0,
    val name: String = "",
    var room_id: Long = 0,
    val login: String = "",
    val password: String = "",
    val role: String = "student",
    var auth_id: Long = 0
)
