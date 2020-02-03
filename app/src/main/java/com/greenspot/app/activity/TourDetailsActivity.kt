package com.greenspot.app.activity

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.greenspot.app.MyApp.Companion.updateLanguage
import com.greenspot.app.R
import com.greenspot.app.adapter.SliderTourAdapter
import com.greenspot.app.adapter.TourDetailsAdapter
import com.greenspot.app.adapter.TourImgOtherServiceAdapter
import com.greenspot.app.adapter.TourPopularAdapter
import com.greenspot.app.model.ItineraryImg
import com.greenspot.app.model.PlaceSubItem
import com.greenspot.app.network.ApiClient
import com.greenspot.app.network.ApiInterface
import com.greenspot.app.responce.tourdetail.*
import com.greenspot.app.utils.*
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView
import hk.ids.gws.android.sclick.SClick
import it.sephiroth.android.library.imagezoom.ImageViewTouch
import kotlinx.android.synthetic.main.activity_tour_details.*
import kotlinx.android.synthetic.main.content_hotel_details.*
import kotlinx.android.synthetic.main.content_tour_details.*
import kotlinx.android.synthetic.main.content_tour_details.lay_next
import kotlinx.android.synthetic.main.content_tour_details.lay_prev
import kotlinx.android.synthetic.main.content_tour_details.rv_otherservicetourdetailsimg
import kotlinx.android.synthetic.main.content_tour_details.txt_populartour
import kotlinx.android.synthetic.main.content_tour_details.txt_tourlocation
import kotlinx.android.synthetic.main.content_tour_details.txt_tourpackgname
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.layoutInflater
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class TourDetailsActivity : AppCompatActivity() {
    private var tourDetailsResponce: String = ""
    private var tourCategory: String = ""
    private var depatureCity: String = ""
    private var tourlocation: String = ""
    private var tourprice: String = ""
    private var nights: String = ""
    private var days: String = ""
    private var tourType: String = ""
    var otherData: ArrayList<PlaceSubItem> = ArrayList()
    var count: Int = 0

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    private var gallaryImageList: ArrayList<GalleryImagesItem> = ArrayList()
    private var amenitiesRecordsMy: ArrayList<AmenitiesItem>? = ArrayList()
    private var popularTour: ArrayList<PopularToursItem> = ArrayList()


    private var progress: Progress? = null
    private var viewDialog: ViewDialog? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null
    private var currncyCode: String? = ""
    private var countryID: String? = ""
    private var langCode: String? = ""
    private var tourID: String = ""
    private var tourName: String = ""
    private var token: String = ""
    private var imgURl: String? = ""


    var itineraryImg: ArrayList<ItineraryImg> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_details)

        val intenttt = getIntent()

        tourID = intenttt.getStringExtra(AppConfig.EXTRA.TOURID)
        tourName = intenttt.getStringExtra(AppConfig.EXTRA.TOURNAME)

        txt_tourname.setText(tourName)

        progress = Progress(this)
        viewDialog = ViewDialog(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)


        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")
        token = "Bearer " + helper!!.LoadStringPref(AppConfig.PREFERENCE.AUTHTOKEN, "")

        tabLayout = findViewById<TabLayout>(com.greenspot.app.R.id.tabLayout)
        viewPager = findViewById<ViewPager>(com.greenspot.app.R.id.viewPager)



        img_tour.setOnClickListener(View.OnClickListener {
            if (imgURl!!.isEmpty()) {

                return@OnClickListener
            }
            showLargeImage(context = this, url = imgURl, type = "I")

        })

        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })


        lay_prev.setOnClickListener(View.OnClickListener {
            if (count == 0) {
                return@OnClickListener
            }
            count--
            imgURl = gallaryImageList[count].imageName
            Glide.with(this)
                .load(imgURl)
                 .dontTransform()
                .into(img_tour)

            rv_tourplaceimg.scrollToPosition(count)
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

                .into(img_tour)

            rv_tourplaceimg.scrollToPosition(count)
        })

        rv_tourplaceimg.affectOnItemClicks() { position, view ->

            count = position

            imgURl = gallaryImageList[position].imageName

            Glide.with(this)
                .load(imgURl)

                .into(img_tour)


        }

        btn_booknowtour.setOnClickListener(View.OnClickListener {

            if (helper!!.LoadIntPref(AppConfig.PREFERENCE.CHECK_LOGINAS, 0) == 2) {

                startActivity(
                    Intent(this, TourBookingActivity::class.java)
                )

            } else {
                alertLogin()
            }


        })

        initviews()

    }

    private fun alertLogin() {
        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage(getString(R.string.alert_loginmsg))
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            getString(R.string.res_ok),
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                helper!!.clearAllPrefs()
                startActivity(
                    Intent(this, LoginActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()

            })

        builder1.setNegativeButton(
            getString(R.string.res_cancel),
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        val alert11 = builder1.create()
        alert11.show()

    }


/*
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

        val tab = tabLayout!!.getTabAt(0)
        tab!!.select()

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
    }
*/

    private fun tablayout() {


        if (amenitiesRecordsMy!!.size == 0) {

            txt_tamenites.visibility = View.GONE
            view_tamenites.visibility = View.GONE
        }

        txt_toverview.setOnClickListener {
            onclickOverivew(it)
        }


        txt_titinerary.setOnClickListener {
            onclickItinerary(it)
        }

        txt_tamenites.setOnClickListener {
            onclickAmenites(it)
        }

        txt_tother.setOnClickListener {
            onclickOther(it)
        }

        txt_treview.setOnClickListener {
            onclickReview(it)
        }

        txt_tcontactus.setOnClickListener {
            onclickContacus(it)
        }

    }

    private fun onclickContacus(it: View?) {
        if (!SClick.check(SClick.BUTTON_CLICK)) return
        startActivity(
            Intent(this, TourTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 5)

        )
    }

    private fun onclickReview(it: View?) {
        if (!SClick.check(SClick.BUTTON_CLICK)) return
        startActivity(
            Intent(this, TourTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 4)

        )
    }

    private fun onclickOther(it: View?) {
        if (!SClick.check(SClick.BUTTON_CLICK)) return
        startActivity(
            Intent(this, TourTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 3)

        )
    }

    private fun onclickAmenites(it: View?) {
        if (!SClick.check(SClick.BUTTON_CLICK)) return
        startActivity(
            Intent(this, TourTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 2)

        )
    }

    private fun onclickItinerary(it: View?) {
        if (!SClick.check(SClick.BUTTON_CLICK)) return
        startActivity(
            Intent(this, TourTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 1)

        )
    }

    private fun onclickOverivew(it: View?) {
        if (!SClick.check(SClick.BUTTON_CLICK)) return
        startActivity(
            Intent(this, TourTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 0)

        )

    }

    private fun initviews() {

        getTourData(
            contryID = countryID.toString(),
            selectCurrency = currncyCode.toString(),
            langCode = langCode.toString()
        )


    }

    private fun setTourDetail() {

        val placeDetailsAdapter = TourDetailsAdapter(this)
        otherData.clear()
        setUpOtherData()
        Common.setVerticalRecyclerView(this, rv_detailtour)
        placeDetailsAdapter.swapData(otherData)
        rv_detailtour.adapter = placeDetailsAdapter
    }


    private fun setVisitorplace() {

        val sliderTourAdapter = SliderTourAdapter(this)
        Common.setHorizontalRecyclerView(this!!, rv_tourplaceimg)
        sliderTourAdapter.swapData(gallaryImageList)
        rv_tourplaceimg.adapter = sliderTourAdapter
    }

    private fun setOtherserviceimg() {

        val imgOtherServiceAdapter = TourImgOtherServiceAdapter(this)
        Common.setHorizontalRecyclerView(this, rv_otherservicetourdetailsimg)
        imgOtherServiceAdapter.swapData(itineraryImg)
        rv_otherservicetourdetailsimg.adapter = imgOtherServiceAdapter
    }

    private fun setPopularTour() {

        val tourPopularAdapter = TourPopularAdapter(this)
        Common.setHorizontalRecyclerView(this, rv_popuplartour)
        tourPopularAdapter.swapData(popularTour)
        rv_popuplartour.adapter = tourPopularAdapter
    }


    private fun getTourData(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
        viewDialog!!.showDialog()
        utils!!.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val responceTourDetails = apiService?.TOUR_DETAILS(
            contryID = contryID,
            selectCurrency = selectCurrency,
            langcode = langCode,
            tourid = tourID
        )

        Log.e("contry", contryID)
        Log.e("currency", selectCurrency)
        Log.e("langcode", langCode)
        Log.e("placeid", tourID)

        responceTourDetails?.enqueue(object : Callback<ResponceTourDetails> {
            override fun onResponse(@NonNull call: Call<ResponceTourDetails>, @NonNull response: Response<ResponceTourDetails>) {
                viewDialog!!.hideDialog()

                val tourDetails = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (tourDetails!!.status == 1) {

                        val gson = Gson()
                        tourDetailsResponce = gson.toJson(tourDetails)

                        helper!!.initPref()
                        helper!!.SaveStringPref(
                            AppConfig.PREFERENCE.TOURDETAILSRESPONCE,
                            tourDetailsResponce
                        )
                        helper?.SaveStringPref(
                            AppConfig.PREFERENCE.SERVICEPROVIDERID,
                            tourDetails.data.mainRecords.createdBy
                        )
                        helper!!.ApplyPref()


                        Log.e("lastpage", "" + tourDetails.message)
                        txt_tourpackgname.setText(tourDetails.data.mainRecords.packageName)
                        txt_tourlocation.setText(tourDetails.data.mainRecords.locations)

                        rt_tourdetails.rating =
                            tourDetails.data.mainRecords.avgReviews.toFloat()






                        gallaryImageList =
                            tourDetails.data.galleryImages as ArrayList<GalleryImagesItem>

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
                        Glide.with(this@TourDetailsActivity)
                            .load(imgURl)
                            .placeholder(R.drawable.travel)

                            .into(img_tour)

                        popularTour = tourDetails.data.popularTours as ArrayList<PopularToursItem>

                        if (popularTour.size == 0) {
                            txt_populartour.visibility = View.GONE
                        } else {
                            txt_populartour.visibility = View.VISIBLE
                        }

                        setPopularTour()
                        setVisitorplace()

                        itineraryImg.clear()
                        setSubImgData(tourDetails.data.mainRecords.includedInTourPackage)
                        setOtherserviceimg()



                        tourCategory = tourDetails.data.mainRecords.tourCategory
                        depatureCity = tourDetails.data.mainRecords.depatureCity
                        tourlocation = tourDetails.data.mainRecords.locations
                        tourprice = tourDetails.data.mainRecords.price
                        nights = tourDetails.data.mainRecords.nights.toString()
                        days = tourDetails.data.mainRecords.days.toString()
                        tourType = tourDetails.data.mainRecords.tourType

                        setTourDetail()

                        lay_booknowtour.visibility = View.VISIBLE
                        btn_booknowtour.text = getString(R.string.res_book_now)
                        btn_booknowtour.background = ContextCompat.getDrawable(
                            this@TourDetailsActivity,
                            R.drawable.btn_fillgreen
                        )
                        txt_perperson.visibility = View.VISIBLE
                        txt_startprice.text =
                            getString(R.string.res_startingfrom) + " " + currncyCode + " " + tourDetails.data.mainRecords.price



                        amenitiesRecordsMy!!.clear()

                        for (aminate in tourDetails.data.amenities!!) {
                            if (aminate.value != "") {
                                amenitiesRecordsMy!!.add(aminate)
                            }

                        }



                        tablayout()


                    } else {

//                        Toast.makeText(
//                            this@ListPlaceActivity,
//                            tourDetails.message,
//                            Toast.LENGTH_SHORT
//                        ).show()

                    }
                } else {
                    Toast.makeText(
                        this@TourDetailsActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponceTourDetails>, @NonNull t: Throwable) {

                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)

                Toast.makeText(
                    this@TourDetailsActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun setSubImgData(includedInTourPackage: List<IncludedInTourPackageItem>?) {
        for (name in includedInTourPackage!!) {

            if (name.masterId.equals("1")) {

                itineraryImg.add(
                    ItineraryImg(
                        "1",
                        getString(R.string.res_flight),
                        getURLForResource(R.drawable.ic_otherflight)
                    )
                )
            }

            if (name.masterId.equals("2")) {

                itineraryImg.add(
                    ItineraryImg(
                        "2",
                        getString(R.string.res_hotel),
                        getURLForResource(R.drawable.ic_ohterhotel)
                    )
                )
            }

            if (name.masterId.equals("3")) {

                itineraryImg.add(
                    ItineraryImg(
                        "3",
                        getString(R.string.res_sightseeing),
                        getURLForResource(R.drawable.ic_othersigghtsign)
                    )
                )
            }
            if (name.masterId.equals("4")) {

                itineraryImg.add(
                    ItineraryImg(
                        "4",
                        getString(R.string.res_meals),
                        getURLForResource(R.drawable.ic_othermeal)
                    )
                )
            }
            if (name.masterId.equals("5")) {

                itineraryImg.add(
                    ItineraryImg(
                        "5",
                        getString(R.string.res_trasnportations),
                        getURLForResource(R.drawable.ic_othertransport)
                    )
                )
            }
            if (name.masterId.equals("6")) {

                itineraryImg.add(
                    ItineraryImg(
                        "6",
                        getString(R.string.res_wifi),
                        getURLForResource(R.drawable.ic_otherservicewifi)
                    )
                )
            }

        }


    }


    private fun setUpOtherData() {

        if (tourCategory.isNotEmpty()) {
            otherData.add(
                PlaceSubItem(
                    "2",
                    getString(R.string.str_tourcategory),
                    tourCategory
                )
            )

        }

        if (depatureCity.isNotEmpty()) {
            otherData.add(
                PlaceSubItem(
                    "3",
                    getString(R.string.str_depaturesicty),
                    depatureCity
                )
            )

        }

        if (tourlocation.isNotEmpty()) {
            otherData.add(
                PlaceSubItem(
                    "4",
                    getString(R.string.str_location),
                    tourlocation
                )
            )
        }

        if (tourprice.isNotEmpty()) {
            otherData.add(
                PlaceSubItem(
                    "5",
                    getString(R.string.sttr_price),
                    currncyCode + " " + tourprice
                )
            )
        }

        if (nights.isNotEmpty()) {
            otherData.add(
                PlaceSubItem(
                    "6",
                    getString(R.string.strr_night),
                    nights
                )
            )
        }

        if (days.isNotEmpty()) {
            otherData.add(
                PlaceSubItem(
                    "7",
                    getString(R.string.strr_days),
                    days
                )
            )
        }



        if (tourType.isNotEmpty()) {
            otherData.add(
                PlaceSubItem(
                    "8",
                    getString(R.string.strr_torutype),
                    tourType
                )
            )
        }


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
            Glide.with(context)
                .load(url)
                .dontTransform()
                .into(ivPreview)
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
            updateLanguage(context)
        }

        dialog.setOnDismissListener {
            youTubePlayer?.pause()
            youTubePlayer = null
            updateLanguage(context)
        }

        dialog.show()
    }


}
