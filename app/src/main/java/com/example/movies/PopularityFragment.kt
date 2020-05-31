package com.example.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.adapters.MovieAdapter
import com.example.movies.utils.NetworkUtils
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
        viewModel.getPopularityMovieList().observe(viewLifecycleOwner, Observer {
            adapter.movies = it
        })
    }

    private fun initRecyclerView() {
        recyclerViewPosters.setHasFixedSize(true)
        recyclerViewPosters.layoutManager = GridLayoutManager(context, 2)
        recyclerViewPosters.adapter = adapter
    }

    private fun initListeners() {
        viewModel.loadData(NetworkUtils.POPULARITY)

        adapter.onPosterClickListener = object : MovieAdapter.OnPosterClickListener {
            override fun onPosterClick(position: Int) {
                val id = adapter.movies[position].id
                val intent = DetailActivity.getIntent(context!!, id, false)
                startActivity(intent)
                Toast.makeText(context, adapter.movies[position].title, Toast.LENGTH_SHORT).show()
            }
        }

        adapter.onReachEndListener = object : MovieAdapter.OnReachEndListener {
            override fun onReachEnd() {
                Toast.makeText(context, "конец списка", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
