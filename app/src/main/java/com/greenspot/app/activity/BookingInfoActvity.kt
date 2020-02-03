package com.greenspot.app.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.network.ApiClient
import com.greenspot.app.network.ApiInterface
import com.greenspot.app.responce.bookinginfo.ResponceBookinginfo
import com.greenspot.app.responce.bookinginfo.TourAmenitiesItem
import com.greenspot.app.responce.bookinginfoevent.ResponceBookinginfoEvent
import com.greenspot.app.responce.bookinginfohotel.ResponceBookinginfoHotel
import com.greenspot.app.responce.bookinginforecreation.ResponceBookinginfoRecreation
import com.greenspot.app.utils.*
import hk.ids.gws.android.sclick.SClick
import kotlinx.android.synthetic.main.activity_booking_info_actvity.*
import kotlinx.android.synthetic.main.content_booking_info_actvity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class BookingInfoActvity : AppCompatActivity() {

    private var amenitiesRecordsMy: ArrayList<TourAmenitiesItem>? = ArrayList()
    private var dayAdultperson: String = ""
    private var dayChildperson: String = ""
    private var nightAdultperson: String = ""
    private var nightChildperson: String = ""
    private var checkbookingType: Int = 0
    private var responcebookingInfo: String = ""
    private var toutTitle: String = ""
    private var bookingID: String? = ""
    private var bookingType: String? = ""
    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null
    private var viewDialog: ViewDialog? = null


    private var currncyCode: String? = ""
    private var countryID: String? = ""
    private var langCode: String? = ""
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_info_actvity)

        progress = Progress(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        viewDialog = ViewDialog(this)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)

        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")
        token = "Bearer " + helper!!.LoadStringPref(AppConfig.PREFERENCE.AUTHTOKEN, "")


        val intent = getIntent();
        bookingID = intent.getStringExtra(AppConfig.EXTRA.BOOKINGID)
        bookingType = intent.getStringExtra(AppConfig.EXTRA.BOOKINGTYPE)
        checkbookingType = intent.getIntExtra(AppConfig.EXTRA.CHECKBOOKINGINFO, 0)

        if (bookingType.equals("Recreation")) {
            txt_title.text = getString(R.string.title_vacationbookingdetails)
        } else if (bookingType.equals("Event")) {
            txt_title.text = getString(R.string.title_eventbookingdetails)
        } else if (bookingType.equals("Tour")) {
            txt_title.text = getString(R.string.title_torubookingdetails)
        } else if (bookingType.equals("Hotel")) {
            txt_title.text = getString(R.string.res_hotelbookingdetails)
        }

        if (checkbookingType == 1) {

            ib_backbookinginfo.setOnClickListener(View.OnClickListener {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            })


        } else if (checkbookingType == 2) {

            ib_backbookinginfo.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })
        }


        txt_tourdetails.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivity(
                Intent(this, BookingInfoTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK, 0)
                    .putExtra(AppConfig.EXTRA.TOURTITLE, toutTitle)
                    .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)

            )
        })

        txt_allperson.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivity(
                Intent(this, BookingInfoTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK, 1)
                    .putExtra(AppConfig.EXTRA.TOURTITLE, toutTitle)
                    .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)

            )
        })

        txt_includefacility.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivity(
                Intent(this, BookingInfoTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK, 2)
                    .putExtra(AppConfig.EXTRA.TOURTITLE, toutTitle)
                    .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)

            )
        })

        txt_contactus.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivity(
                Intent(this, BookingInfoTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK, 3)
                    .putExtra(AppConfig.EXTRA.TOURTITLE, toutTitle)
                    .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)

            )
        })

        txt_itineary.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivity(
                Intent(this, BookingInfoTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK, 4)
                    .putExtra(AppConfig.EXTRA.TOURTITLE, toutTitle)
                    .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)

            )
        })

        txt_eventdetails.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivity(
                Intent(this, BookingInfoTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK, 0)
                    .putExtra(AppConfig.EXTRA.TOURTITLE, toutTitle)
                    .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)

            )
        })



        txt_eventincludefacility.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivity(
                Intent(this, BookingInfoTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK, 1)
                    .putExtra(AppConfig.EXTRA.TOURTITLE, toutTitle)
                    .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)

            )
        })

        txt_eventcontactus.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivity(
                Intent(this, BookingInfoTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK, 2)
                    .putExtra(AppConfig.EXTRA.TOURTITLE, toutTitle)
                    .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)

            )
        })

        txt_vacationdetails.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivity(
                Intent(this, BookingInfoTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK, 0)
                    .putExtra(AppConfig.EXTRA.TOURTITLE, toutTitle)
                    .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)

            )
        })

        txt_vacationincludefacility.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivity(
                Intent(this, BookingInfoTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK, 1)
                    .putExtra(AppConfig.EXTRA.TOURTITLE, toutTitle)
                    .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)

            )
        })

        txt_vacationstay.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivity(
                Intent(this, BookingInfoTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK, 2)
                    .putExtra(AppConfig.EXTRA.TOURTITLE, toutTitle)
                    .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)

            )
        })

        txt_vacationcontactus.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivity(
                Intent(this, BookingInfoTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK, 3)
                    .putExtra(AppConfig.EXTRA.TOURTITLE, toutTitle)
                    .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)

            )
        })
     val txt_hoteldetails= findViewById<TextView>(R.id.txt_hoteldetails)

        txt_hoteldetails.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;

            startActivity(
                Intent(this, BookingInfoTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK, 0)
                    .putExtra(AppConfig.EXTRA.TOURTITLE, toutTitle)
                    .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)

            )
        })


        txt_hotelrooms.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivity(
                Intent(this, BookingInfoTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK, 1)
                    .putExtra(AppConfig.EXTRA.TOURTITLE, toutTitle)
                    .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)

            )
        })

        txt_hotelincludefacility.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivity(
                Intent(this, BookingInfoTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK, 2)
                    .putExtra(AppConfig.EXTRA.TOURTITLE, toutTitle)
                    .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)

            )
        })

        txt_hotelpolicy.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivity(
                Intent(this, BookingInfoTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK, 3)
                    .putExtra(AppConfig.EXTRA.TOURTITLE, toutTitle)
                    .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)

            )
        })

        txt_hotelcontactus.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivity(
                Intent(this, BookingInfoTabActivity::class.java)
                    .putExtra(AppConfig.EXTRA.TABCHECK, 4)
                    .putExtra(AppConfig.EXTRA.TOURTITLE, toutTitle)
                    .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)

            )
        })



        intiView()

    }

    private fun intiView() {

        if (bookingType.equals("Tour")) {
            tourbookingInfo(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!
            )

        } else if (bookingType.equals("Event")) {

            eventbookingInfo(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!
            )
        } else if (bookingType.equals("Recreation")) {
            vacationbookingInfo(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!
            )

        } else if (bookingType.equals("Hotel")) {
            hotelbookingInfo(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!
            )

        }


    }

    private fun tourbookingInfo(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))

        viewDialog!!.showDialog()
        utils!!.hideKeyboard()
        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val checkBookingResponce = apiService?.CALL_BOOKINGINFO(
            token = token,
            langcode = langCode,
            bookingid = this.bookingID!!,
            bookingtype = this.bookingType!!
        )

        checkBookingResponce?.enqueue(object : Callback<ResponceBookinginfo> {
            override fun onResponse(@NonNull call: Call<ResponceBookinginfo>, @NonNull response: Response<ResponceBookinginfo>) {
                viewDialog!!.hideDialog()
                val bookinginfo = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (bookinginfo!!.status == 1) {

//                        currencyData()
                        lay_eventtab.visibility = View.GONE
                        lay_touttab.visibility = View.VISIBLE
                        lay_vacationtab.visibility = View.GONE
                        lay_hoteltab.visibility = View.GONE

                        lay_daypass.visibility = View.GONE
                        lay_nightpass.visibility = View.GONE
                        lay_noofperson.visibility = View.VISIBLE


                        val gson = Gson()
                        responcebookingInfo = gson.toJson(bookinginfo)

                        helper!!.initPref()
                        helper!!.SaveStringPref(
                            AppConfig.PREFERENCE.BOOKINGINFO,
                            responcebookingInfo
                        )
                        helper!!.ApplyPref()


                        Glide.with(this@BookingInfoActvity)
                            .load(bookinginfo.data.tourData.mainImage)
                            .placeholder(R.drawable.travel)
                            .centerCrop()
                            .into(img_booking)

                        toutTitle = bookinginfo.data.tourData.packageName

                        txt_itemanme.text = bookinginfo.data.tourData.packageName
                        txt_itemlocation.text =
                            getString(R.string.txt_location) + " " + bookinginfo.data.tourData.locations

                        txt_labeldate.text = getString(R.string.txt_tourdate)
                        txt_date.text =
                            bookinginfo.data.paymentInfo.fromDate + " " + getString(R.string.res_to) + " " + bookinginfo.data.paymentInfo.toDate
                        txt_noofperson.text = bookinginfo.data.paymentInfo.noOfPerson
                        txt_bookingdate.text = bookinginfo.data.paymentInfo.createdAt
                        txt_bookingid.text = bookinginfo.data.paymentInfo.bookingId
                        txt_paymenttype.text = bookinginfo.data.paymentInfo.paymentType
                        txt_totalprice.text =
                            bookinginfo.data.paymentInfo.originalPaymentCurrency + " " + bookinginfo.data.paymentInfo.originalPaymentPrice
                        if (bookinginfo.data.userInfo.title.equals("Mr")) {

                            txt_name.text =
                                getString(R.string.mr) + " " + bookinginfo.data.userInfo.firstName + " " + bookinginfo.data.userInfo.lastName

                        } else if (bookinginfo.data.userInfo.title.equals("Mrs")) {

                            txt_name.text =
                                getString(R.string.res_mrs) + " " + bookinginfo.data.userInfo.firstName + " " + bookinginfo.data.userInfo.lastName

                        } else if (bookinginfo.data.userInfo.title.equals("Miss")) {

                            txt_name.text =
                                getString(R.string.res_miss) + " " + bookinginfo.data.userInfo.firstName + " " + bookinginfo.data.userInfo.lastName
                        }


                        txt_dob.text = bookinginfo.data.userInfo.dob
                        if (bookinginfo.data.userInfo.gender.equals("M")) {
                            txt_gender.text = getString(R.string.res_male)
                        } else if (bookinginfo.data.userInfo.gender.equals("F")) {
                            txt_gender.text = getString(R.string.res_female)
                        } else {
                            txt_gender.text = getString(R.string.res_na)
                        }

                        txt_email.text = bookinginfo.data.userInfo.email
                        txt_contactno.text = bookinginfo.data.userInfo.contactNumber

                        amenitiesRecordsMy!!.clear()

                        for (aminate in bookinginfo.data.tourAmenities!!) {
                            if (aminate.value != "") {
                                amenitiesRecordsMy!!.add(aminate)
                            }

                        }

                        if (amenitiesRecordsMy!!.size == 0) {

                            txt_includefacility.visibility = View.GONE
                            view_facility.visibility = View.GONE
                        }


                    } else {

                        Toast.makeText(
                            this@BookingInfoActvity,
                            bookinginfo.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (response.code() == AppConfig.URL.TOKEN_EXPIRE) {

                    login()

                } else {
                    Toast.makeText(
                        this@BookingInfoActvity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponceBookinginfo>, @NonNull t: Throwable) {
                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)

                Toast.makeText(
                    this@BookingInfoActvity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    private fun login() {

        helper!!.clearAllPrefs()
        startActivity(
            Intent(this, MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        finish()
    }


    private fun eventbookingInfo(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))

        viewDialog!!.showDialog()
        utils!!.hideKeyboard()
        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val checkBookingResponce = apiService?.CALL_EVENTBOOKINGINFO(
            token = token,
            langcode = langCode,
            bookingid = this.bookingID!!,
            bookingtype = this.bookingType!!
        )

        checkBookingResponce?.enqueue(object : Callback<ResponceBookinginfoEvent> {
            override fun onResponse(@NonNull call: Call<ResponceBookinginfoEvent>, @NonNull response: Response<ResponceBookinginfoEvent>) {
                viewDialog!!.hideDialog()
                val bookinginfo = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (bookinginfo!!.status == 1) {

//                        currencyData()
                        lay_hoteltab.visibility = View.GONE
                        lay_eventtab.visibility = View.VISIBLE
                        lay_touttab.visibility = View.GONE
                        lay_vacationtab.visibility = View.GONE

                        lay_daypass.visibility = View.GONE
                        lay_nightpass.visibility = View.GONE
                        lay_noofperson.visibility = View.VISIBLE

                        val gson = Gson()
                        responcebookingInfo = gson.toJson(bookinginfo)

                        helper!!.initPref()
                        helper!!.SaveStringPref(
                            AppConfig.PREFERENCE.EVENTBOOKINGINFO,
                            responcebookingInfo
                        )
                        helper!!.ApplyPref()


                        Glide.with(this@BookingInfoActvity)
                            .load(bookinginfo.data.eventData.mainImage)
                            .placeholder(R.drawable.travel)
                            .centerCrop()
                            .into(img_booking)

                        toutTitle = bookinginfo.data.eventData.title

                        txt_itemanme.text = bookinginfo.data.eventData.title
                        txt_itemlocation.text =
                            bookinginfo.data.eventData.city + ", " + bookinginfo.data.eventData.district + ", " + bookinginfo.data.eventData.country

                        txt_labeldate.text = getString(R.string.txt_eventdate)
                        txt_date.text =
                            bookinginfo.data.paymentInfo.fromDate + " " + getString(R.string.res_to) + "  " + bookinginfo.data.paymentInfo.toDate
                        txt_noofperson.text = bookinginfo.data.paymentInfo.noOfPerson
                        txt_bookingdate.text = bookinginfo.data.paymentInfo.createdAt
                        txt_bookingid.text = bookinginfo.data.paymentInfo.bookingId

                        if (bookinginfo.data.paymentInfo.originalPaymentPrice.isEmpty()) {
                            txt_totalprice.text = getString(R.string.res_free)
                            txt_paymenttype.text = getString(R.string.res_na)
                        } else {
                            txt_totalprice.text =
                                bookinginfo.data.paymentInfo.originalPaymentCurrency + " " + bookinginfo.data.paymentInfo.originalPaymentPrice
                            txt_paymenttype.text = bookinginfo.data.paymentInfo.paymentType
                        }

                        if (bookinginfo.data.userInfo.title.equals("Mr")) {

                            txt_name.text =
                                getString(R.string.mr) + " " + bookinginfo.data.userInfo.firstName + " " + bookinginfo.data.userInfo.lastName

                        } else if (bookinginfo.data.userInfo.title.equals("Mrs")) {

                            txt_name.text =
                                getString(R.string.res_mrs) + " " + bookinginfo.data.userInfo.firstName + " " + bookinginfo.data.userInfo.lastName

                        } else if (bookinginfo.data.userInfo.title.equals("Miss")) {

                            txt_name.text =
                                getString(R.string.res_miss) + " " + bookinginfo.data.userInfo.firstName + " " + bookinginfo.data.userInfo.lastName
                        }


                        txt_dob.text = bookinginfo.data.userInfo.dob
                        if (bookinginfo.data.userInfo.gender.equals("M")) {
                            txt_gender.text = getString(R.string.res_male)
                        } else if (bookinginfo.data.userInfo.gender.equals("F")) {
                            txt_gender.text = getString(R.string.res_female)
                        } else {
                            txt_gender.text = getString(R.string.res_na)
                        }

                        txt_email.text = bookinginfo.data.userInfo.email
                        txt_contactno.text = bookinginfo.data.userInfo.contactNumber


                    } else {

                        Toast.makeText(
                            this@BookingInfoActvity,
                            bookinginfo.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (response.code() == AppConfig.URL.TOKEN_EXPIRE) {

                    login()

                } else {

                    Toast.makeText(
                        this@BookingInfoActvity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponceBookinginfoEvent>, @NonNull t: Throwable) {
                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)

                Toast.makeText(
                    this@BookingInfoActvity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    private fun hotelbookingInfo(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))

        viewDialog!!.showDialog()
        utils!!.hideKeyboard()
        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val checkBookingResponce = apiService?.CALL_HOTELBOOKINGINFO(
            token = token,
            langcode = langCode,
            bookingid = this.bookingID!!,
            bookingtype = this.bookingType!!
        )

        checkBookingResponce?.enqueue(object : Callback<ResponceBookinginfoHotel> {
            override fun onResponse(@NonNull call: Call<ResponceBookinginfoHotel>, @NonNull response: Response<ResponceBookinginfoHotel>) {
                viewDialog!!.hideDialog()
                val bookinginfo = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (bookinginfo!!.status == 1) {

//                        currencyData()

                        lay_hoteltab.visibility = View.VISIBLE
                        lay_eventtab.visibility = View.GONE
                        lay_touttab.visibility = View.GONE
                        lay_vacationtab.visibility = View.GONE

                        lay_daypass.visibility = View.GONE
                        lay_nightpass.visibility = View.GONE
                        lay_noofperson.visibility = View.VISIBLE

                        val gson = Gson()
                        responcebookingInfo = gson.toJson(bookinginfo)

                        helper!!.initPref()
                        helper!!.SaveStringPref(AppConfig.PREFERENCE.HOTELBOOKINGINFO, responcebookingInfo)
                        helper!!.ApplyPref()


                        Glide.with(this@BookingInfoActvity)
                            .load(bookinginfo.data.hotelData.mainImage)
                            .placeholder(R.drawable.travel)
                            .centerCrop()
                            .into(img_booking)

                        toutTitle = bookinginfo.data.hotelData.name

                        txt_itemanme.text = bookinginfo.data.hotelData.name
                        txt_itemlocation.text = bookinginfo.data.hotelData.address

                        txt_labeldate.text = getString(R.string.res_roombooked_date)
                        txt_date.text =
                            bookinginfo.data.paymentInfo.fromDate + " " + getString(R.string.res_to) + "  " + bookinginfo.data.paymentInfo.toDate
                        label_noofperson.text = getString(R.string.str_no_of_rooms)
                        txt_noofperson.text = bookinginfo.data.paymentInfo.noOfRooms.toString()
                        txt_bookingdate.text = bookinginfo.data.paymentInfo.createdAt
                        txt_bookingid.text = bookinginfo.data.paymentInfo.bookingId

                        txt_totalprice.text =
                            bookinginfo.data.paymentInfo.originalPaymentCurrency + " " + bookinginfo.data.paymentInfo.originalPaymentPrice
                        txt_paymenttype.text = bookinginfo.data.paymentInfo.paymentType


                        if (bookinginfo.data.userInfo.title.equals("Mr")) {

                            txt_name.text =
                                getString(R.string.mr) + " " + bookinginfo.data.userInfo.firstName + " " + bookinginfo.data.userInfo.lastName

                        } else if (bookinginfo.data.userInfo.title.equals("Mrs")) {

                            txt_name.text =
                                getString(R.string.res_mrs) + " " + bookinginfo.data.userInfo.firstName + " " + bookinginfo.data.userInfo.lastName

                        } else if (bookinginfo.data.userInfo.title.equals("Miss")) {

                            txt_name.text =
                                getString(R.string.res_miss) + " " + bookinginfo.data.userInfo.firstName + " " + bookinginfo.data.userInfo.lastName
                        }


                        txt_dob.text = bookinginfo.data.userInfo.dob
                        if (bookinginfo.data.userInfo.gender.equals("M")) {
                            txt_gender.text = getString(R.string.res_male)
                        } else if (bookinginfo.data.userInfo.gender.equals("F")) {
                            txt_gender.text = getString(R.string.res_female)
                        } else {
                            txt_gender.text = getString(R.string.res_na)
                        }

                        txt_email.text = bookinginfo.data.userInfo.email
                        txt_contactno.text = bookinginfo.data.userInfo.contactNumber


                    } else {

                        Toast.makeText(
                            this@BookingInfoActvity,
                            bookinginfo.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (response.code() == AppConfig.URL.TOKEN_EXPIRE) {

                    login()

                } else {

                    Toast.makeText(
                        this@BookingInfoActvity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponceBookinginfoHotel>, @NonNull t: Throwable) {
                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)

                Toast.makeText(
                    this@BookingInfoActvity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    private fun vacationbookingInfo(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))

        viewDialog!!.showDialog()
        utils!!.hideKeyboard()
        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val checkBookingResponce = apiService?.CALL_VACATIONBOOKINGINFO(
            token = token,
            langcode = langCode,
            bookingid = this.bookingID!!,
            bookingtype = this.bookingType!!
        )

        checkBookingResponce?.enqueue(object : Callback<ResponceBookinginfoRecreation> {
            override fun onResponse(@NonNull call: Call<ResponceBookinginfoRecreation>, @NonNull response: Response<ResponceBookinginfoRecreation>) {
                viewDialog!!.hideDialog()
                val bookinginfo = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (bookinginfo!!.status == 1) {

//                        currencyData()
                        lay_hoteltab.visibility = View.GONE
                        lay_eventtab.visibility = View.GONE
                        lay_touttab.visibility = View.GONE
                        lay_vacationtab.visibility = View.VISIBLE

                        lay_daypass.visibility = View.GONE
                        lay_nightpass.visibility = View.GONE
                        lay_noofperson.visibility = View.GONE

                        val gson = Gson()
                        responcebookingInfo = gson.toJson(bookinginfo)

                        helper!!.initPref()
                        helper!!.SaveStringPref(
                            AppConfig.PREFERENCE.VACATIONBOOKINGINFO,
                            responcebookingInfo
                        )
                        helper!!.ApplyPref()


                        Glide.with(this@BookingInfoActvity)
                            .load(bookinginfo.data.recreationData.mainImage)
                            .placeholder(R.drawable.travel)
                            .centerCrop()
                            .into(img_booking)

                        val simpleDateFormat = SimpleDateFormat("EEEE")
                        val df = SimpleDateFormat("dd MMM, yyyy")
                        val c = df.parse(bookinginfo.data.paymentInfo.fromDate)
                        val dayString = simpleDateFormat.format(c)
                        val paymentcurrency =
                            bookinginfo.data.paymentInfo.originalPaymentCurrency + " "
                        Log.e("day ", " " + dayString)

                        if (bookinginfo.data.paymentInfo.dayPassAdultPerson.isNotEmpty()) {


                            if (dayString.equals("Saturday") || dayString.equals("Sunday")) {
                                dayAdultperson =
                                    bookinginfo.data.paymentInfo.dayPassAdultPerson + " " + getString(
                                        R.string.txt_adults
                                    ) + "\n" + getString(R.string.res_weekenddaypass) + "(" +
                                            bookinginfo.data.paymentInfo.dayPassAdultPerson + " X " + paymentcurrency +
                                            bookinginfo.data.paymentInfo.dayPassAdultPersonPrice + " )"
                            } else {

                                dayAdultperson =
                                    bookinginfo.data.paymentInfo.dayPassAdultPerson + " " + getString(
                                        R.string.txt_adults
                                    ) + "\n" + getString(
                                        R.string.res_midweekdaypass
                                    ) + "(" +
                                            bookinginfo.data.paymentInfo.dayPassAdultPerson + " X " + paymentcurrency +
                                            bookinginfo.data.paymentInfo.dayPassAdultPersonPrice + " )"
                            }

                        }

                        if (bookinginfo.data.paymentInfo.dayPassChildPerson.isNotEmpty()) {

                            if (dayAdultperson.isNotEmpty()) {
                                if (dayString.equals("Saturday") || dayString.equals("Sunday")) {
                                    dayAdultperson =
                                        bookinginfo.data.paymentInfo.dayPassAdultPerson + " " + getString(
                                            R.string.txt_adults
                                        ) + "\n" + getString(R.string.res_weekenddaypass) + "" +
                                                bookinginfo.data.paymentInfo.dayPassAdultPerson + " X " + paymentcurrency +
                                                bookinginfo.data.paymentInfo.dayPassAdultPersonPrice + " ), \n" +

                                                bookinginfo.data.paymentInfo.dayPassChildPerson + " " + getString(
                                            R.string.txt_child
                                        ) + "\n" + getString(R.string.res_weekenddaypass) + "(" +
                                                bookinginfo.data.paymentInfo.dayPassChildPerson + " X " + paymentcurrency +
                                                bookinginfo.data.paymentInfo.dayPassChildPersonPrice + " )"
                                } else {
                                    dayAdultperson =
                                        bookinginfo.data.paymentInfo.dayPassAdultPerson + " " + getString(
                                            R.string.txt_adults
                                        ) + "\n" + getString(R.string.res_midweekdaypass) + "(" +
                                                bookinginfo.data.paymentInfo.dayPassAdultPerson + " X " + paymentcurrency +
                                                bookinginfo.data.paymentInfo.dayPassAdultPersonPrice + " ), \n" +

                                                bookinginfo.data.paymentInfo.dayPassChildPerson + " " + getString(
                                            R.string.txt_child
                                        ) + "\n" + getString(R.string.res_midweekdaypass) + "(" +
                                                bookinginfo.data.paymentInfo.dayPassChildPerson + " X " + paymentcurrency +
                                                bookinginfo.data.paymentInfo.dayPassChildPersonPrice + " )"

                                }

                            } else {
                                if (dayString.equals("Saturday") || dayString.equals("Sunday")) {
                                    dayAdultperson =
                                        bookinginfo.data.paymentInfo.dayPassChildPerson + " " + getString(
                                            R.string.txt_child
                                        ) + "\n" + getString(R.string.res_weekenddaypass) + "(" +
                                                bookinginfo.data.paymentInfo.dayPassChildPerson + " X " + paymentcurrency +
                                                bookinginfo.data.paymentInfo.dayPassChildPersonPrice + " )"
                                } else {

                                    dayAdultperson =
                                        bookinginfo.data.paymentInfo.dayPassChildPerson + " " + getString(
                                            R.string.txt_child
                                        ) + "\n" + getString(R.string.res_midweekdaypass) + "(" +
                                                bookinginfo.data.paymentInfo.dayPassChildPerson + " X " + paymentcurrency +
                                                bookinginfo.data.paymentInfo.dayPassChildPersonPrice + " )"
                                }

                            }
                        }

                        if (bookinginfo.data.paymentInfo.nightPassAdultPerson.isNotEmpty()) {
                            if (dayString.equals("Saturday") || dayString.equals("Sunday")) {
                                nightAdultperson =
                                    bookinginfo.data.paymentInfo.nightPassAdultPerson + " " + getString(
                                        R.string.txt_adults
                                    ) + "\n" + getString(R.string.res_weekenddaypass) + "(" +
                                            bookinginfo.data.paymentInfo.nightPassAdultPerson + " X " + paymentcurrency +
                                            bookinginfo.data.paymentInfo.nightPassAdultPersonPrice + " )"
                            } else {

                                nightAdultperson =
                                    bookinginfo.data.paymentInfo.nightPassAdultPerson + " " + getString(
                                        R.string.txt_adults
                                    ) + "\n" + getString(R.string.res_midweekdaypass) + "(" +
                                            bookinginfo.data.paymentInfo.nightPassAdultPerson + " X " + paymentcurrency +
                                            bookinginfo.data.paymentInfo.nightPassAdultPersonPrice + " )"
                            }

                        }

                        if (bookinginfo.data.paymentInfo.nightPassChildPerson.isNotEmpty()) {
                            if (nightAdultperson.isNotEmpty()) {

                                if (dayString.equals("Saturday") || dayString.equals("Sunday")) {
                                    nightAdultperson =
                                        bookinginfo.data.paymentInfo.nightPassAdultPerson + " " + getString(
                                            R.string.txt_adults
                                        ) + "\n" + getString(R.string.res_weekendnightpass) + "(" +
                                                bookinginfo.data.paymentInfo.nightPassAdultPerson + " X " + paymentcurrency +
                                                bookinginfo.data.paymentInfo.nightPassAdultPersonPrice + " ), \n" +

                                                bookinginfo.data.paymentInfo.nightPassChildPerson + " " + getString(
                                            R.string.txt_child
                                        ) + "\n" + getString(R.string.res_weekendnightpass) + "(" +
                                                bookinginfo.data.paymentInfo.nightPassChildPerson + " X " + paymentcurrency +
                                                bookinginfo.data.paymentInfo.nightPassChildPersonPrice + " )"
                                } else {
                                    nightAdultperson =
                                        bookinginfo.data.paymentInfo.nightPassAdultPerson + " " + getString(
                                            R.string.txt_adults
                                        ) + "\n" + getString(R.string.res_midweeknightpass) + "(" +
                                                bookinginfo.data.paymentInfo.nightPassAdultPerson + " X " + paymentcurrency +
                                                bookinginfo.data.paymentInfo.nightPassAdultPersonPrice + " ), \n" +

                                                bookinginfo.data.paymentInfo.nightPassChildPerson + " " + getString(
                                            R.string.txt_child
                                        ) + "\n" + getString(R.string.res_midweeknightpass) + "(" +
                                                bookinginfo.data.paymentInfo.nightPassChildPerson + " X " + paymentcurrency +
                                                bookinginfo.data.paymentInfo.nightPassChildPersonPrice + " )"

                                }


                            } else {

                                if (dayString.equals("Saturday") || dayString.equals("Sunday")) {
                                    nightAdultperson =
                                        bookinginfo.data.paymentInfo.nightPassChildPerson + " " + getString(
                                            R.string.txt_child
                                        ) + "\n" + getString(R.string.res_weekendnightpass) + "(" +
                                                bookinginfo.data.paymentInfo.nightPassChildPerson + " X " + paymentcurrency +
                                                bookinginfo.data.paymentInfo.nightPassChildPersonPrice + " )"
                                } else {

                                    nightAdultperson =
                                        bookinginfo.data.paymentInfo.nightPassChildPerson + " " + getString(
                                            R.string.txt_child
                                        ) + "\n" + getString(R.string.res_midweeknightpass) + "(" +
                                                bookinginfo.data.paymentInfo.nightPassChildPerson + " X " + paymentcurrency +
                                                bookinginfo.data.paymentInfo.nightPassChildPersonPrice + " )"
                                }

                            }

                        }

                        if (dayAdultperson.isNotEmpty()) {

                            lay_daypass.visibility = View.VISIBLE
                            txt_daypass.visibility = View.VISIBLE
                            txt_daypass.text = dayAdultperson
                        } else {
                            lay_daypass.visibility = View.GONE
                            txt_daypass.visibility = View.GONE
                        }

                        if (nightAdultperson.isNotEmpty()) {

                            lay_nightpass.visibility = View.VISIBLE
                            txt_nightpass.visibility = View.VISIBLE
                            txt_nightpass.text = nightAdultperson
                        } else {
                            lay_nightpass.visibility = View.GONE
                            txt_nightpass.visibility = View.GONE
                        }

                        toutTitle = bookinginfo.data.recreationData.placeName

                        txt_itemanme.text = bookinginfo.data.recreationData.placeName
                        txt_itemlocation.text =
                            bookinginfo.data.recreationData.city + ", " + bookinginfo.data.recreationData.district + ", " + bookinginfo.data.recreationData.country

                        txt_labeldate.text = getString(R.string.str_vationdate)
                        txt_date.text = bookinginfo.data.paymentInfo.fromDate
                        txt_noofperson.text = bookinginfo.data.paymentInfo.noOfPerson.toString()
                        txt_bookingdate.text = bookinginfo.data.paymentInfo.createdAt
                        txt_bookingid.text = bookinginfo.data.paymentInfo.bookingId

                        txt_totalprice.text =
                            bookinginfo.data.paymentInfo.originalPaymentCurrency + " " + bookinginfo.data.paymentInfo.originalPaymentPrice
                        txt_paymenttype.text = bookinginfo.data.paymentInfo.paymentType

                        if (bookinginfo.data.userInfo.title.equals("Mr")) {

                            txt_name.text =
                                getString(R.string.mr) + " " + bookinginfo.data.userInfo.firstName + " " + bookinginfo.data.userInfo.lastName

                        } else if (bookinginfo.data.userInfo.title.equals("Mrs")) {

                            txt_name.text =
                                getString(R.string.res_mrs) + " " + bookinginfo.data.userInfo.firstName + " " + bookinginfo.data.userInfo.lastName

                        } else if (bookinginfo.data.userInfo.title.equals("Miss")) {

                            txt_name.text =
                                getString(R.string.res_miss) + " " + bookinginfo.data.userInfo.firstName + " " + bookinginfo.data.userInfo.lastName
                        }


                        txt_dob.text = bookinginfo.data.userInfo.dob
                        if (bookinginfo.data.userInfo.gender.equals("M")) {
                            txt_gender.text = getString(R.string.res_male)
                        } else if (bookinginfo.data.userInfo.gender.equals("F")) {
                            txt_gender.text = getString(R.string.res_female)
                        } else {
                            txt_gender.text = getString(R.string.res_na)
                        }

                        txt_email.text = bookinginfo.data.userInfo.email
                        txt_contactno.text = bookinginfo.data.userInfo.contactNumber


                    } else {

                        Toast.makeText(
                            this@BookingInfoActvity,
                            bookinginfo.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (response.code() == AppConfig.URL.TOKEN_EXPIRE) {

                    login()

                } else {

                    Toast.makeText(
                        this@BookingInfoActvity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponceBookinginfoRecreation>, @NonNull t: Throwable) {
                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)

                Toast.makeText(
                    this@BookingInfoActvity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


}
