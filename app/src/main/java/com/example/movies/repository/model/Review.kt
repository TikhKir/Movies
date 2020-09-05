package com.example.movies.repository.model

import com.example.movies.utils.diffutil.Identified

data class Review(
    val author: String = "",
    val content: String = "",

    override val identifier: String = content
): Identified