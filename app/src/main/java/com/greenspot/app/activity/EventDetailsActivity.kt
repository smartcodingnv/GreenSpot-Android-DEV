package com.greenspot.app.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.greenspot.app.MyApp
import com.greenspot.app.R
import com.greenspot.app.adapter.EventPopularAdapter
import com.greenspot.app.adapter.SliderEventAdapter
import com.greenspot.app.network.ApiClient
import com.greenspot.app.network.ApiInterface
import com.greenspot.app.responce.eventdetails.AmenitiesItem
import com.greenspot.app.responce.eventdetails.EvnetDetailsResponce
import com.greenspot.app.responce.eventdetails.GalleryImagesItem
import com.greenspot.app.responce.eventdetails.PopularEventsItem
import com.greenspot.app.utils.*
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView
import hk.ids.gws.android.sclick.SClick
import it.sephiroth.android.library.imagezoom.ImageViewTouch
import kotlinx.android.synthetic.main.activity_event_details.*
import kotlinx.android.synthetic.main.content_event_details.*
import kotlinx.android.synthetic.main.content_event_details.lay_next
import kotlinx.android.synthetic.main.content_event_details.lay_prev
import kotlinx.android.synthetic.main.content_event_details.txt_viewonmap
import kotlinx.android.synthetic.main.content_hotel_details.*
import kotlinx.android.synthetic.main.content_tour_details.*
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.layoutInflater
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EventDetailsActivity : AppCompatActivity() {

    val TAG = "EventDetailsActivity"
    private var eventDetailsResponce: String = ""

    private var gallaryImageList: ArrayList<GalleryImagesItem> = ArrayList()
    private var eventID: String = ""
    private var evnetTitle: String = ""

    private var progress: Progress? = null
    private var viewDialog: ViewDialog? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null
    private var currncyCode: String? = ""
    private var countryID: String? = ""
    private var langCode: String? = ""

    private var latitude: String? = ""
    private var longitude: String? = ""

    var count: Int = 0

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    private var imgURl: String? = ""
    private var imgURllogo: String? = ""


    private var popularEvent: ArrayList<PopularEventsItem> = ArrayList()


    private var amenitiesRecordsMy: ArrayList<AmenitiesItem>? = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)


        tabLayout = findViewById<TabLayout>(R.id.tabLayoutevent)
        viewPager = findViewById<ViewPager>(R.id.viewPagerevent)

        val intenttt = intent

        eventID = intenttt.getStringExtra(AppConfig.EXTRA.EVENTID)
        evnetTitle = intenttt.getStringExtra(AppConfig.EXTRA.EVENTNAME)

        txt_title.setText(evnetTitle)

        progress = Progress(this)
        viewDialog = ViewDialog(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)


        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")


        initviews()





        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        txt_viewonmap.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            val uri =
                "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);

        })

        img_place.setOnClickListener(View.OnClickListener {
            if (imgURl!!.isEmpty()) {
                return@OnClickListener
            }
            showLargeImage(context = this, url = imgURl, type = "I")


        })

        img_orglogo.setOnClickListener(View.OnClickListener {

            if (imgURllogo!!.isEmpty()) {
                return@OnClickListener
            }

            showLargeImage(context = this, url = imgURllogo, type = "I")
        })

           btn_booknow.setOnClickListener(View.OnClickListener {
               if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
               startActivity(Intent(this, EventBookingActivity::class.java))

           })




        lay_prev.setOnClickListener(View.OnClickListener {
            if (count == 0) {
                return@OnClickListener
            }
            count--

            imgURl = gallaryImageList[count].imageName

            Glide.with(this)
                .load(imgURl)
                .placeholder(R.drawable.travel)
                .centerCrop()
                .into(img_place)
            rv_placeimg.scrollToPosition(count)
        })

        lay_next.setOnClickListener(View.OnClickListener {
            val position = gallaryImageList.size - 1
            if (position == count) {
                return@OnClickListener
            }
            count++
            imgURl = gallaryImageList[count].imageName
            Glide.with(this)
                .load(imgURl)
                .placeholder(R.drawable.travel)
                .centerCrop()
                .into(img_place)
            rv_placeimg.scrollToPosition(count)
        })

        rv_placeimg.affectOnItemClicks() { position, view ->

            count = position
            imgURl = gallaryImageList[position].imageName
            Glide.with(this)
                .load(imgURl)
                .placeholder(R.drawable.travel)
                .centerCrop()
                .into(img_place)
        }

    }

    /*private fun tablayout() {

        tabLayout!!.addTab(tabLayout!!.newTab().setText("OVERVIEW"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("AMENITIES"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("CONTACT US"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("WRITE A REVIEW"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        if (amenitiesRecordsMy!!.size == 0) {

//            (tabLayout!!.getTabAt(3)!!.view as ViewGroup).visibility = View.GONE
            (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(1).visibility = View.GONE

        }

        val tab = tabLayout!!.getTabAt(0)
        tab!!.select()

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
    }*/

    /*private fun tablayout() {

        tabLayout!!.addTab(tabLayout!!.newTab().setText("OVERVIEW"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("AMENITIES"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("CONTACT US"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("WRITE A REVIEW"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        if (amenitiesRecordsMy!!.size == 0) {

//            (tabLayout!!.getTabAt(3)!!.view as ViewGroup).visibility = View.GONE
            (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(1).visibility = View.GONE

        }

        val tab = tabLayout!!.getTabAt(0)
        tab!!.select()

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
    }*/

    private fun tablayout() {


        if (amenitiesRecordsMy!!.size == 0) {

            txt_eamenites.visibility = View.GONE
            view_eamenites.visibility = View.GONE

        }

        txt_eoverview.setOnClickListener {
            onclickOverview(it)
        }

        txt_eamenites.setOnClickListener {
            onclickAmenites(it)
        }

        txt_econtactus.setOnClickListener {
            onclickContactus(it)
        }

        txt_ereview.setOnClickListener {
            onclickReivew(it)
        }

    }

    private fun onclickReivew(it: View?) {

        if (!SClick.check(SClick.BUTTON_CLICK)) return
        startActivity(
            Intent(this, EventTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 2)

        )

    }

    private fun onclickContactus(it: View?) {
        if (!SClick.check(SClick.BUTTON_CLICK)) return
        startActivity(
            Intent(this, EventTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 3)

        )

    }

    private fun onclickAmenites(it: View?) {
        if (!SClick.check(SClick.BUTTON_CLICK)) return
        startActivity(
            Intent(this, EventTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 1)

        )

    }

    private fun onclickOverview(it: View?) {

        if (!SClick.check(SClick.BUTTON_CLICK)) return
        startActivity(
            Intent(this, EventTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 0)

        )
    }

    private fun initviews() {
        getEventData(
            contryID = countryID.toString(),
            selectCurrency = currncyCode.toString(),
            langCode = langCode.toString()
        )


    }

    private fun setVisitorplace() {

        val sliderEventAdapter = SliderEventAdapter(this)
        Common.setHorizontalRecyclerView(this, rv_placeimg)
        sliderEventAdapter.swapData(gallaryImageList)
        rv_placeimg.adapter = sliderEventAdapter
    }

    private fun setPopularevent() {

        val eventPopularAdapter = EventPopularAdapter(this)
        Common.setHorizontalRecyclerView(this, rv_popuplarevent)
        eventPopularAdapter.swapData(popularEvent)
        rv_popuplarevent.adapter = eventPopularAdapter
    }


    private fun getEventData(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
        viewDialog!!.showDialog()
        utils!!.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val eventdetResponce = apiService?.EVENT_DETAILS(
            contryID = contryID,
            selectCurrency = selectCurrency,
            langcode = langCode,
            eventid = eventID

        )

        Log.e("contry", contryID)
        Log.e("currency", selectCurrency)
        Log.e("langcode", langCode)
        Log.e("placeid", eventID)

        eventdetResponce?.enqueue(object : Callback<EvnetDetailsResponce> {
            override fun onResponse(@NonNull call: Call<EvnetDetailsResponce>, @NonNull response: Response<EvnetDetailsResponce>) {
                viewDialog!!.hideDialog()

                val eventDataResponce = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (eventDataResponce!!.status == 1) {


                        gallaryImageList = eventDataResponce.data.galleryImages as ArrayList<GalleryImagesItem>

                        gallaryImageList.sortBy {
                           it.orderId
                        }

                        if(gallaryImageList.size==1){
                            lay_next.visibility = View.GONE
                            lay_prev.visibility = View.GONE

                        }else{
                            lay_next.visibility = View.VISIBLE
                            lay_prev.visibility = View.VISIBLE

                        }
                        imgURl = gallaryImageList.get(0).imageName
                        Glide.with(this@EventDetailsActivity)
                            .load(imgURl)
                            .placeholder(R.drawable.travel)
                            .centerCrop()
                            .into(img_place)

                        popularEvent =
                            eventDataResponce.data.popularEvents as ArrayList<PopularEventsItem>

                        if (popularEvent.size == 0) {
                            txt_popularevent.visibility = View.GONE
                        } else {
                            txt_popularevent.visibility = View.VISIBLE
                        }

                        setVisitorplace()
                        setPopularevent()

                        latitude = eventDataResponce.data.mainRecords.latitude
                        longitude = eventDataResponce.data.mainRecords.longitude

                        if (latitude!!.isEmpty() && longitude!!.isEmpty()) {

                            txt_viewonmap.visibility = View.GONE
                        } else {
                            txt_viewonmap.visibility = View.VISIBLE
                        }

                        txt_eventname.text = eventDataResponce.data.mainRecords.title

                        if (eventDataResponce.data.mainRecords.startDate.isEmpty()) {
                            txt_venuedate.text = getString(R.string.str_everyday)
                        } else {
                            txt_venuedate.text =
                                eventDataResponce.data.mainRecords.startDate + " To " + eventDataResponce.data.mainRecords.endDate
                        }
                        txt_venuetime.text =
                            eventDataResponce.data.mainRecords.startTime + " To " + eventDataResponce.data.mainRecords.endTime
                        txt_eventlocation.text = eventDataResponce.data.mainRecords.address
                        rt_event.rating = eventDataResponce.data.mainRecords.avgReviews.toFloat()

                        if (eventDataResponce.data.mainRecords.discount.isEmpty()) {
                            txt_offer.visibility = View.GONE
                        } else {
                            txt_offer.text = eventDataResponce.data.mainRecords.discount
                        }
                        if (eventDataResponce.data.mainRecords.basePrice.equals("0")) {
                            txt_baseprice.visibility = View.GONE
                            view_baseprice.visibility = View.GONE

                        } else {
                            view_baseprice.visibility = View.VISIBLE
                            txt_baseprice.text =
                                currncyCode + " " + eventDataResponce.data.mainRecords.basePrice
                        }

                        if (eventDataResponce.data.mainRecords.finalPrice.equals("0")) {
                            txt_price.visibility = View.GONE
                        } else {
                            txt_price.text =
                                currncyCode + " " + eventDataResponce.data.mainRecords.finalPrice
                        }

                        if (eventDataResponce.data.mainRecords.bookingRequiredId.equals("1")) {

                            txt_bookingna.text = getString(R.string.res_bookings)+": "+ getString(R.string.txt_bookingreq)

                        } else {
                            txt_bookingna.text = getString(R.string.res_bookings)+": "+getString(R.string.txt_bookingnotreq)
                        }



                        if (eventDataResponce.data.mainRecords.eventTypes.isEmpty()) {
                            txt_eventytype.visibility = View.GONE

                        } else {
                            txt_eventytype.text =
                                getString(R.string.str_eventtype) + eventDataResponce.data.mainRecords.eventTypes
                        }

                        if (eventDataResponce.data.mainRecords.suitableFor.isEmpty()) {
                            txt_suitablefor.visibility = View.GONE

                        } else {
                            txt_suitablefor.text =
                                getString(R.string.txt_suitablefor)+ " " + eventDataResponce.data.mainRecords.suitableFor
                        }


                        txt_companyname.text =
                            eventDataResponce.data.mainRecords.organizerCompanyName
                        txt_orgaddress.text = eventDataResponce.data.mainRecords.organizerAddress

                        imgURllogo = eventDataResponce.data.mainRecords.organizerLogo

                        Glide.with(this@EventDetailsActivity)
                            .load(imgURllogo)
                            .placeholder(R.drawable.travel)
                            .centerCrop()
                            .into(img_orglogo)

                        lay_booknowevent.visibility = View.VISIBLE
                        if (eventDataResponce.data.mainRecords.bookingOptionId.equals("1")) {
                            btn_booknow.visibility = View.VISIBLE
                            txt_booknow.visibility = View.GONE
//                            btn_booknow.text = getString(R.string.res_book_now)
//                            btn_booknow.background = ContextCompat.getDrawable(
//                                this@EventDetailsActivity,
//                                R.drawable.btn_fillgreen
//                            )


                        } else {
                            btn_booknow.visibility = View.GONE
                            txt_booknow.visibility = View.VISIBLE

                        }

                        if (eventDataResponce.data.mainRecords.paidEvent.equals("1")) {
                            txt_eventsperperson.visibility = View.VISIBLE
                            txt_eventstartprice.text = getString(R.string.res_startingfrom) +" "+  currncyCode + " " + eventDataResponce.data.mainRecords.finalPrice.toString()

                        } else {

                            txt_eventsperperson.visibility = View.GONE
                            txt_eventstartprice.text = getString(R.string.res_free)

                        }


                        val gson = Gson()
                        eventDetailsResponce = gson.toJson(eventDataResponce)

                        helper!!.initPref()
                        helper!!.SaveStringPref(
                            AppConfig.PREFERENCE.EVENTDETAILSRESPONCE,
                            eventDetailsResponce
                        )
                        helper?.SaveStringPref(
                            AppConfig.PREFERENCE.SERVICEPROVIDERID,
                            eventDataResponce.data.mainRecords.createdBy
                        )
                        helper!!.ApplyPref()



                        amenitiesRecordsMy!!.clear()

                        for (aminate in eventDataResponce.data.amenities!!) {
                            if (aminate.value != "") {
                                amenitiesRecordsMy!!.add(aminate)
                            }

                        }


                        tablayout()


                    } else {

//                        Toast.makeText(
//                            this@ListPlaceActivity,
//                            eventDataResponce.message,
//                            Toast.LENGTH_SHORT
//                        ).show()

                    }
                } else {
                    Toast.makeText(
                        this@EventDetailsActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<EvnetDetailsResponce>, @NonNull t: Throwable) {

                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)

                Toast.makeText(
                    this@EventDetailsActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    fun RecyclerView.affectOnItemClicks(
        onClick: ((position: Int, view: View) -> Unit)? = null,
        onLongClick: ((position: Int, view: View) -> Unit)? = null
    ) {
        this.addOnChildAttachStateChangeListener(
            RecyclerItemClickListener(
                this,
                onClick,
                onLongClick
            )
        )
    }

    fun showLargeImage(
        context: Context,
        url: String? = null,
        bitmap: Bitmap? = null,
        type: String
    ) {
        val dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(context.layoutInflater.inflate(R.layout.layout_large_video, null))

        val btnClose = dialog.findViewById<ImageButton>(R.id.btnIvClose)
        val ivPreview = dialog.findViewById<ImageViewTouch>(R.id.iv_preview_image)
        val youtubeView = dialog.findViewById<YouTubePlayerView>(R.id.youtubeView)
        var youTubePlayer: YouTubePlayer? = null


        if (bitmap != null) {
            youtubeView.visibility = View.VISIBLE
            ivPreview.imageBitmap = bitmap
        }

        if (type == "I" && bitmap == null) {
            youtubeView.visibility = View.GONE
            btnClose.visibility = View.VISIBLE
            ivPreview.visibility = View.VISIBLE
            Glide.with(context).load(url).into(ivPreview)
        } else if (type == "V") {
            ivPreview.visibility = View.GONE
            youtubeView.visibility = View.VISIBLE
            btnClose.visibility = View.VISIBLE
            youtubeView.initialize({
                it.addListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady() {
                        youTubePlayer = it
                        it.loadVideo(Common.getYoutubeId(url!!)!!, 0f)
                    }
                })
            }, true)
            youtubeView.enterFullScreen()
            youtubeView.addFullScreenListener(object : YouTubePlayerFullScreenListener {
                override fun onYouTubePlayerEnterFullScreen() {}

                override fun onYouTubePlayerExitFullScreen() {
                    dialog.dismiss()
                }
            })
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
            MyApp.updateLanguage(context)
        }

        dialog.setOnDismissListener {
            youTubePlayer?.pause()
            youTubePlayer = null
            MyApp.updateLanguage(context)
        }

        dialog.show()
    }


}
