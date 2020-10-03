package com.example.movies.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.movies.R
import com.example.movies.repository.model.Movie
import com.example.movies.utils.datatypes.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_detail.*

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private val reviewAdapter = ReviewAdapter()
    private val trailerAdapter = TrailerAdapter(this)
    private var movie: Movie? = null
    private var isFavourite = false

    companion object {
        private const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"

        fun getIntent(context: Context, movieId: Int): Intent {
            val intent = Intent(context, DetailActivity::class.java)
                .putExtra(EXTRA_MOVIE_ID, movieId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        initViewModel()
        initListeners()
        initRecyclerViews()
    }

    private fun initViewModel() {
        supportActionBar?.title = getString(R.string.loading)

        var movieId = -1
        if (intent.hasExtra(EXTRA_MOVIE_ID)) {
            movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)
        } else finish()

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.movieId = movieId
        //viewModel = getViewModel(movieId)

        viewModel.loadMovie(movieId).observe(this, Observer {
            movie = it
            setMovieInfo(it)
            if (viewModel.isFavourite()) activateStar() else disableStar()
        })

        viewModel.liveDataReview.observe(this, Observer {
            reviewAdapter.submitList(it.toList())
        })

        viewModel.liveDataTrailers.observe(this, Observer {
            trailerAdapter.submitList(it.toList())
        })

        viewModel.liveDataNetworkState.observe(this, Observer {
            progress_bar_detail.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            view_hide_screen.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_detail.visibility =
                if (it == NetworkState.CONNECTION_LOST) View.VISIBLE else View.GONE
        })
    }

    private fun setMovieInfo(movie: Movie) {
        Glide.with(this)
            .load(movie.bigPosterPath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .thumbnail(
                Glide.with(this)
                    .load(movie.posterPath)
            )
            .into(imageViewBigPoster)
        supportActionBar?.title = movie.title
        textViewOriginalTitle.text = movie.originalTitle
        val voteString = "${movie.voteAverage} (${movie.voteCount} ${getString(R.string.voted_all)}"
        textViewRating.text = voteString
        textViewReleaseDate.text = movie.releaseDate
        textViewDescription.text = movie.overview
    }

    private fun initListeners() {
        imageViewStar.setOnClickListener {
            if (isFavourite) {
                disableStar()
                Toast.makeText(this, getString(R.string.delete_from_favourite), Toast.LENGTH_SHORT)
                    .show()
            } else {
                activateStar()
                Toast.makeText(this, getString(R.string.add_to_favourite), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun initRecyclerViews() {
        recyclerViewTrailers.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewTrailers.adapter = trailerAdapter

        recyclerViewReviews.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewReviews.adapter = reviewAdapter
    }

    private fun disableStar() {
        isFavourite = false
        imageViewStar.setImageResource(R.drawable.star_icon_black)
    }

    private fun activateStar() {
        isFavourite = true
        imageViewStar.setImageResource(R.drawable.star_icon_gold)
    }

    override fun onStop() {
        super.onStop()
        if (isFavourite) viewModel.addMovieToFavourite()
        else viewModel.deleteFromFavourite()
        //todo: возможно не успевает отрабоать до смерти viewModel...
    }

}
