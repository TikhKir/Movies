package com.example.movies.network.rawmodel

import com.example.movies.data.model.Review
import com.google.gson.annotations.SerializedName


data class ReviewRaw(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("author")
    val author: String = "",

    @SerializedName("content")
    val content: String = "",

    @SerializedName("url")
    val url: String = ""
) {

    fun toReview(): Review {
        return Review(
            author,
            content
        )
    }

}

