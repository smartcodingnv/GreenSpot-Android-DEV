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
import com.greenspot.app.model.PlaceSubItem
import com.greenspot.app.responce.bookinginforecreation.ResponceBookinginfoRecreation
import com.greenspot.app.responce.recreationdetails.ResponceRecDetails
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.fragment_place_availabality.*
import kotlinx.android.synthetic.main.fragment_place_availabality.view.*
import kotlinx.android.synthetic.main.fragment_place_contactus_fragmenet.*
import java.util.ArrayList


class PlaceContactusFragmenet : Fragment() {
    private var checkother: Int =0
    private lateinit var mView: View

    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var addrss: String = ""
    private var phone: String = ""
    private var email: String = ""
    var eventcontactusdata: ArrayList<PlaceSubItem> = ArrayList()


    fun newInstance(checkflag: Int): PlaceContactusFragmenet {

        arguments = Bundle()
        val fragment = PlaceContactusFragmenet()
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

        if(checkother==1){

            val gson = Gson()
            val respncedetails =
                gson.fromJson(
                    helper!!.LoadStringPref(AppConfig.PREFERENCE.PLACEDETAILSRESPONCE, ""),
                    ResponceRecDetails::class.java
                )

            addrss = respncedetails.data.contactUs.address
            phone = respncedetails.data.contactUs.phone
            email = respncedetails.data.contactUs.email

            eventcontactusdata.clear()
            setUpEventContactusData()

            val placedetailsAdapter = PlaceDetailsAdapter(activity)
            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            placedetailsAdapter.swapData(eventcontactusdata)
            rv_placemenu.adapter = placedetailsAdapter

        }else if(checkother==2){
            val gson = Gson()
            val bookinginfoo = gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.VACATIONBOOKINGINFO, ""),
                ResponceBookinginfoRecreation::class.java
            )

            addrss = bookinginfoo.data.recreationData.spAddress
            phone = bookinginfoo.data.recreationData.spPhone
            email = bookinginfoo.data.recreationData.spEmail

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
        arguments?.getInt(AppConfig.BUNDLE.CHECKANIMATIES)?.let {
            checkother = it
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


}
