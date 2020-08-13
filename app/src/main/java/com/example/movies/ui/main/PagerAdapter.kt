package com.example.movies.ui.main

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.movies.ui.main.popularity.PopularityFragment
import com.example.movies.ui.main.toprated.TopRatedFragment

class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    companion object {
        internal const val TOP_RATED_SCREEN_POSITION = 0
        internal const val POPULARITY_SCREEN_POSITION = 1

        internal const val TRANSACTION_SCREEN_OFFSCREEN_LIMIT = 1
        internal const val TRANSACTION_SCREENS_NUMBER = 2
    }

    override fun getItemCount(): Int {
        return TRANSACTION_SCREENS_NUMBER
    }

    override fun createFragment(position: Int): Fragment = when (position) {
        POPULARITY_SCREEN_POSITION -> PopularityFragment()
        TOP_RATED_SCREEN_POSITION -> TopRatedFragment()
        else -> throw IllegalStateException("Invalid adapter position")
    }


}