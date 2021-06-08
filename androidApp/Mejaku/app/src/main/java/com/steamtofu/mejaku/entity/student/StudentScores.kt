package com.steamtofu.mejaku.entity.student

data class StudentScores(
    val name: String,
    val studentMail: String,
    val parentMail: String,
    val quiz: Float,
    val assignment1: Float,
    val assignment2: Float,
    val assignment3: Float,
    var scoresPrediction: Float = 0.0f
)
