package com.greenspot.app.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.HotelRoomListAdapter
import com.greenspot.app.adapter.HotelSelectRoomAdapter
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.model.ListSelectRoom
import com.greenspot.app.responce.hoteldetails.ResponceHotelDetails
import com.greenspot.app.responce.hoteldetails.RoomsItem
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.activity_hotel_room_list.*
import kotlinx.android.synthetic.main.content_hotel_room_list.*
import org.jetbrains.anko.toast
import java.math.RoundingMode
import kotlin.math.roundToInt

class HotelRoomListActivity : AppCompatActivity(), ItemClickListener {

    private var hotelPrice: String? =""
    private var roomBookinglimit: Int = 0
    private var finalPrice: Float = 0.0F
    private lateinit var hotelselectRoomAdapter: HotelSelectRoomAdapter
    private var roomList: ArrayList<RoomsItem>? = ArrayList()
    private var selectroomList: ArrayList<ListSelectRoom>? = ArrayList()
    private var progress: Progress? = null
    private var viewDialog: ViewDialog? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null
    private var currncyCode: String? = ""
    private var countryID: String? = ""
    private var langCode: String? = ""
    private var hotelID: String = ""
    private var hotelName: String = ""
    private var token: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_room_list)


        progress = Progress(this)
        viewDialog = ViewDialog(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)

        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")
        token = "Bearer " + helper!!.LoadStringPref(AppConfig.PREFERENCE.AUTHTOKEN, "")

        val gson = Gson()
        val hoteldetails =
            gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.HOTELDETAILSRESPONCE, ""),
                ResponceHotelDetails::class.java
            )

        roomList = hoteldetails.data.rooms as ArrayList<RoomsItem>?

        roomBookinglimit = hoteldetails.data.mainRecords.roomBookingLimit

        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        btn_booknowhotel.setOnClickListener(View.OnClickListener {
            if (selectroomList!!.size == 0) {
                toast("Please select at least one room")
                return@OnClickListener
            }
            var totalAdult = 0
            var totalChild = 0
            for (totalpricee in selectroomList!!) {

                totalAdult += totalpricee.adult.toInt()
                totalChild += totalpricee.child.toInt()

            }

            val message =
                "Hey, You have selected " + selectroomList!!.size.toString() + " rooms and they will allow " + totalAdult + " adult and " + totalChild + " Children"

            alerthotelBooking(message)

//            toast()

        })

        initView()

    }

    private fun alerthotelBooking(message: String) {
        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage(message)
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            getString(R.string.res_continue),
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                val bundle = Bundle()
                val intent = Intent(this, HotelBookingActivity::class.java)
                intent.putExtra(AppConfig.EXTRA.HOTELROOMPRICE, hotelPrice)
                bundle.putSerializable(AppConfig.EXTRA.SELECTROOMLIST, selectroomList)
                intent.putExtras(bundle)
                startActivity(intent)
            })

        builder1.setNegativeButton(
            getString(R.string.str_editselection),
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        val alert11 = builder1.create()
        alert11.show()

    }

    private fun initView() {

        setRoomList()
        setSelectRoomList()
    }

    private fun setRoomList() {

        val hotelRoomListAdapter = HotelRoomListAdapter(this, this)
        Common.setVerticalRecyclerView(this, rv_roomlist)
        hotelRoomListAdapter.swapData(this.roomList!!)
        rv_roomlist.adapter = hotelRoomListAdapter
    }

    private fun setSelectRoomList() {

        hotelselectRoomAdapter = HotelSelectRoomAdapter(this, this, 1)
        Common.setHorizontalRecyclerView(this, rv_selectroom)
        hotelselectRoomAdapter.swapData(this.selectroomList!!)
        rv_selectroom.adapter = hotelselectRoomAdapter
    }

    override fun recyclerViewListClicked(v: View, position: Int, flag: Int) {
        var totalprice = 0.0F
        if (flag == 1) {

            if (roomBookinglimit == selectroomList!!.size) {
                toast(getString(R.string.msg_maxroomlimit) + roomBookinglimit)
                return
            }

            selectroomList!!.add(
                ListSelectRoom(
                    roomList!!.get(position).masterId,
                    roomList!!.get(position).roomType,
                    roomList!!.get(position).maximumAdultInRoom.toString(),
                    roomList!!.get(position).maximumChildInRoom.toString(),
                    roomList!!.get(position).finalPrice
                )
            )

            hotelselectRoomAdapter.notifyDataSetChanged()
            rv_selectroom.scrollToPosition(selectroomList!!.size - 1)

            Log.e("listsize", " " + selectroomList!!.size)

            for (totalpricee in selectroomList!!) {

                totalprice += totalpricee.roomPrice.toFloat()

            }

            if (totalprice != 0.0F) {
                hotelPrice = totalprice.toBigDecimal().setScale(2, RoundingMode.UP).toString()
                txt_totalprice.text = currncyCode + " " + totalprice.toBigDecimal().setScale(2, RoundingMode.UP).toString()
                txt_labelpernight.visibility = View.VISIBLE

            } else {

                txt_totalprice.text = getString(R.string.res_select_room)
                txt_labelpernight.visibility = View.GONE

            }

        } else if (flag == 2) {

            Log.e("listsize", " " + selectroomList!!.size)
            for (totalpricee in selectroomList!!) {

                totalprice += totalpricee.roomPrice.toFloat().toBigDecimal()
                    .setScale(2, RoundingMode.HALF_EVEN).toFloat()

            }

            if (totalprice != 0.0F) {
                hotelPrice = totalprice.toBigDecimal().setScale(2, RoundingMode.UP).toString()
                txt_totalprice.text = currncyCode + " " + totalprice.toBigDecimal().setScale(2, RoundingMode.UP).toString()
                txt_labelpernight.visibility = View.VISIBLE
            } else {
                txt_totalprice.text = getString(R.string.res_select_room)
                txt_labelpernight.visibility = View.GONE
            }

        }

    }
}
