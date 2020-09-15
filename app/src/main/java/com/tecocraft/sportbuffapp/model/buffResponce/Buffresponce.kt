package com.tecocraft.sportbuffapp.model.buffResponce

data class BuffResponce(
    val result: Result
)

data class Result(
    val answers: List<Answer>,
    val author: Author,
    val client_id: Int,
    val created_at: String,
    val id: Int,
    val language: String,
    val priority: Int,
    val question: Question,
    val stream_id: Int,
    val time_to_show: Int
)

data class Answer(
    val buff_id: Int,
    val id: Int,
    val image: Image,
    val title: String
)

data class Author(
    val first_name: String,
    val image: String,
    val last_name: String
)

data class Question(
    val category: Int,
    val id: Int,
    val title: String
)

data class Image(
    val `0`: I1,
    val `1`: I2,
    val `2`: I3
)

data class I1(
    val id: String,
    val key: String,
    val url: String
)

data class I2(
    val id: String,
    val key: String,
    val url: String
)

data class I3(
    val id: String,
    val key: String,
    val url: String
)