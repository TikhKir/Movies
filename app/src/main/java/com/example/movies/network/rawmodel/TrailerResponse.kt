package com.example.movies.network.rawmodel

import com.google.gson.annotations.SerializedName

data class TrailerResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("results")
    val trailerListRaw: List<TrailerRaw>
)