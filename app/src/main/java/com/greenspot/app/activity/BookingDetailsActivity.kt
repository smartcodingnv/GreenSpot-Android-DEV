package com.greenspot.app.activity

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.activity_booking_details.*
import kotlinx.android.synthetic.main.activity_place_booking.ib_back
import kotlinx.android.synthetic.main.content_booking_details.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Matcher
import java.util.regex.Pattern


class BookingDetailsActivity : AppCompatActivity() {

    private var final_eur_price: String = ""
    private var transcationID: String = ""
    private lateinit var editModel: ListBookingDetails
    private var guestIndex: Int = 0
    private var totalguest: Int = 0
    private var recyclerView: RecyclerView? = null
    private var customAdapter: CustomAdapter? = null
    var editModelArrayList: ArrayList<ListBookingDetails>? = null
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
    private var tourID: String = ""
    private var single_price: String? = ""
    private var persons: String = ""
    private var formDate: String? = ""
    private var no_of_person: String? = ""

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
        final_price = helper!!.LoadStringPref(AppConfig.PREFERENCE.FINAL_PRICE, "")
        single_price = helper!!.LoadStringPref(AppConfig.PREFERENCE.START_PRICE, "")
        formDate = helper!!.LoadStringPref(AppConfig.PREFERENCE.FROM_DATE, "")
        no_of_person = helper!!.LoadStringPref(AppConfig.PREFERENCE.NO_OF_PERSON, "")
        tourID = helper!!.LoadStringPref(AppConfig.PREFERENCE.PLACEID, "")!!  //tourid


        token = "Bearer " + helper!!.LoadStringPref(AppConfig.PREFERENCE.AUTHTOKEN, "")


//
//        val first = params[0] // "status"
//
//        val second = params[1]

//

//        val data: Uri? = intent?.data
//
//        Log.e("tag", "oncreate " + action)
//        Log.e("tag", "oncreate " + scheme)
//        Log.e("tag", " tag" + intent.extras!!.get("checkkk"))

        Log.e("oncrate ", "   checkkk")
        Log.e(
            "oncrate ", "   checkkk" + helper!!.LoadIntPref(AppConfig.PREFERENCE.CHECKPAYMENT, 0)
        )

        /*   if (helper!!.LoadIntPref(AppConfig.PREFERENCE.CHECKPAYMENT, 0) == 1) {

               helper!!.initPref()
               helper!!.SaveIntPref(AppConfig.PREFERENCE.CHECKPAYMENT, 0)
               helper!!.ApplyPref()

               sendPaymnetData(
                   contryID = this.countryID!!,
                   langCode = this.langCode!!,
                   selectCurrency = this.currncyCode!!
               )

               Toast.makeText(
                   this@BookingDetailsActivity,
                   "Payment successful",
                   Toast.LENGTH_SHORT
               ).show()

               return
           }*/


        val intent = getIntent();
        totalguest = intent.getIntExtra(AppConfig.EXTRA.TOTALGUEST, 0)

        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })


//        linearLayout = findViewById(R.id.lay_details);
//
//        for (i in 1..2) {
//            val textView = LinearLayout(this)
//            textView.text = "TextView $i"
//            linearLayout.addView(textView)
//        }

