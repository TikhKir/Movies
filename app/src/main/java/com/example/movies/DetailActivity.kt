package com.example.movies

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.adapters.ReviewAdapter
import com.example.movies.adapters.TrailerAdapter
import com.example.movies.data.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private val reviewAdapter = ReviewAdapter()
    private val trailerAdapter = TrailerAdapter()
    private lateinit var viewModel: DetailViewModel
    private lateinit var movie: Movie
    private var isFavourite = false

    companion object {
        private const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"
        private const val EXTRA_FROM_FAVOURITE_ACTIVITY = "EXTRA_FROM_FAVOURITE_ACTIVITY"

        fun getIntent(context: Context, movieId: Int, fromFavouriteActivity: Boolean): Intent {
            val intent = Intent(context, DetailActivity::class.java)
                .putExtra(EXTRA_MOVIE_ID, movieId)
                .putExtra(EXTRA_FROM_FAVOURITE_ACTIVITY, fromFavouriteActivity)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initViewModel()
        initRecyclerViews()
        initListeners()
    }

    private fun initMovieInfo(movie: Movie) {
        Picasso.get().load(movie.bigPosterPath).into(imageViewBigPoster)
        textViewTitle.text = movie.title
        textViewOriginalTitle.text = movie.originalTitle
        textViewRating.text = movie.voteAverage.toString()
        textViewReleaseDate.text = movie.releaseDate
        textViewDescription.text = movie.overview
    }


    private fun initViewModel() {
        var movieId = -1

        if (intent.hasExtra(EXTRA_MOVIE_ID)) {
            movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)
        } else finish()

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.loadMovieInfo(movieId)
        viewModel.loadTrailersData(movieId)
        viewModel.loadReviewsData(movieId)


//        if (fromFavourite && isFavourite) {
//            viewModel.getFavouriteMovieById(movieId).observe(this, Observer {
//                movie = FavouriteMovie.convertToNotFavourite(it)
//                initMovieInfo(movie)
//            })
//        } else {
//            viewModel.getMovieById(movieId).observe(this, Observer {
//                movie = it
//                initMovieInfo(movie)
//            })
//        }


        viewModel.getAnyMovieById(movieId).observe(this, Observer {
            movie = it
            initMovieInfo(movie)

            if (it.isFavourite == 1) {
                isFavourite = true
                imageViewStar.setImageResource(R.drawable.star_icon_gold)
            } else {
                isFavourite = false
                imageViewStar.setImageResource(R.drawable.star_icon_black)
            }
        })

        viewModel.getTrailers().observe(this, Observer {
            trailerAdapter.trailers = it
        })

        viewModel.getReviews().observe(this, Observer {
            reviewAdapter.reviews = it
        })

    }

    private fun initListeners() {
        fun onActiveStar() {
            viewModel.deleteFromFavourite(movie)
        }

        fun onInactiveStar() {
            viewModel.addMovieToFavourite(movie)
        }

        imageViewStar.setOnClickListener {
            if (isFavourite) {
                onActiveStar()
            } else {
                onInactiveStar()
            }
        }

        trailerAdapter.onTrailerClickListener = object : TrailerAdapter.OnTrailerClickListener{
            override fun onTrailerClick(position: Int) {
                val link = trailerAdapter.trailers[position].key
                val intentToTrailer = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intentToTrailer)
            }

        }
    }

    private fun initRecyclerViews() {
        recyclerViewTrailers.setHasFixedSize(true)
        recyclerViewTrailers.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewTrailers.adapter = trailerAdapter

        recyclerViewReviews.setHasFixedSize(true)
        recyclerViewReviews.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewReviews.adapter = reviewAdapter
    }


}
