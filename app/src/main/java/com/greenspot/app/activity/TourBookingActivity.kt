package com.greenspot.app.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.TourDateAdapter
import com.greenspot.app.adapter.TourMonthAdapter
import com.greenspot.app.adapter.TourOtherServiceAdapter
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.model.ItineraryImg
import com.greenspot.app.responce.tourdetail.BookingDatesItem
import com.greenspot.app.responce.tourdetail.IncludedInTourPackageItem
import com.greenspot.app.responce.tourdetail.ResponceTourDetails
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.activity_tour_booking.*
import kotlinx.android.synthetic.main.content_tour_booking.*
import kotlinx.android.synthetic.main.dialog_contry.*
import java.math.RoundingMode
import java.util.*


class TourBookingActivity : AppCompatActivity(), ItemClickListener {

    private var bookingDateData: List<String>? = null
    private var bookingMonthdata: List<BookingDatesItem>? = null
    private var finalPrice: String = ""
    private var startPrice: String = ""
    private var otherServiceList: List<IncludedInTourPackageItem>? = null
    private var childcount: Int = 0
    private var adultcount: Int = 2
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
    private lateinit var dialog: Dialog

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
        txt_depature.text = getString(R.string.txt_depature)+": " + tourdetails.data.mainRecords.depatureCity
        txt_tourlocation.text = getString(R.string.txt_location)+": " + tourdetails.data.mainRecords.locations
        txt_duration.text =
            getString(R.string.txt_duration)+ ": " + tourdetails.data.mainRecords.nights.toString() + getString(R.string.str_night)+"  & " + tourdetails.data.mainRecords.days.toString() + " "+
                    getString(R.string.str_day)

        rt_tourrating.rating = tourdetails.data.mainRecords.avgReviews.toFloat()
        txt_riview.text = tourdetails.data.mainRecords.totalReviews.toString() + " "+getString(R.string.str_reviews)

        startPrice = tourdetails.data.mainRecords.price
        val totalprice = startPrice.toFloat() * adultcount
        finalPrice = startPrice
        txt_startprice.text = currncyCode + " " + totalprice

        Log.e("Tourbooking", "  " + tourdetails.data.mainRecords.includedInTourPackage!!.size)

        otherServiceList = tourdetails.data.mainRecords.includedInTourPackage
        bookingMonthdata = tourdetails.data.bookingDates

        setTourOtherDetail()

//        val adapter: ArrayAdapter<String> =
//            ArrayAdapter<String>(this, R.layout.spinner_item, pass)
//        sp_pass.setAdapter(adapter);


//        val c = Calendar.getInstance().time
//
//        val df = SimpleDateFormat("MM/dd/yyyy")
//        val formattedDate = df.format(c)
//
//        txt_date.setText(formattedDate)

        txt_date.setOnClickListener(View.OnClickListener {

            //            selectDate()
            if (txt_month.text.isEmpty() || txt_month.text.equals("Month")) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.alert_selectmonth),
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            openDateDialog()
        })

        txt_month.setOnClickListener(View.OnClickListener {
            openMonthDialog()

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
//            finalPrice = String.format("%.2f", totalprice)
            txt_startprice.text = currncyCode + " " + totalprice.toBigDecimal().setScale(2, RoundingMode.UP).toString()
        })

        ib_adultadd.setOnClickListener(View.OnClickListener {
            adultcount++

            val totalprice = startPrice.toFloat() * adultcount
//            finalPrice = String.format("%.2f", totalprice)
            txt_startprice.text = currncyCode + " " + totalprice.toBigDecimal().setScale(2, RoundingMode.UP).toString()

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

    private fun openMonthDialog() {

        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_contry)
        dialog.window?.setBackgroundDrawableResource(R.color.colorIdolDetailDialogBackground)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.txt_title.setText(getString(R.string.res_selectmonth))
        val tourMonthAdapter = TourMonthAdapter(this, this)

//        contryData.clear()
//        setContryData()
        Common.setVerticalRecyclerView(this, dialog.rv_bestseller)
        tourMonthAdapter.swapData(this.bookingMonthdata!!)

//        dialog.rv_bestseller.affectOnItemClicks() { position, view ->
//
//
//
//        }

        dialog.rv_bestseller.adapter = tourMonthAdapter

        dialog.lay_dialog.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun openDateDialog() {

        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_contry)
        dialog.window?.setBackgroundDrawableResource(R.color.colorIdolDetailDialogBackground)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.txt_title.setText(getString(R.string.res_selectmonth))
        val tourDateAdapter = TourDateAdapter(this, this)

//        contryData.clear()
//        setContryData()
        Common.setVerticalRecyclerView(this, dialog.rv_bestseller)
        tourDateAdapter.swapData(this.bookingDateData!!)

//        dialog.rv_bestseller.affectOnItemClicks() { position, view ->
//
//
//
//        }

        dialog.rv_bestseller.adapter = tourDateAdapter

        dialog.lay_dialog.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun selectDate() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR);
        val month = c.get(Calendar.MONTH);
        val day = c.get(Calendar.DAY_OF_MONTH);

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
            }, year, month, day
        )
        dialog.datePicker.minDate = System.currentTimeMillis() - 1000
        dialog.show()
    }

    private fun placeBooking() {

        if (txt_month.text.isEmpty() || txt_month.text.toString().equals("Month")) {
            Toast.makeText(
                applicationContext,
                getString(R.string.alert_month),
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (txt_date.text.isEmpty() || txt_date.text.toString().equals("Date")) {
            Toast.makeText(
                applicationContext,
                getString(R.string.alert_selectdate),
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        Log.e("dateeee","  "+txt_date.text.toString())

        val totalguest = adultcount + childcount

        helper!!.initPref()
        helper!!.SaveStringPref(AppConfig.PREFERENCE.TOUR_START_PRICE, startPrice)
        helper!!.SaveStringPref(AppConfig.PREFERENCE.TOUR_FINAL_PRICE, finalPrice)
        helper!!.SaveStringPref(AppConfig.PREFERENCE.TOUR_FROM_DATE, txt_date.text.toString())
        helper!!.SaveStringPref(AppConfig.PREFERENCE.TOUR_NO_OF_PERSON, totalguest.toString())
        helper!!.ApplyPref()


        startActivity(
            Intent(this, BookingDetailsActivity::class.java)
                .putExtra(AppConfig.EXTRA.TOTALGUEST, totalguest)
                .putExtra(AppConfig.EXTRA.TYPE, "Tour")
        )
    }


    private fun isValidMobile(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }


    private fun setTourOtherDetail() {

        val imgOtherServiceAdapter = TourOtherServiceAdapter(this)
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

    override fun recyclerViewListClicked(v: View, position: Int, flag: Int) {
        if (flag == 1) {

            helper!!.initPref()
            helper!!.SaveStringPref(
                AppConfig.PREFERENCE.SELECTTOURMONTH,
                bookingMonthdata!!.get(position).title
            )
            helper!!.ApplyPref()

            bookingDateData = bookingMonthdata!!.get(position).dates
            dialog.dismiss()
            txt_month.setText(bookingMonthdata!!.get(position).title)

        } else if (flag == 2) {
            helper!!.initPref()
            helper!!.SaveStringPref(
                AppConfig.PREFERENCE.SELECTTOURDATE,
                bookingDateData!!.get(position).toString()
            )
            helper!!.ApplyPref()
            dialog.dismiss()
            txt_date.setText(bookingDateData!!.get(position).toString())

        }


    }


}
