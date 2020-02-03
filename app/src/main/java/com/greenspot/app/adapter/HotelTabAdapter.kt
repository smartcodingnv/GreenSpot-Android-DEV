package com.greenspot.app.adapter


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.greenspot.app.fragment.*

class HotelTabAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    internal var totalTabs: Int
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return PlaceDescriptionFragment().newInstance(4)
            }
            1 -> {
                // val movieFragment = MovieFragment()
                return PlaceAnemitiesFragment().newInstance(7)
            }
            2 -> {
                // val movieFragment = MovieFragment()
                return TourOthersFragment().newInstance(2)
            }
            3 -> {
                // val movieFragment = MovieFragment()
                return HotelReviewFragment().newInstance()
            }
            4 -> {
                // val movieFragment = MovieFragment()
                return PlaceOthersFragment().newInstance(6)
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