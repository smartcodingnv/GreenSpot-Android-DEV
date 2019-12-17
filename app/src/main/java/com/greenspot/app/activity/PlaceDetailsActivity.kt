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
import com.greenspot.app.R
import com.greenspot.app.adapter.SliderVisiterPlaceAdapter
import com.greenspot.app.adapter.TourSuggestedAdapter
import com.greenspot.app.adapter.VisiterPlaceSuggestedAdapter
import com.greenspot.app.model.GalleryImgList
import com.greenspot.app.network.ApiClient
import com.greenspot.app.network.ApiInterface
import com.greenspot.app.responce.recreationdetails.*
import com.greenspot.app.utils.*
import com.greenspot.app.utils.Common.Companion.getYoutubeId
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_place_details.*
import kotlinx.android.synthetic.main.activity_tour_list.ib_back
import kotlinx.android.synthetic.main.content_place_details.*
import org.jetbrains.anko.layoutInflater
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.MalformedURLException
import java.net.URL


class PlaceDetailsActivity : AppCompatActivity() {


    private var imgURl: String? = ""
    private var videoId: String? = ""
    private var videoLink: String? = ""
    private var suggestedTour: ArrayList<SuggestedToursItem> = ArrayList()
    private var nearByplace: ArrayList<NearbyPlacesItem> = ArrayList()

    private var placeDetailResponce: String = ""
    private var placeDescription: String = ""
    val TAG = "PlaceDetailsActivity"

    private var gallaryImageList: ArrayList<GalleryImagesItem> = ArrayList()
    private var gallaryVidoList: ArrayList<SocialLinksItem> = ArrayList()
    private var gallaryCombineList: ArrayList<GalleryImgList> = ArrayList()

    private var placeID: String = ""
    private var placeName: String = ""

    private var progress: Progress? = null
    private var viewDialog: ViewDialog? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null

    private var currncyCode: String? = ""
    private var countryID: String? = ""
    private var langCode: String? = ""

    var count: Int = 0

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    private var amenitiesRecordsMy: ArrayList<AmenitiesRecordsItem>? = ArrayList()
    private var otherRecordsMy: java.util.ArrayList<OtherItem>? = java.util.ArrayList()
    private var stayRecordsMy: java.util.ArrayList<StayItem>? = java.util.ArrayList()
    private var paymnetRecordsMy: java.util.ArrayList<PaymentRecordsItem>? = java.util.ArrayList()
    private var paymnetotherRecordsMy: java.util.ArrayList<OtherRecordsItem>? =
        java.util.ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.greenspot.app.R.layout.activity_place_details)


        val intenttt = getIntent()

        placeID = intenttt.getStringExtra(AppConfig.EXTRA.RECREATIONITEMID)
        placeName = intenttt.getStringExtra(AppConfig.EXTRA.RECREATIONITEMNAME)

        txt_placename.setText(placeName)

        progress = Progress(this)
        viewDialog = ViewDialog(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)


        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")

        tabLayout = findViewById<TabLayout>(com.greenspot.app.R.id.tabLayout)
        viewPager = findViewById<ViewPager>(com.greenspot.app.R.id.viewPager)

        initviews()




        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })


