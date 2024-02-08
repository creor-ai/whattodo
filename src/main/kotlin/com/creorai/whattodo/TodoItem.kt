package com.creorai.whattodo

data class TodoItem(
    val filename: String,
    val filepath: String,
    val line: Int,
    val comment: String,
    val type: String
)
