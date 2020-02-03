package com.greenspot.app.activity


import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.BookingPlaceAdapter
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.model.RecBookingList
import com.greenspot.app.responce.recreationdetails.ResponceRecDetails
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.activity_place_booking.*
import kotlinx.android.synthetic.main.content_place_booking.*
import kotlinx.android.synthetic.main.content_place_booking.txt_date
import kotlinx.android.synthetic.main.content_place_booking.txt_name
import kotlinx.android.synthetic.main.content_tour_booking.*
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PlaceBookingActivity : AppCompatActivity(), ItemClickListener {

    private var totalPrice: Float= 0.0F
    private var totalnoofperson: Int= 2
    private var midWeekDayPassAdult: String = ""
    private var midWeekDayPassChild: String = ""
    private var midWeekNightPassAdult: String = ""
    private var midWeekNightPassChild: String = ""

    private var weekendDayPassAdult: String = ""
    private var weekendDayPassChild: String = ""
    private var weekendNightPassAdult: String = ""
    private var weekendNightPassChild: String = ""

    private var dayString: String = ""
    private lateinit var myDoubleList: List<Double>
    private var childcount: Int = 0
    private var adultcount: Int = 1


    private lateinit var datepicker: DatePickerDialog

    var listBookingdetails: ArrayList<RecBookingList> = ArrayList()



    private var progress: Progress? = null
    private var viewDialog: ViewDialog? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null
    private var currncyCode: String? = ""
    private var countryID: String? = ""
    private var langCode: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_booking)

        progress = Progress(this)
        viewDialog = ViewDialog(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)

        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")

        val gson = Gson()
        val placedetails =
            gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.PLACEDETAILSRESPONCE, ""),
                ResponceRecDetails::class.java
            )


        midWeekDayPassAdult = placedetails.data.prices.midWeekDayPassAdult
        midWeekDayPassChild = placedetails.data.prices.midWeekDayPassChild
        midWeekNightPassAdult = placedetails.data.prices.midWeekNightPassAdult
        midWeekNightPassChild = placedetails.data.prices.midWeekNightPassChild

        weekendDayPassAdult = placedetails.data.prices.weekendDayPassAdult
        weekendDayPassChild = placedetails.data.prices.weekendDayPassChild
        weekendNightPassAdult = placedetails.data.prices.weekendNightPassAdult
        weekendNightPassChild = placedetails.data.prices.weekendNightPassChild


        var pass = arrayOf(resources.getString(R.string.res_day_pass), resources.getString(R.string.res_night_pass))

        txt_name.text = placedetails.data.mainRecords.placeName
        txt_address.text = placedetails.data.mainRecords.district + ", " + placedetails.data.mainRecords.country

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.spinner_item, pass)
        sp_pass.setAdapter(adapter);

        val c = Calendar.getInstance().time

        val simpleDateFormat = SimpleDateFormat("EEEE")
        val df = SimpleDateFormat("MM/dd/yyyy")
        dayString = simpleDateFormat.format(c)
        val formattedDate = df.format(c)
        Log.e("PickedDate: ", "Date: $dayString")
        txt_date.setText(formattedDate)

        if (dayString.equals("Saturday") || dayString.equals("Sunday")) {

            if (weekendDayPassAdult.isNotEmpty()) {

                totalPrice = weekendDayPassAdult.toFloat() * 2
                txt_priceee.text = currncyCode + " " + totalPrice.toString()

            }

        }else{

            if (midWeekDayPassAdult.isNotEmpty()) {
                val total = midWeekDayPassAdult.toFloat() * 2
                totalPrice = total.toBigDecimal().setScale(2,RoundingMode.HALF_EVEN).toFloat()
                txt_priceee.text = currncyCode + " " + totalPrice.toString()

            }
        }




        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })




        txt_date.setOnClickListener(View.OnClickListener {

            selectDate()

        })



        btn_continue.setOnClickListener(View.OnClickListener {

            placeBooking()

        })


        initView()

    }

    private fun initView() {

        listBookingdetails.clear()
        setUpBookingData()
        val bookingPlaceAdapter = BookingPlaceAdapter(this, this)
        Common.setVerticalRecyclerView(this!!, rv_recreationbooking)
        bookingPlaceAdapter.swapData(listBookingdetails)
        rv_recreationbooking.adapter = bookingPlaceAdapter

    }

    private fun selectDate() {

        val c = Calendar.getInstance()
        val dialog = DatePickerDialog(
            this,
            OnDateSetListener { view, year, month, dayOfMonth ->

                val _year = year.toString()
                val _month =
                    if (month + 1 < 10) "0" + (month + 1) else (month + 1).toString()
                val _date =
                    if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()
                val _pickedDate = "$_month/$_date/$_year"
                Log.e("PickedDate: ", "Date: $_pickedDate")
                txt_date.setText(_pickedDate)// 2019-02-12

                val simpleDateFormat = SimpleDateFormat("EEEE")
                val date = Date(year, month, dayOfMonth - 1)
                dayString = simpleDateFormat.format(date)
                Log.e("PickedDate: ", "Date: $dayString")

                initView()
//                val GregorianCalendar =
//                    GregorianCalendar(year, month, dayOfMonth - 1)
//
//                val dayOfWeek: Int = date.get(date.DAY_OF_WEEK)

            }, c[Calendar.YEAR], c[Calendar.MONTH], c[Calendar.MONTH]
        )

        dialog.datePicker.minDate = System.currentTimeMillis() - 1000
        dialog.show()

    }


    private fun placeBooking() {

        val totalguest = adultcount + childcount


        if (dayString.equals("Saturday") || dayString.equals("Sunday")) {

            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.RECREATION_FINAL_PRICE, totalPrice.toString())
            helper!!.SaveStringPref(AppConfig.PREFERENCE.RECREATION_FROM_DATE, txt_date.text.toString())

            helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_day_pass_adult_price, "")
            helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_day_pass_adult_nop, "")

            helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_day_pass_child_price, "")
            helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_day_pass_child_nop, "")

            helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_night_pass_adult_price,"")
            helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_night_pass_adult_nop, "")

            helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_night_pass_child_price, "")
            helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_night_pass_child_nop,"")


            if(weekendDayPassAdult.isNotEmpty()){
                helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_day_pass_adult_price, weekendDayPassAdult)
                helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_day_pass_adult_nop, listBookingdetails[0].count)
            }else{
                helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_day_pass_adult_price, "")
                helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_day_pass_adult_nop, "")
            }

            if(weekendDayPassChild.isNotEmpty()){

                helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_day_pass_child_price, weekendDayPassChild)
                helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_day_pass_child_nop, listBookingdetails[1].count)
            }else{

                helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_day_pass_child_price, "")
                helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_day_pass_child_nop, "")
            }


            if(weekendNightPassAdult.isNotEmpty()){

                helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_night_pass_adult_price, weekendNightPassAdult)
                helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_night_pass_adult_nop, listBookingdetails[2].count)
            }else{

                helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_night_pass_adult_price, "")
                helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_night_pass_adult_nop, "")
            }

            if(weekendNightPassChild.isNotEmpty()){

                helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_night_pass_child_price, weekendNightPassChild)
                helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_night_pass_child_nop, listBookingdetails[3].count)

            }else{

                helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_night_pass_child_price, "")
                helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_night_pass_child_nop, "")
            }


            helper!!.ApplyPref()

        }else
        {


            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.RECREATION_FINAL_PRICE, totalPrice.toString())
            helper!!.SaveStringPref(AppConfig.PREFERENCE.RECREATION_FROM_DATE, txt_date.text.toString())

            helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_day_pass_adult_price, "")
            helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_day_pass_adult_nop, "")

            helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_day_pass_child_price, "")
            helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_day_pass_child_nop, "")

            helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_night_pass_adult_price, "")
            helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_night_pass_adult_nop, "")

            helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_night_pass_child_price, "")
            helper!!.SaveStringPref(AppConfig.PREFERENCE.weekend_night_pass_child_nop, "")

            if(midWeekDayPassAdult.isNotEmpty()){
                helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_day_pass_adult_price, midWeekDayPassAdult)
                helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_day_pass_adult_nop, listBookingdetails[0].count)
            }else{

                helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_day_pass_adult_price, "")
                helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_day_pass_adult_nop, "")

            }
            if(midWeekDayPassChild.isNotEmpty()){
                helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_day_pass_child_price, midWeekDayPassChild)
                helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_day_pass_child_nop, listBookingdetails[1].count)
            }else{
                helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_day_pass_child_price, "")
                helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_day_pass_child_nop, "")
            }


            if(midWeekNightPassAdult.isNotEmpty()){
                helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_night_pass_adult_price, midWeekNightPassAdult)
                helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_night_pass_adult_nop, listBookingdetails[2].count)
            }else{
                helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_night_pass_adult_price,"")
                helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_night_pass_adult_nop, "")
            }

            if(midWeekNightPassChild.isNotEmpty()){
                helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_night_pass_child_price, midWeekNightPassChild)
                helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_night_pass_child_nop, listBookingdetails[3].count)
            }else{
                helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_night_pass_child_price, "")
                helper!!.SaveStringPref(AppConfig.PREFERENCE.mid_week_night_pass_child_nop,"")
            }

            helper!!.ApplyPref()

        }


