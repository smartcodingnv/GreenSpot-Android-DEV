package com.greenspot.app.adapter


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.greenspot.app.fragment.*

class TourTabAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    internal var totalTabs: Int
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return PlaceDescriptionFragment().newInstance(2)
            }
            1 -> {
                return TourIteneraryFragment().newInstance()  // tour details
            }
            2 -> {
                // val movieFragment = MovieFragment()
                return PlaceAnemitiesFragment().newInstance(2)
            }
            3 -> {
                // val movieFragment = MovieFragment()
                return TourOthersFragment().newInstance(1)
            }
            4 -> {
                // val movieFragment = MovieFragment()
                return TourReviewFragment().newInstance()
            }
            5 -> {
                // val movieFragment = MovieFragment()
                return PlaceOthersFragment().newInstance(2)
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