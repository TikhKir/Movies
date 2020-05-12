package com.example.movies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movies.adapters.MovieAdapter
import com.example.movies.data.Movie
import com.example.movies.utils.JSONUtils
import com.example.movies.utils.NetworkUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private val adapter = MovieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        initViewModel()
        initListeners()
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getMovieList().observe(this, Observer {
            adapter.movies = it
        })
    }

    private fun initRecyclerView() {
        recyclerViewPosters.layoutManager = GridLayoutManager(this, 2)
        recyclerViewPosters.adapter = adapter
    }

    private fun initListeners() {

        fun onCheckTopRated() {
            switchSort.isChecked = true
            viewModel.methodOfSort = NetworkUtils.TOP_RATED
            viewModel.deleteMovies()
            viewModel.loadData()
        }

        fun onCheckPopularity() {
            switchSort.isChecked = false
            viewModel.methodOfSort = NetworkUtils.POPULARITY
            viewModel.deleteMovies()
            viewModel.loadData()
        }

        switchSort.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) onCheckPopularity()
            else onCheckTopRated()
        }

        textViewPopularity.setOnClickListener {
            if (switchSort.isChecked)
                onCheckPopularity()
        }

        textViewTopRated.setOnClickListener {
            if (!switchSort.isChecked)
                onCheckTopRated()
        }

        adapter.onPosterClickListener = object : MovieAdapter.OnPosterClickListener {
            override fun onPosterClick(position: Int) {
                val id = adapter.movies[position].id
                val intent = DetailActivity.getIntent(this@MainActivity, id, false)
                startActivity(intent)
                Toast.makeText(this@MainActivity, adapter.movies[position].title, Toast.LENGTH_SHORT).show()

            }

        }

        adapter.onReachEndListener = object : MovieAdapter.OnReachEndListener {
            override fun onReachEnd() {
                Toast.makeText(this@MainActivity, "конец списка", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItemFavourite -> {
                val intent = Intent(this, FavouriteActivity::class.java)
                startActivity(intent)
            }


        }
        return super.onOptionsItemSelected(item)
    }
}
