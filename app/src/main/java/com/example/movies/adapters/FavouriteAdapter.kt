package com.example.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.data.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.favourite_item.view.*

class FavouriteAdapter : RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {

    var onFavouriteItemClickListener : OnFavouriteItemClickListener? = null
    var favouriteMovies = listOf<Movie>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.favourite_item, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favouriteMovies.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        Picasso.get()
            .load(favouriteMovies[position].posterPath)
            .into(holder.imageViewPoster)
        holder.textViewTitle.text = favouriteMovies[position].title
        holder.textViewDate.text = favouriteMovies[position].releaseDate
        val votes = "${favouriteMovies[position].voteAverage} (${favouriteMovies[position].voteCount})"
        holder.textViewDescription.text = votes
    }

    inner class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            super.itemView.setOnClickListener{
                onFavouriteItemClickListener?.onFavouriteItemClick(adapterPosition)
            }
        }

        val imageViewPoster = itemView.ImageViewFavouritePoster
        val textViewTitle = itemView.textViewFavouriteItemTitle
        val textViewDate = itemView.textViewFavouriteItemDate
        val textViewDescription = itemView.textViewFavouriteItemDescription
    }

    interface OnFavouriteItemClickListener {
        fun onFavouriteItemClick(position: Int)
    }
}