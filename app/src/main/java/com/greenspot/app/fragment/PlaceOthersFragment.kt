package com.greenspot.app.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.PlaceDetailsAdapter
import com.greenspot.app.adapter.PlaceOtherAdapter
import com.greenspot.app.model.PlaceSubItem

import com.greenspot.app.responce.eventdetails.EvnetDetailsResponce
import com.greenspot.app.responce.recreationdetails.OtherItem
import com.greenspot.app.responce.recreationdetails.ResponceRecDetails
import com.greenspot.app.responce.tourdetail.ResponceTourDetails
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.fragment_place_availabality.*
import kotlinx.android.synthetic.main.fragment_place_availabality.view.*
import java.util.*


class PlaceOthersFragment : Fragment() {
    private lateinit var mView: View
    private var checkOther: Int = 0

    var eventcontactusdata: ArrayList<PlaceSubItem> = ArrayList()
    var tourcontactusdata: ArrayList<PlaceSubItem> = ArrayList()

    private var otherRecordsMy: ArrayList<OtherItem>? = ArrayList()

    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null

    private var addrss: String = ""
    private var office_open_time: String = ""
    private var office_close_time: String = ""
    private var phone: String = ""
    private var email: String = ""

    fun newInstance(checkfrag: Int): PlaceOthersFragment {

        arguments = Bundle()
        val fragment = PlaceOthersFragment()
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
        return inflater.inflate(R.layout.fragment_place_anemities, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mView = view

        if (checkOther == 1) {

            //vacation place other

            otherRecordsMy!!.clear()

            val gson = Gson()
            val respncedetails =
                gson.fromJson(
                    helper!!.LoadStringPref(AppConfig.PREFERENCE.PLACEDETAILSRESPONCE, ""),
                    ResponceRecDetails::class.java
                )

            for (otherdata in respncedetails.data.other!!) {
                if (otherdata.value != "") {
                    otherRecordsMy!!.add(otherdata)
                }

            }

        } else if (checkOther == 2) {

            //tour contact us
            val gson = Gson()
            val tourdetails =
                gson.fromJson(
                    helper!!.LoadStringPref(AppConfig.PREFERENCE.TOURDETAILSRESPONCE, ""),
                    ResponceTourDetails::class.java
                )

            addrss = tourdetails.data.contactUs.address
            phone = tourdetails.data.contactUs.phone
            email = tourdetails.data.contactUs.email

        } else if (checkOther == 3) {

            //event contact us

            val gson = Gson()
            val eventdetails =
                gson.fromJson(
                    helper!!.LoadStringPref(AppConfig.PREFERENCE.EVENTDETAILSRESPONCE, ""),
                    EvnetDetailsResponce::class.java
                )


            addrss = eventdetails.data.contactUs.address
            office_open_time = eventdetails.data.contactUs.officeOpenTime
            office_close_time = eventdetails.data.contactUs.officeCloseTime
            phone = eventdetails.data.contactUs.phone
            email = eventdetails.data.contactUs.email
        }

        initView()

    }

    private fun initView() {


        if (checkOther == 1) {


            val placeOtherAdapter = PlaceOtherAdapter(activity)
            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            placeOtherAdapter.swapData(this.otherRecordsMy!!)
            rv_placemenu.adapter = placeOtherAdapter


        }else if(checkOther==2){

            tourcontactusdata.clear()
            setUpTourContactusData()

            val placedetailsAdapter = PlaceDetailsAdapter(activity)
            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            placedetailsAdapter.swapData(tourcontactusdata)
            rv_placemenu.adapter = placedetailsAdapter




        } else if (checkOther == 3) {

            eventcontactusdata.clear()
            setUpEventContactusData()

            val placedetailsAdapter = PlaceDetailsAdapter(activity)
            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            placedetailsAdapter.swapData(eventcontactusdata)
            rv_placemenu.adapter = placedetailsAdapter

        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt(AppConfig.BUNDLE.CHECKAOTHER)?.let {
            checkOther = it
        }
    }

    override fun onDetach() {
        super.onDetach()

    }


    private fun setUpEventContactusData() {

        if (addrss.isNotEmpty()) {
            eventcontactusdata.add(
                PlaceSubItem(
                    "1",
                    "Address :",
                    addrss
                )
            )
        }
        if (office_open_time.isNotEmpty()) {
            eventcontactusdata.add(
                PlaceSubItem(
                    "2",
                    "Office Open Time :",
                    office_open_time
                )
            )
        }
        if (office_close_time.isNotEmpty()) {
            eventcontactusdata.add(
                PlaceSubItem(
                    "3",
                    "Office Close Time :",
                    office_close_time
                )
            )
        }

        if (phone.isNotEmpty()) {
            eventcontactusdata.add(
                PlaceSubItem(
                    "4",
                    "Phone :",
                    phone
                )
            )
        }
        if (email.isNotEmpty()) {
            eventcontactusdata.add(
                PlaceSubItem(
                    "4",
                    "Email :",
                    email
                )
            )
        }


    }


    private fun setUpTourContactusData() {

        if (addrss.isNotEmpty()) {
            tourcontactusdata.add(
                PlaceSubItem(
                    "1",
                    "Address :",
                    addrss
                )
            )
        }

        if (phone.isNotEmpty()) {
            tourcontactusdata.add(
                PlaceSubItem(
                    "4",
                    "Phone :",
                    phone
                )
            )
        }
        if (email.isNotEmpty()) {
            tourcontactusdata.add(
                PlaceSubItem(
                    "4",
                    "Email :",
                    email
                )
            )
        }


    }


}
