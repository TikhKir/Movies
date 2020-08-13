package com.example.movies.ui.main.toprated

import android.os.Bundle
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
import com.example.movies.ui.detail.DetailActivity
import com.example.movies.ui.main.MovieListAdapter
import com.example.movies.utils.NpaGridLayoutManager
import com.example.movies.utils.datatypes.NetworkState
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_popularity.*
import kotlinx.android.synthetic.main.fragment_popularity.recyclerViewPosters
import kotlinx.android.synthetic.main.fragment_top_rated.*

class TopRatedFragment : Fragment() {

    private lateinit var viewModel: TopRatedViewModel
    private val layoutManager = NpaGridLayoutManager(context, 2)
    private val adapter = MovieListAdapter()

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
        viewModel = ViewModelProvider(this).get(TopRatedViewModel::class.java)
        viewModel.getMoviesLiveData().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.toList())
        })
        viewModel.getNetworkStateLiveData().observe(viewLifecycleOwner, Observer {
            progress_bar_top_rated.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            txt_error_top_rated.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty() && it == NetworkState.ERROR) {
                Snackbar.make(activity!!.VPMain, R.string.connection_error, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun initRecyclerView() {
        recyclerViewPosters.layoutManager = layoutManager
        recyclerViewPosters.adapter = adapter
    }

    private fun initListeners() {
//        adapter.onPosterClickListener = object :
//            MovieListAdapter.OnPosterClickListener {
//            override fun onPosterClick(position: Int) {
//                val id = adapter.movies[position].id
//                val intent = DetailActivity.getIntent(context!!, id, false)
//                startActivity(intent)
//            }
//        }

        recyclerViewPosters.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL ||
                    newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val total = adapter.itemCount

                if (isScrolling && (visibleItemCount + pastVisibleItem >= total - 6)) {
                    isScrolling = false
                    viewModel.loadTopRated()
                }
            }
        })
    }
}
