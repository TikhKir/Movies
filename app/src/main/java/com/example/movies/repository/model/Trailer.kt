package com.example.movies.repository.model

import com.example.movies.utils.diffutil.Identified

data class Trailer(
    val name: String = "",
    val key: String = "",

    override val identifier: String = key
) : Identified