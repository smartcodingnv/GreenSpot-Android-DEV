package com.greenspot.app.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.DatePicker.OnDateChangedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.greenspot.app.R
import com.greenspot.app.adapter.HotelRoomDetailsAdapter
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.model.ListAddGuestDetails
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.Common
import com.greenspot.app.utils.PreferenceHelper
import kotlinx.android.synthetic.main.activity_hotel_dateand_guest.*
import kotlinx.android.synthetic.main.activity_tour_list.ib_back
import kotlinx.android.synthetic.main.content_hotel_dateand_guest.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HotelDateandGuestActivity : AppCompatActivity(), ItemClickListener {

    private lateinit var calendar2: Calendar
    private lateinit var calendar: Calendar
    private lateinit var hotelroomDetailsAdapter: HotelRoomDetailsAdapter
    private var filterdata: ListAddGuestDetails? = null
    private var roomguesedata: String = ""
    private var checkoutdDate: String = ""
    private var checkin: Long = 0
    private var checkout: Long = 0

    private var checkindDate: String = ""
    private var checkdate: Int = 1
    private var checkdateguest: Int = 0
    private var checkdateguestscreen: Int = 0
    private lateinit var editModel: ListAddGuestDetails
    private var totalroom: Int = 1
    var placeTitle: ArrayList<ListAddGuestDetails> = ArrayList()
    private var placeTitleee: java.util.ArrayList<ListAddGuestDetails?> = ArrayList()

    private lateinit var datePickercheckin: DatePicker
    private lateinit var datePickercheckout: DatePicker
    private var helper: PreferenceHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_dateand_guest)

        datePickercheckin = findViewById(R.id.cal_checkin)
        datePickercheckout = findViewById(R.id.cal_checkout)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        val intent = getIntent();
        val bundle = Bundle()
        checkdateguest = intent.getIntExtra(AppConfig.EXTRA.CHECKDATEGUEST, 0)
        checkindDate = intent.getStringExtra(AppConfig.EXTRA.CHECKINDATE)
        checkoutdDate = intent.getStringExtra(AppConfig.EXTRA.CHECKOUTDATE)
        checkdateguestscreen = intent.getIntExtra(AppConfig.EXTRA.CHECKDATEGUESTSCREEN, 0)

        if (checkdateguestscreen == 1) {

            lay_rooms.visibility = View.VISIBLE

        } else if (checkdateguestscreen == 2) {

            lay_rooms.visibility = View.GONE
        }

        ib_back.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            setResult(2000, intent)
            finish()
        })


