package com.greenspot.app.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.*
import com.greenspot.app.responce.bookinginfo.ResponceBookinginfo
import com.greenspot.app.responce.bookinginfo.TourAmenitiesItem
import com.greenspot.app.responce.bookinginfoevent.EventAmenitiesItem
import com.greenspot.app.responce.bookinginfoevent.ResponceBookinginfoEvent
import com.greenspot.app.responce.bookinginfohotel.ResponceBookinginfoHotel
import com.greenspot.app.responce.bookinginforecreation.ResponceBookinginfoRecreation
import com.greenspot.app.responce.eventdetails.AmenitiesItem
import com.greenspot.app.responce.eventdetails.EvnetDetailsResponce
import com.greenspot.app.responce.hoteldetails.ResponceHotelDetails
import com.greenspot.app.responce.recreationdetails.AmenitiesRecordsItem
import com.greenspot.app.responce.recreationdetails.RecordsItem
import com.greenspot.app.responce.recreationdetails.ResponceRecDetails
import com.greenspot.app.responce.tourdetail.ResponceTourDetails
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.fragment_place_anemities.*
import kotlinx.android.synthetic.main.fragment_place_availabality.rv_placemenu
import kotlinx.android.synthetic.main.fragment_place_availabality.view.*


class PlaceAnemitiesFragment : Fragment() {
    private var bookingrecreationAmenities: ArrayList<com.greenspot.app.responce.bookinginforecreation.AmenitiesItem>? =
        ArrayList()

    private var bookinghotelAmintes: ArrayList<com.greenspot.app.responce.bookinginfohotel.AmenitiesItem>? = ArrayList()
    private var hotelAmintes: ArrayList<com.greenspot.app.responce.hoteldetails.AmenitiesItem>? = ArrayList()
    private var bookingeventAmintes: ArrayList<EventAmenitiesItem>? = ArrayList()
    private var bookingTourAmintes: ArrayList<TourAmenitiesItem>? = ArrayList()
    private var otherActivity: String = ""
    private var amenitiesRecordsMy: ArrayList<AmenitiesRecordsItem>? = ArrayList()
    private var eventamenitiesRecordsMy: ArrayList<AmenitiesItem>? = ArrayList()
    private var touramenitiesRecordsMy: ArrayList<com.greenspot.app.responce.tourdetail.AmenitiesItem>? =
        ArrayList()
    private var checkAnimatities: Int = 0
    private lateinit var mView: View


    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null


    fun newInstance(checkflag: Int): PlaceAnemitiesFragment {

        arguments = Bundle()
        val fragment = PlaceAnemitiesFragment()
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

        if (checkAnimatities == 1) {

            val gson = Gson()
            val respncedetails =
                gson.fromJson(
                    helper!!.LoadStringPref(AppConfig.PREFERENCE.PLACEDETAILSRESPONCE, ""),
                    ResponceRecDetails::class.java
                )

            otherActivity = respncedetails.data.amenities.otherActivities
            amenitiesRecordsMy!!.clear()

            for (aminate in respncedetails.data.amenities.amenitiesRecords!!) {
                val amenitiessubRecordsMy: ArrayList<RecordsItem>? = ArrayList()
                for (temp1 in aminate.records!!) {
                    if (temp1.value != "") {
                        amenitiessubRecordsMy!!.add(temp1)
                    }
                }
                if (amenitiessubRecordsMy!!.size > 0) {
                    val temp2 = aminate
                    temp2.records = amenitiessubRecordsMy
                    amenitiesRecordsMy!!.add(temp2)
                }
            }

        } else if (checkAnimatities == 2) {

            val gson = Gson()
            val tourdetails =
                gson.fromJson(
                    helper!!.LoadStringPref(AppConfig.PREFERENCE.TOURDETAILSRESPONCE, ""),
                    ResponceTourDetails::class.java
                )


            touramenitiesRecordsMy!!.clear()

            for (aminate in tourdetails.data.amenities!!) {
                if (aminate.value != "") {
                    touramenitiesRecordsMy!!.add(aminate)
                }

            }


        } else if (checkAnimatities == 3) {


            val gson = Gson()
            val eventdetails =
                gson.fromJson(
                    helper!!.LoadStringPref(AppConfig.PREFERENCE.EVENTDETAILSRESPONCE, ""),
                    EvnetDetailsResponce::class.java
                )


            eventamenitiesRecordsMy!!.clear()

            for (aminate in eventdetails.data.amenities!!) {
                if (aminate.value != "") {
                    eventamenitiesRecordsMy!!.add(aminate)
                }

            }

        } else if (checkAnimatities == 4) {
            //booking info included facilites

            val gson = Gson()
            val bookinginfo = gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.BOOKINGINFO, ""),
                ResponceBookinginfo::class.java
            )

            bookingTourAmintes!!.clear()

