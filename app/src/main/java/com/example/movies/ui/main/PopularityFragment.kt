package com.example.movies.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.data.model.Review
import com.example.movies.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_popularity.*

class PopularityFragment : Fragment() {

    private lateinit var viewModel: MainFragmentsViewModel
    private val layoutManager = GridLayoutManager(context, 2)
    private val adapter = MovieListAdapter()
    private var isScrolling = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_popularity, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModel()
        initListeners()
        initRecyclerView()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainFragmentsViewModel::class.java)
        viewModel.getPopularityLiveData().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun initRecyclerView() {
        recyclerViewPosters.setHasFixedSize(true)
        recyclerViewPosters.layoutManager = layoutManager
        recyclerViewPosters.adapter = adapter
    }

    private fun initListeners() {
        adapter.onPosterClickListener = object : MovieListAdapter.OnPosterClickListener {
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
                    //viewModel.pagePopularity++
                    //viewModel.loadPopularity()
                    Log.e("TAG", "SCROLL TRIGGERED")
                }
            }
        })
    }
}

