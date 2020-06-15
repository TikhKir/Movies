package com.example.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.adapters.FavouriteAdapter
import com.example.movies.utils.rxutils.RxComposers
import kotlinx.android.synthetic.main.activity_favourite.*

class FavouriteActivity : AppCompatActivity() {

    private val adapter = FavouriteAdapter()
    private lateinit var viewModel: FavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)


        initViewModel()
        initListeners()
    }

    private fun initListeners() {
        adapter.onFavouriteItemClickListener = object : FavouriteAdapter.OnFavouriteItemClickListener{
            override fun onFavouriteItemClick(position: Int) {
                val id = adapter.favouriteMovies[position].id
                val intent = DetailActivity.getIntent(this@FavouriteActivity, id, true)
                startActivity(intent)
            }

        }
    }

    private fun initRecyclerView() {
        recyclerViewFavourite.layoutManager = LinearLayoutManager(this)
        recyclerViewFavourite.adapter = adapter


        viewModel.execute(
            viewModel.getFavouriteMovies()
                .compose(RxComposers.applyObservableSchedulers())
                .subscribe({
                    adapter.favouriteMovies = it
                },{
                    Log.e("LOAD_FAV_ERROR", it.message)
                })
        )
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        initRecyclerView()
    }
}
