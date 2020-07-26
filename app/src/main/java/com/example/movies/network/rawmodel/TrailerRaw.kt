package com.example.movies.network.rawmodel

import com.example.movies.data.model.Trailer
import com.example.movies.network.services.BaseService
import com.google.gson.annotations.SerializedName


data class TrailerRaw(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("iso_639_1")
    val iso6391: String = "",

    @SerializedName("iso_3166_1")
    val iso31661: String = "",

    @SerializedName("key")
    val key: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("site")
    val site: String = "",

    @SerializedName("size")
    val size: Long? = null,

    @SerializedName("type")
    val type: String = ""
) {

    fun toTrailer(): Trailer {
        return Trailer(name, buildYouTubeUrl(key))
    }

    private fun buildYouTubeUrl(key: String): String {
        return BaseService.KEY_YOUTUBE_BASE_URL + key
    }

}

