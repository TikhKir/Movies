package com.example.movies.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.R
import com.example.movies.ui.detail.DetailActivity
import com.example.movies.utils.rxutils.RxComposers
import kotlinx.android.synthetic.main.fragment_popularity.*

class PopularityFragment : Fragment() {

    private lateinit var viewModel: PopularityViewModel
    private val adapter = MovieAdapter(this)

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
        viewModel = ViewModelProvider(this).get(PopularityViewModel::class.java)
        viewModel.getPopularityLiveData().observe(viewLifecycleOwner, Observer {
            adapter.movies = it
        })
    }

    private fun initRecyclerView() {
        recyclerViewPosters.setHasFixedSize(true)
        recyclerViewPosters.layoutManager = GridLayoutManager(context, 2)
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


    }


}

