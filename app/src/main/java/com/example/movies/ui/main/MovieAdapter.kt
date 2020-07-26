package com.example.movies.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.data.model.Movie
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieAdapter(private val mContext: Fragment): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    var onPosterClickListener: OnPosterClickListener? = null
    var movies = listOf<Movie>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        Glide.with(mContext)
            .load(movie.bigPosterPath)
            .thumbnail(0.2f)
            .override(400)
            .into(holder.imageViewSmallPoster)
    }

    override fun getItemCount(): Int {
        return movies.size
    }


    inner class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            super.itemView.setOnClickListener {
                onPosterClickListener?.onPosterClick(adapterPosition)
            }
        }

        val imageViewSmallPoster = itemView.imageViewSmallPoster
    }

    interface OnPosterClickListener {
        fun onPosterClick(position: Int)
    }

}