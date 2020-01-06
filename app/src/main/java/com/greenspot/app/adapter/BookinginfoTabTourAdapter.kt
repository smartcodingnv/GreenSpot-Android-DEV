package com.greenspot.app.adapter


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.greenspot.app.fragment.*

class BookinginfoTabTourAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    internal var totalTabs: Int
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return BookingTourDetailsFragment().newInstance(1)  //  tour detailss
            }
            1 -> {
                return BookingAllpersonInfo().newInstance(1)
            }
            2 -> {
                // val movieFragment = MovieFragment()
                return BookTourIteneraryFragment().newInstance()

            }
            3 -> {
                // val movieFragment = MovieFragment()
                return PlaceAnemitiesFragment().newInstance(4) // booking aminetes

            }
            4 -> {
                return PlaceOthersFragment().newInstance(4)   //booking contact
                // val movieFragment = MovieFragment()
               //booking tour
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