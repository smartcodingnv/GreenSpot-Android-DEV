package com.greenspot.app.adapter


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.greenspot.app.fragment.*

class EventTabAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    internal var totalTabs: Int
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return PlaceDescriptionFragment().newInstance(3)
            }
            1 -> {
                return PlaceAnemitiesFragment().newInstance(3)
            }
            2 -> {
                // val movieFragment = MovieFragment()
                return PlaceOthersFragment().newInstance(3)
            }
            3 -> {
                // val movieFragment = MovieFragment()
                return EventReviewFragment().newInstance()

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