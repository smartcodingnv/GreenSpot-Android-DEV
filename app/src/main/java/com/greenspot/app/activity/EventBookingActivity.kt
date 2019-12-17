package com.greenspot.app.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.greenspot.app.R
import com.greenspot.app.utils.AppConfig
import kotlinx.android.synthetic.main.activity_event_booking.*
import kotlinx.android.synthetic.main.content_place_booking.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class EventBookingActivity : AppCompatActivity() {
    private var childcount: Int =0
    private var adultcount: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_booking)



        val c = Calendar.getInstance().time

        val df = SimpleDateFormat("dd-MM-yyyy")
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

        })

        ib_adultadd.setOnClickListener(View.OnClickListener {
            adultcount++
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

        txt_date.setOnClickListener(View.OnClickListener {

            selectDate()

//            datepicker.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
//           DatePickerDialog(
//                this, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
//                myCalendar[Calendar.DAY_OF_MONTH]
//            ).show()


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
                val _pickedDate = "$_date-$_month-$year"
                Log.e("PickedDate: ", "Date: $_pickedDate") //
                txt_date.setText(_pickedDate)// 2019-02-12
            }, c[Calendar.YEAR], c[Calendar.MONTH], c[Calendar.MONTH]
        )
        dialog.datePicker.minDate = System.currentTimeMillis() - 1000
        dialog.show()
    }


    private fun placeBooking() {

        val name = et_name.getText().toString()
        val email = et_email.getText().toString()
        val number = et_number.getText().toString()
        if (name.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.enter_the_name),
                Toast.LENGTH_SHORT
            ).show()
            return

        }
        if (email.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.res_enteremail),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (number.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.enter_the_number),
                Toast.LENGTH_SHORT
            ).show()

            return
        }
        if (!emailValidator(email)) {
            Toast.makeText(
                applicationContext,
                getString(R.string.res_validemail),
                Toast.LENGTH_SHORT
            ).show()

            return
        }

//        if (isValidMobile(number)) {
//            Toast.makeText(
//                applicationContext,
//                getString(R.string.res_validnumber),
//                Toast.LENGTH_SHORT
//            ).show()
//
//            return
//
//        }

        var totalguest = adultcount + childcount

        startActivity(
            Intent(this, BookingDetailsActivity::class.java)
                .putExtra(AppConfig.EXTRA.TOTALGUEST,totalguest)
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
}