//        editModelArrayList = populateList()


        placeTitle.clear()
        val bookingDetailsAdapter = BookDetailsAdapter(this)
        populateList()
        Common.setVerticalRecyclerView(this, recycler)
        placeTitle[0].setExpanded(true)
        placeTitle[0].first_name = helper!!.LoadStringPref(AppConfig.PREFERENCE.USER_FNAME, "")!!
        placeTitle[0].last_name = helper!!.LoadStringPref(AppConfig.PREFERENCE.USER_LNAME, "")!!
        placeTitle[0].email = helper!!.LoadStringPref(AppConfig.PREFERENCE.USER_EMAIL, "")!!

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
                            "First Name required " + value.person,
                            Toast.LENGTH_SHORT
                        ).show()

                        return@OnClickListener
                    }

                    if (value.last_name.isEmpty()) {
                        Toast.makeText(
                            applicationContext,
                            "Last Name required " + value.person,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@OnClickListener
                    }

                    if (value.email.isEmpty()) {

                        Toast.makeText(
                            applicationContext,
                            "Email required " + value.person,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@OnClickListener
                    }

                    if (value.contact_number.isEmpty()) {

                        Toast.makeText(
                            applicationContext,
                            "Contact no required " + value.person,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@OnClickListener

                    }

                    if (!emailValidator(value.email)) {
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


//
//        customAdapter = CustomAdapter(this, editModelArrayList)
//        recyclerView!!.setAdapter(customAdapter)
//        recyclerView!!.setLayoutManager(
//            LinearLayoutManager(
//                applicationContext,
//                LinearLayoutManager.VERTICAL,
//                false
//            )
//        )


    }

    private fun alertProcessPayment(message: String) {
        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage(message)
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            "Ok",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                checkBooking(
                    contryID = this.countryID!!,
                    selectCurrency = this.currncyCode!!,
                    langCode = this.langCode!!
                )

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

    fun emailValidator(email: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }


    private fun checkBooking(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
        viewDialog!!.showDialog()
        utils!!.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val checkBookingResponce = apiService?.CALL_CHECKBEFOREPAY(
            token = token,
            langcode = langCode,
            tourid = tourID,
            selectCurrency = selectCurrency,
            masterType = "T",
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
                } else {
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
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            "Ok",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                startActivity(
                    Intent(this, HomeActivity::class.java)
                )
                finish()

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
            Log.e("onResume ", "   checkkk"+valueOne)

        }



        if (helper!!.LoadIntPref(AppConfig.PREFERENCE.CHECKPAYMENT, 0) == 1) {


            helper!!.initPref()
            helper!!.SaveIntPref(AppConfig.PREFERENCE.CHECKPAYMENT, 0)
            helper!!.ApplyPref()

            sendPaymnetData(
                contryID = this.countryID!!,
                langCode = this.langCode!!,
                selectCurrency = this.currncyCode!!
            )


        }


    }

    override fun onDestroy() {
        super.onDestroy()

        helper!!.initPref()
        helper!!.SaveIntPref(AppConfig.PREFERENCE.CHECKPAYMENT, 0)
        helper!!.ApplyPref()

    }


    private fun sendPaymnetData(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
        viewDialog!!.showDialog()

        utils!!.hideKeyboard()
        val jsonArray = JSONArray(helper!!.LoadStringPref(AppConfig.PREFERENCE.PERSONDETAILS, ""))
        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val responcePlaceReview = apiService?.PAYMENTMOLIE_CALL(
            token = token,
            contryID = contryID,
            selectCurrency = "EUR",
            langcode = langCode,
            person = jsonArray,
            tourid = tourID,
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
        Log.e("tourid", tourID)
        Log.e("finalPrice", this.final_price!!)
        Log.e("formDate", this.formDate!!)
        Log.e("noofperson", this.no_of_person!!)
        Log.e("singlePrice", this.single_price!!)
        Log.e("tanstationID", helper!!.LoadStringPref(AppConfig.PREFERENCE.TRANSACTIONID, "")!!)


        responcePlaceReview?.enqueue(object : Callback<ComanResponce> {

            override fun onResponse(@NonNull call: Call<ComanResponce>, @NonNull response: Response<ComanResponce>) {
                viewDialog!!.hideDialog()

                val postreivewData = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {

                    if (postreivewData!!.status == 1) {

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

    private fun alertsuccessbooking(message: String) {
        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage(message)
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            "Ok",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                startActivity(
                    Intent(this, HomeActivity::class.java)
                )
                finish()

            })


        val alert11 = builder1.create()
        alert11.show()

    }


}
