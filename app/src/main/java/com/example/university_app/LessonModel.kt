package com.example.university_app

data class LessonModel(
    var id: Int = 0,
    var subject: String = "",
    var tutor: String = "",
    var starttime: String = "",
    var auditory: String = "",
    var day: String = "",
    var type: Int = 0
)
