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
import com.example.movies.utils.datatypes.NetworkState
import com.example.movies.utils.diffutil.Identified
import com.example.movies.utils.diffutil.IdentityDiffUtilCallback
import kotlinx.android.synthetic.main.movie_item.view.*
import kotlinx.android.synthetic.main.netstate_item.view.*

class MovieListAdapter(val context: Context?) :
    ListAdapter<Identified, RecyclerView.ViewHolder>(IdentityDiffUtilCallback<Identified>()) {

    private var netState: NetworkState? = null
    val VIEWTYPE_MOVIE = 1
    val VIEWTYPE_NETSTATE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            VIEWTYPE_MOVIE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
                MovieViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.netstate_item, parent, false)
                NetStateViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEWTYPE_MOVIE) {
            val movie = getItem(position) as Movie
            (holder as MovieViewHolder).bind(movie)
        } else {
            (holder as NetStateViewHolder).bind(netState)
        }
    }



    private fun hasNetStateRow(): Boolean {
        return netState != null && netState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasNetStateRow() && position == itemCount - 1) VIEWTYPE_NETSTATE
        else VIEWTYPE_MOVIE
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasNetStateRow()) 1 else 0
    }




    inner class NetStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(netState: NetworkState?) {
            if (netState != null && netState == NetworkState.LOADING)
                itemView.progress_bar_item.visibility = View.VISIBLE
            else itemView.progress_bar_item.visibility = View.GONE
        }
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

    fun setNetState(netState: NetworkState) {
        this.netState = netState
        when (netState) {
            NetworkState.LOADING ->
                if (!hasNetStateRow()) notifyItemInserted(super.getItemCount())
            else ->
                if (hasNetStateRow()) notifyItemRemoved(super.getItemCount())
        }
    }
}