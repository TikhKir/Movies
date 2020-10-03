package com.example.movies.ui.favourite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_favourite.*

@AndroidEntryPoint
class FavouriteActivity : AppCompatActivity() {

    private val adapter = FavouriteAdapter(this)
    private lateinit var viewModel: FavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        supportActionBar?.title = getString(R.string.itemFavourite)

        initViewModel()
        initRecyclerView()
    }




    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)

        viewModel.loadFavourite().observe(this, Observer {
            adapter.submitList(it.toList())
        })
    }

    private fun initRecyclerView() {
        recyclerViewFavourite.layoutManager = LinearLayoutManager(this)
        recyclerViewFavourite.adapter = adapter
    }

}
