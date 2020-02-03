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
import com.greenspot.app.responce.bookinginfo.ResponceBookinginfo
import com.greenspot.app.responce.bookinginfoevent.ResponceBookinginfoEvent
import com.greenspot.app.responce.bookinginfohotel.ResponceBookinginfoHotel

import com.greenspot.app.responce.eventdetails.EvnetDetailsResponce
import com.greenspot.app.responce.hoteldetails.ResponceHotelDetails
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

    var hotelcontactusdata: ArrayList<PlaceSubItem> = ArrayList()
    var eventcontactusdata: ArrayList<PlaceSubItem> = ArrayList()
    var tourcontactusdata: ArrayList<PlaceSubItem> = ArrayList()
    private var bookingContactus: ArrayList<PlaceSubItem> = ArrayList()

    private var otherRecordsMy: ArrayList<OtherItem>? = ArrayList()

    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null

    private var addrss: String = ""
    private var contactperon: String = ""
    private var office_open_time: String = ""
    private var office_close_time: String = ""
    private var phone: String = ""
    private var mobileno: String = ""
    private var faxno: String = ""
    private var email: String = ""
    private var whatsuppno: String = ""
    private var spName: String = ""
    private var spAddress: String = ""
    private var spPhone: String = ""
    private var spEmail: String = ""

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
        } else if (checkOther == 4) {

            val gson = Gson()
            val bookinginfo = gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.BOOKINGINFO, ""),
                ResponceBookinginfo::class.java
            )

            spName = bookinginfo.data.tourData.spFirstName + " " + bookinginfo.data.tourData.spLastName
            spAddress = bookinginfo.data.tourData.spAddress
            spPhone = bookinginfo.data.tourData.spPhone
            spEmail = bookinginfo.data.tourData.spEmail

        }else if (checkOther == 5) {

            val gson = Gson()
            val bookinginfo = gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.EVENTBOOKINGINFO, ""),
                ResponceBookinginfoEvent::class.java
            )

            addrss = bookinginfo.data.eventData.address
            office_open_time = bookinginfo.data.eventData.officeOpenTime
            office_close_time = bookinginfo.data.eventData.officeCloseTime
            phone = bookinginfo.data.eventData.phone
            email = bookinginfo.data.eventData.email


        }else if(checkOther==6){
            val gson = Gson()
            val hoteldetal = gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.HOTELDETAILSRESPONCE, ""),
                ResponceHotelDetails::class.java
            )

            contactperon = hoteldetal.data.contactUs.contactPersonName
            addrss = hoteldetal.data.contactUs.address
            email = hoteldetal.data.contactUs.email
            whatsuppno = hoteldetal.data.contactUs.whatsappNumber

            if(hoteldetal.data.contactUs.landline2.isNotEmpty()){
                phone = hoteldetal.data.contactUs.landline + ", "+hoteldetal.data.contactUs.landline2
            }else{
                phone = hoteldetal.data.contactUs.landline
            }

            if(hoteldetal.data.contactUs.mobile2.isNotEmpty()){
                mobileno = hoteldetal.data.contactUs.mobile + ", "+hoteldetal.data.contactUs.mobile2
            }else{
                mobileno = hoteldetal.data.contactUs.mobile
            }
            faxno = hoteldetal.data.contactUs.fax

        }else if(checkOther==7){
            val gson = Gson()
            val hoteldetal = gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.HOTELBOOKINGINFO, ""),
                ResponceBookinginfoHotel::class.java
            )


            contactperon = hoteldetal.data.hotelData.contactPersonName
            addrss = hoteldetal.data.hotelData.address
            email = hoteldetal.data.hotelData.email
            whatsuppno = hoteldetal.data.hotelData.whatsappNumber
            phone = hoteldetal.data.hotelData.landline
            mobileno = hoteldetal.data.hotelData.mobile

            faxno = hoteldetal.data.hotelData.fax

        }

        initView()

    }

    private fun initView() {


        if (checkOther == 1) {


            val placeOtherAdapter = PlaceOtherAdapter(activity)
            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            placeOtherAdapter.swapData(this.otherRecordsMy!!)
            rv_placemenu.adapter = placeOtherAdapter


        } else if (checkOther == 2) {

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

        } else if (checkOther == 4) {
            //booking info contact us

            bookingContactus.clear()
            setUpBookingContacusData()

            val placedetailsAdapter = PlaceDetailsAdapter(activity)
            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            placedetailsAdapter.swapData(bookingContactus)
            rv_placemenu.adapter = placedetailsAdapter

        }
        else if (checkOther == 5) {
            //booking info contact us

            eventcontactusdata.clear()
            setUpEventContactusData()

            val placedetailsAdapter = PlaceDetailsAdapter(activity)
            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            placedetailsAdapter.swapData(eventcontactusdata)
            rv_placemenu.adapter = placedetailsAdapter

        }else if(checkOther == 6){

            hotelcontactusdata.clear()
            setUpHotelContactusData()
            val placedetailsAdapter = PlaceDetailsAdapter(activity)
            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            placedetailsAdapter.swapData(hotelcontactusdata)
            rv_placemenu.adapter = placedetailsAdapter
        }else if(checkOther == 7){

            hotelcontactusdata.clear()
            setUpHotelContactusData()
            val placedetailsAdapter = PlaceDetailsAdapter(activity)
            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            placedetailsAdapter.swapData(hotelcontactusdata)
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


    private fun setUpHotelContactusData() {

        if (contactperon.isNotEmpty()) {
            hotelcontactusdata.add(
                PlaceSubItem(
                    "1",
                    getString(R.string.rcontact_person)+" :",
                    contactperon
                )
            )
        }
        if (addrss.isNotEmpty()) {
            hotelcontactusdata.add(
                PlaceSubItem(
                    "2",
                    getString(R.string.item_address)+" :",
                    addrss
                )
            )
        }
        if (mobileno.isNotEmpty()) {
            hotelcontactusdata.add(
                PlaceSubItem(
                    "3",
                   getString(R.string.res_phone_no) +" :",
                    mobileno
                )
            )
        }

        if (phone.isNotEmpty()) {
            hotelcontactusdata.add(
                PlaceSubItem(
                    "4",
                    getString(R.string.item_phoneno)+" :",
                    phone
                )
            )
        }
        if (email.isNotEmpty()) {
            hotelcontactusdata.add(
                PlaceSubItem(
                    "5",
                    getString(R.string.res_email)+" :",
                    email
                )
            )
        }

        if (faxno.isNotEmpty()) {
            hotelcontactusdata.add(
                PlaceSubItem(
                    "6",
                    getString(R.string.str_fax)+" :",
                    faxno
                )
            )
        }


    }


    private fun setUpEventContactusData() {

        if (addrss.isNotEmpty()) {
            eventcontactusdata.add(
                PlaceSubItem(
                    "1",
                    getString(R.string.item_address)+" :",
                    addrss
                )
            )
        }
        if (office_open_time.isNotEmpty()) {
            eventcontactusdata.add(
                PlaceSubItem(
                    "2",
                   getString(R.string.item_officeopentime) +" :",
                    office_open_time
                )
            )
        }
        if (office_close_time.isNotEmpty()) {
            eventcontactusdata.add(
                PlaceSubItem(
                    "3",
                    getString(R.string.item_officeclosttime)+" :",
                    office_close_time
                )
            )
        }

        if (phone.isNotEmpty()) {
            eventcontactusdata.add(
                PlaceSubItem(
                    "4",
                    getString(R.string.item_phoneno)+" :",
                    phone
                )
            )
        }
        if (email.isNotEmpty()) {
            eventcontactusdata.add(
                PlaceSubItem(
                    "4",
                    getString(R.string.res_email)+" :",
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
                    getString(R.string.item_address)+" :",
                    addrss
                )
            )
        }

        if (phone.isNotEmpty()) {
            tourcontactusdata.add(
                PlaceSubItem(
                    "4",
                    getString(R.string.item_phoneno)+" :",
                    phone
                )
            )
        }
        if (email.isNotEmpty()) {
            tourcontactusdata.add(
                PlaceSubItem(
                    "4",
                    getString(R.string.res_email)+" :",
                    email
                )
            )
        }


    }


    private fun setUpBookingContacusData() {

        if (spName.isNotEmpty()) {
            bookingContactus.add(
                PlaceSubItem(
                    "1",
                    getString(R.string.res_name)+" :",
                    spName
                )
            )
        }

        if (spAddress.isNotEmpty()) {
            bookingContactus.add(
                PlaceSubItem(
                    "2",
                    getString(R.string.item_address)+" :",
                    spAddress
                )
            )
        }
        if (phone.isNotEmpty()) {
            bookingContactus.add(
                PlaceSubItem(
                    "3",
                    getString(R.string.item_phoneno)+" :",
                    spPhone
                )
            )
        }

        if (spEmail.isNotEmpty()) {
            bookingContactus.add(
                PlaceSubItem(
                    "3",
                    getString(R.string.res_email)+" :",
                    spEmail
                )
            )
        }


    }


}
