package com.example.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.data.Trailer
import kotlinx.android.synthetic.main.item_trailer.view.*

class TrailerAdapter : RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>() {

    var onTrailerClickListener: OnTrailerClickListener? = null
    var trailers = listOf<Trailer>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trailer, parent, false)
        return TrailerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        val trailer = trailers[position]

        holder.textViewTrailerName.text = trailer.name
    }

    override fun getItemCount(): Int {
        return trailers.size
    }


    inner class TrailerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            super.itemView.setOnClickListener {
                onTrailerClickListener?.onTrailerClick(adapterPosition)
            }
        }

        var textViewTrailerName = itemView.textViewTrailerName
    }

    interface OnTrailerClickListener {
        fun onTrailerClick(position: Int)
    }
}