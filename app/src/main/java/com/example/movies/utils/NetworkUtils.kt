package com.example.movies.utils

import android.net.Uri
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class NetworkUtils {

    companion object {
        private const val API_KEY = "018f57ebd435772093350921a7d5c2b7"
        private const val BASE_URL = "https://api.themoviedb.org/3"

        private const val PATH_DISCOVER = "discover"
        private const val PATH_MOVIE = "movie"
        private const val PATH_VIDEOS = "videos"
        private const val PATH_REVIEWS = "reviews"

        private const val PARAMS_API_KEY = "api_key"
        private const val PARAMS_LANGUAGE = "language"
        private const val PARAMS_SORT_BY = "sort_by"
        private const val PARAMS_PAGE = "page"
        private const val PARAMS_MIN_VOTE_COUNT = "vote_count.gte"

        private const val LANGUAGE_VALUE = "ru-RU"
        private const val SORT_BY_POPULARITY = "popularity.desc"
        private const val SORT_BY_TOP_RATED = "vote_average.desc"
        private const val MIN_VOTE_COUNT_VALUE = "500"

        const val POPULARITY = 0
        const val TOP_RATED = 1



        fun getMovies(sortBy: Int, page: Int): Single<JSONObject> {
            val url = buildURL(sortBy, page)
            return jsonLoadSingle(url)
        }

        fun getMovieByID(movieId: Int): Single<JSONObject> {
            val url = buildURL(movieId)
            return jsonLoadSingle(url)
        }

        fun getJSONForVideos(movieId: Int): Single<JSONObject> {
            val url = buildURLToVideos(movieId)
            return  jsonLoadSingle(url)
        }

        fun getJSONForReviews(movieId: Int): Single<JSONObject> {
            val url = buildURLToReviews(movieId)
            return  jsonLoadSingle(url)
        }




        private fun buildURL(sortBy: Int, page: Int): URL {
            var result: URL? = null
            val methodOfSort =
                when (sortBy) {
                    POPULARITY -> SORT_BY_POPULARITY
                    else -> SORT_BY_TOP_RATED
                }

            val uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(PATH_DISCOVER)
                .appendPath(PATH_MOVIE)
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .appendQueryParameter(PARAMS_SORT_BY, methodOfSort)
                .appendQueryParameter(PARAMS_PAGE, page.toString())
                .appendQueryParameter(PARAMS_MIN_VOTE_COUNT, MIN_VOTE_COUNT_VALUE)
                .build()

            try {
                result = URL(uri.toString())
            } catch (e: MalformedURLException) {
                Log.e("buildURL", e.message)
            }
            if (result != null) return result
            else return URL("")
        }

        private fun buildURL(movieId: Int): URL {
            var result: URL? = null

            val uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(movieId.toString())
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .build()

            try {
                result = URL(uri.toString())
            } catch (e: MalformedURLException) {
                Log.e("buildURL", e.message)
            }
            if (result != null) return result
            else return URL("")
        }

        private fun buildURLToVideos(movieId: Int): URL {
            var result: URL? = null

            val uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(movieId.toString())
                .appendPath(PATH_VIDEOS)
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .build()

            try {
                result = URL(uri.toString())
            } catch (e: MalformedURLException) {
                Log.e("buildURL", e.message)
            }
            if (result != null) return result
            else return URL("")
        }

        private fun buildURLToReviews(movieId: Int): URL {
            var result: URL? = null

            val uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(movieId.toString())
                .appendPath(PATH_REVIEWS)
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                //.appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .build()

            try {
                result = URL(uri.toString())
            } catch (e: MalformedURLException) {
                Log.e("buildURL", e.message)
            }
            if (result != null) return result
            else return URL("")
        }





        private fun jsonLoadSingle(url: URL): Single<JSONObject> {
            return Single.create<JSONObject> {
                var result: JSONObject? = null
                var connection: HttpURLConnection? = null

                try {
                    connection = url.openConnection() as HttpURLConnection
                    val inputStream = connection.inputStream
                    val inputStreamReader = InputStreamReader(inputStream)
                    val reader = BufferedReader(inputStreamReader)
                    val builder = StringBuilder()
                    var line = reader.readLine()

                    while (line != null) {
                        builder.append(line)
                        line = reader.readLine()
                    }

                    result = JSONObject(builder.toString())
                    reader.close()

                } catch (e: IOException) {
                    it.onError(e)
                } catch (e: JSONException) {
                    it.onError(e)
                } finally {
                    connection?.disconnect()
                }

//                if (result == null) it.onError(Throwable("жопа"))
//                else it.onSuccess(result)

                if (result != null) it.onSuccess(result)
            }
        }

    }


}




