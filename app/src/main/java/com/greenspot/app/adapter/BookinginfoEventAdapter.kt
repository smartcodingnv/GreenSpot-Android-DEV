package com.greenspot.app.adapter


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.greenspot.app.fragment.*

class BookinginfoEventAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    internal var totalTabs: Int
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return BookingTourDetailsFragment().newInstance(2)  //eventdetails
            }
            1 -> {
                // val movieFragment = MovieFragment()
                return PlaceAnemitiesFragment().newInstance(5) // booking aminetes event
            }
            2 -> {
                // val movieFragment = MovieFragment()
                return PlaceOthersFragment().newInstance(5)   //booking contact
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