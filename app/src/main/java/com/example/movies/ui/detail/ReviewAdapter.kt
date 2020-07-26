package com.example.movies.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.data.model.Review
import kotlinx.android.synthetic.main.item_review.view.*

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    val onReviewClickListener: OnReviewClickListener? = null
    var reviews = listOf<Review>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]

        holder.textViewReviewAuthor.text = review.author
        holder.textViewReviewContent.text = review.content
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            super.itemView.setOnClickListener {
                onReviewClickListener?.onReviewClick(adapterPosition)
            }
        }

        var textViewReviewAuthor = itemView.textViewReviewAuthor
        var textViewReviewContent = itemView.textViewReviewContent
    }

    interface OnReviewClickListener {
        fun onReviewClick(position: Int)
    }
}