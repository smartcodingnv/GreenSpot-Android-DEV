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
import com.greenspot.app.adapter.PlaceTitleAdapter
import com.greenspot.app.model.PlaceAvaliblityTitle
import com.greenspot.app.model.PlaceSubItem
import com.greenspot.app.responce.recreationdetails.ResponceRecDetails
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.fragment_place_availabality.*
import kotlinx.android.synthetic.main.fragment_place_availabality.view.*
import java.util.*


class PlaceAvailabalityFragment : Fragment() {
    // TODO: Rename and change types of parameters


    private var place_open_time: String = ""
    private var place_close_time: String = ""
    private var office_open_time: String = ""
    private var office_close_time: String = ""
    private var address: String = ""
    private var office_address: String = ""
    private var phone: String = ""
    private var mobile: String = ""
    private var email: String = ""
    private var known_for: String = ""
    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null


    var placeTitle: ArrayList<PlaceAvaliblityTitle> = ArrayList()
    var placeData: ArrayList<PlaceSubItem> = ArrayList()
    var officeData: ArrayList<PlaceSubItem> = ArrayList()
    var otherData: ArrayList<PlaceSubItem> = ArrayList()
    private lateinit var mView: View

    fun newInstance(): PlaceAvailabalityFragment {

        val args = Bundle()

        val fragment = PlaceAvailabalityFragment()
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
        return inflater.inflate(R.layout.fragment_place_availabality, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view

        val gson = Gson()
        val respncedetails =
            gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.PLACEDETAILSRESPONCE, ""),
                ResponceRecDetails::class.java
            )

        place_open_time = respncedetails.data.availability.placeOpenTime
        place_close_time = respncedetails.data.availability.placeCloseTime
        office_open_time = respncedetails.data.availability.officeOpenTime
        office_close_time = respncedetails.data.availability.officeCloseTime
        address = respncedetails.data.availability.address
        office_address = respncedetails.data.availability.officeAddress
        phone = respncedetails.data.availability.phone
        mobile = respncedetails.data.availability.mobile
        email = respncedetails.data.availability.email
        known_for = respncedetails.data.availability.knownFor


        initView()

    }

    private fun initView() {

        placeData.clear()
        officeData.clear()
        placeTitle.clear()
        otherData.clear()

        setPlaceData()
        setUpOfficeData()
        setUpPlaceData()
        setUpOtherData()

        val visiterPlaceAdapter = PlaceTitleAdapter(activity)

        Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
        visiterPlaceAdapter.swapData(placeTitle)
        rv_placemenu.adapter = visiterPlaceAdapter

        val placeDetailsAdapter = PlaceDetailsAdapter(activity)
        Common.setVerticalRecyclerView(context!!, rv_placeinformation)
        placeDetailsAdapter.swapData(otherData)
        rv_placeinformation.adapter = placeDetailsAdapter
    }


    // TODO: Rename method, update argument and hook method into UI event

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

    }

    private fun setUpPlaceData() {
        if(placeData.size>0){
            placeTitle.add(
                PlaceAvaliblityTitle(
                    "1",
                    getString(R.string.item_place),
                    placeData

                )
            )

        }
        if(officeData.size > 0){
            placeTitle.add(
                PlaceAvaliblityTitle(
                    "2",
                    getString(R.string.item_office),
                    officeData

                )
            )
        }

    }

    private fun setPlaceData() {
        if (place_open_time.isNotEmpty()) {
            placeData.add(
                PlaceSubItem(
                    "1",
                    getString(R.string.item_opentime),
                    place_open_time
                )
            )
        }

        if (place_close_time.isNotEmpty()) {
            placeData.add(
                PlaceSubItem(
                    "2",
                    getString(R.string.item_closetime),
                    place_close_time
                )
            )
        }


        if (address.isNotEmpty()) {
            placeData.add(
                PlaceSubItem(
                    "3",
                    getString(R.string.item_address),
                    address
                )
            )
        }

    }

    private fun setUpOfficeData() {

        if (office_open_time.isNotEmpty()) {
            officeData.add(
                PlaceSubItem(
                    "1",
                    getString(R.string.item_opentime),
                    office_open_time
                )
            )
        }

        if (office_close_time.isNotEmpty()) {
            officeData.add(
                PlaceSubItem(
                    "2",
                    getString(R.string.item_closetime),
                    office_close_time
                )
            )

        }


        if (office_address.isNotEmpty()) {

            officeData.add(
                PlaceSubItem(
                    "3",
                    getString(R.string.item_officeaddress),
                    office_address
                )
            )
        }

    }


    private fun setUpOtherData() {
        if (phone.isNotEmpty()) {
            otherData.add(
                PlaceSubItem(
                    "1",
                    getString(R.string.item_phoneno),
                    phone
                )
            )
        }

        if (mobile.isNotEmpty()) {

            otherData.add(
                PlaceSubItem(
                    "2",
                    getString(R.string.item_mobileno),
                    mobile
                )
            )
        }


        if (email.isNotEmpty()) {
            otherData.add(
                PlaceSubItem(
                    "3",
                    getString(R.string.res_email),
                    email
                )
            )
        }

        if (known_for.isNotEmpty()) {
            otherData.add(
                PlaceSubItem(
                    "4",
                    getString(R.string.res_know_more),
                    known_for
                )
            )
        }

    }


}
