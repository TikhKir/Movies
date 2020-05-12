package com.example.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.adapters.FavouriteAdapter
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

        viewModel.getFavouriteMoviesFromDB().observe(this, Observer {
            adapter.favouriteMovies = it
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        initRecyclerView()
    }
}
