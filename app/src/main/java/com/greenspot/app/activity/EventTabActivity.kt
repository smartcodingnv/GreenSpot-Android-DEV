package com.greenspot.app.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.EventTabAdapter
import com.greenspot.app.responce.eventdetails.AmenitiesItem
import com.greenspot.app.responce.eventdetails.EvnetDetailsResponce
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.activity_event_tab.*
import java.util.*

class EventTabActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_event_tab)

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


        tabLayout = findViewById<TabLayout>(com.greenspot.app.R.id.tabLayoutevent)
        viewPager = findViewById<ViewPager>(com.greenspot.app.R.id.viewPagerevent)

        val gson = Gson()
        val eventdetails =
            gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.EVENTDETAILSRESPONCE, ""),
                EvnetDetailsResponce::class.java
            )

        txt_eventname.text = eventdetails.data.mainRecords.title


        amenitiesRecordsMy!!.clear()

        for (aminate in eventdetails.data.amenities!!) {
            if (aminate.value != "") {
                amenitiesRecordsMy!!.add(aminate)
            }

        }

        tablayout()



    }

    private fun tablayout() {

        tabLayout!!.addTab(tabLayout!!.newTab().setText("OVERVIEW"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("AMENITIES"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("CONTACT US"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("WRITE A REVIEW"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        if (amenitiesRecordsMy!!.size == 0) {

//            (tabLayout!!.getTabAt(3)!!.view as ViewGroup).visibility = View.GONE
            (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(1).visibility = View.GONE

        }

//        val tab = tabLayout!!.getTabAt(0)
//        tab!!.select()

        val adapter = EventTabAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
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
