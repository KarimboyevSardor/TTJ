package com.example.talabalarniroyxatgaolish.data

data class Rate(
    val id: Long,
    val meeting_id: Long,
    val name: String,
    var rate: String,
    val student_id: Long,
    var emoji: String
)