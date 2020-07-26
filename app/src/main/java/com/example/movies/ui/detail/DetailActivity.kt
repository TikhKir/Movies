package com.example.movies.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.data.model.Movie
import com.example.movies.utils.rxutils.RxComposers
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
        supportActionBar?.title = movie.title
        textViewOriginalTitle.text = movie.originalTitle
        val voteString = "${movie.voteAverage} (${movie.voteCount} ${getString(R.string.voted_all)}"
        textViewRating.text = voteString
        textViewReleaseDate.text = movie.releaseDate
        textViewDescription.text = movie.overview
    }

    private fun initViewModel() {
        var movieId = -1

        if (intent.hasExtra(EXTRA_MOVIE_ID)) {
            movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)
        } else finish()

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

//        viewModel.execute(
//            viewModel.getMovieById(movieId)
//                .compose(RxComposers.applyObservableSchedulers())
//                .subscribe({
//                    it?.let {
//                        movie = it
//                        initMovieInfo(it)
//                    }
//                    if (it?.isFavourite == 1) {
//                        isFavourite = true
//                        imageViewStar.setImageResource(R.drawable.star_icon_gold)
//                    } else {
//                        isFavourite = false
//                        imageViewStar.setImageResource(R.drawable.star_icon_black)
//                    }
//                },{
//                    Log.e("LOAD_MOVIE", it.message)
//                })
//        )



        viewModel.execute(
            viewModel.getTrailers(movieId)
                .compose(RxComposers.applySingleSchedulers())
                .subscribe({
                    trailerAdapter.trailers = it
                },{
                    Log.e("LOAD_TRAILERS", it.message)
                })
        )

        viewModel.execute(
            viewModel.getReviews(movieId)
                .compose(RxComposers.applySingleSchedulers())
                .subscribe({
                    reviewAdapter.reviews = it
                },{
                    Log.e("LOAD_REVIEWS", it.message)
                })
        )


    }

    private fun initListeners() {
        fun onActiveStar() {
            viewModel.deleteFromFavourite(movie)
            Toast.makeText(this, getString(R.string.delete_from_favourite), Toast.LENGTH_SHORT).show()
        }

        fun onInactiveStar() {
            viewModel.addMovieToFavourite(movie)
            Toast.makeText(this, getString(R.string.add_to_favourite), Toast.LENGTH_SHORT).show()
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
