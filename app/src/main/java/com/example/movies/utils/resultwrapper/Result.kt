package com.example.movies.utils.resultwrapper


data class Result<T>(
    var status: StatusType,
    var data: T? = null
)