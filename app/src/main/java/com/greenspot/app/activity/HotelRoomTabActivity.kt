package com.greenspot.app.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.BookHotelRoomTabAdapter
import com.greenspot.app.adapter.HotelRoomTabAdapter
import com.greenspot.app.adapter.HotelTabAdapter
import com.greenspot.app.responce.bookinginfohotel.ResponceBookinginfoHotel
import com.greenspot.app.responce.hoteldetails.ResponceHotelDetails
import com.greenspot.app.responce.hoteldetails.RoomImagesItem
import com.greenspot.app.utils.*

import kotlinx.android.synthetic.main.activity_hotel_room_tab.*
import kotlinx.android.synthetic.main.activity_hotel_room_tab.txt_hotelname
import java.util.ArrayList

class HotelRoomTabActivity : AppCompatActivity() {

    private var roomTabflag: Int =0
    private var roomId: String? =""
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

    private var roomImageItem: ArrayList<RoomImagesItem>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_room_tab)

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
        roomTabflag = intenttt.getIntExtra(AppConfig.EXTRA.ROOMTABFLAG, 0)

        roomId = helper!!.LoadStringPref(AppConfig.PREFERENCE.ROOMMASTERID,"")

        tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        viewPager = findViewById<ViewPager>(R.id.viewPager)

        if(roomTabflag==1){

            val gson = Gson()
            val hoteldetails = gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.HOTELDETAILSRESPONCE, ""),
                ResponceHotelDetails::class.java
            )
            for (room in hoteldetails.data.rooms!!){

                if(room.masterId.equals(roomId)){
                    txt_hotelname.text = room.roomType

                }
            }

            tablayout()

        }else if(roomTabflag==2){

            val gson = Gson()
            val hoteldetails =
                gson.fromJson(
                    helper!!.LoadStringPref(AppConfig.PREFERENCE.HOTELBOOKINGINFO, ""),
                    ResponceBookinginfoHotel::class.java
                )

            for (room in hoteldetails.data.roomInfo!!){

                if(room.masterId.equals(roomId)){
                    txt_hotelname.text = room.roomType
                }
            }

            bookroomtablayout()
        }



        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

    }

    private fun tablayout() {


        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.str_images)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.tab_amenities)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.str_options)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.str_include)))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL


     /*   if (amenitiesRecordsMy!!.size == 0) {

//            (tabLayout!!.getTabAt(3)!!.view as ViewGroup).visibility = View.GONE
            (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(2).visibility = View.GONE

        }*/

//        val tab = tabLayout!!.getTabAt(0)
//        tab!!.select()

//        (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(2).visibility = View.GONE
//        //Set the next  tab as selected tab
//        val tab = tabLayout!!.getTabAt(0)
//        tab!!.select()


        val adapter = HotelRoomTabAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
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

    private fun bookroomtablayout() {



        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.tab_amenities)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.str_options)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.str_include)))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL


     /*   if (amenitiesRecordsMy!!.size == 0) {

//            (tabLayout!!.getTabAt(3)!!.view as ViewGroup).visibility = View.GONE
            (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(2).visibility = View.GONE

        }*/

//        val tab = tabLayout!!.getTabAt(0)
//        tab!!.select()

//        (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(2).visibility = View.GONE
//        //Set the next  tab as selected tab
//        val tab = tabLayout!!.getTabAt(0)
//        tab!!.select()


        val adapter = BookHotelRoomTabAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
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
