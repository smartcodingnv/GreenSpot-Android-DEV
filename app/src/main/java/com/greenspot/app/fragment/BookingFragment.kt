package com.greenspot.app.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.greenspot.app.R
import com.greenspot.app.utils.PreferenceHelper
import com.greenspot.app.utils.Progress
import com.greenspot.app.utils.Utils


class BookingFragment : Fragment() {
    // TODO: Rename and change types of parameters

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    private lateinit var mView: View
    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun newInstance(): BookingFragment {

        val args = Bundle()

        val fragment = BookingFragment()
        fragment.setArguments(args)
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view

        tabLayout = mView.findViewById<TabLayout>(R.id.tab_booking)



//        viewPager.pagingEnabled = false

        Log.e("listbooking", "  onViewCreated")

        setUpcomingFragmnet(BookingUpcomingFagment().newInstance())
        tablayout()


    }

    // TODO: Rename method, update argument and hook method into UI event

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("listbooking", " onAttach")
    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun setUpcomingFragmnet(setUpcomingFagment: BookingUpcomingFagment) {
        val t = activity!!.supportFragmentManager.beginTransaction()
        t.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        t.replace(R.id.main_booking, setUpcomingFagment)
        t.commitAllowingStateLoss()
    }

    private fun setPastFragmnet(setPastFragment: BookingPastFragment) {
        val t = activity!!.supportFragmentManager.beginTransaction()
        t.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        t.replace(R.id.main_booking, setPastFragment)
        t.commitAllowingStateLoss()
    }


    private fun tablayout() {


        tabLayout!!.addTab(tabLayout!!.newTab().setText("UPCOMING"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("PAST"))

        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL


        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                if(tab.position==0){
                    setUpcomingFragmnet(BookingUpcomingFagment().newInstance())
                }else if(tab.position==1){
                    setPastFragmnet(BookingPastFragment().newInstance())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })


    }


}
