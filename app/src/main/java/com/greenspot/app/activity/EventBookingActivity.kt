package com.greenspot.app.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.responce.eventdetails.EvnetDetailsResponce
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.activity_event_booking.*
import kotlinx.android.synthetic.main.content_event_booking.*
import java.text.SimpleDateFormat
import java.util.*

class EventBookingActivity : AppCompatActivity() {
    private var childcount: Int = 0
    private var adultcount: Int = 2

    private var progress: Progress? = null
    private var viewDialog: ViewDialog? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null
    private var currncyCode: String? = ""
    private var countryID: String? = ""
    private var langCode: String? = ""

    private var finalPrice: String = ""
    private var startPrice: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_booking)


        progress = Progress(this)
        viewDialog = ViewDialog(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)

        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")

        val gson = Gson()
        val eventdetails =
            gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.EVENTDETAILSRESPONCE, ""),
                EvnetDetailsResponce::class.java
            )

        txt_name.text = eventdetails.data.mainRecords.title
        txt_address.text = eventdetails.data.mainRecords.address

        if (eventdetails.data.mainRecords.startDate.isEmpty()) {
            txt_eventdate.text =  getString(R.string.res_date)+ " : Every Day"
            txt_schdule.visibility = View.VISIBLE
            lay_date.visibility = View.VISIBLE

        } else {
            txt_schdule.visibility = View.GONE
            lay_date.visibility = View.GONE
            txt_eventdate.text = getString(R.string.res_date) +": " + eventdetails.data.mainRecords.startDate + " "+   getString(R.string.res_to) + " " + eventdetails.data.mainRecords.endDate
        }

        txt_eventtime.text = getString(R.string.res_time)+": " + eventdetails.data.mainRecords.startTime +  " "+   getString(R.string.res_to) + " "  + eventdetails.data.mainRecords.endTime

        if (eventdetails.data.mainRecords.finalPrice.equals("0")) {
            startPrice = ""
            finalPrice = startPrice
            txt_price.text = getString(R.string.res_free)
        } else {

            startPrice = eventdetails.data.mainRecords.finalPrice
            val totalprice = startPrice.toFloat() * adultcount
            finalPrice = startPrice
            txt_price.text = currncyCode + " " + totalprice
        }


        val c = Calendar.getInstance().time

        val df = SimpleDateFormat("MM/dd/yyyy")
        val formattedDate = df.format(c)

        txt_date.setText(formattedDate)

        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        ib_adultminus.setOnClickListener(View.OnClickListener {
            if (adultcount == 1) {
                return@OnClickListener
            }
            adultcount--
            txt_adultcount.setText(adultcount.toString())

            if (startPrice.isNotEmpty()) {
                val totalprice = startPrice.toFloat() * adultcount
//                finalPrice = String.format("%.2f", totalprice)
                txt_price.text = currncyCode + " " + totalprice
            }

        })

        ib_adultadd.setOnClickListener(View.OnClickListener {
            adultcount++
            txt_adultcount.setText(adultcount.toString())
            if (startPrice.isNotEmpty()) {
                val totalprice = startPrice.toFloat() * adultcount
//                finalPrice = String.format("%.2f", totalprice)
                txt_price.text = currncyCode + " " + totalprice
            }


        })



        txt_date.setOnClickListener(View.OnClickListener {

            selectDate()

        })



        btn_continue.setOnClickListener(View.OnClickListener {

            if (eventdetails.data.mainRecords.startDate.isEmpty()) {

                if(txt_date.text.toString().isEmpty()){
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.res_bookings),
                        Toast.LENGTH_SHORT
                    ).show()

                    return@OnClickListener
                }
            }
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
                val _date = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()
                val _pickedDate = "$_month/$_date/$_year"
                Log.e("PickedDate: ", "Date: $_pickedDate") //
                txt_date.setText(_pickedDate)// 2019-02-12
            }, c[Calendar.YEAR], c[Calendar.MONTH], c[Calendar.MONTH]
        )
        dialog.datePicker.minDate = System.currentTimeMillis() - 1000
        dialog.show()
    }


    private fun placeBooking() {


        var totalguest = adultcount + childcount

        helper!!.initPref()
        helper!!.SaveStringPref(AppConfig.PREFERENCE.EVENT_START_PRICE, startPrice)
        helper!!.SaveStringPref(AppConfig.PREFERENCE.EVENT_FINAL_PRICE, finalPrice)
        helper!!.SaveStringPref(AppConfig.PREFERENCE.EVENT_FROM_DATE, txt_date.text.toString())
        helper!!.SaveStringPref(AppConfig.PREFERENCE.EVENT_NO_OF_PERSON, totalguest.toString())
        helper!!.ApplyPref()

        startActivity(
            Intent(this, BookingDetailsActivity::class.java)
                .putExtra(AppConfig.EXTRA.TOTALGUEST, 1)
                .putExtra(AppConfig.EXTRA.TYPE, "Event")
        )
    }


    private fun isValidMobile(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }
}
