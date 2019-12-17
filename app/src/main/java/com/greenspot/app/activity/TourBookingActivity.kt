package com.greenspot.app.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.ImgOtherServiceAdapter
import com.greenspot.app.model.ItineraryImg
import com.greenspot.app.responce.tourdetail.IncludedInTourPackageItem
import com.greenspot.app.responce.tourdetail.ResponceTourDetails
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.activity_tour_booking.*
import kotlinx.android.synthetic.main.activity_tour_booking.ib_back

import kotlinx.android.synthetic.main.content_tour_booking.*
import kotlinx.android.synthetic.main.content_tour_booking.ib_adultadd
import kotlinx.android.synthetic.main.content_tour_booking.ib_adultminus
import kotlinx.android.synthetic.main.content_tour_booking.ib_childrenadd
import kotlinx.android.synthetic.main.content_tour_booking.ib_childresnminus
import kotlinx.android.synthetic.main.content_tour_booking.txt_adultcount
import kotlinx.android.synthetic.main.content_tour_booking.txt_childrencount
import kotlinx.android.synthetic.main.content_tour_booking.txt_date
import kotlinx.android.synthetic.main.content_tour_booking.txt_name
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class TourBookingActivity : AppCompatActivity() {

    private var finalPrice: String =""
    private  var startPrice: String =""
    private var otherServiceList: List<IncludedInTourPackageItem>? = null
    private var childcount: Int = 0
    private var adultcount: Int = 1
    var pass =
        arrayOf("10 Des, 2019", "15 Des, 2019", "20 Des, 2019", "25 Des, 2019")

    private var progress: Progress? = null
    private var viewDialog: ViewDialog? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null
    private var currncyCode: String? = ""
    private var countryID: String? = ""
    private var langCode: String? = ""

    var itineraryImg: ArrayList<ItineraryImg> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_booking)

        progress = Progress(this)
        viewDialog = ViewDialog(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)

        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")



        val gson = Gson()
        val tourdetails =
            gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.TOURDETAILSRESPONCE, ""),
                ResponceTourDetails::class.java
            )


        txt_name.text = tourdetails.data.mainRecords.packageName
        txt_depature.text = "Departure: " + tourdetails.data.mainRecords.depatureCity
        txt_tourlocation.text = "Departure: " + tourdetails.data.mainRecords.locations
        txt_tourlocation.text = "Departure: " + tourdetails.data.mainRecords.locations
        txt_duration.text =
            tourdetails.data.mainRecords.days.toString() + " Days & " + tourdetails.data.mainRecords.nights.toString() + " Nights"

        startPrice = tourdetails.data.mainRecords.price
        finalPrice = startPrice

        txt_startprice.text =
            getString(R.string.str_startingform)+ " " + currncyCode + " " +  startPrice

        Log.e("Tourbooking","  "+ tourdetails.data.mainRecords.includedInTourPackage!!.size)

        otherServiceList = tourdetails.data.mainRecords.includedInTourPackage

        setTourOtherDetail()

