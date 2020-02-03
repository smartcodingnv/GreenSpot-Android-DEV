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
import com.greenspot.app.MyApp
import com.greenspot.app.R
import com.greenspot.app.adapter.HotelPopularAdapter
import com.greenspot.app.adapter.SliderHotelAdapter
import com.greenspot.app.adapter.TourImgOtherServiceAdapter
import com.greenspot.app.model.ItineraryImg
import com.greenspot.app.network.ApiClient
import com.greenspot.app.network.ApiInterface
import com.greenspot.app.responce.hoteldetails.*
import com.greenspot.app.utils.*
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView
import hk.ids.gws.android.sclick.SClick
import it.sephiroth.android.library.imagezoom.ImageViewTouch
import kotlinx.android.synthetic.main.activity_hotel_details.*
import kotlinx.android.synthetic.main.content_hotel_details.*
import kotlinx.android.synthetic.main.content_hotel_details.lay_next
import kotlinx.android.synthetic.main.content_hotel_details.lay_prev
import kotlinx.android.synthetic.main.content_hotel_details.rv_otherservicetourdetailsimg
import kotlinx.android.synthetic.main.content_hotel_details.txt_populartour
import kotlinx.android.synthetic.main.content_hotel_details.txt_tourlocation
import kotlinx.android.synthetic.main.content_hotel_details.txt_tourpackgname
import kotlinx.android.synthetic.main.content_hotel_details.txt_viewonmap
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.layoutInflater
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class  HotelDetailsActivity : AppCompatActivity() {


    private var roomdata: ArrayList<RoomsItem> = ArrayList()
    private var hoteldetailsResponce: String = ""


    var count: Int = 0

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    private var amenitiesRecordsMy: ArrayList<AmenitiesItem>? = ArrayList()
    private lateinit var popularHotel: ArrayList<NearbyPlacesItem>

    private var progress: Progress? = null
    private var viewDialog: ViewDialog? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null
    private var currncyCode: String? = ""
    private var countryID: String? = ""
    private var langCode: String? = ""
    private var hotelID: String = ""
    private var hotelName: String = ""
    private var token: String = ""
    private var imgURl: String? = ""
    private var gallaryImageList: ArrayList<GalleryImagesItem> = ArrayList()
    private var latitude: String? = ""
    private var longitude: String? = ""
    var itineraryImg: java.util.ArrayList<ItineraryImg> = java.util.ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_details)


        val intenttt = getIntent()

        hotelID = intenttt.getStringExtra(AppConfig.EXTRA.HOTELID)
        hotelName = intenttt.getStringExtra(AppConfig.EXTRA.HOTELNAME)

        txt_hotelname.setText(hotelName)

        progress = Progress(this)
        viewDialog = ViewDialog(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)

        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")
        token = "Bearer " + helper!!.LoadStringPref(AppConfig.PREFERENCE.AUTHTOKEN, "")

        img_hotel.setOnClickListener(View.OnClickListener {
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
                .into(img_hotel)

            rv_hotelplaceimg.scrollToPosition(count)
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

                .into(img_hotel)

            rv_hotelplaceimg.scrollToPosition(count)
        })

        rv_hotelplaceimg.affectOnItemClicks() { position, view ->

            count = position

            imgURl = gallaryImageList[position].imageName

            Glide.with(this)
                .load(imgURl)

                .into(img_hotel)


        }

        btn_booknowhotel.setOnClickListener(View.OnClickListener {

            if (helper!!.LoadIntPref(AppConfig.PREFERENCE.CHECK_LOGINAS, 0) == 2) {

                startActivity(
                    Intent(this, HotelRoomListActivity::class.java)
                )

            } else {
                alertLogin()
            }


        })

        txt_viewonmap.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            val uri =
                "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);

        })

        initviews()

    }

    private fun initviews() {

        getHotelData(
            contryID = countryID.toString(),
            selectCurrency = currncyCode.toString(),
            langCode = langCode.toString()
        )


    }

    private fun getHotelData(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
        viewDialog!!.showDialog()
        utils!!.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val responceTourDetails = apiService?.HOTEL_DETAILS(
            contryID = contryID,
            selectCurrency = selectCurrency,
            langcode = langCode,
            tourid = hotelID
        )

        Log.e("contry", contryID)
        Log.e("currency", selectCurrency)
        Log.e("langcode", langCode)
        Log.e("placeid", hotelID)

        responceTourDetails?.enqueue(object : Callback<ResponceHotelDetails> {
            override fun onResponse(@NonNull call: Call<ResponceHotelDetails>, @NonNull response: Response<ResponceHotelDetails>) {
                viewDialog!!.hideDialog()

                val hotelDetails = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (hotelDetails!!.status == 1) {

                        val gson = Gson()
                        hoteldetailsResponce = gson.toJson(hotelDetails)

                        helper!!.initPref()
                        helper!!.SaveStringPref(
                            AppConfig.PREFERENCE.HOTELDETAILSRESPONCE,
                            hoteldetailsResponce
                        )
                        helper?.SaveStringPref(
                            AppConfig.PREFERENCE.SERVICEPROVIDERID,
                            hotelDetails.data.mainRecords.createdBy
                        )
                        helper!!.ApplyPref()


                        Log.e("lastpage", "" + hotelDetails.message)
                        txt_tourpackgname.setText(hotelDetails.data.mainRecords.name)
                        txt_tourlocation.setText(hotelDetails.data.mainRecords.address)
                        latitude = hotelDetails.data.mainRecords.latitude
                        longitude = hotelDetails.data.mainRecords.longitude

                        rt_hoteldetails.rating =
                            hotelDetails.data.mainRecords.avgReviews.toFloat()


                        gallaryImageList =
                            hotelDetails.data.galleryImages as ArrayList<GalleryImagesItem>

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
                        Glide.with(this@HotelDetailsActivity)
                            .load(imgURl)
                            .placeholder(R.drawable.travel)
                            .into(img_hotel)

                        popularHotel = hotelDetails.data.nearbyPlaces as ArrayList<NearbyPlacesItem>

                        if (popularHotel.size == 0) {
                            txt_populartour.visibility = View.GONE
                        } else {
                            txt_populartour.visibility = View.VISIBLE
                        }

                        latitude = hotelDetails.data.mainRecords.latitude
                        longitude = hotelDetails.data.mainRecords.longitude

                        if (latitude!!.isEmpty() && longitude!!.isEmpty()) {

                            txt_viewonmap.visibility = View.GONE
                        } else {
                            txt_viewonmap.visibility = View.VISIBLE
                        }

                        setPopularHotel()
                        setVisitorplace()
                        itineraryImg.clear()
                        setSubImgData(hotelDetails.data.mainRecords.includedInHotel)
                        setOtherserviceimg()

                        lay_booknowhotel.visibility = View.VISIBLE
                        btn_booknowhotel.text = getString(R.string.res_select_room)
                        btn_booknowhotel.background = ContextCompat.getDrawable(
                            this@HotelDetailsActivity,
                            R.drawable.btn_fillgreen
                        )
                        txt_perperson.visibility = View.VISIBLE

                        roomdata = hotelDetails.data.rooms as ArrayList<RoomsItem>

                        roomdata.sortedWith(compareBy {
                            it.finalPrice
                        })

                        txt_startprice.text =
                            getString(R.string.res_startingfrom) + " " + currncyCode + " " + roomdata[0].finalPrice

                        amenitiesRecordsMy!!.clear()

                        for (aminate in hotelDetails.data.amenities!!) {
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
                        this@HotelDetailsActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponceHotelDetails>, @NonNull t: Throwable) {

                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)

                Toast.makeText(
                    this@HotelDetailsActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun tablayout() {


        if (amenitiesRecordsMy!!.size == 0) {

            txt_hamenites.visibility = View.GONE
            view_hamenites.visibility = View.GONE
        }

        txt_hoverview.setOnClickListener {
            onclickOverivew(it)
        }



        txt_hamenites.setOnClickListener {
            onclickAmenites(it)
        }

        txt_hother.setOnClickListener {
            onclickOther(it)
        }

        txt_hreview.setOnClickListener {
            onclickReview(it)
        }

        txt_hcontactus.setOnClickListener {
            onclickContacus(it)
        }

    }

    private fun onclickContacus(it: View?) {
        if (!SClick.check(SClick.BUTTON_CLICK)) return
        startActivity(
            Intent(this, HotelTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 4)

        )
    }

    private fun onclickReview(it: View?) {
        if (!SClick.check(SClick.BUTTON_CLICK)) return
        startActivity(
            Intent(this, HotelTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 3)

        )
    }

    private fun onclickOther(it: View?) {
        if (!SClick.check(SClick.BUTTON_CLICK)) return
        startActivity(
            Intent(this, HotelTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 2)

        )
    }

    private fun onclickAmenites(it: View?) {
        if (!SClick.check(SClick.BUTTON_CLICK)) return
        startActivity(
            Intent(this, HotelTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 1)

        )
    }


    private fun onclickOverivew(it: View?) {
        if (!SClick.check(SClick.BUTTON_CLICK)) return
        startActivity(
            Intent(this, HotelTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 0)

        )

    }

    private fun setVisitorplace() {

        val sliderTourAdapter = SliderHotelAdapter(this)
        Common.setHorizontalRecyclerView(this, rv_hotelplaceimg)
        sliderTourAdapter.swapData(gallaryImageList)
        rv_hotelplaceimg.adapter = sliderTourAdapter
    }

    private fun setPopularHotel() {

        val hotelPopularAdapter = HotelPopularAdapter(this)
        Common.setHorizontalRecyclerView(this, rv_popuplarhotel)
        hotelPopularAdapter.swapData(popularHotel)
        rv_popuplarhotel.adapter = hotelPopularAdapter
    }

    private fun setOtherserviceimg() {

        val imgOtherServiceAdapter = TourImgOtherServiceAdapter(this)
        Common.setHorizontalRecyclerView(this, rv_otherservicetourdetailsimg)
        imgOtherServiceAdapter.swapData(itineraryImg)
        rv_otherservicetourdetailsimg.adapter = imgOtherServiceAdapter
    }


    private fun setSubImgData(includedInHotel: List<com.greenspot.app.responce.hoteldetails.IncludedInHotelItem>?) {
        for (name in includedInHotel!!) {

            if (name.masterId.equals("1")) {

                itineraryImg.add(
                    ItineraryImg(
                        "1",
                        getString(R.string.str_wifi),
                        getURLForResource(R.drawable.ic_otherservicewifi)
                    )
                )
            }

            if (name.masterId.equals("2")) {

                itineraryImg.add(
                    ItineraryImg(
                        "2",
                        getString(R.string.str_parking),
                        getURLForResource(R.drawable.ic_parking)
                    )
                )
            }

            if (name.masterId.equals("3")) {

                itineraryImg.add(
                    ItineraryImg(
                        "3",
                        getString(R.string.str_pool),
                        getURLForResource(R.drawable.ic_pool)
                    )
                )
            }
            if (name.masterId.equals("4")) {

                itineraryImg.add(
                    ItineraryImg(
                        "4",
                        getString(R.string.str_meals),
                        getURLForResource(R.drawable.ic_othermeal)
                    )
                )
            }
            if (name.masterId.equals("5")) {

                itineraryImg.add(
                    ItineraryImg(
                        "5",
                        getString(R.string.str_transportation),
                        getURLForResource(R.drawable.ic_othertransport)
                    )
                )
            }


        }


    }
    fun getURLForResource(resourceId: Int): String {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://" + com.greenspot.app.R::class.java!!.getPackage()!!.getName() + "/" + resourceId)
            .toString()
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
