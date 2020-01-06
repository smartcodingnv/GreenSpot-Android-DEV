package com.greenspot.app.activity

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.greenspot.app.R
import com.greenspot.app.adapter.BookDetailsAdapter
import com.greenspot.app.adapter.CustomAdapter
import com.greenspot.app.model.ListBookingDetails
import com.greenspot.app.network.ApiClient
import com.greenspot.app.network.ApiInterface
import com.greenspot.app.responce.ComanResponce
import com.greenspot.app.responce.currencyconvert.CurrencyConvert
import com.greenspot.app.responce.idealpayment.ResponceIdealPayment
import com.greenspot.app.responce.paymentmollie.ResponcePaymentMollie
import com.greenspot.app.utils.*
import hk.ids.gws.android.sclick.SClick
import kotlinx.android.synthetic.main.activity_booking_details.*
import kotlinx.android.synthetic.main.content_booking_details.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BookingDetailsActivity : AppCompatActivity() {

    private var paymnetID: String = ""
    private var final_eur_price: String = ""
    private var transcationID: String = ""
    private lateinit var editModel: ListBookingDetails

    private var totalguest: Int = 0
    private var bookingType: String = ""

    var placeTitle: ArrayList<ListBookingDetails> = ArrayList()

    private var progress: Progress? = null
    private var viewDialog: ViewDialog? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null
    private var currncyCode: String? = ""
    private var countryID: String? = ""
    private var langCode: String? = ""
    private var final_price: String? = ""
    private var token: String = ""
    private var itemID: String = ""
    private var single_price: String? = ""
    private var persons: String = ""
    private var formDate: String? = ""
    private var no_of_person: String? = ""


    private var mid_week_day_pass_adult_price: String? = ""
    private var mid_week_day_pass_adult_nop: String? = ""

    private var mid_week_day_pass_child_price: String? = ""
    private var mid_week_day_pass_child_nop: String? = ""


    private var mid_week_night_pass_adult_price: String? = ""
    private var mid_week_night_pass_adult_nop: String? = ""

    private var mid_week_night_pass_child_price: String? = ""
    private var mid_week_night_pass_child_nop: String? = ""


    private var weekend_day_pass_adult_price: String? = ""
    private var weekend_day_pass_adult_nop: String? = ""

    private var weekend_day_pass_child_price: String? = ""
    private var weekend_day_pass_child_nop: String? = ""


    private var weekend_night_pass_adult_price: String? = ""
    private var weekend_night_pass_adult_nop: String? = ""

    private var weekend_night_pass_child_price: String? = ""
    private var weekend_night_pass_child_nop: String? = ""



    private lateinit var jsonOBject: JSONObject


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)

        progress = Progress(this)
        viewDialog = ViewDialog(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)
        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")
        token = "Bearer " + helper!!.LoadStringPref(AppConfig.PREFERENCE.AUTHTOKEN, "")
        itemID = helper!!.LoadStringPref(AppConfig.PREFERENCE.PLACEID, "")!!  //tourid

        val intent = getIntent();
        totalguest = intent.getIntExtra(AppConfig.EXTRA.TOTALGUEST, 0)
        bookingType = intent.getStringExtra(AppConfig.EXTRA.TYPE)


        if (bookingType.equals("Tour")) {

            final_price = helper!!.LoadStringPref(AppConfig.PREFERENCE.TOUR_FINAL_PRICE, "")
            single_price = helper!!.LoadStringPref(AppConfig.PREFERENCE.TOUR_START_PRICE, "")
            formDate = helper!!.LoadStringPref(AppConfig.PREFERENCE.TOUR_FROM_DATE, "")
            no_of_person = helper!!.LoadStringPref(AppConfig.PREFERENCE.TOUR_NO_OF_PERSON, "")

        } else if (bookingType.equals("Event")) {

            final_price = helper!!.LoadStringPref(AppConfig.PREFERENCE.EVENT_FINAL_PRICE, "")
            single_price = helper!!.LoadStringPref(AppConfig.PREFERENCE.EVENT_START_PRICE, "")
            formDate = helper!!.LoadStringPref(AppConfig.PREFERENCE.EVENT_FROM_DATE, "")
            no_of_person = helper!!.LoadStringPref(AppConfig.PREFERENCE.EVENT_NO_OF_PERSON, "")

        }else if(bookingType.equals("Recreation")){

            final_price = helper!!.LoadStringPref(AppConfig.PREFERENCE.RECREATION_FINAL_PRICE, "")
            formDate = helper!!.LoadStringPref(AppConfig.PREFERENCE.RECREATION_FROM_DATE, "")

            mid_week_day_pass_adult_price = helper!!.LoadStringPref(AppConfig.PREFERENCE.mid_week_day_pass_adult_price, "")
            mid_week_day_pass_adult_nop = helper!!.LoadStringPref(AppConfig.PREFERENCE.mid_week_day_pass_adult_nop, "")

            mid_week_day_pass_child_price = helper!!.LoadStringPref(AppConfig.PREFERENCE.mid_week_day_pass_child_price, "")
            mid_week_day_pass_child_nop = helper!!.LoadStringPref(AppConfig.PREFERENCE.mid_week_day_pass_child_nop, "")

            mid_week_night_pass_adult_price = helper!!.LoadStringPref(AppConfig.PREFERENCE.mid_week_night_pass_adult_price, "")
            mid_week_night_pass_adult_nop = helper!!.LoadStringPref(AppConfig.PREFERENCE.mid_week_night_pass_adult_nop, "")

            mid_week_night_pass_child_price = helper!!.LoadStringPref(AppConfig.PREFERENCE.mid_week_night_pass_child_price, "")
            mid_week_night_pass_child_nop = helper!!.LoadStringPref(AppConfig.PREFERENCE.mid_week_night_pass_child_nop, "")

            weekend_day_pass_adult_price = helper!!.LoadStringPref(AppConfig.PREFERENCE.weekend_day_pass_adult_price, "")
            weekend_day_pass_adult_nop = helper!!.LoadStringPref(AppConfig.PREFERENCE.weekend_day_pass_adult_nop, "")

            weekend_day_pass_child_price = helper!!.LoadStringPref(AppConfig.PREFERENCE.weekend_day_pass_child_price, "")
            weekend_day_pass_child_nop = helper!!.LoadStringPref(AppConfig.PREFERENCE.weekend_day_pass_child_nop, "")

            weekend_night_pass_adult_price = helper!!.LoadStringPref(AppConfig.PREFERENCE.weekend_night_pass_adult_price, "")
            weekend_night_pass_adult_nop = helper!!.LoadStringPref(AppConfig.PREFERENCE.weekend_night_pass_adult_nop, "")

            weekend_night_pass_child_price = helper!!.LoadStringPref(AppConfig.PREFERENCE.weekend_night_pass_child_price, "")
            weekend_night_pass_child_nop = helper!!.LoadStringPref(AppConfig.PREFERENCE.weekend_night_pass_child_nop, "")




        }




        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        placeTitle.clear()
        val bookingDetailsAdapter = BookDetailsAdapter(this)
        populateList()
        Common.setVerticalRecyclerView(this, recycler)
        placeTitle[0].setExpanded(true)
        placeTitle[0].first_name = helper!!.LoadStringPref(AppConfig.PREFERENCE.USER_FNAME, "")!!
        placeTitle[0].last_name = helper!!.LoadStringPref(AppConfig.PREFERENCE.USER_LNAME, "")!!
        placeTitle[0].email = helper!!.LoadStringPref(AppConfig.PREFERENCE.USER_EMAIL, "")!!
        placeTitle[0].contact_number = helper!!.LoadStringPref(AppConfig.PREFERENCE.USER_CONTACTNO, "")!!

        bookingDetailsAdapter.swapData(placeTitle)
        recycler.adapter = bookingDetailsAdapter




        btn_confirm.setOnClickListener(View.OnClickListener {

            /*            for (name in placeTitle) {

                            if (name.age.isEmpty()) {
                                Toast.makeText(
                                    applicationContext,
                                    "enter the age",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@OnClickListener
                            }
                        }*/

            for ((index, value) in placeTitle.withIndex()) {
                println("$index: $value")
                Log.e("booking", "" + index)


                if (index == 0) {

                    if (value.first_name.isEmpty()) {
                        Toast.makeText(
                            applicationContext,
                            "Please enter first name of " + value.person,
                            Toast.LENGTH_SHORT
                        ).show()

                        return@OnClickListener
                    }

                    if (value.last_name.isEmpty()) {
                        Toast.makeText(
                            applicationContext,
                            "Please enter last name of " + value.person,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@OnClickListener
                    }

                    if (value.email.isEmpty()) {

                        Toast.makeText(
                            applicationContext,
                            "Please enter email of " + value.person,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@OnClickListener
                    }
                    if (value.dob.isEmpty()) {

                        Toast.makeText(
                            applicationContext,
                            "Please select date of birth of " + value.person,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@OnClickListener
                    }

                    if (value.gender.isEmpty()) {

                        Toast.makeText(
                            applicationContext,
                            "Please select gender of " + value.person,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@OnClickListener
                    }

                    if (value.contact_number.isEmpty()) {

                        Toast.makeText(
                            applicationContext,
                            "Please enter contact no of " + value.person,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@OnClickListener

                    }

                    if (!isValidEmail(value.email)) {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.res_validemail),
                            Toast.LENGTH_SHORT
                        ).show()

                        return@OnClickListener
                    }

                }


            }


            val gson = Gson()
            val listString = gson.toJson(
                placeTitle,
                object : TypeToken<ArrayList<ListBookingDetails?>?>() {}.type
            )

            persons = listString
            Log.e("Booking", " " + listString)
//            helper!!.initPref()
//            helper!!.SaveStringPref(AppConfig.PREFERENCE.PERSONDETAILS, listString)
//            helper!!.ApplyPref()


            alertProcessPayment("Please wait and don't close app while we are redirecting you to payment gateway.")


//            currencyConvertData(this.final_price!!, this.currncyCode!!, this.langCode!!)


//            startActivity(Intent(this, PaymentActivity::class.java))


        })


    }

    private fun alertProcessPayment(message: String) {
        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage(message)
        builder1.setCancelable(false)

        builder1.setPositiveButton(
            "Ok",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()

                if (bookingType.equals("Tour")) {

                    checkBooking(
                        contryID = this.countryID!!,
                        selectCurrency = this.currncyCode!!,
                        langCode = this.langCode!!,
                        masterTyppe = "T"

                    )


                } else if (bookingType.equals("Event")) {

                    if (final_price == "") {

                        eventfreesendPaymnetData(
                            contryID = this.countryID!!,
                            langCode = this.langCode!!,
                            selectCurrency = this.currncyCode!!
                        )

                    } else {

                        checkBooking(
                            contryID = this.countryID!!,
                            selectCurrency = this.currncyCode!!,
                            langCode = this.langCode!!,
                            masterTyppe = "E"
                        )

                    }


                }else if(bookingType.equals("Recreation")){


                    checkBookingRecreation(
                        contryID = this.countryID!!,
                        selectCurrency = this.currncyCode!!,
                        langCode = this.langCode!!,
                        masterTyppe = "R"
                    )
                }


            })


        val alert11 = builder1.create()
        alert11.show()

    }


    private fun populateList() {

        for (i in 1..totalguest) {


            editModel = ListBookingDetails(
                person = "Person " + i,
                contact_number = "",
                email = "",
                id = "",
                gender = "",
                dob = "",
                first_name = "",
                last_name = "",
                title = ""
            )
            placeTitle.add(editModel)
        }

    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }


    private fun checkBooking(
        contryID: String,
        selectCurrency: String,
        langCode: String,
        masterTyppe: String
    ) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
        viewDialog!!.showDialog()
        utils!!.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val checkBookingResponce = apiService?.CALL_CHECKBEFOREPAY(
            token = token,
            langcode = langCode,
            tourid = itemID,
            selectCurrency = selectCurrency,
            masterType = masterTyppe,
            singleprice = this.single_price!!

        )

        checkBookingResponce?.enqueue(object : Callback<ComanResponce> {
            override fun onResponse(@NonNull call: Call<ComanResponce>, @NonNull response: Response<ComanResponce>) {
                viewDialog!!.hideDialog()

                val checkbookingcall = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (checkbookingcall!!.status == 1) {

                        currencyData()


                    } else {

                        alertbooking(checkbookingcall.message)
//                        Toast.makeText(
//                            this@ListPlaceActivity,
//                            tourDetails.message,
//                            Toast.LENGTH_SHORT
//                        ).show()

                    }
                } else if (response.code() == AppConfig.URL.TOKEN_EXPIRE) {

                    login()

                }else {
                    Toast.makeText(
                        this@BookingDetailsActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ComanResponce>, @NonNull t: Throwable) {

                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)

                Toast.makeText(
                    this@BookingDetailsActivity,
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

    private fun checkBookingRecreation(
        contryID: String,
        selectCurrency: String,
        langCode: String,
        masterTyppe: String
    ) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
        viewDialog!!.showDialog()
        utils!!.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val checkBookingResponce = apiService?.CALL_CHECKBEFOREPAYRECREATION(
            token = token,
            langcode = langCode,
            tourid = itemID,
            selectCurrency = selectCurrency,
            masterType = masterTyppe,
            singleprice = this.single_price!!,
            mid_week_day_pass_adult = this.mid_week_day_pass_adult_price!!,
            mid_week_day_pass_child = mid_week_day_pass_child_price!!,
            mid_week_night_pass_adult = mid_week_night_pass_adult_price!!,
            mid_week_night_pass_child = mid_week_night_pass_child_price!!,
            weekend_day_pass_adult = weekend_day_pass_adult_price!!,
            weekend_day_pass_child = weekend_day_pass_child_price!!,
            weekend_night_pass_adult = weekend_night_pass_adult_price!!,
            weekend_night_pass_child = weekend_night_pass_child_price!!


        )

        checkBookingResponce?.enqueue(object : Callback<ComanResponce> {
            override fun onResponse(@NonNull call: Call<ComanResponce>, @NonNull response: Response<ComanResponce>) {
                viewDialog!!.hideDialog()

                val checkbookingcall = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (checkbookingcall!!.status == 1) {

                        currencyData()


                    } else {

                        alertbooking(checkbookingcall.message)
//                        Toast.makeText(
//                            this@ListPlaceActivity,
//                            tourDetails.message,
//                            Toast.LENGTH_SHORT
//                        ).show()

                    }
                } else if (response.code() == AppConfig.URL.TOKEN_EXPIRE) {

                    login()

                }else {
                    Toast.makeText(
                        this@BookingDetailsActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ComanResponce>, @NonNull t: Throwable) {

                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)

                Toast.makeText(
                    this@BookingDetailsActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun currencyData() {

        currencyConvertData(this.final_price!!, this.currncyCode!!, this.langCode!!)

    }

    private fun alertbooking(message: String) {
        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage(message)
        builder1.setCancelable(false)

        builder1.setPositiveButton(
            "Ok",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                startActivity(
                    Intent(this, HomeActivity::class.java)

                )
                finishAffinity()

            })


        val alert11 = builder1.create()
        alert11.show()

    }


    private fun currencyConvertData(price: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))

        utils!!.hideKeyboard()


        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val currencyConvert = apiService?.CURRENCY_CONVERT(
            price,
            selectCurrency,
            "EUR",
            langCode

        )

        currencyConvert?.enqueue(object : Callback<CurrencyConvert> {

            override fun onResponse(@NonNull call: Call<CurrencyConvert>, @NonNull response: Response<CurrencyConvert>) {


                val currencyConvertdata = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (currencyConvertdata!!.status == 1) {

                        final_eur_price = currencyConvertdata.data.convertPrice

                        val json = JSONObject()
                        json.put("currency", "EUR")
                        json.put("value", final_eur_price)
                        jsonOBject = JSONObject()
                        jsonOBject.put("amount", json)
                        jsonOBject.put("description", "Order #12345")
                        jsonOBject.put(
                            "redirectUrl",
                            "com.greenspot.app://post-authentication-return-url?keyOne=123456"
//                                    app://ndp.myteamspace.org
                        )
                        jsonOBject.put(
                            "webhookUrl",
                            "https://webshop.example.org/payments/webhook/"
                        )

                        Log.e("paymnet", "jsonOBject " + jsonOBject.toString())

                        sendIdealPaymnetData()


                    } else {

                        Toast.makeText(
                            this@BookingDetailsActivity,
                            currencyConvertdata.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {

                    Toast.makeText(
                        this@BookingDetailsActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<CurrencyConvert>, @NonNull t: Throwable) {

                viewDialog!!.hideDialog()
                Log.e("fail", " " + t.message)
                Toast.makeText(
                    this@BookingDetailsActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun sendIdealPaymnetData() {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))

        utils!!.hideKeyboard()


        val apiService = ApiClient.client2?.create(ApiInterface::class.java)
        val responcePlaceReview = apiService?.PAYMENTBBODY_CALL(
            token = "Bearer test_QUnmcf4JQEFQGk3d5BDGwNy5NcWvdh",
            jsonBody = jsonOBject.toString()
//          jsonBody = jsonOBject
        )

        responcePlaceReview?.enqueue(object : Callback<ResponceIdealPayment> {
            override fun onResponse(@NonNull call: Call<ResponceIdealPayment>, @NonNull response: Response<ResponceIdealPayment>) {
                viewDialog!!.hideDialog()

                Log.e("ideal", " " + response.code())

                if (response.code() == 201 || response.code() == 200) {

                    val paymentSessionData = response.body()
                    val redirectUrl = paymentSessionData!!.Links.checkout.href
                    if (redirectUrl.isEmpty()) {
                        Toast.makeText(
                            this@BookingDetailsActivity,
                            "Try with again",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

//                    token = token,
//                    contryID = contryID,
//                    selectCurrency = "EUR",
//                    langcode = langCode,
//                    person = jsonArray,
//                    tourid = tourID,
//                    formDate = this.formDate!!,
//                    noofperson = this.no_of_person!!,
//                    singlePrice = final_eur_price,
//                    tanstationID = transcationID,
//                    originalPaymentcurrency = selectCurrency,
//                    originalPaymentprice = this.final_price!!,
//                    originalsingleprice = this.single_price!!


                    transcationID = paymentSessionData.id

                    helper!!.initPref()
                    helper!!.SaveIntPref(AppConfig.PREFERENCE.CHECKPAYMENT, 1)
                    helper!!.SaveStringPref(AppConfig.PREFERENCE.PERSONDETAILS, persons)
                    helper!!.SaveStringPref(AppConfig.PREFERENCE.FINALEURPRICE, final_eur_price)
                    helper!!.SaveStringPref(AppConfig.PREFERENCE.TRANSACTIONID, transcationID)
                    helper!!.ApplyPref()

                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl)).putExtra(
                        "checkkk",
                        "123"
                    )
                    startActivity(browserIntent)

                }

            }

            override fun onFailure(@NonNull call: Call<ResponceIdealPayment>, @NonNull t: Throwable) {
                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)
                Log.e("fail", " " + t.localizedMessage)
                Log.e("fail", " " + t.stackTrace)
                Toast.makeText(
                    this@BookingDetailsActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()

        Log.e("onResume ", "   checkkk")
        Log.e(
            "onResume ",
            "   checkkk" + helper!!.LoadIntPref(AppConfig.PREFERENCE.CHECKPAYMENT, 0)
        )


        val intent = intent
        if (Intent.ACTION_VIEW == intent.action) {
            val uri = intent.data
            val valueOne = uri!!.getQueryParameter("keyOne")
            Log.e("onResume ", "   checkkk" + valueOne)

        }



        if (helper!!.LoadIntPref(AppConfig.PREFERENCE.CHECKPAYMENT, 0) == 1) {


            helper!!.initPref()
            helper!!.SaveIntPref(AppConfig.PREFERENCE.CHECKPAYMENT, 0)
            helper!!.ApplyPref()

            if (bookingType.equals("Tour")) {
                toursendPaymnetData(
                    contryID = this.countryID!!,
                    langCode = this.langCode!!,
                    selectCurrency = this.currncyCode!!
                )


            } else if (bookingType.equals("Event")) {

                eventsendPaymnetData(
                    contryID = this.countryID!!,
                    langCode = this.langCode!!,
                    selectCurrency = this.currncyCode!!
                )

            }else if(bookingType.equals("Recreation")){
                recreationsendPaymnetData(
                    contryID = this.countryID!!,
                    langCode = this.langCode!!,
                    selectCurrency = this.currncyCode!!
                )

            }


        }


    }

    override fun onDestroy() {
        super.onDestroy()

        helper!!.initPref()
        helper!!.SaveIntPref(AppConfig.PREFERENCE.CHECKPAYMENT, 0)
        helper!!.ApplyPref()

    }


    private fun toursendPaymnetData(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
        viewDialog!!.showDialog()

        utils!!.hideKeyboard()
        val jsonArray = JSONArray(helper!!.LoadStringPref(AppConfig.PREFERENCE.PERSONDETAILS, ""))
        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val responcePlaceReview = apiService?.TOURPAYMENTMOLIE_CALL(
            token = token,
            contryID = contryID,
            selectCurrency = "EUR",
            langcode = langCode,
            person = jsonArray,
            tourid = itemID,
            formDate = this.formDate!!,
            noofperson = this.no_of_person!!,
            singlePrice = helper!!.LoadStringPref(AppConfig.PREFERENCE.FINALEURPRICE, "")!!,
            tanstationID = helper!!.LoadStringPref(AppConfig.PREFERENCE.TRANSACTIONID, "")!!,
            originalPaymentcurrency = selectCurrency,
            originalPaymentprice = this.final_price!!,
            originalsingleprice = this.single_price!!

        )

        Log.e("token", token)
        Log.e("contryID", contryID)
        Log.e("selectCurrency", selectCurrency)
        Log.e("langcode", langCode)
        Log.e("person", jsonArray.toString())
        Log.e("tourid", itemID)
        Log.e("finalPrice", this.final_price!!)
        Log.e("formDate", this.formDate!!)
        Log.e("noofperson", this.no_of_person!!)
        Log.e("singlePrice", this.single_price!!)
        Log.e("tanstationID", helper!!.LoadStringPref(AppConfig.PREFERENCE.TRANSACTIONID, "")!!)


        responcePlaceReview?.enqueue(object : Callback<ResponcePaymentMollie> {

            override fun onResponse(@NonNull call: Call<ResponcePaymentMollie>, @NonNull response: Response<ResponcePaymentMollie>) {
                viewDialog!!.hideDialog()

                val postreivewData = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {

                    if (postreivewData!!.status == 1) {

                        paymnetID = postreivewData.data.id
                        alertsuccessbooking(postreivewData.message)

//                        Toast.makeText(
//                            this@BookingDetailsActivity,
//                            postreivewData.message,
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                        startActivity(Intent(this@BookingDetailsActivity, HomeActivity::class.java))
//                        finish()


                    } else {

                        Toast.makeText(
                            this@BookingDetailsActivity,
                            postreivewData.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }else if (response.code() == AppConfig.URL.TOKEN_EXPIRE) {

                    login()

                } else {

                    Log.e("postreivewData", " error" + response.errorBody())
                    Log.e("postreivewData", " error" + response.body().toString())
                    Log.e("postreivewData", " error" + response.code())

                    Toast.makeText(
                        this@BookingDetailsActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponcePaymentMollie>, @NonNull t: Throwable) {

                viewDialog!!.hideDialog()
                Log.e("fail", " " + t.message)
                Toast.makeText(
                    this@BookingDetailsActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    private fun eventfreesendPaymnetData(
        contryID: String,
        selectCurrency: String,
        langCode: String
    ) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
        viewDialog!!.showDialog()

        utils!!.hideKeyboard()
        val jsonArray = JSONArray(persons)
        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val responcePlaceReview = apiService?.EVENTPAYMENTMOLIE_CALL(
            token = token,
            contryID = contryID,
            selectCurrency = "",
            langcode = langCode,
            person = jsonArray,
            tourid = itemID,
            formDate = this.formDate!!,
            noofperson = this.no_of_person!!,
            singlePrice = "",
            tanstationID = "",
            originalPaymentcurrency = "",
            originalPaymentprice = "",
            originalsingleprice = ""

        )

        Log.e("token", token)
        Log.e("contryID", contryID)
        Log.e("selectCurrency", selectCurrency)
        Log.e("langcode", langCode)
        Log.e("person", jsonArray.toString())
        Log.e("eventid", itemID)
        Log.e("finalPrice", this.final_price!!)
        Log.e("formDate", this.formDate!!)
        Log.e("noofperson", this.no_of_person!!)
        Log.e("singlePrice", this.single_price!!)
        Log.e("tanstationID", helper!!.LoadStringPref(AppConfig.PREFERENCE.TRANSACTIONID, "")!!)


        responcePlaceReview?.enqueue(object : Callback<ResponcePaymentMollie> {

            override fun onResponse(@NonNull call: Call<ResponcePaymentMollie>, @NonNull response: Response<ResponcePaymentMollie>) {
                viewDialog!!.hideDialog()

                val postreivewData = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {

                    if (postreivewData!!.status == 1) {

                        paymnetID = postreivewData.data.id
                        alertsuccessbooking(postreivewData.message)

//                        Toast.makeText(
//                            this@BookingDetailsActivity,
//                            postreivewData.message,
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                        startActivity(Intent(this@BookingDetailsActivity, HomeActivity::class.java))
//                        finish()


                    } else {

                        Toast.makeText(
                            this@BookingDetailsActivity,
                            postreivewData.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }else if (response.code() == AppConfig.URL.TOKEN_EXPIRE) {

                    login()

                } else {

                    Log.e("postreivewData", " error" + response.errorBody())
                    Log.e("postreivewData", " error" + response.body().toString())
                    Log.e("postreivewData", " error" + response.code())

                    Toast.makeText(
                        this@BookingDetailsActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponcePaymentMollie>, @NonNull t: Throwable) {

                viewDialog!!.hideDialog()
                Log.e("fail", " " + t.message)
                Toast.makeText(
                    this@BookingDetailsActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    private fun eventsendPaymnetData(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
        viewDialog!!.showDialog()

        utils!!.hideKeyboard()
        val jsonArray = JSONArray(helper!!.LoadStringPref(AppConfig.PREFERENCE.PERSONDETAILS, ""))
        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val responcePlaceReview = apiService?.EVENTPAYMENTMOLIE_CALL(
            token = token,
            contryID = contryID,
            selectCurrency = "EUR",
            langcode = langCode,
            person = jsonArray,
            tourid = itemID,
            formDate = this.formDate!!,
            noofperson = this.no_of_person!!,
            singlePrice = helper!!.LoadStringPref(AppConfig.PREFERENCE.FINALEURPRICE, "")!!,
            tanstationID = helper!!.LoadStringPref(AppConfig.PREFERENCE.TRANSACTIONID, "")!!,
            originalPaymentcurrency = selectCurrency,
            originalPaymentprice = this.final_price!!,
            originalsingleprice = this.single_price!!

        )

        Log.e("token", token)
        Log.e("contryID", contryID)
        Log.e("selectCurrency", selectCurrency)
        Log.e("langcode", langCode)
        Log.e("person", jsonArray.toString())
        Log.e("eventid", itemID)
        Log.e("finalPrice", this.final_price!!)
        Log.e("formDate", this.formDate!!)
        Log.e("noofperson", this.no_of_person!!)
        Log.e("singlePrice", this.single_price!!)
        Log.e("tanstationID", helper!!.LoadStringPref(AppConfig.PREFERENCE.TRANSACTIONID, "")!!)


        responcePlaceReview?.enqueue(object : Callback<ResponcePaymentMollie> {

            override fun onResponse(@NonNull call: Call<ResponcePaymentMollie>, @NonNull response: Response<ResponcePaymentMollie>) {
                viewDialog!!.hideDialog()

                val postreivewData = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {

                    if (postreivewData!!.status == 1) {

                        paymnetID = postreivewData.data.id
                        alertsuccessbooking(postreivewData.message)

//                        Toast.makeText(
//                            this@BookingDetailsActivity,
//                            postreivewData.message,
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                        startActivity(Intent(this@BookingDetailsActivity, HomeActivity::class.java))
//                        finish()


                    } else {

                        Toast.makeText(
                            this@BookingDetailsActivity,
                            postreivewData.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else if (response.code() == AppConfig.URL.TOKEN_EXPIRE) {

                    login()

                }else {

                    Log.e("postreivewData", " error" + response.errorBody())
                    Log.e("postreivewData", " error" + response.body().toString())
                    Log.e("postreivewData", " error" + response.code())

                    Toast.makeText(
                        this@BookingDetailsActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponcePaymentMollie>, @NonNull t: Throwable) {

                viewDialog!!.hideDialog()
                Log.e("fail", " " + t.message)
                Toast.makeText(
                    this@BookingDetailsActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    private fun recreationsendPaymnetData(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
        viewDialog!!.showDialog()

        utils!!.hideKeyboard()
        val jsonArray = JSONArray(helper!!.LoadStringPref(AppConfig.PREFERENCE.PERSONDETAILS, ""))
        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val responcePlaceReview = apiService?.RECREATIONPAYMENTMOLIE_CALL(
            token = token,
            contryID = contryID,
            selectCurrency = "EUR",
            originalPaymentcurrency = selectCurrency,
            langcode = langCode,
            person = jsonArray,
            tourid = itemID,
            formDate = this.formDate!!,
            tanstationID = helper!!.LoadStringPref(AppConfig.PREFERENCE.TRANSACTIONID, "")!!,
            midweekdayadultpass = mid_week_day_pass_adult_price!!,
            midweekdayadultpassnop = mid_week_day_pass_adult_nop!!,
            midweekdaychildpass = mid_week_day_pass_child_price!!,
            midweekdaychildpassnop = mid_week_day_pass_child_nop!!,
            midweekNightadultpass = mid_week_night_pass_adult_price!!,
            midweekNightadultpassnop = mid_week_night_pass_adult_nop!!,
            midweekNightchildpass = mid_week_night_pass_child_price!!,
            midweekNightchildpassnop = mid_week_night_pass_child_nop!!,
            weekenddayadultpass = weekend_day_pass_adult_price!!,
            weekenddayadultpassnop = weekend_day_pass_adult_nop!!,
            weekenddaychildpass = weekend_day_pass_child_price!!,
            weekenddaychildpassnop = weekend_day_pass_child_nop!!,
            weekendNightadultpass = weekend_night_pass_adult_price!!,
            weekendNightadultpassnop = weekend_night_pass_adult_nop!!,
            weekendNightchildpass = weekend_night_pass_child_price!!,
            weekendNightchildpassnop = weekend_night_pass_child_nop!!
        )

        Log.e("token", token)
        Log.e("contryID", contryID)
        Log.e("selectCurrency", selectCurrency)
        Log.e("langcode", langCode)
        Log.e("person", jsonArray.toString())
        Log.e("eventid", itemID)
        Log.e("formDate", this.formDate!!)
        Log.e("tanstationID", helper!!.LoadStringPref(AppConfig.PREFERENCE.TRANSACTIONID, "")!!)


        responcePlaceReview?.enqueue(object : Callback<ResponcePaymentMollie> {

            override fun onResponse(@NonNull call: Call<ResponcePaymentMollie>, @NonNull response: Response<ResponcePaymentMollie>) {
                viewDialog!!.hideDialog()

                val postreivewData = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {

                    if (postreivewData!!.status == 1) {

                        paymnetID = postreivewData.data.id
                        alertsuccessbooking(postreivewData.message)

//                        Toast.makeText(
//                            this@BookingDetailsActivity,
//                            postreivewData.message,
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                        startActivity(Intent(this@BookingDetailsActivity, HomeActivity::class.java))
//                        finish()


                    } else {

                        Toast.makeText(
                            this@BookingDetailsActivity,
                            postreivewData.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }else if (response.code() == AppConfig.URL.TOKEN_EXPIRE) {

                    login()

                } else {

                    Log.e("postreivewData", " error" + response.errorBody())
                    Log.e("postreivewData", " error" + response.body().toString())
                    Log.e("postreivewData", " error" + response.code())

                    Toast.makeText(
                        this@BookingDetailsActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponcePaymentMollie>, @NonNull t: Throwable) {

                viewDialog!!.hideDialog()
                Log.e("fail", " " + t.message)
                Toast.makeText(
                    this@BookingDetailsActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun alertsuccessbooking(message: String) {
        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage(message)
        builder1.setCancelable(false)

        builder1.setPositiveButton(
            "Ok",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                if (bookingType.equals("Tour")) {

                    startActivity(
                        Intent(this, BookingInfoActvity::class.java)
                            .putExtra(AppConfig.EXTRA.BOOKINGID, paymnetID)
                            .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)
                            .putExtra(AppConfig.EXTRA.CHECKBOOKINGINFO, 1)
                    )
                    finishAffinity()

                } else if (bookingType.equals("Event")) {

                    startActivity(
                        Intent(this, BookingInfoActvity::class.java)
                            .putExtra(AppConfig.EXTRA.BOOKINGID, paymnetID)
                            .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)
                            .putExtra(AppConfig.EXTRA.CHECKBOOKINGINFO, 1)
                    )
                    finishAffinity()
                }else if(bookingType.equals("Recreation")){

                    startActivity(
                        Intent(this, BookingInfoActvity::class.java)
                            .putExtra(AppConfig.EXTRA.BOOKINGID, paymnetID)
                            .putExtra(AppConfig.EXTRA.BOOKINGTYPE, bookingType)
                            .putExtra(AppConfig.EXTRA.CHECKBOOKINGINFO, 1)
                    )
                    finishAffinity()

                }


            })


        val alert11 = builder1.create()
        alert11.show()

    }


}
