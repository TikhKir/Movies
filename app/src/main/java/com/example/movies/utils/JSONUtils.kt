package com.example.movies.utils

import android.util.Log
import com.example.movies.data.Movie
import com.example.movies.data.Review
import com.example.movies.data.Trailer
import org.json.JSONException
import org.json.JSONObject

class JSONUtils {

    companion object {

        const val KEY_YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v="

        //for movies info
        const val KEY_RESULTS = "results"
        const val KEY_VOTE_COUNT = "vote_count"
        const val KEY_ID = "id"
        const val KEY_TITLE = "title"
        const val KEY_ORIGINAL_TITLE = "original_title"
        const val KEY_OVERVIEW = "overview"
        const val KEY_POSTER_PATH = "poster_path"
        const val KEY_BACKDROP_PATH = "backdrop_path"
        const val KEY_VOTE_AVERAGE = "vote_average"
        const val KEY_RELEASE_DATE = "release_date"

        //for comment
        const val KEY_REVIEW_AUTHOR = "author"
        const val KEY_REVIEW_CONTENT = "content"

        //for videos
        const val KEY_VIDEO_KEY = "key"
        const val KEY_VIDEO_NAME = "name"

        //for images
        const val BASE_POSTER_URL = "https://image.tmdb.org/t/p/"
        const val SMALL_POSTER_SIZE = "w185"
        const val BIG_POSTER_SIZE = "w780"


        fun getListReviewsDataFromJsonObject(jsonObject: JSONObject): List<Review> {
            val jsonArray = jsonObject.getJSONArray(KEY_RESULTS)
            val result = ArrayList<Review>()

            try {
                for (i in 0 until jsonArray.length()) {
                    val tempObject = jsonArray[i] as JSONObject

                    val author = tempObject.getString(KEY_REVIEW_AUTHOR)
                    val content = tempObject.getString(KEY_REVIEW_CONTENT)
                    val review = Review(author, content)
                    result.add(review)
                }
            } catch (e: JSONException) {
                Log.e("JSONutils", e.message)
            }
            return result
        }

        fun getListTrailersDataFromJsonObject(jsonObject: JSONObject): List<Trailer> {
            val jsonArray = jsonObject.getJSONArray(KEY_RESULTS)
            val result = ArrayList<Trailer>()

            try {
                for (i in 0 until jsonArray.length()) {
                    val tempObject = jsonArray[i] as JSONObject

                    val name = tempObject.getString(KEY_VIDEO_NAME)
                    val key = KEY_YOUTUBE_BASE_URL + tempObject.getString(KEY_VIDEO_KEY)
                    val trailer = Trailer(name, key)
                    result.add(trailer)
                }
            } catch (e: JSONException) {
                Log.e("JSONutils", e.message)
            }
            return result
        }

        fun getListMovieDataFromJsonObject(jsonObject: JSONObject): List<Movie> {
            val jsonArray = jsonObject.getJSONArray(KEY_RESULTS)
            val result = ArrayList<Movie>()

            try {
                for (i in 0 until jsonArray.length()) {
                    val tempObject = jsonArray[i] as JSONObject

                    val id = tempObject.getInt(KEY_ID)
                    val voteCount = tempObject.getInt(KEY_VOTE_COUNT)
                    val title = tempObject.getString(KEY_TITLE)
                    val originalTitle = tempObject.getString(KEY_ORIGINAL_TITLE)
                    val overview = tempObject.getString(KEY_OVERVIEW)
                    val posterPath = BASE_POSTER_URL + SMALL_POSTER_SIZE + tempObject.getString(KEY_POSTER_PATH)
                    val bigPosterPath = BASE_POSTER_URL + BIG_POSTER_SIZE + tempObject.getString(KEY_POSTER_PATH)
                    val backdropPath = tempObject.getString(KEY_BACKDROP_PATH)
                    val voteAverage = tempObject.getDouble(KEY_VOTE_AVERAGE)
                    val releaseDate = tempObject.getString(KEY_RELEASE_DATE)
                    val movie = Movie(id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath, backdropPath, voteAverage, releaseDate, 0)
                    result.add(movie)
                }
            } catch (e: JSONException) {
                Log.e("JSONutils", e.message)
            }
            return result
        }


        fun getMovieDataFromJsonObject(jsonObject: JSONObject): Movie? {
            var result : Movie? = null

            try {
                val id = jsonObject.getInt(KEY_ID)
                val voteCount = jsonObject.getInt(KEY_VOTE_COUNT)
                val title = jsonObject.getString(KEY_TITLE)
                val originalTitle = jsonObject.getString(KEY_ORIGINAL_TITLE)
                val overview = jsonObject.getString(KEY_OVERVIEW)
                val posterPath = BASE_POSTER_URL + SMALL_POSTER_SIZE + jsonObject.getString(KEY_POSTER_PATH)
                val bigPosterPath = BASE_POSTER_URL + BIG_POSTER_SIZE + jsonObject.getString(KEY_POSTER_PATH)
                val backdropPath = jsonObject.getString(KEY_BACKDROP_PATH)
                val voteAverage = jsonObject.getDouble(KEY_VOTE_AVERAGE)
                val releaseDate = jsonObject.getString(KEY_RELEASE_DATE)
                result = Movie(id, voteCount, title, originalTitle, overview, posterPath, bigPosterPath, backdropPath, voteAverage, releaseDate, 0)
            } catch (e: JSONException) {
                Log.e("JSONutils", e.message)
            }
            return result
        }


    }
}