//        datePickercheckin.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
//        ) { datePicker, year, month, dayOfMonth ->
//
//
//            Log.e("dateee"," "+checkindDate)
////            val string_date = dayOfMonth / (month + 1) / year
////            Log.e("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth)
//
//        }


        if (checkdateguest == 1) {
            checkdate = 1
            datePickercheckin.visibility = View.VISIBLE
            datePickercheckout.visibility = View.GONE
            rv_addguest.visibility = View.GONE
            btn_addmore.visibility = View.INVISIBLE
            lay_rooms.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgray))
            lay_checkout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgray))
            lay_checkin.setBackgroundColor(ContextCompat.getColor(this, R.color.white))


        } else if (checkdateguest == 2) {
            checkdate = 2
            datePickercheckin.visibility = View.GONE
            datePickercheckout.visibility = View.VISIBLE
            btn_addmore.visibility = View.INVISIBLE
            lay_rooms.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgray))
            lay_checkout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            lay_checkin.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgray))

        } else if (checkdateguest == 3) {

            datePickercheckin.visibility = View.GONE
            datePickercheckout.visibility = View.GONE
            rv_addguest.visibility = View.VISIBLE
            btn_addmore.visibility = View.VISIBLE
            lay_rooms.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            lay_checkout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgray))
            lay_checkin.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgray))
        }

        placeTitleee =
            intent.getSerializableExtra(AppConfig.EXTRA.ROOMGUESTRESPONCE) as ArrayList<ListAddGuestDetails?>

        if (placeTitleee.isNullOrEmpty() || placeTitleee.size == 0) {
            hotelroomDetailsAdapter = HotelRoomDetailsAdapter(this, this)
            populateList()
            Common.setVerticalRecyclerView(this, rv_addguest)
            hotelroomDetailsAdapter.swapData(placeTitle)
            rv_addguest.adapter = hotelroomDetailsAdapter
        } else {
            Log.e("checkroomguestdata", roomguesedata)

//            filterdata = gson2.fromJson(roomguesedata, ListAddGuestDetails::class.java)

            placeTitle = placeTitleee as ArrayList<ListAddGuestDetails>
            hotelroomDetailsAdapter = HotelRoomDetailsAdapter(this, this)
            Common.setVerticalRecyclerView(this, rv_addguest)
            hotelroomDetailsAdapter.swapData(placeTitle)
            rv_addguest.adapter = hotelroomDetailsAdapter

        }


        btn_addmore.setOnClickListener(View.OnClickListener {
            totalroom++
            populateList()
            hotelroomDetailsAdapter.notifyItemInserted(placeTitle.size - 1)
            txt_rooms.text = totalroom.toString() + " " + getString(R.string.str_rooms)
        })

        btn_canel.setOnClickListener(View.OnClickListener {


            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.CHECKINDATE, "")
            helper!!.SaveStringPref(AppConfig.PREFERENCE.CHECKOUTDATE, "")
            helper!!.ApplyPref()
            checkindDate = ""
            checkoutdDate = ""
            totalroom = 0
            placeTitle.clear()
            hotelroomDetailsAdapter.notifyDataSetChanged()
            txt_rooms.text = totalroom.toString() + " " + getString(R.string.str_rooms)
            txt_checkindate.text = getString(R.string.str_dateformat)
            txt_checkoutdate.text = getString(R.string.str_dateformat)
        })


        calendar = Calendar.getInstance()
        calendar2 = Calendar.getInstance()


        if (checkoutdDate.isNotEmpty() && !checkoutdDate.equals(getString(R.string.str_dateformat))) {
            txt_checkoutdate.text = checkoutdDate
            val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
            var date11: Date? = null
            date11 = formatter.parse(checkoutdDate) as Date
            calendar.time = date11
            calendar.roll(Calendar.DATE, -1);
            datePickercheckin.maxDate = calendar.timeInMillis
        } else {
            datePickercheckin.minDate = System.currentTimeMillis() - 1000
            calendar.setTimeInMillis(System.currentTimeMillis());
        }

        if (checkindDate.isNotEmpty() && !checkindDate.equals(getString(R.string.str_dateformat))) {
            txt_checkindate.text = checkindDate
            val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
            var date11: Date? = null
            date11 = formatter.parse(checkindDate) as Date
            calendar2.time = date11
            calendar2.roll(Calendar.DATE, true);
            datePickercheckout.minDate = 0
            datePickercheckout.minDate = calendar2.timeInMillis

        } else {

            datePickercheckout.minDate = System.currentTimeMillis() - 1000
            calendar2.setTimeInMillis(System.currentTimeMillis());

        }



        datePickercheckin.init(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH],
            OnDateChangedListener { datePicker, year, month, dayOfMonth ->

                val _year = year.toString()
                val _month = if (month + 1 < 10) "0" + (month + 1) else (month + 1).toString()
                val _date = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()

                /*if(checkdate==1){
                    checkin = calendar.timeInMillis
                    Log.e("cureetntime ", checkin.toString())
                    checkindDate = "$_date-$_month-$_year"
                    txt_checkindate.text = checkindDate
                }*/

                if (checkdate == 1) {
                    checkin = calendar.timeInMillis
                    Log.e("cureetntime ", checkin.toString())
                    checkindDate = "$_date-$_month-$_year"
                    txt_checkindate.text = checkindDate
                }



                if (checkindDate.isEmpty()) {
                    datePickercheckout.minDate = System.currentTimeMillis() - 1000
                    calendar2.setTimeInMillis(System.currentTimeMillis());
                } else {
                    if (!checkindDate.equals(getString(R.string.str_dateformat))) {
                        val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
                        var date11: Date? = null
                        date11 = formatter.parse(checkindDate) as Date
                        calendar2.time = date11
                        calendar2.add(Calendar.DATE, 1);
//                        calendar2.roll(Calendar.DATE, true);
                        datePickercheckout.minDate = 0
                        datePickercheckout.minDate = calendar2.timeInMillis
                    }

                    Log.e("dateeeeeeee", " " + checkindDate)
                }
                Log.e(
                    "Dateeee",
                    "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth
                )

            })


        datePickercheckout.init(
            calendar2.get(Calendar.YEAR),
            calendar2.get(Calendar.MONTH),
            calendar2.get(Calendar.DAY_OF_MONTH)
        ) { datePicker, year, month, dayOfMonth ->

            val _year = year.toString()
            val _month = if (month + 1 < 10) "0" + (month + 1) else (month + 1).toString()
            val _date = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()

            if (checkdate == 2) {
                checkout = calendar2.timeInMillis
                checkoutdDate = "$_date-$_month-$_year"
                txt_checkoutdate.text = checkoutdDate
            }

            if (checkoutdDate.isEmpty()) {
                datePickercheckin.minDate = System.currentTimeMillis() - 1000
                calendar.setTimeInMillis(System.currentTimeMillis());
            } else {
                if (!checkoutdDate.equals(getString(R.string.str_dateformat))) {
                    val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
                    var date11: Date? = null
                    date11 = formatter.parse(checkoutdDate) as Date
                    calendar.time = date11
                    calendar.roll(Calendar.DATE, -1);
                    datePickercheckin.maxDate = calendar.timeInMillis
                }

                Log.e("dateeeeeeee", " " + checkout)
            }


        }


        lay_checkin.setOnClickListener(View.OnClickListener {
            checkdate = 1
            datePickercheckin.visibility = View.VISIBLE
            datePickercheckout.visibility = View.GONE
            rv_addguest.visibility = View.GONE
            btn_addmore.visibility = View.INVISIBLE
            lay_rooms.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgray))
            lay_checkout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgray))
            lay_checkin.setBackgroundColor(ContextCompat.getColor(this, R.color.white))


        })

        lay_checkout.setOnClickListener(View.OnClickListener {
            checkdate = 2
            datePickercheckin.visibility = View.GONE
            datePickercheckout.visibility = View.VISIBLE
            rv_addguest.visibility = View.GONE
            btn_addmore.visibility = View.INVISIBLE
            lay_rooms.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgray))
            lay_checkout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            lay_checkin.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgray))
        })

        lay_rooms.setOnClickListener(View.OnClickListener {
            datePickercheckin.visibility = View.GONE
            datePickercheckout.visibility = View.GONE
            rv_addguest.visibility = View.VISIBLE
            btn_addmore.visibility = View.VISIBLE
            lay_rooms.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            lay_checkout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgray))
            lay_checkin.setBackgroundColor(ContextCompat.getColor(this, R.color.lightgray))
        })

        ib_done.setOnClickListener(View.OnClickListener {

            //            placeTitleee = placeTitle as java.util.ArrayList<ListAddGuestDetails?>

            var totalguest = 0
            for (name in placeTitle) {
                var totalguesttt = 0
                var adult = 0
                var child = 0


                if (name.adult.isNotEmpty()) {
                    adult = name.adult.toInt()
                }

                if (name.child.isNotEmpty()) {
                    child = name.child.toInt()
                }

                totalguesttt = adult + child


                totalguest += totalguesttt
            }

            Log.e("total", " " + totalguest)

            val intent = Intent()
            intent.putExtra(AppConfig.EXTRA.CHECKINDATE, txt_checkindate.text);
            intent.putExtra(AppConfig.EXTRA.CHECKOUTDATE, txt_checkoutdate.text);
            intent.putExtra(AppConfig.EXTRA.TOTALROOM, totalroom.toString());
            intent.putExtra(AppConfig.EXTRA.ROOMTOTALGUEST, totalguest.toString())
            bundle.putSerializable(AppConfig.EXTRA.ROOMGUESTRESPONCE, placeTitle)
            intent.putExtras(bundle)
            setResult(3000, intent)
            finish()

        })


    }

    private fun populateList() {


        editModel = ListAddGuestDetails(
            id = "",
            adult = "2",
            child = "0",
            child1 = "",
            child2 = "",
            child3 = "",
            child4 = "",
            room = getString(R.string.str_room) + " " + totalroom
        )
        placeTitle.add(editModel)
    }

    override fun recyclerViewListClicked(v: View, position: Int, flag: Int) {
        if (flag == 1) {
            totalroom--
        }

    }


}
