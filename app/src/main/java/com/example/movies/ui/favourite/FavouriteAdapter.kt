package com.example.movies.ui.favourite

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
import kotlinx.android.synthetic.main.favourite_item.view.*

class FavouriteAdapter(val context: Context?) : ListAdapter<Identified, RecyclerView.ViewHolder>(IdentityDiffUtilCallback<Identified>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.favourite_item, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = getItem(position) as Movie
        (holder as FavouriteViewHolder).bind(movie)
    }

    inner class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie) {
            Glide.with(itemView.context)
                .load(movie.posterPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.ImageViewFavouritePoster)
            itemView.textViewFavouriteItemTitle.text = movie.title
            itemView.textViewFavouriteItemDate.text = movie.releaseDate
            val votes = "${movie.voteAverage} (${movie.voteCount} ${R.string.voted_all})"
            itemView.textViewFavouriteItemDescription.text = votes

            itemView.setOnClickListener {
                val intent = DetailActivity.getIntent(context!!, movie.id)
                context.startActivity(intent)
            }
        }
    }


}