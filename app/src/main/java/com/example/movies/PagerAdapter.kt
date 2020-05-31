package com.example.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.item_view_pager.view.*

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