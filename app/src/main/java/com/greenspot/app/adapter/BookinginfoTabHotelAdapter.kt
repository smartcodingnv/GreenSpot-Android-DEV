package com.greenspot.app.adapter


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.greenspot.app.fragment.*

class BookinginfoTabHotelAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    internal var totalTabs: Int
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return PlaceDescriptionFragment().newInstance(5)  //hotelbookinginfo
            }
            1 -> {
                // val movieFragment = MovieFragment()
                return HotelBookRoomFragment().newInstance() // booking aminetes event
            }
            2 -> {
                // val movieFragment = MovieFragment()
                return PlaceAnemitiesFragment().newInstance(8)// booking stay
            }
            3 -> {
                // val movieFragment = MovieFragment()
                return TourOthersFragment().newInstance(3)
            }
            4 -> {
                // val movieFragment = MovieFragment()
                return PlaceOthersFragment().newInstance(7)
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