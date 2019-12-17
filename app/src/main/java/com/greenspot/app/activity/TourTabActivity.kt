package com.greenspot.app.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.TourTabAdapter
import com.greenspot.app.responce.tourdetail.AmenitiesItem
import com.greenspot.app.responce.tourdetail.ResponceTourDetails
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.activity_tour_tab.*
import kotlinx.android.synthetic.main.activity_tour_tab.ib_back
import java.util.*

class TourTabActivity : AppCompatActivity() {

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    private var checkTab: Int = 0

    private var amenitiesRecordsMy: ArrayList<AmenitiesItem>? = ArrayList()
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
        setContentView(R.layout.activity_tour_tab)


        progress = Progress(this)
        viewDialog = ViewDialog(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)


        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")

        val intenttt = getIntent()

        checkTab = intenttt.getIntExtra(AppConfig.EXTRA.TABCHECK, 0)




        tabLayout = findViewById<TabLayout>(com.greenspot.app.R.id.tabLayout)
        viewPager = findViewById<ViewPager>(com.greenspot.app.R.id.viewPager)

        val gson = Gson()
        val tourdetails =
            gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.TOURDETAILSRESPONCE, ""),
                ResponceTourDetails::class.java
            )


        txt_tourname.text = tourdetails.data.mainRecords.packageName


        amenitiesRecordsMy!!.clear()

        for (aminate in tourdetails.data.amenities!!) {
            if (aminate.value != "") {
                amenitiesRecordsMy!!.add(aminate)
            }

        }

        tablayout()

        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
    }

    private fun tablayout() {


        tabLayout!!.addTab(tabLayout!!.newTab().setText("OVERVIEW"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("ITINERARY"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("AMENITIES"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("OTHERS"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("WRITE A REVIEW"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("CONTACT US"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL


        if (amenitiesRecordsMy!!.size == 0) {

//            (tabLayout!!.getTabAt(3)!!.view as ViewGroup).visibility = View.GONE
            (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(1).visibility = View.GONE

        }

//        val tab = tabLayout!!.getTabAt(0)
//        tab!!.select()

//        (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(2).visibility = View.GONE
//        //Set the next  tab as selected tab
//        val tab = tabLayout!!.getTabAt(0)
//        tab!!.select()


        val adapter = TourTabAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter


//        viewPager!!.currentItem = tab.position


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
