package com.example.movies

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.movies.adapters.PagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.plugins.RxJavaPlugins
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.net.SocketException

class MainActivity : AppCompatActivity() {

    private lateinit var viewPagerAdapter: PagerAdapter
    private lateinit var viewModel: PopularityViewModel

    companion object {
        // Added here to not confuse with usages of this variable in onPageSelected()
        private var selectedTabPosition = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxJavaPlugins.setErrorHandler{
            if ((it is IOException) || (it is SocketException)) {
                Log.e("TAG", "ONERRORNEW!")
                return@setErrorHandler
            }
        }


        setupViewPager()
    }

    private fun setupViewPager() {
        viewPagerAdapter = PagerAdapter(this)
        VPMain.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = PagerAdapter.TRANSACTION_SCREEN_OFFSCREEN_LIMIT as Int
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    selectedTabPosition = position
                    //populateUI(transaction)
                }
            })
            currentItem = selectedTabPosition
        }


        TabLayoutMediator(tabLayout, VPMain) { currentTab, currentPosition ->
            currentTab.text = when (currentPosition) {
                PagerAdapter.POPULARITY_SCREEN_POSITION -> getString(R.string.most_popular)
                PagerAdapter.TOP_RATED_SCREEN_POSITION -> getString(R.string.top_rated)
                else -> getString(R.string.chucker_response)
            }
        }.attach()
    }


    //    private fun initViewModel() {
//        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//        viewModel.getMovieList().observe(this, Observer {
//            adapter.movies = it
//        })
//    }
//
//    private fun initRecyclerView() {
//        recyclerViewPosters.layoutManager = GridLayoutManager(this, 2)
//        recyclerViewPosters.adapter = adapter
//    }
//
//    private fun initListeners() {
//
//        fun onCheckTopRated() {
//            switchSort.isChecked = true
//            viewModel.methodOfSort = NetworkUtils.TOP_RATED
//            viewModel.deleteMovies()
//            viewModel.loadData()
//        }
//
//        fun onCheckPopularity() {
//            switchSort.isChecked = false
//            viewModel.methodOfSort = NetworkUtils.POPULARITY
//            viewModel.deleteMovies()
//            viewModel.loadData()
//        }
//
//        switchSort.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (!isChecked) onCheckPopularity()
//            else onCheckTopRated()
//        }
//
//        textViewPopularity.setOnClickListener {
//            if (switchSort.isChecked)
//                onCheckPopularity()
//        }
//
//        textViewTopRated.setOnClickListener {
//            if (!switchSort.isChecked)
//                onCheckTopRated()
//        }
//
//        adapter.onPosterClickListener = object : MovieAdapter.OnPosterClickListener {
//            override fun onPosterClick(position: Int) {
//                val id = adapter.movies[position].id
//                val intent = DetailActivity.getIntent(this@MainActivity, id, false)
//                startActivity(intent)
//                Toast.makeText(this@MainActivity, adapter.movies[position].title, Toast.LENGTH_SHORT).show()
//
//            }
//
//        }
//
//        adapter.onReachEndListener = object : MovieAdapter.OnReachEndListener {
//            override fun onReachEnd() {
//                Toast.makeText(this@MainActivity, "конец списка", Toast.LENGTH_SHORT).show()
//            }
//
//        }
//    }
//
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
