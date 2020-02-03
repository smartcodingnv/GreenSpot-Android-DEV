package com.greenspot.app.activity

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.HotelSelectRoomAdapter
import com.greenspot.app.adapter.TourOtherServiceAdapter
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.model.ItineraryImg
import com.greenspot.app.model.ListAddGuestDetails
import com.greenspot.app.model.ListRoom
import com.greenspot.app.model.ListSelectRoom
import com.greenspot.app.responce.hoteldetails.IncludedInHotelItem
import com.greenspot.app.responce.hoteldetails.ResponceHotelDetails
import com.greenspot.app.utils.*
import hk.ids.gws.android.sclick.SClick
import kotlinx.android.synthetic.main.activity_hotel_booking.*
import kotlinx.android.synthetic.main.content_hotel_booking.*
import org.jetbrains.anko.toast
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*


class HotelBookingActivity : AppCompatActivity(), ItemClickListener {


    private var finalPrice: Float = 0.0f
    private var totalNight: Int = 0
    private var hotelPrice: String = ""
    private var otherServiceList: List<IncludedInHotelItem>? = null
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

    private var selectroomList: ArrayList<ListSelectRoom>? = ArrayList()
    private var roomList: ArrayList<ListRoom>? = ArrayList()


    private lateinit var hotelselectRoomAdapter: HotelSelectRoomAdapter

    private var checkindateApply: Int = 3000
    private var placeTitle: java.util.ArrayList<ListAddGuestDetails?> = ArrayList()
    private var roomGuest: String = ""
    private var checkinDate: String = ""
    private var checkoutDate: String = ""
    private var totalRoom: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_booking)
        val intent = getIntent();
        progress = Progress(this)
        viewDialog = ViewDialog(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)

        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")

        hotelPrice = intent.getStringExtra(AppConfig.EXTRA.HOTELROOMPRICE)

        checkinDate = helper!!.LoadStringPref(AppConfig.PREFERENCE.CHECKINDATE, "")!!
        checkoutDate = helper!!.LoadStringPref(AppConfig.PREFERENCE.CHECKOUTDATE, "")!!

        Log.e("checkinDate", " " + checkinDate)
        Log.e("checkoutDate", " " + checkoutDate)

        txt_startprice.text = currncyCode + " " + hotelPrice.toBigDecimal().setScale(2, RoundingMode.UP).toString()

        if (checkinDate.isEmpty() &&  checkoutDate.isEmpty() ) {

            txt_duration.visibility = View.GONE

        } else {

            if(!checkinDate.equals(getString(R.string.str_dateformat))){
                txt_checkinn.text = checkinDate
            }
            if(!checkoutDate.equals(getString(R.string.str_dateformat))){
                txt_checkout.text = checkoutDate
            }

            if(!checkinDate.equals(getString(R.string.str_dateformat)) && !checkoutDate.equals(getString(R.string.str_dateformat))){
                val dfDate = SimpleDateFormat("dd-MM-yyyy")
                if(dfDate.parse(checkinDate).before(dfDate.parse(checkoutDate))){

                    totalNight = Daybetween(checkinDate, checkoutDate, "dd-MM-yyyy")
                    txt_duration.visibility = View.VISIBLE
                    txt_duration.text = totalNight.toString() + " " + getString(R.string.str_night)
                    finalPrice = hotelPrice.toFloat() * totalNight
                    txt_startprice.text = currncyCode + " " + finalPrice.toString()
                }


            }




        }

        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })


        val gson = Gson()
        val hoteldetails =
            gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.HOTELDETAILSRESPONCE, ""),
                ResponceHotelDetails::class.java
            )

        selectroomList =
            intent.getSerializableExtra(AppConfig.EXTRA.SELECTROOMLIST) as ArrayList<ListSelectRoom>
