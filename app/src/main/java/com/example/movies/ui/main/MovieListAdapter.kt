package com.example.movies.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.data.model.Movie
import com.example.movies.utils.diffutil.Identified
import com.example.movies.utils.diffutil.IdentityDiffUtilCallback
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieListAdapter :
    ListAdapter<Identified, RecyclerView.ViewHolder>(IdentityDiffUtilCallback<Identified>()) {

    var onPosterClickListener: OnPosterClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = getItem(position) as Movie
        (holder as MovieViewHolder).bind(movie)
    }

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            super.itemView.setOnClickListener {
                onPosterClickListener?.onPosterClick(adapterPosition)
            }
        }

        fun bind(movie: Movie?) {
            Glide.with(itemView.context)
                .load(movie?.posterPath)
                .into(itemView.imageViewSmallPoster)
        }
    }

    interface OnPosterClickListener {
        fun onPosterClick(position: Int)
    }
}