//        val adapter: ArrayAdapter<String> =
//            ArrayAdapter<String>(this, R.layout.spinner_item, pass)
//        sp_pass.setAdapter(adapter);


        val c = Calendar.getInstance().time

        val df = SimpleDateFormat("MM/dd/yyyy")
        val formattedDate = df.format(c)

        txt_date.setText(formattedDate)

        txt_date.setOnClickListener(View.OnClickListener {

            selectDate()

//            datepicker.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
//           DatePickerDialog(
//                this, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
//                myCalendar[Calendar.DAY_OF_MONTH]
//            ).show()


        })

        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        ib_adultminus.setOnClickListener(View.OnClickListener {
            if (adultcount == 1) {
                return@OnClickListener
            }
            adultcount--

            txt_adultcount.setText(adultcount.toString())

            val totalprice = startPrice.toFloat() * adultcount
            finalPrice = String.format("%.2f", totalprice)
            txt_startprice.text =
                getString(R.string.str_startingform)+ " " + currncyCode + " " + finalPrice
        })

        ib_adultadd.setOnClickListener(View.OnClickListener {
            adultcount++
            val totalprice = startPrice.toFloat() * adultcount
            finalPrice = String.format("%.2f", totalprice)
            txt_startprice.text =
                getString(R.string.str_startingform)+ " " + currncyCode + " " +  finalPrice

            txt_adultcount.setText(adultcount.toString())

        })

        ib_childrenadd.setOnClickListener(View.OnClickListener {

            childcount++
            txt_childrencount.setText(childcount.toString())
        })

        ib_childresnminus.setOnClickListener(View.OnClickListener {

            if (childcount == 0) {
                return@OnClickListener
            }
            childcount--
            txt_childrencount.setText(childcount.toString())
        })


        btn_continue.setOnClickListener(View.OnClickListener {

            placeBooking()
        })

    }

    private fun selectDate() {

        val c = Calendar.getInstance()
        val dialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val _year = year.toString()
                val _month =
                    if (month + 1 < 10) "0" + (month + 1) else (month + 1).toString()
                val _date =
                    if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()
                val _pickedDate = "$_month/$_date/$year"
                Log.e("PickedDate: ", "Date: $_pickedDate") //
                txt_date.setText(_pickedDate)// 2019-02-12
            }, c[Calendar.YEAR], c[Calendar.MONTH], c[Calendar.MONTH]
        )
        dialog.datePicker.minDate = System.currentTimeMillis() - 1000
        dialog.show()
    }

    private fun placeBooking() {

//        val name = et_name.getText().toString()
//        val email = et_email.getText().toString()
//        val number = et_number.getText().toString()
//        if (name.isEmpty()) {
//
//            Toast.makeText(
//                applicationContext,
//                getString(R.string.enter_the_name),
//                Toast.LENGTH_SHORT
//            ).show()
//            return
//
//        }
//        if (email.isEmpty()) {
//
//            Toast.makeText(
//                applicationContext,
//                getString(R.string.res_enteremail),
//                Toast.LENGTH_SHORT
//            ).show()
//            return
//        }
//        if (number.isEmpty()) {
//
//            Toast.makeText(
//                applicationContext,
//                getString(R.string.enter_the_number),
//                Toast.LENGTH_SHORT
//            ).show()
//
//            return
//        }
//        if (!emailValidator(email)) {
//            Toast.makeText(
//                applicationContext,
//                getString(R.string.res_validemail),
//                Toast.LENGTH_SHORT
//            ).show()
//
//            return
//        }
//
////        if (isValidMobile(number)) {
////            Toast.makeText(
////                applicationContext,
////                getString(R.string.res_validnumber),
////                Toast.LENGTH_SHORT
////            ).show()
////
////            return
////
////        }

        val totalguest = adultcount + childcount

        helper!!.initPref()
        helper!!.SaveStringPref(AppConfig.PREFERENCE.START_PRICE,startPrice)
        helper!!.SaveStringPref(AppConfig.PREFERENCE.FINAL_PRICE,finalPrice)
        helper!!.SaveStringPref(AppConfig.PREFERENCE.FROM_DATE,txt_date.text.toString())
        helper!!.SaveStringPref(AppConfig.PREFERENCE.NO_OF_PERSON,totalguest.toString())
        helper!!.ApplyPref()


        startActivity(
            Intent(this, BookingDetailsActivity::class.java)
                .putExtra(AppConfig.EXTRA.TOTALGUEST, totalguest)
        )
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

    private fun isValidMobile(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }


    private fun setTourOtherDetail() {

        val imgOtherServiceAdapter = ImgOtherServiceAdapter(this)
        itineraryImg.clear()
        setSubImgData()
        Common.setGridRecyclerView(this, rv_otherservice, 2)
        imgOtherServiceAdapter.swapData(itineraryImg)
        rv_otherservice.adapter = imgOtherServiceAdapter


    }

    private fun setSubImgData() {


        for (name in this.otherServiceList!!) {

            if (name.masterId.equals("1")) {

                itineraryImg.add(
                    ItineraryImg(
                        "1",
                        "Flight",
                        getURLForResource(R.drawable.ic_otherflight)
                    )
                )
            }

            if (name.masterId.equals("2")) {

                itineraryImg.add(
                    ItineraryImg(
                        "2",
                        "Hotel",
                        getURLForResource(R.drawable.ic_ohterhotel)
                    )
                )
            }

            if (name.masterId.equals("3")) {

                itineraryImg.add(
                    ItineraryImg(
                        "3",
                        "Sightseeing",
                        getURLForResource(R.drawable.ic_othersigghtsign)
                    )
                )
            }
            if (name.masterId.equals("4")) {

                itineraryImg.add(
                    ItineraryImg(
                        "4",
                        "Meals",
                        getURLForResource(R.drawable.ic_othermeal)
                    )
                )
            }
            if (name.masterId.equals("5")) {

                itineraryImg.add(
                    ItineraryImg(
                        "5",
                        "Transportation",
                        getURLForResource(R.drawable.ic_othertransport)
                    )
                )
            }
            if (name.masterId.equals("6")) {

                itineraryImg.add(
                    ItineraryImg(
                        "6",
                        "Wifi",
                        getURLForResource(R.drawable.ic_otherservicewifi)
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


}