//        selectroomList = selectroomListtt as ArrayList<ListSelectRoom>
        txt_name.text = hoteldetails.data.mainRecords.name

        txt_hotellocation.text =
            getString(R.string.txt_location) + " " + hoteldetails.data.mainRecords.address

        rt_tourrating.rating = hoteldetails.data.mainRecords.avgReviews.toFloat()
        txt_hotelriview.text =
            hoteldetails.data.mainRecords.totalReviews.toString() + " " + getString(R.string.str_reviews)

        otherServiceList = hoteldetails.data.mainRecords.includedInHotel

        setTourOtherDetail()

        setSelectRoomList()

        lay_checkin.setOnClickListener(View.OnClickListener {

            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivityForResult(
                Intent(this, HotelDateandGuestActivity::class.java)
                    .putExtra(AppConfig.EXTRA.CHECKDATEGUESTSCREEN, 2)
                    .putExtra(AppConfig.EXTRA.CHECKDATEGUEST, 1)
                    .putExtra(AppConfig.EXTRA.CHECKINDATE, checkinDate)
                    .putExtra(AppConfig.EXTRA.CHECKOUTDATE, checkoutDate)
                    .putExtra(AppConfig.EXTRA.TOTALROOM, totalRoom)
                    .putExtra(AppConfig.EXTRA.ROOMTOTALGUEST, roomGuest)
                    .putExtra(AppConfig.EXTRA.ROOMGUESTRESPONCE, placeTitle), checkindateApply
            )

        })


        lay_checkout.setOnClickListener(View.OnClickListener {

            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivityForResult(
                Intent(this, HotelDateandGuestActivity::class.java)
                    .putExtra(AppConfig.EXTRA.CHECKDATEGUESTSCREEN, 2)
                    .putExtra(AppConfig.EXTRA.CHECKDATEGUEST, 2)
                    .putExtra(AppConfig.EXTRA.CHECKINDATE, checkinDate)
                    .putExtra(AppConfig.EXTRA.CHECKOUTDATE, checkoutDate)
                    .putExtra(AppConfig.EXTRA.TOTALROOM, totalRoom)
                    .putExtra(AppConfig.EXTRA.ROOMTOTALGUEST, roomGuest)
                    .putExtra(AppConfig.EXTRA.ROOMGUESTRESPONCE, placeTitle), checkindateApply
            )

        })


        Log.e("select room", " " + selectroomList)

        roomList!!.clear()
        for (rooms in selectroomList!!) {

            roomList!!.add(
                ListRoom(
                    rooms.id,
                    "1",
                    rooms.roomPrice

                )
            )
        }


        btn_continue.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;

            var count: Int = roomList!!.size

            for (i in 0 until count) {
                var j = i + 1
                while (j < count) {
                    if (roomList!!.get(i).master_id.equals(roomList!!.get(j).master_id)) {
                        roomList!!.removeAt(j--)
                        count--
                    }
                    j++
                }
            }

            for (room in roomList!!) {
                var selectedroom = 1
                selectroomList!!.forEach {
                    if (it.id == room.master_id) {
                        room.room_selected = selectedroom++.toString()
                    }
                }
            }

            Log.e("Rooms", " " + roomList.toString())

            if (txt_checkinn.text.isEmpty() || txt_checkinn.text.equals(getString(R.string.str_dateformat))) {

                toast(getString(R.string.alert_checkindate))
                return@OnClickListener
            }

            if (txt_checkout.text.isEmpty() || txt_checkout.text.equals(getString(R.string.str_dateformat))) {
                toast(getString(R.string.alert_checkoutdate))
                return@OnClickListener
            }


            val dfDate = SimpleDateFormat("dd-MM-yyyy")
            if(dfDate.parse(checkoutDate).before(dfDate.parse(checkinDate))){

                toast("Please select checkout date more then check in date")
                return@OnClickListener
            }
            if(dfDate.parse(checkoutDate).equals(dfDate.parse(checkinDate))){
                toast("Please select checkout date more then check in date")
                return@OnClickListener
            }

            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.CHECKINDATE, txt_checkinn.text.toString())
            helper!!.SaveStringPref(AppConfig.PREFERENCE.CHECKOUTDATE, txt_checkout.text.toString())
            helper!!.SaveStringPref(AppConfig.PREFERENCE.HOTEL_FINAL_PRICE, finalPrice.toString())
            helper!!.SaveStringPref(
                AppConfig.PREFERENCE.HOTEL_NO_OF_ROOM,
                selectroomList!!.size.toString()
            )
            helper!!.ApplyPref()


            val bundle = Bundle()

            val intent = Intent(this, BookingDetailsActivity::class.java)
            intent.putExtra(AppConfig.EXTRA.TOTALGUEST, 1)
            intent.putExtra(AppConfig.EXTRA.TYPE, "Hotel")
            bundle.putSerializable(AppConfig.EXTRA.ROOMDATA, roomList)
            intent.putExtras(bundle)
            startActivity(intent)


        })


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        Log.e(TAG, resultCode.toString()) ave e
//        Log.e(TAG, requestCode.toString()) jemoklo e


        if (resultCode == checkindateApply) {

            val bundle = data?.extras
            checkinDate = data?.getStringExtra(AppConfig.EXTRA.CHECKINDATE)!!
            checkoutDate = data.getStringExtra(AppConfig.EXTRA.CHECKOUTDATE)!!
            totalRoom = data.getStringExtra(AppConfig.EXTRA.TOTALROOM)!!
            roomGuest = data.getStringExtra(AppConfig.EXTRA.ROOMTOTALGUEST)!!
            placeTitle =
                data.getSerializableExtra(AppConfig.EXTRA.ROOMGUESTRESPONCE) as ArrayList<ListAddGuestDetails?>

            txt_checkinn.text = checkinDate
            txt_checkout.text = checkoutDate

            if (checkinDate.isNotEmpty() && !checkinDate.equals(getString(R.string.str_dateformat)) && checkoutDate.isNotEmpty() && !checkoutDate.equals(
                    getString(R.string.str_dateformat)
                )
            ) {
                val dfDate = SimpleDateFormat("dd-MM-yyyy")
                if(dfDate.parse(checkinDate).before(dfDate.parse(checkoutDate))){
                    totalNight = Daybetween(checkinDate, checkoutDate, "dd-MM-yyyy")
                    txt_duration.visibility = View.VISIBLE
                    txt_duration.text = totalNight.toString() + " " + getString(R.string.str_night)
                    finalPrice = hotelPrice.toFloat() * totalNight
                    txt_startprice.text = currncyCode + " " + finalPrice.toBigDecimal().setScale(2, RoundingMode.UP).toString()
                }


            } else {

                txt_duration.visibility = View.GONE
                txt_startprice.text = currncyCode + " " + hotelPrice.toBigDecimal().setScale(2, RoundingMode.UP).toString()
            }


//            txt_roomss.text = totalRoom + " " + getString(R.string.str_rooms)
//            txt_guest.text = roomGuest + " " + getString(R.string.str_guest)
//            questions =
//                intent.getSerializableExtra("QuestionListExtra") as ArrayList<Questions?>


        }
    }

    private fun setSelectRoomList() {

        hotelselectRoomAdapter = HotelSelectRoomAdapter(this, this, 2)
        Common.setHorizontalRecyclerView(this, rv_selectedroom)
        hotelselectRoomAdapter.swapData(this.selectroomList!!)
        rv_selectedroom.adapter = hotelselectRoomAdapter
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
        for (name in otherServiceList!!) {

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
        return Uri.parse("android.resource://" + com.greenspot.app.R::class.java.getPackage()!!.getName() + "/" + resourceId)
            .toString()
    }

    override fun recyclerViewListClicked(v: View, position: Int, flag: Int) {

    }

    fun Daybetween(
        date1: String?,
        date2: String?,
        pattern: String?
    ): Int {
        val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
        var Date1: Date? = null
        var Date2: Date? = null
        try {
            Date1 = sdf.parse(date1)
            Date2 = sdf.parse(date2)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ((Date2!!.time - Date1!!.time) / (24 * 60 * 60 * 1000)).toInt()
    }


}
