package com.greenspot.app.adapter


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.greenspot.app.fragment.*

class BookHotelRoomTabAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    internal var totalTabs: Int
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        when (position) {

            0 -> {
                // val movieFragment = MovieFragment()
                return BookHotelRoomAminites().newInstance(1)
            }
            1 -> {
                // val movieFragment = MovieFragment()
                return BookHotelRoomAminites().newInstance(2)
            }
            2 -> {
                // val movieFragment = MovieFragment()
                return BookHotelRoomAminites().newInstance(3)
            }


            else -> return Fragment()
        }
    }

    // this is for fragment tabs

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}