//        btn_booknow.setOnClickListener(View.OnClickListener {
//
//            startActivity(
//                Intent(this, PlaceBookingActivity::class.java)
//            )
//        })

        lay_prev.setOnClickListener(View.OnClickListener {
            if (count == 0) {
                return@OnClickListener
            }
            count--

            if (gallaryCombineList[count].flag == 1) {

                img_play.visibility = View.VISIBLE
                videoId = extractYoutubeId(gallaryCombineList[count].item_name);
                imgURl = "http://img.youtube.com/vi/" + videoId + "/0.jpg"
                videoLink = gallaryCombineList[count].item_name
                Glide.with(this@PlaceDetailsActivity)
                    .load(imgURl)
                    .placeholder(R.drawable.travel)
                    .centerCrop()
                    .into(img_place)
            } else {

                img_play.visibility = View.GONE
                Glide.with(this@PlaceDetailsActivity)
                    .load(gallaryCombineList[count].item_name)
                    .placeholder(R.drawable.travel)
                    .centerCrop()
                    .into(img_place)
            }

            /*      Glide.with(this)
                      .load(gallaryImageList[count].imageName)
                      .placeholder(R.drawable.travel)
                      .centerCrop()
                      .into(img_place)*/
        })


        img_play.setOnClickListener(View.OnClickListener {

            if (videoLink!!.isEmpty()) {

                return@OnClickListener
            }
            showLargeImageInDialog(context = this, url = videoLink, type = "V")

        })


        lay_next.setOnClickListener(View.OnClickListener {
            val position = gallaryImageList.size - 1
            if (position == count) {
                return@OnClickListener
            }
            count++

            if (gallaryCombineList[count].flag == 1) {

                img_play.visibility = View.VISIBLE
                videoId = extractYoutubeId(gallaryCombineList[count].item_name);
                imgURl = "http://img.youtube.com/vi/" + videoId + "/0.jpg"
                videoLink = gallaryCombineList[count].item_name
                Glide.with(this@PlaceDetailsActivity)
                    .load(imgURl)
                    .placeholder(R.drawable.travel)
                    .centerCrop()
                    .into(img_place)
            } else {

                img_play.visibility = View.GONE
                Glide.with(this@PlaceDetailsActivity)
                    .load(gallaryCombineList[count].item_name)
                    .placeholder(R.drawable.travel)
                    .centerCrop()
                    .into(img_place)
            }

            /* Glide.with(this)
                 .load(gallaryImageList[count].imageName)
                 .placeholder(R.drawable.travel)
                 .centerCrop()
                 .into(img_place)*/
        })

        rv_placeimg.affectOnItemClicks() { position, view ->

            count = position

            if (gallaryCombineList[count].flag == 1) {


                img_play.visibility = View.VISIBLE
                videoId = extractYoutubeId(gallaryCombineList[count].item_name);
                imgURl = "http://img.youtube.com/vi/" + videoId + "/0.jpg"
                videoLink = gallaryCombineList[count].item_name
                Glide.with(this@PlaceDetailsActivity)
                    .load(imgURl)
                    .placeholder(R.drawable.travel)
                    .centerCrop()
                    .into(img_place)
            } else {

                img_play.visibility = View.GONE
                Glide.with(this@PlaceDetailsActivity)
                    .load(gallaryCombineList[count].item_name)
                    .placeholder(R.drawable.travel)
                    .centerCrop()
                    .into(img_place)
            }


            /*Glide.with(this)
                .load(gallaryImageList[position].imageName)
                .placeholder(R.drawable.travel)
                .centerCrop()
                .into(img_place)*/
        }

    }

    private fun tablayout() {


        if (amenitiesRecordsMy!!.size == 0) {

            txt_pamenites.visibility = View.GONE
            view_pamenites.visibility = View.GONE

        }
        if (stayRecordsMy!!.size == 0) {

            txt_pstay.visibility = View.GONE
            view_pstay.visibility = View.GONE
        }
        if (paymnetRecordsMy!!.size == 0 && paymnetotherRecordsMy!!.size == 0) {

            txt_ppyament.visibility = View.GONE
            view_ppyament.visibility = View.GONE


        }
        if (otherRecordsMy!!.size == 0) {

            txt_pother.visibility = View.GONE
            view_pother.visibility = View.GONE

        }


        txt_pdescription.setOnClickListener(View.OnClickListener {

            startActivity(
                Intent(this, PlaceTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK,0)

            )
        })

        txt_pamenites.setOnClickListener(View.OnClickListener {

            startActivity(
                Intent(this, PlaceTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK,0)

            )
        })

        txt_pavailibility.setOnClickListener(View.OnClickListener {

            startActivity(
                Intent(this, PlaceTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK,1)

            )
        })

        txt_pamenites.setOnClickListener(View.OnClickListener {

            startActivity(
                Intent(this, PlaceTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK,2)

            )
        })

        txt_pstay.setOnClickListener(View.OnClickListener {

            startActivity(
                Intent(this, PlaceTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK,3)

            )
        })

        txt_pother.setOnClickListener(View.OnClickListener {

            startActivity(
                Intent(this, PlaceTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK,4)

            )
        })
        txt_ppyament.setOnClickListener(View.OnClickListener {

            startActivity(
                Intent(this, PlaceTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK,5)

            )
        })

        txt_preview.setOnClickListener(View.OnClickListener {

            startActivity(
                Intent(this, PlaceTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK,6)

            )
        })

        txt_pcontactus.setOnClickListener(View.OnClickListener {

            startActivity(
                Intent(this, PlaceTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK,7)

            )
        })
    }

    /*private fun tablayout() {

        tabLayout!!.addTab(tabLayout!!.newTab().setText("DESCRIPTION"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("AVAILABILTY"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("AMENITIES&ATTRACTIONS"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("STAY"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("PAYMENT"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("OTHERS"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("WRITE A REVIEW"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("CONTACT US"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        Log.e("stay", " " + stayRecordsMy!!.size)
        Log.e("paymnet", " " + stayRecordsMy!!.size)

        if (amenitiesRecordsMy!!.size == 0) {


            (tabLayout!!.getTabAt(2)!!.view as ViewGroup).visibility = View.GONE


//            (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(2).visibility = View.GONE

        }
        if (stayRecordsMy!!.size == 0) {

//            (tabLayout!!.getTabAt(3)!!.view as ViewGroup).visibility = View.GONE
            (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(3).visibility = View.GONE

        }
        if (paymnetRecordsMy!!.size == 0 && paymnetotherRecordsMy!!.size == 0) {

//            (tabLayout!!.getTabAt(4)!!.view as ViewGroup).visibility = View.GONE
            (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(4).visibility = View.GONE

        }
        if (otherRecordsMy!!.size == 0) {


//            (tabLayout!!.getTabAt(5)!!.view as ViewGroup).visibility = View.GONE
            (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(5).visibility = View.GONE

        }

//        https://medium.com/@mujjtahidah/add-and-remove-fragment-tab-layout-dynamically-9fe57add53fb

        val tab = tabLayout!!.getTabAt(0)
        tab!!.select()
//


//        (tabLayout!!.getChildAt(0) as ViewGroup).getChildAt(4).visibility = View.GONE
//        //Set the next  tab as selected tab


        val adapter = PlaceTabAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

        viewPager!!.setOnTouchListener(OnTouchListener { v, event -> true })
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


    private fun initviews() {
        getRecratetionData(
            contryID = countryID.toString(),
            selectCurrency = currncyCode.toString(),
            langCode = langCode.toString()
        )


    }

    private fun setVisitorplace() {

        val visiterPlaceAdapter = SliderVisiterPlaceAdapter(this)
        Common.setHorizontalRecyclerView(this, rv_placeimg)
        visiterPlaceAdapter.swapData(gallaryCombineList)
        rv_placeimg.adapter = visiterPlaceAdapter
    }

    private fun setVisitorNearplace() {

        val visiterPlaceSuggestedAdapter = VisiterPlaceSuggestedAdapter(this)
        Common.setHorizontalRecyclerView(this, rv_nearbyplace)
        visiterPlaceSuggestedAdapter.swapData(nearByplace)
        rv_nearbyplace.adapter = visiterPlaceSuggestedAdapter
    }

    private fun setSuggestedTour() {

        val tourSuggestedAdapter = TourSuggestedAdapter(this)
        Common.setHorizontalRecyclerView(this, rv_suggestedtour)
        tourSuggestedAdapter.swapData(suggestedTour)
        rv_suggestedtour.adapter = tourSuggestedAdapter
    }


    fun getURLForResource(resourceId: Int): String {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://" + com.greenspot.app.R::class.java!!.getPackage()!!.getName() + "/" + resourceId)
            .toString()
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


    private fun getRecratetionData(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
//        progress!!.showDialog()
        viewDialog!!.showDialog()
        utils!!.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val responceRecDetails = apiService?.RECREATION_DETAILS(
            contryID = contryID,
            selectCurrency = selectCurrency,
            langcode = langCode,
            recreationID = placeID.toString()

        )

        Log.e("contry", contryID)
        Log.e("currency", selectCurrency)
        Log.e("langcode", langCode)
        Log.e("placeid", placeID)

        responceRecDetails?.enqueue(object : Callback<ResponceRecDetails> {
            override fun onResponse(@NonNull call: Call<ResponceRecDetails>, @NonNull response: Response<ResponceRecDetails>) {
                viewDialog!!.hideDialog()

                val resRecreationDetails = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (resRecreationDetails!!.status == 1) {


                        Log.e("lastpage", "" + resRecreationDetails.message)
                        txt_vacationplacename.setText(resRecreationDetails.data.mainRecords.placeName)
                        txt_placelocation.setText(resRecreationDetails.data.mainRecords.district + ", " + resRecreationDetails.data.mainRecords.country)
                        txt_placesortdes.setText(resRecreationDetails.data.mainRecords.shortDescription)
                        rt_placerating.rating =
                            resRecreationDetails.data.mainRecords.avgReviews.toFloat()

                        helper?.initPref()
                        helper?.SaveStringPref(
                            AppConfig.PREFERENCE.SERVICEPROVIDERID,
                            resRecreationDetails.data.mainRecords.createdBy
                        )
                        helper?.ApplyPref()




                        gallaryVidoList =
                            resRecreationDetails.data.socialLinks as ArrayList<SocialLinksItem>

                        gallaryVidoList.sortBy {

                            it.orderId

                        }
                        gallaryImageList =
                            resRecreationDetails.data.galleryImages as ArrayList<GalleryImagesItem>

                        gallaryImageList.sortBy {

                            it.orderId

                        }

                        for (video in gallaryVidoList) {

                            gallaryCombineList.add(
                                GalleryImgList(
                                    video.masterId,
                                    video.videoName,
                                    video.orderId,
                                    1
                                )
                            )
                        }

                        for (image in gallaryImageList) {

                            gallaryCombineList.add(
                                GalleryImgList(
                                    image.masterId,
                                    image.imageName,
                                    image.orderId,
                                    2
                                )
                            )
                        }


                        if (gallaryCombineList[0].flag == 1) {

                            img_play.visibility = View.VISIBLE

                            videoLink = gallaryCombineList[0].item_name
                            videoId = extractYoutubeId(gallaryCombineList[0].item_name);
                            imgURl = "http://img.youtube.com/vi/" + videoId + "/0.jpg"

                            Glide.with(this@PlaceDetailsActivity)
                                .load(imgURl)
                                .placeholder(R.drawable.travel)
                                .centerCrop()
                                .into(img_place)
                        } else {

                            img_play.visibility = View.GONE
                            Glide.with(this@PlaceDetailsActivity)
                                .load(gallaryCombineList[0].item_name)
                                .placeholder(R.drawable.travel)
                                .centerCrop()
                                .into(img_place)
                        }

                        suggestedTour =
                            resRecreationDetails.data.suggestedTours as ArrayList<SuggestedToursItem>
                        nearByplace =
                            resRecreationDetails.data.nearbyPlaces as ArrayList<NearbyPlacesItem>

//
//


                        if (nearByplace.size == 0) {
                            txt_nearbyplace.visibility = View.GONE
                        } else {
                            txt_nearbyplace.visibility = View.VISIBLE
                        }

                        if (suggestedTour.size == 0) {
                            txt_suggesttour.visibility = View.GONE
                        } else {
                            txt_suggesttour.visibility = View.VISIBLE
                        }

                        setVisitorplace()
                        setVisitorNearplace()
                        setSuggestedTour()

                        lay_booknow.visibility = View.VISIBLE
                        if (resRecreationDetails.data.mainRecords.bookingOption == 1) {

                            btn_booknow.text = getString(R.string.res_book_now)
                            btn_booknow.background = ContextCompat.getDrawable(
                                this@PlaceDetailsActivity,
                                R.drawable.btn_fillgreen
                            )


                        } else {
                            val colorValue =
                                ContextCompat.getColor(this@PlaceDetailsActivity, R.color.tansparnt)
                            btn_booknow.setBackgroundColor(colorValue)
                            btn_booknow.text = getString(R.string.res_book_now_na)
                        }

                        if (resRecreationDetails.data.mainRecords.isBooking == 1) {
                            txt_perperson.visibility = View.VISIBLE
                            txt_startprice.text =
                                getString(R.string.str_startingform) + currncyCode + " " + resRecreationDetails.data.mainRecords.minPriceRange.toString()
                        } else {
                            txt_startprice.text = getString(R.string.str_pricenotavaible)
                            txt_perperson.visibility = View.GONE
                        }


                        val gson = Gson()
                        placeDetailResponce = gson.toJson(resRecreationDetails)

                        helper!!.initPref()
                        helper!!.SaveStringPref(
                            AppConfig.PREFERENCE.PLACEDETAILSRESPONCE,
                            placeDetailResponce
                        )
                        helper!!.ApplyPref()


                        for (aminate in resRecreationDetails.data.amenities.amenitiesRecords!!) {
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

                        for (otherdata in resRecreationDetails.data.other!!) {
                            if (otherdata.value != "") {
                                otherRecordsMy!!.add(otherdata)
                            }

                        }

                        for (stay in resRecreationDetails.data.stay!!) {
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

                        for (payment in resRecreationDetails.data.payments.paymentRecords!!) {
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

                        for (otherdata in resRecreationDetails.data.payments.otherRecords!!) {
                            if (otherdata.value != "") {
                                paymnetotherRecordsMy!!.add(otherdata)
                            }

                        }


                        tablayout()


                    } else {

//                        Toast.makeText(
//                            this@ListPlaceActivity,
//                            resRecreationDetails.message,
//                            Toast.LENGTH_SHORT
//                        ).show()

                    }
                } else {
                    Toast.makeText(
                        this@PlaceDetailsActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponceRecDetails>, @NonNull t: Throwable) {

                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)

                Toast.makeText(
                    this@PlaceDetailsActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    @Throws(MalformedURLException::class)
    fun extractYoutubeId(url: String?): String? {
        val query: String = URL(url).getQuery()
        val param = query.split("&").toTypedArray()
        var id: String? = null
        for (row in param) {
            val param1 = row.split("=").toTypedArray()
            if (param1[0] == "v") {
                id = param1[1]
            }
        }
        return id
    }

    fun showLargeImageInDialog(
        context: Context,
        url: String? = null,
        bitmap: Bitmap? = null,
        type: String = "I"
    ) {
        val dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(context.layoutInflater.inflate(R.layout.layout_large_imageview, null))

        val btnClose = dialog.findViewById<ImageButton>(R.id.btnIvClose)
        val youtubeView = dialog.findViewById<YouTubePlayerView>(R.id.youtubeView)
        var youTubePlayer: YouTubePlayer? = null


        if (bitmap != null) {
            youtubeView.visibility = View.GONE

        }

        if (type == "I" && bitmap == null) {
            youtubeView.visibility = View.GONE

        } else if (type == "V") {

            btnClose.visibility = View.GONE
            youtubeView.initialize({
                it.addListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady() {
                        youTubePlayer = it
                        it.loadVideo(getYoutubeId(url!!)!!, 0f)
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
        }

        dialog.setOnDismissListener {
            youTubePlayer?.pause()
            youTubePlayer = null
        }

        dialog.show()
    }


}
