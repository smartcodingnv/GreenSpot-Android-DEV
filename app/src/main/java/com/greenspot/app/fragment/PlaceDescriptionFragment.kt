package com.greenspot.app.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.greenspot.app.model.PlaceAvaliblityTitle
import com.greenspot.app.responce.bookinginfohotel.ResponceBookinginfoHotel
import com.greenspot.app.responce.eventdetails.EvnetDetailsResponce
import com.greenspot.app.responce.hoteldetails.ResponceHotelDetails
import com.greenspot.app.responce.recreationdetails.ResponceRecDetails
import com.greenspot.app.responce.tourdetail.ResponceTourDetails
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import com.greenspot.app.utils.Progress
import com.greenspot.app.utils.Utils
import kotlinx.android.synthetic.main.fragment_place_description.*
import java.util.*


class PlaceDescriptionFragment : Fragment() {


    private var checkDiscription: Int = 0

    var placeTitle: ArrayList<PlaceAvaliblityTitle> = ArrayList()
    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    fun newInstance(checkflag: Int): PlaceDescriptionFragment {

        arguments = Bundle()
        val fragment = PlaceDescriptionFragment()
        arguments!!.putInt(AppConfig.BUNDLE.CHECKDISCRIPTION, checkflag)
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

        return inflater.inflate(
            com.greenspot.app.R.layout.fragment_place_description,
            container,
            false
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (checkDiscription == 1) {


            val gson = Gson()
            val respncedetails =
                gson.fromJson(
                    helper!!.LoadStringPref(AppConfig.PREFERENCE.PLACEDETAILSRESPONCE, ""),
                    ResponceRecDetails::class.java
                )
            lay_moredesc.visibility = View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txt_placedesc.text = Html.fromHtml(
                    respncedetails.data.description.description,
                    Html.FROM_HTML_MODE_COMPACT
                )
            } else {
                txt_placedesc.text = Html.fromHtml(respncedetails.data.description.description)
            }

        } else if (checkDiscription == 2) {

            val gson = Gson()
            val tourdetails =
                gson.fromJson(
                    helper!!.LoadStringPref(AppConfig.PREFERENCE.TOURDETAILSRESPONCE, ""),
                    ResponceTourDetails::class.java
                )
            lay_moredesc.visibility = View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txt_placedesc.text =
                    Html.fromHtml(tourdetails.data.ovierview.ovierview, Html.FROM_HTML_MODE_COMPACT)
            } else {
                txt_placedesc.text = Html.fromHtml(tourdetails.data.ovierview.ovierview)
            }

        } else if (checkDiscription == 3) {

            val gson = Gson()
            val eventdetails =
                gson.fromJson(
                    helper!!.LoadStringPref(AppConfig.PREFERENCE.EVENTDETAILSRESPONCE, ""),
                    EvnetDetailsResponce::class.java
                )
            lay_moredesc.visibility = View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txt_placedesc.text = Html.fromHtml(
                    eventdetails.data.ovierview.ovierview,
                    Html.FROM_HTML_MODE_COMPACT
                )
            } else {
                txt_placedesc.text = Html.fromHtml(eventdetails.data.ovierview.ovierview)
            }
        } else if (checkDiscription == 4) {

            val gson = Gson()
            val hoteldetails =
                gson.fromJson(
                    helper!!.LoadStringPref(AppConfig.PREFERENCE.HOTELDETAILSRESPONCE, ""),
                    ResponceHotelDetails::class.java
                )

            lay_moredesc.visibility = View.VISIBLE

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txt_placedesc.text = Html.fromHtml(
                    hoteldetails.data.description.description,
                    Html.FROM_HTML_MODE_COMPACT
                )
            } else {
                txt_placedesc.text = Html.fromHtml(hoteldetails.data.description.description)
            }



            if (hoteldetails.data.description.propertyType.isNotEmpty()) {

                lable_property.visibility = View.VISIBLE
                txt_proptype.visibility = View.VISIBLE
                txt_proptype.text = hoteldetails.data.description.propertyType
            }
            if (hoteldetails.data.description.suitableFor.isNotEmpty()) {
                lable_suitable.visibility = View.VISIBLE
                txt_suitable.visibility = View.VISIBLE
                txt_suitable.text = hoteldetails.data.description.suitableFor

            }


        } else if (checkDiscription == 5) {

            val gson = Gson()
            val hoteldetails =
                gson.fromJson(
                    helper!!.LoadStringPref(AppConfig.PREFERENCE.HOTELBOOKINGINFO, ""),
                    ResponceBookinginfoHotel::class.java
                )

            lay_moredesc.visibility = View.VISIBLE

            lable_hotelname.visibility = View.VISIBLE
            txt_hotelname.visibility = View.VISIBLE
            lable_address.visibility = View.VISIBLE
            txt_address.visibility = View.VISIBLE
            lable_location.visibility = View.VISIBLE
            txt_location.visibility = View.VISIBLE

            txt_hotelname.text = hoteldetails.data.hotelData.name
            txt_address.text = hoteldetails.data.hotelData.address
            txt_location.text = hoteldetails.data.hotelData.city + ", " + hoteldetails.data.hotelData.district + ", " + hoteldetails.data.hotelData.country


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txt_placedesc.text = Html.fromHtml(
                    hoteldetails.data.hotelData.detailDescription,
                    Html.FROM_HTML_MODE_COMPACT
                )
            } else {
                txt_placedesc.text = Html.fromHtml(hoteldetails.data.hotelData.detailDescription)
            }

            if (hoteldetails.data.hotelData.propertyTypes.isNotEmpty()) {

                lable_property.visibility = View.VISIBLE
                txt_proptype.visibility = View.VISIBLE
                txt_proptype.text = hoteldetails.data.hotelData.propertyTypes
            }
            if (hoteldetails.data.hotelData.suitableFors.isNotEmpty()) {
                lable_suitable.visibility = View.VISIBLE
                txt_suitable.visibility = View.VISIBLE
                txt_suitable.text = hoteldetails.data.hotelData.suitableFors

            }


        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt(AppConfig.BUNDLE.CHECKDISCRIPTION)?.let {
            checkDiscription = it
        }
    }

    override fun onDetach() {
        super.onDetach()

    }


}
