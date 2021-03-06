package com.greenspot.app.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.BookinginfoEventAdapter
import com.greenspot.app.adapter.BookinginfoTabHotelAdapter
import com.greenspot.app.adapter.BookinginfoTabTourAdapter
import com.greenspot.app.adapter.BookinginfoTabVacationAdapter
import com.greenspot.app.responce.bookinginfo.ResponceBookinginfo
import com.greenspot.app.responce.bookinginfo.TourAmenitiesItem
import com.greenspot.app.responce.bookinginfohotel.AmenitiesItem
import com.greenspot.app.responce.bookinginfohotel.ResponceBookinginfoHotel
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.activity_booking_info_tab.*
import java.util.ArrayList

class BookingInfoTabActivity : AppCompatActivity() {

    private var amenitiesRecordsMy: ArrayList<TourAmenitiesItem>? = ArrayList()
    private var hotelamenitiesRecordsMy: ArrayList<AmenitiesItem>? = ArrayList()

    private var bookingType: String =""
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    private var checkTab: Int = 0

    private var progress: Progress? = null
    private var viewDialog: ViewDialog? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null
    private var currncyCode: String? = ""
    private var countryID: String? = ""
    private var langCode: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_info_tab)

        progress = Progress(this)
        viewDialog = ViewDialog(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)

        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")

        tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        viewPager = findViewById<ViewPager>(R.id.viewPager)


        val intent = getIntent();
        checkTab = intent.getIntExtra(AppConfig.EXTRA.TABCHECK, 0)
        bookingType = intent.getStringExtra(AppConfig.EXTRA.BOOKINGTYPE)

        txt_title.text = intent.getStringExtra(AppConfig.EXTRA.TOURTITLE)

        Log.e("bookinginfo"," "+bookingType)

//        val gson = Gson()
//        val tourBookingifo =
//            gson.fromJson(helper!!.LoadStringPref(AppConfig.PREFERENCE.TOURDETAILSRESPONCE, ""), ResponceBookinginfo::class.java)


        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        if(bookingType.equals("Tour")){

            tourtablayout()

        }else if(bookingType.equals("Event")){

            eventTablayout()

        }else if(bookingType.equals("Recreation")){

            vacationTablayout()

        }else if(bookingType.equals("Hotel")){

            hotelTablayout()
        }


    }

    private fun tourtablayout() {

        val gson = Gson()
        val bookinginfo = gson.fromJson(
            helper!!.LoadStringPref(AppConfig.PREFERENCE.BOOKINGINFO, ""),
            ResponceBookinginfo::class.java
        )


        amenitiesRecordsMy!!.clear()

        for (aminate in bookinginfo.data.tourAmenities!!) {
            if (aminate.value != "") {
                amenitiesRecordsMy!!.add(aminate)
            }

        }


        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.res_tour_details)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.tab_allperson)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.res_itinerary)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.res_included_facilities)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.tab_contact_us)))

        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        if (amenitiesRecordsMy!!.size == 0) {

//            (tabLayout!!.getTabAt(3)!!.view as ViewGroup).visibility = View.GONE
            (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(3).visibility = View.GONE

        }

        val adapter = BookinginfoTabTourAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

        viewPager!!.setOnTouchListener(View.OnTouchListener { v, event -> true })
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        tabLayout!!.getTabAt(checkTab)!!.select()
    }

    private fun eventTablayout() {

        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.res_event_details)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.res_included_facilities)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.tab_contact_us)))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL


        val adapter = BookinginfoEventAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

        viewPager!!.setOnTouchListener(View.OnTouchListener { v, event -> true })
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        tabLayout!!.getTabAt(checkTab)!!.select()
    }


    private fun vacationTablayout() {

        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.res_vacation_details)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.res_included_facilities)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.tab_stay)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.tab_contact_us)))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL


        val adapter = BookinginfoTabVacationAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

        viewPager!!.setOnTouchListener(View.OnTouchListener { v, event -> true })
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        tabLayout!!.getTabAt(checkTab)!!.select()
    }

    private fun hotelTablayout() {

//        val gson = Gson()
//        val bookinginfo = gson.fromJson(
//            helper!!.LoadStringPref(AppConfig.PREFERENCE.HOTELBOOKINGINFO, ""),
//            ResponceBookinginfoHotel::class.java
//        )

//        hotelamenitiesRecordsMy!!.clear()
//        for (aminate in bookinginfo.data.amenities!!) {
//            if (aminate.value != "") {
//                hotelamenitiesRecordsMy!!.add(aminate)
//            }
//
//        }

        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.res_hoteldetails)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.res_rooms)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.res_included_facilities)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.res_policies)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.tab_contact_us)))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL


        val adapter = BookinginfoTabHotelAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

        viewPager!!.setOnTouchListener(View.OnTouchListener { v, event -> true })
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        tabLayout!!.getTabAt(checkTab)!!.select()
    }


}
