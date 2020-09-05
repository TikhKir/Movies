package com.example.movies.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.repository.model.Trailer
import com.example.movies.utils.diffutil.Identified
import com.example.movies.utils.diffutil.IdentityDiffUtilCallback
import kotlinx.android.synthetic.main.item_trailer.view.*

class TrailerAdapter(val context: Context?) :
    ListAdapter<Identified, RecyclerView.ViewHolder>(IdentityDiffUtilCallback<Identified>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trailer, parent, false)
        return TrailerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val trailer = getItem(position) as Trailer
        (holder as TrailerViewHolder).bind(trailer)
    }


    inner class TrailerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(trailer: Trailer) {
            itemView.textViewTrailerName.text = trailer.name

            itemView.setOnClickListener {
                val intentToTrailer = Intent(Intent.ACTION_VIEW, Uri.parse(trailer.key))
                context?.startActivity(intentToTrailer)
            }
        }

    }

}