package com.greenspot.app.adapter


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.greenspot.app.fragment.*

class HotelRoomTabAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    internal var totalTabs: Int
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return HotelRoomImagesFragment().newInstance()
            }
            1 -> {
                // val movieFragment = MovieFragment()
                return HotelRoomAminites().newInstance(1)
            }
            2 -> {
                // val movieFragment = MovieFragment()
                return HotelRoomAminites().newInstance(2)
            }
            3 -> {
                // val movieFragment = MovieFragment()
                return HotelRoomAminites().newInstance(3)
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