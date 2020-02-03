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
import com.greenspot.app.adapter.PlaceTabAdapter
import com.greenspot.app.responce.recreationdetails.*
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.activity_place_tab.*


class PlaceTabActivity : AppCompatActivity() {

    private var checkTab: Int = 0
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    private var progress: Progress? = null
    private var viewDialog: ViewDialog? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null
    private var currncyCode: String? = ""
    private var countryID: String? = ""
    private var langCode: String? = ""

    private var amenitiesRecordsMy: ArrayList<AmenitiesRecordsItem>? = ArrayList()
    private var otherRecordsMy: java.util.ArrayList<OtherItem>? = java.util.ArrayList()
    private var stayRecordsMy: java.util.ArrayList<StayItem>? = java.util.ArrayList()
    private var paymnetRecordsMy: java.util.ArrayList<PaymentRecordsItem>? = java.util.ArrayList()
    private var paymnetotherRecordsMy: java.util.ArrayList<OtherRecordsItem>? = java.util.ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_tab)

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


        val gson = Gson()
        val respncedetails = gson.fromJson(
            helper!!.LoadStringPref(AppConfig.PREFERENCE.PLACEDETAILSRESPONCE, ""),
            ResponceRecDetails::class.java
        )

        txt_placename.text = respncedetails.data.mainRecords.placeName

        tabLayout = findViewById<TabLayout>(com.greenspot.app.R.id.tabLayout)
        viewPager = findViewById<ViewPager>(com.greenspot.app.R.id.viewPager)

        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })


        for (aminate in respncedetails.data.amenities.amenitiesRecords!!) {
            val amenitiessubRecordsMy: ArrayList<RecordsItem>? =
                ArrayList()
            for (temp1 in aminate.records!!) {
                if (temp1.value != "") {
                    amenitiessubRecordsMy!!.add(temp1)
                }
            }
            if (amenitiessubRecordsMy!!.size > 0) {
                val temp2 = aminate
                temp2.records = amenitiessubRecordsMy
                amenitiesRecordsMy!!.add(temp2)
            }
        }

        for (otherdata in respncedetails.data.other!!) {
            if (otherdata.value != "") {
                otherRecordsMy!!.add(otherdata)
            }

        }

        for (stay in respncedetails.data.stay!!) {
            val staysubRecordsMy: java.util.ArrayList<RecordsItem>? =
                java.util.ArrayList()
            for (temp1 in stay.records!!) {
                if (temp1.value != "") {
                    staysubRecordsMy!!.add(temp1)
                }
            }
            if (staysubRecordsMy!!.size > 0) {
                val temp2 = stay
                temp2.records = staysubRecordsMy
                stayRecordsMy!!.add(temp2)
            }
        }

        for (payment in respncedetails.data.payments.paymentRecords!!) {
            val paymnetsubRecordsMy: java.util.ArrayList<RecordsItem>? =
                java.util.ArrayList()
            for (temp1 in payment.records!!) {
                if (temp1.value != "") {
                    paymnetsubRecordsMy!!.add(temp1)
                }
            }
            if (paymnetsubRecordsMy!!.size > 0) {
                val temp2 = payment
                temp2.records = paymnetsubRecordsMy
                paymnetRecordsMy!!.add(temp2)
            }
        }

        for (otherdata in respncedetails.data.payments.otherRecords!!) {
            if (otherdata.value != "") {
                paymnetotherRecordsMy!!.add(otherdata)
            }

        }


        tablayout()


    }


    private fun tablayout() {

        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.tab_description)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.tab_availability)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.tab_amenities)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.tab_stay)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.tab_others)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.tab_paymnet)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.tab_write_a_review)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.tab_contact_us)))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        Log.e("stay", " " + stayRecordsMy!!.size)
        Log.e("paymnet", " " + stayRecordsMy!!.size)

        if (amenitiesRecordsMy!!.size == 0) {

            (tabLayout!!.getTabAt(2)!!.view as ViewGroup).visibility = View.GONE

        }
        if (stayRecordsMy!!.size == 0) {

//            (tabLayout!!.getTabAt(3)!!.view as ViewGroup).visibility = View.GONE
//            (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(3).visibility = View.GONE
            (tabLayout!!.getTabAt(3)!!.view as ViewGroup).visibility = View.GONE
        }
        if (paymnetRecordsMy!!.size == 0 && paymnetotherRecordsMy!!.size == 0) {

//            (tabLayout!!.getTabAt(4)!!.view as ViewGroup).visibility = View.GONE
//            (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(4).visibility = View.GONE
            (tabLayout!!.getTabAt(4)!!.view as ViewGroup).visibility = View.GONE
        }
        if (otherRecordsMy!!.size == 0) {


//            (tabLayout!!.getTabAt(5)!!.view as ViewGroup).visibility = View.GONE
//            (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(5).visibility = View.GONE
            (tabLayout!!.getTabAt(5)!!.view as ViewGroup).visibility = View.GONE
        }


//        (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(4).visibility = View.GONE
//        //Set the next  tab as selected tab


        val adapter = PlaceTabAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

//        viewPager!!.setOnTouchListener(View.OnTouchListener { v, event -> true })

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
