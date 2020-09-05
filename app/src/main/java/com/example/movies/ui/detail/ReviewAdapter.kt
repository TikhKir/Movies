package com.example.movies.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.repository.model.Review
import com.example.movies.utils.diffutil.Identified
import com.example.movies.utils.diffutil.IdentityDiffUtilCallback
import kotlinx.android.synthetic.main.item_review.view.*

class ReviewAdapter :
    ListAdapter<Identified, RecyclerView.ViewHolder>(IdentityDiffUtilCallback<Identified>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val review = getItem(position) as Review
        (holder as ReviewViewHolder).bind(review)
    }


    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(review: Review) {
            itemView.textViewReviewAuthor.text = review.author
            itemView.textViewReviewContent.text = review.content
        }
    }


}