            for (aminate in bookinginfo.data.tourAmenities!!) {
                if (aminate.value != "") {
                    bookingTourAmintes!!.add(aminate)
                }

            }


        } else if (checkAnimatities == 5) {
            //booking info included facilites

            val gson = Gson()
            val bookinginfo = gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.EVENTBOOKINGINFO, ""),
                ResponceBookinginfoEvent::class.java
            )

            bookingeventAmintes!!.clear()

            for (aminate in bookinginfo.data.eventAmenities!!) {
                if (aminate.value != "") {
                    bookingeventAmintes!!.add(aminate)
                }

            }


        } else if (checkAnimatities == 6) {

            val gson = Gson()
            val bookinginfoo = gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.VACATIONBOOKINGINFO, ""),
                ResponceBookinginfoRecreation::class.java
            )


            otherActivity = bookinginfoo.data.otherActivities
            bookingrecreationAmenities!!.clear()

            for (aminate in bookinginfoo.data.amenities!!) {
                val amenitiessubRecordsMy: ArrayList<com.greenspot.app.responce.bookinginforecreation.RecordsItem>? =
                    ArrayList()
                for (temp1 in aminate.records!!) {
                    if (temp1.value != "") {
                        amenitiessubRecordsMy!!.add(temp1)
                    }
                }
                if (amenitiessubRecordsMy!!.size > 0) {
                    val temp2 = aminate
                    temp2.records = amenitiessubRecordsMy
                    bookingrecreationAmenities!!.add(temp2)
                }
            }

        }else if (checkAnimatities == 7) {
            //hotel amenintes

            val gson = Gson()
            val hotelinfo = gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.HOTELDETAILSRESPONCE, ""),
                ResponceHotelDetails::class.java
            )

            hotelAmintes!!.clear()

            for (aminate in hotelinfo.data.amenities!!) {
                if (aminate.value != "") {
                    hotelAmintes!!.add(aminate)
                }

            }


        }else if (checkAnimatities == 8) {
            //hotel amenintes

            val gson = Gson()
            val hotelinfo = gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.HOTELBOOKINGINFO, ""),
                ResponceBookinginfoHotel::class.java
            )

            bookinghotelAmintes!!.clear()

            for (aminate in hotelinfo.data.amenities!!) {
                if (aminate.value != "") {
                    bookinghotelAmintes!!.add(aminate)
                }

            }


        }


        initView()


    }

    private fun initView() {


        if (checkAnimatities == 1) {


            if (otherActivity.isNullOrEmpty()) {
                txt_otheractivity.visibility = View.GONE
                txt_labelother.visibility = View.GONE
            } else {
                txt_otheractivity.visibility = View.VISIBLE
                txt_labelother.visibility = View.VISIBLE
                txt_otheractivity.text = otherActivity
            }


            val visiterPlaceAdapter = PlaceAminitesTitleAdapter(activity)

            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            visiterPlaceAdapter.swapData(this.amenitiesRecordsMy!!)
            rv_placemenu.adapter = visiterPlaceAdapter

        } else if (checkAnimatities == 2) {

            txt_otheractivity.visibility = View.GONE
            txt_labelother.visibility = View.GONE


            val tourAminitesTitleAdapter = TourAminitesTitleAdapter(activity)

            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            tourAminitesTitleAdapter.swapData(this.touramenitiesRecordsMy!!)
            rv_placemenu.adapter = tourAminitesTitleAdapter


        } else if (checkAnimatities == 3) {
            txt_otheractivity.visibility = View.GONE
            txt_labelother.visibility = View.GONE


            val eventAminitesTitleAdapter = EventAminitesTitleAdapter(activity)

            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            eventAminitesTitleAdapter.swapData(this.eventamenitiesRecordsMy!!)
            rv_placemenu.adapter = eventAminitesTitleAdapter
        } else if (checkAnimatities == 4) {

            txt_otheractivity.visibility = View.GONE
            txt_labelother.visibility = View.GONE


            val bookingTourAminitesTitleAdapter = BookingTourAminitesTitleAdapter(activity)

            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            bookingTourAminitesTitleAdapter.swapData(this.bookingTourAmintes!!)
            rv_placemenu.adapter = bookingTourAminitesTitleAdapter
        } else if (checkAnimatities == 5) {

            txt_otheractivity.visibility = View.GONE
            txt_labelother.visibility = View.GONE


            val bookingTourAminitesTitleAdapter = BookingEventAminitesTitleAdapter(activity)

            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            bookingTourAminitesTitleAdapter.swapData(this.bookingeventAmintes!!)
            rv_placemenu.adapter = bookingTourAminitesTitleAdapter
        } else if (checkAnimatities == 6) {

            if (otherActivity.isNullOrEmpty()) {
                txt_otheractivity.visibility = View.GONE
                txt_labelother.visibility = View.GONE
            } else {
                txt_otheractivity.visibility = View.VISIBLE
                txt_labelother.visibility = View.VISIBLE
                txt_otheractivity.text = otherActivity
            }


            val visiterPlaceAdapter = BookingVacationAminitesTitleAdapter(activity)

            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            visiterPlaceAdapter.swapData(this.bookingrecreationAmenities!!)
            rv_placemenu.adapter = visiterPlaceAdapter

        }else if (checkAnimatities == 7) {

            txt_otheractivity.visibility = View.GONE
            txt_labelother.visibility = View.GONE


            val hotelAminitesTitleAdapter = HotelAminitesTitleAdapter(activity)

            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            hotelAminitesTitleAdapter.swapData(this.hotelAmintes!!)
            rv_placemenu.adapter = hotelAminitesTitleAdapter
        }else if (checkAnimatities == 8) {

            txt_otheractivity.visibility = View.GONE
            txt_labelother.visibility = View.GONE


            val hotelAminitesTitleAdapter = BookingHotelAminitesTitleAdapter(activity)

            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            hotelAminitesTitleAdapter.swapData(this.bookinghotelAmintes!!)
            rv_placemenu.adapter = hotelAminitesTitleAdapter
        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt(AppConfig.BUNDLE.CHECKANIMATIES)?.let {
            checkAnimatities = it
        }

    }

    override fun onDetach() {
        super.onDetach()

    }

}
