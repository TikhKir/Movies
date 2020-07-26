package com.example.movies.constants

enum class SortTypes(val value: String) {
    //use .ordinal as index for db searchBy value (order is important!)
    //use .value as query for network request

    POPULARITY("popularity.desc"),
    TOP_RATED("vote_average.desc")
}