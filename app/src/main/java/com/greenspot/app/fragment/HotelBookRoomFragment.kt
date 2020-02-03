package com.greenspot.app.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.HotelBookRoomListAdapter
import com.greenspot.app.responce.bookinginfohotel.ResponceBookinginfoHotel
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.fragment_hotel_book_room.*


class HotelBookRoomFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private lateinit var mView: View





    fun newInstance(): HotelBookRoomFragment {

        val args = Bundle()

        val fragment = HotelBookRoomFragment()
        fragment.setArguments(args)
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

        return inflater.inflate(R.layout.fragment_hotel_book_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view

        val gson = Gson()
        val hoteldetails =
            gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.HOTELBOOKINGINFO, ""),
                ResponceBookinginfoHotel::class.java
            )

        val rv_bookroom = mView.findViewById<RecyclerView>(R.id.rv_bookroom)
        val hotelRoomListAdapter = HotelBookRoomListAdapter(activity)
        Common.setVerticalRecyclerView(this.activity!!, rv_bookroom)
        hotelRoomListAdapter.swapData(hoteldetails.data.roomInfo!!)
        rv_bookroom.adapter = hotelRoomListAdapter
    }




    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

    }




}
