package com.greenspot.app.adapter


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.greenspot.app.fragment.*

class PlaceTabAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    internal var totalTabs: Int
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return PlaceDescriptionFragment().newInstance(1)
            }
            1 -> {
                return PlaceAvailabalityFragment().newInstance()
            }
            2 -> {

                return PlaceAnemitiesFragment().newInstance(1)
            }
            3 -> {
                // val movieFragment = MovieFragment()
                return PlaceStayFragment().newInstance(1)
            }
            4 -> {
                // val movieFragment = MovieFragment()
                return PlaceOthersFragment().newInstance(1)

            }
            5 -> {
                // val movieFragment = MovieFragment()
                return PaymentFragment().newInstance()
            }
            6 -> {
                // val movieFragment = MovieFragment()
                return PlaceReviewFragment().newInstance()
            }
            7 -> {
                // val movieFragment = MovieFragment()
                return PlaceContactusFragmenet().newInstance(1)
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