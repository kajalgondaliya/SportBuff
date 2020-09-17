package com.tecocraft.buffoverlayview.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Akshay Jariwala
 */
data class Buff(
    val id: Long? = null,
    val priority: Int? = null,
    val author: Author? = null,
    val question: Question? = null,
    val answers: List<Answer>? = null,

    @SerializedName("time_to_show")
    val timeToShow: Int? = null
) {

    data class Author(
        val image: String?,

        @SerializedName("first_name")
        val firstName: String?,

        @SerializedName("last_name")
        val lastName: String?
    )

    data class Question(
        val id: Long?,
        val title: String?
    )

    data class Answer(
        val id: Long?,
        val title: String?,

        @SerializedName("image")
        val imageModel: AnswerImageModel,

        @SerializedName("buff_id")
        val buffId: Long?
    )

    data class AnswerImageModel(
        @SerializedName("0")
        val image0: AnswerImage?,

        @SerializedName("1")
        val image1: AnswerImage?,

        @SerializedName("2")
        val image2: AnswerImage?
    )

    data class AnswerImage(
        val id: String?,
        val key: String?,
        val url: String?
    )
}

