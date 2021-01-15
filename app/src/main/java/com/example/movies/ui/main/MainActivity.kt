package com.example.movies.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.movies.R
import com.example.movies.ui.favourite.FavouriteActivity
import com.example.movies.ui.search.SearchActivity
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.plugins.RxJavaPlugins
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.net.SocketException

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewPagerAdapter: PagerAdapter

    companion object {
        // Added here to not confuse with usages of this variable in onPageSelected()
        private var selectedTabPosition = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxJavaPlugins.setErrorHandler {
            if ((it is IOException) || (it is SocketException)) {
                Log.e("RX_ERROR", "ONERRORNEW!")
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItemFavourite -> {
                val intent = Intent(this, FavouriteActivity::class.java)
                startActivity(intent)
            }

            R.id.menuItemSearch -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }


        }
        return super.onOptionsItemSelected(item)
    }
}
