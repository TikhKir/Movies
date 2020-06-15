package com.example.movies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.adapters.MovieAdapter
import com.example.movies.utils.rxutils.RxComposers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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

        viewModel.execute(
            viewModel.getPopularity()
                .compose(RxComposers.applyObservableSchedulers())
                .subscribe({
                    adapter.movies = it
                }, {
                    Log.e("ACTIVITY_OPEN_POP", it.message)
                })
        )
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

        adapter.onReachEndListener = object : MovieAdapter.OnReachEndListener {
            override fun onReachEnd() {

                viewModel.pagePopularity++
                viewModel.execute(
                    viewModel.getPopularity()
                        .compose(RxComposers.applyObservableSchedulers())
                        .subscribe({
                            adapter.movies.plus(it)
                        }, {
                            Log.e("ACTIVITY_OPEN_POP", it.message)
                        })
                )
                Toast.makeText(context, "Pop_Movies is loading", Toast.LENGTH_SHORT).show()
                Log.e("POP_FRAGMENT", "END OF LIST")
            }


        }
    }
}
