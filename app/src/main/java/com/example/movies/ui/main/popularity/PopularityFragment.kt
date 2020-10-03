package com.example.movies.ui.main.popularity

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
import com.example.movies.ui.main.MovieListAdapter
import com.example.movies.utils.NpaGridLayoutManager
import com.example.movies.utils.datatypes.NetworkState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_popularity.*

@AndroidEntryPoint
class PopularityFragment : Fragment() {

    private lateinit var viewModel: PopularityViewModel
    private lateinit var layoutManager: NpaGridLayoutManager
    private lateinit var adapter: MovieListAdapter
    private var isScrolling = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_popularity, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = MovieListAdapter(context)

        initViewModel()
        initListeners()
        initRecyclerView()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(PopularityViewModel::class.java)
        viewModel.getMoviesLiveData().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.toList())
        })

        viewModel.getNetworkStateLiveData().observe(viewLifecycleOwner, Observer {
            progress_bar_popular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            txt_error_popular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.CONNECTION_LOST) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty() && it == NetworkState.CONNECTION_LOST) {
                Snackbar.make(activity!!.VPMain, R.string.connection_error, Snackbar.LENGTH_LONG).show()
            }

            if (!viewModel.listIsEmpty()) adapter.setNetState(it)
        })
    }

    private fun initRecyclerView() {
        layoutManager = NpaGridLayoutManager(context, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return if (viewType == adapter.VIEWTYPE_MOVIE) 1 else 2
            }
        }

        recyclerViewPosters.layoutManager = layoutManager
        recyclerViewPosters.adapter = adapter
    }

    private fun initListeners() {

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

                if (isScrolling && (visibleItemCount + pastVisibleItem == total)) {
                    isScrolling = false
                    viewModel.loadPopularity()
                    //Log.e("TAG", "${visibleItemCount} ${pastVisibleItem} ${total}")
                }
            }
        })
    }
}

