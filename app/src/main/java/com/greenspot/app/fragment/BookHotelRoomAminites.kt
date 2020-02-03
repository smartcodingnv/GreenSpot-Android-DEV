package com.greenspot.app.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.HotelRoomAmenitesAdapter
import com.greenspot.app.adapter.SliderHotelRoomAdapter
import com.greenspot.app.responce.bookinginfohotel.ResponceBookinginfoHotel
import com.greenspot.app.responce.hoteldetails.ResponceHotelDetails
import com.greenspot.app.responce.hoteldetails.RoomImagesItem
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.content_hotel_details.*

import kotlinx.android.synthetic.main.fragment_hotel_room_aminites.*
import java.util.ArrayList


class BookHotelRoomAminites : Fragment() {
    // TODO: Rename and change types of parameters

    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var checkOther: Int = 0

    var roomAminites = ArrayList<String>()
    var roomOptions = ArrayList<String>()
    var roomInclude = ArrayList<String>()
    private var roomId: String? =""

    fun newInstance(checkfrag: Int): BookHotelRoomAminites {

        arguments = Bundle()
        val fragment = BookHotelRoomAminites()
        arguments!!.putInt(AppConfig.BUNDLE.CHECKAOTHER, checkfrag)
        fragment.setArguments(arguments)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        progress = Progress(this.activity!!)
        utils = Utils(this.activity!!)
        helper = PreferenceHelper(this.activity!!, AppConfig.PREFERENCE.PREF_FILE)
        return inflater.inflate(R.layout.fragment_hotel_room_aminites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val gson = Gson()
        val hoteldetails =
            gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.HOTELBOOKINGINFO, ""),
                ResponceBookinginfoHotel::class.java

            )

        roomId = helper!!.LoadStringPref(AppConfig.PREFERENCE.ROOMMASTERID,"")

        for (room in hoteldetails.data.roomInfo!!){

            if(room.masterId.equals(roomId)){
                roomAminites = room.other as ArrayList<String>
                roomOptions = room.options as ArrayList<String>
                roomInclude = room.includeThisPrice as ArrayList<String>
//                gallaryImageList = room.roomImages as ArrayList<RoomImagesItem>
            }
        }

        if(checkOther==1){
//            roomAminites = hoteldetails.data.rooms[0].other as ArrayList<String>
            setAminites()
        }else if(checkOther==2){
//            roomOptions = hoteldetails.data.rooms!![0].options as ArrayList<String>
            setOptions()
        }else if(checkOther==3){
//            roomInclude = hoteldetails.data.rooms!![0].includeThisPrice as ArrayList<String>
            setINcludeItem()
        }



    }

    private fun setAminites() {

        val sliderTourAdapter = HotelRoomAmenitesAdapter(activity!!)
        Common.setVerticalRecyclerView(activity!!, rv_roomamenites)
        sliderTourAdapter.swapData(roomAminites)
        rv_roomamenites.adapter = sliderTourAdapter


    }

    private fun setOptions() {

        val sliderTourAdapter = HotelRoomAmenitesAdapter(activity!!)
        Common.setVerticalRecyclerView(activity!!, rv_roomamenites)
        sliderTourAdapter.swapData(roomOptions)
        rv_roomamenites.adapter = sliderTourAdapter


    }

    private fun setINcludeItem() {

        val sliderTourAdapter = HotelRoomAmenitesAdapter(activity!!)
        Common.setVerticalRecyclerView(activity!!, rv_roomamenites)
        sliderTourAdapter.swapData(roomInclude)
        rv_roomamenites.adapter = sliderTourAdapter


    }

    // TODO: Rename method, update argument and hook method into UI event

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt(AppConfig.BUNDLE.CHECKAOTHER)?.let {
            checkOther = it
        }
    }
    override fun onDetach() {
        super.onDetach()

    }

}
