package com.example.movies.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.movies.R
import com.example.movies.repository.model.Movie
import com.example.movies.ui.detail.DetailActivity
import com.example.movies.utils.diffutil.Identified
import com.example.movies.utils.diffutil.IdentityDiffUtilCallback
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieListAdapter(val context: Context?) :
    ListAdapter<Identified, RecyclerView.ViewHolder>(IdentityDiffUtilCallback<Identified>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = getItem(position) as Movie
        (holder as MovieViewHolder).bind(movie)
    }

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie) {
            Glide.with(itemView.context)
                .load(movie.posterPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.imageViewSmallPoster)
            itemView.textViewPosterTitle.text = movie.title
            itemView.textViewPosterDate.text = movie.releaseDate.take(4)

            itemView.setOnClickListener {
                val intent = DetailActivity.getIntent(context!!, movie.id)
                context.startActivity(intent)
            }
        }
    }


}