package com.example.movies.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.ui.detail.DetailActivity
import com.example.movies.utils.rxutils.RxComposers
import kotlinx.android.synthetic.main.fragment_popularity.*
import kotlinx.android.synthetic.main.fragment_popularity.recyclerViewPosters
import kotlinx.android.synthetic.main.fragment_top_rated.*

class TopRatedFragment : Fragment() {

    private lateinit var viewModel: PopularityViewModel
    private val layoutManager = GridLayoutManager(context, 2)
    private val adapter = MovieAdapter(this)

    private var isScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_rated, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModel()
        initListeners()
        initRecyclerView()
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(PopularityViewModel::class.java)
        viewModel.getTopRatedLiveData().observe(viewLifecycleOwner, Observer {
            adapter.movies = it
        })
    }

    private fun initRecyclerView() {
        recyclerViewPosters.layoutManager = layoutManager
        recyclerViewPosters.adapter = adapter

    }

    private fun initListeners() {

        adapter.onPosterClickListener = object : MovieAdapter.OnPosterClickListener {
            override fun onPosterClick(position: Int) {
                val id = adapter.movies[position].id
                val intent = DetailActivity.getIntent(context!!, id, false)
                startActivity(intent)
            }
        }

        recyclerViewPosters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val total = adapter.itemCount

                if (isScrolling && (visibleItemCount + pastVisibleItem == total)) {
                    isScrolling = false
                    viewModel.pageTopRated++
                    viewModel.loadTopRated()
                    Log.e("TAG", "SCROLL TRIGGERED")
                }
            }
        }


        )


    }
}
