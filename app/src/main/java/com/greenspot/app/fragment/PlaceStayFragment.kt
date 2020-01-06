package com.greenspot.app.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.BookingPlaceStayTitleAdapter
import com.greenspot.app.adapter.PlaceStayTitleAdapter
import com.greenspot.app.responce.bookinginforecreation.ResponceBookinginfoRecreation
import com.greenspot.app.responce.recreationdetails.RecordsItem
import com.greenspot.app.responce.recreationdetails.ResponceRecDetails
import com.greenspot.app.responce.recreationdetails.StayItem

import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.fragment_place_availabality.*
import kotlinx.android.synthetic.main.fragment_place_availabality.view.*
import java.util.*


class PlaceStayFragment : Fragment() {

    private lateinit var mView: View


    private var stayRecordsMy: ArrayList<StayItem>? = ArrayList()
    val bookingstayRecordsMy: ArrayList<com.greenspot.app.responce.bookinginforecreation.StayItem>? =
        ArrayList()
    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null

    private var checkStay: Int = 0


    fun newInstance(checkflag: Int): PlaceStayFragment {

        arguments = Bundle()
        val fragment = PlaceStayFragment()
        arguments!!.putInt(AppConfig.BUNDLE.CHECKANIMATIES, checkflag)
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

        return inflater.inflate(R.layout.fragment_place_anemities, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mView = view

        if(checkStay==1){


            val gson = Gson()
            val respncedetails =
                gson.fromJson(
                    helper!!.LoadStringPref(AppConfig.PREFERENCE.PLACEDETAILSRESPONCE, ""),
                    ResponceRecDetails::class.java
                )

            stayRecordsMy!!.clear()

            for (stay in respncedetails.data.stay!!) {
                val staysubRecordsMy: ArrayList<RecordsItem>? = ArrayList()
                for (temp1 in stay.records!!) {
                    if (temp1.value != "") {
                        staysubRecordsMy!!.add(temp1)
                    }
                }
                if (staysubRecordsMy!!.size > 0) {
                    val temp2 = stay
                    temp2.records = staysubRecordsMy
                    stayRecordsMy!!.add(temp2)
                }
            }

        }else if(checkStay==2){

            val gson = Gson()
            val bookinginfoo = gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.VACATIONBOOKINGINFO, ""),
                ResponceBookinginfoRecreation::class.java
            )

            bookingstayRecordsMy!!.clear()

            for (stay in bookinginfoo.data.stay!!) {
                val staysubRecordsMy: ArrayList<com.greenspot.app.responce.bookinginforecreation.RecordsItem>? = ArrayList()
                for (temp1 in stay.records!!) {
                    if (temp1.value != "") {
                        staysubRecordsMy!!.add(temp1)
                    }
                }
                if (staysubRecordsMy!!.size > 0) {
                    val temp2 = stay
                    temp2.records = staysubRecordsMy
                    bookingstayRecordsMy.add(temp2)
                }
            }


        }


        initView()

    }

    private fun initView() {

        if(checkStay==1){

            val visiterPlaceAdapter = PlaceStayTitleAdapter(activity)

            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            visiterPlaceAdapter.swapData(this.stayRecordsMy!!)
            rv_placemenu.adapter = visiterPlaceAdapter
        }else if(checkStay==2){

            val visiterPlaceAdapter = BookingPlaceStayTitleAdapter(activity)

            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            visiterPlaceAdapter.swapData(this.bookingstayRecordsMy!!)
            rv_placemenu.adapter = visiterPlaceAdapter

        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt(AppConfig.BUNDLE.CHECKANIMATIES)?.let {
            checkStay = it
        }
    }

    override fun onDetach() {
        super.onDetach()

    }




}