//        for(item in listBookingdetails){
//
//            if(item.equals(""))
//        }


        if(totalnoofperson==0){
        Toast.makeText(
            applicationContext,
            getString(R.string.alert_selectonepass),
            Toast.LENGTH_SHORT
        ).show()
            return
        }

        startActivity(
            Intent(this, BookingDetailsActivity::class.java)
                .putExtra(AppConfig.EXTRA.TOTALGUEST, 1)
                .putExtra(AppConfig.EXTRA.TYPE, "Recreation")
        )


    }


    private fun isValidMobile(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

    private fun setUpBookingData() {

        if (dayString.equals("Saturday") || dayString.equals("Sunday")) {

            if (weekendDayPassAdult.isNotEmpty()) {
                listBookingdetails.add(
                    RecBookingList(
                        "1",
                        getString(R.string.str_weekend)+" "+getString(R.string.str_day)+" "+getString(R.string.txt_adults) +" "+ getString(R.string.str_pass),
                        weekendDayPassAdult,
                        "2",
                        ""
                    )
                )
            }


            if (weekendDayPassChild.isNotEmpty()) {
                listBookingdetails.add(
                    RecBookingList(
                        "2",
                        getString(R.string.str_weekend)+" "+getString(R.string.str_day)+" "+getString(R.string.txt_child) +" "+ getString(R.string.str_pass),
                        weekendDayPassChild,
                        "0",
                        ""
                    )
                )
            }

            if (weekendNightPassAdult.isNotEmpty()) {
                listBookingdetails.add(
                    RecBookingList(
                        "3",
                        getString(R.string.str_weekend)+" "+getString(R.string.str_night)+" "+getString(R.string.txt_adults) +" "+ getString(R.string.str_pass),
                        weekendNightPassAdult,
                        "0",
                        ""
                    )
                )
            }

            if (weekendNightPassChild.isNotEmpty()) {
                listBookingdetails.add(
                    RecBookingList(
                        "4",
                        getString(R.string.str_weekend)+" "+getString(R.string.str_night)+" "+getString(R.string.txt_child) +" "+ getString(R.string.str_pass),
                        weekendNightPassChild,
                        "0",
                        ""
                    )
                )
            }


        } else {

            if (midWeekDayPassAdult.isNotEmpty()) {
                listBookingdetails.add(
                    RecBookingList(
                        "1",
                        getString(R.string.str_midweek)+" "+getString(R.string.str_day)+" "+getString(R.string.txt_adults) +" "+ getString(R.string.str_pass),
                        midWeekDayPassAdult,
                        "2",
                        ""
                    )
                )

            }


            if (midWeekDayPassChild.isNotEmpty()) {
                listBookingdetails.add(
                    RecBookingList(
                        "2",
                        getString(R.string.str_midweek)+" "+getString(R.string.str_day)+" "+getString(R.string.txt_child) +" "+ getString(R.string.str_pass),
                        midWeekDayPassChild,
                        "0",
                        ""
                    )
                )
            }

            if (midWeekNightPassAdult.isNotEmpty()) {
                listBookingdetails.add(
                    RecBookingList(
                        "3",
                        getString(R.string.str_midweek)+" "+getString(R.string.str_night)+" "+getString(R.string.txt_adults) +" "+ getString(R.string.str_pass),
                        midWeekNightPassAdult,
                        "0",
                        ""
                    )
                )

            }
            if (midWeekNightPassChild.isNotEmpty()) {

                listBookingdetails.add(
                    RecBookingList(
                        "4",
                        getString(R.string.str_midweek)+" "+getString(R.string.str_night)+" "+getString(R.string.txt_child) +" "+ getString(R.string.str_pass),
                        midWeekNightPassChild,
                        "0",
                        ""
                    )
                )
            }

        }

    }

    override fun recyclerViewListClicked(v: View, position: Int, flag: Int) {


//        val arr = intArrayOf()
//
//        val emptyStringArray = arrayOf<Int>()
//
//

        var sumFinalprice: Float = 0.0F
        var sumNoofperson: Int = 0


        for (fPrice in listBookingdetails) {

            if (fPrice.finalPrice != "") {
                sumFinalprice += fPrice.finalPrice.toFloat()
                sumNoofperson += fPrice.count.toInt()
            }

        }

        Log.e("placebooking ", " " + sumFinalprice)
        Log.e("placebooking ", " " + sumNoofperson)

        totalPrice = sumFinalprice.toBigDecimal().setScale(2,RoundingMode.HALF_EVEN).toFloat()
        totalnoofperson = sumNoofperson
        txt_priceee.text = currncyCode + " " + totalPrice.toString()

//        txt_noofperson.text = currncyCode + " " + sumNoofperson.toString()

    }


}


