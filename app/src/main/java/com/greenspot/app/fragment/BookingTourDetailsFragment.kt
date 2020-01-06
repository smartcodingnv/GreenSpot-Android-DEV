package com.greenspot.app.fragment

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.responce.bookinginfo.ResponceBookinginfo
import com.greenspot.app.responce.bookinginfoevent.ResponceBookinginfoEvent
import com.greenspot.app.responce.bookinginforecreation.ResponceBookinginfoRecreation
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.fragment_tour_details.*


class BookingTourDetailsFragment : Fragment() {

    private val TAG = BookingTourDetailsFragment::class.java.simpleName


    private var progress: Progress? = null
    private var viewDialog: ViewDialog? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null
    private lateinit var mView: View
    private var checkDetails: Int = 0


    fun newInstance(checkflag: Int): BookingTourDetailsFragment {

        arguments = Bundle()
        val fragment = BookingTourDetailsFragment()
        arguments!!.putInt(AppConfig.BUNDLE.CHECKANIMATIES, checkflag)
        fragment.setArguments(arguments)
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        progress = Progress(this.activity!!)
        viewDialog = ViewDialog(this.activity!!)
        utils = Utils(this.activity!!)
        helper = PreferenceHelper(this.activity!!, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this.activity!!, AppConfig.PREFERENCE.PREF_FILE_LANG)

        return inflater.inflate(R.layout.fragment_tour_details, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view

        if (checkDetails == 1) {
            val gson = Gson()
            val bookinginfo = gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.BOOKINGINFO, ""),
                ResponceBookinginfo::class.java
            )

            lay_tour.visibility = View.VISIBLE
            lay_event.visibility = View.GONE
            lay_vacation.visibility = View.GONE


            Log.e(TAG, "  " + bookinginfo)
            Log.e(TAG, "  " + bookinginfo.data.tourData.packageName)

            txt_tourname.text = bookinginfo.data.tourData.packageName
            txt_location.text = bookinginfo.data.tourData.locations
            txt_duration.text =
                bookinginfo.data.tourData.nights.toString() + " Nights & " + bookinginfo.data.tourData.days.toString() + " Days"
            txt_departure.text = bookinginfo.data.tourData.departureCity
            txt_category.text = bookinginfo.data.tourData.category
            txt_type.text = bookinginfo.data.tourData.tourType

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txt_overview.text = Html.fromHtml(
                    bookinginfo.data.tourData.tourDetail,
                    Html.FROM_HTML_MODE_COMPACT
                )
                txt_importantnote.text = Html.fromHtml(
                    bookinginfo.data.tourData.importantNote,
                    Html.FROM_HTML_MODE_COMPACT
                )
                txt_termscon.text = Html.fromHtml(
                    bookinginfo.data.tourData.termAndCondition,
                    Html.FROM_HTML_MODE_COMPACT
                )
            } else {
                txt_overview.text = Html.fromHtml(bookinginfo.data.tourData.tourDetail)
                txt_importantnote.text = Html.fromHtml(bookinginfo.data.tourData.importantNote)
                txt_termscon.text = Html.fromHtml(bookinginfo.data.tourData.termAndCondition)
            }


        } else if (checkDetails == 2) {

            val gson = Gson()
            val bookinginfo = gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.EVENTBOOKINGINFO, ""),
                ResponceBookinginfoEvent::class.java
            )

            lay_tour.visibility = View.GONE
            lay_event.visibility = View.VISIBLE
            lay_vacation.visibility = View.GONE

            txt_eventname.text = bookinginfo.data.eventData.title
            if (bookinginfo.data.eventData.eventHappeningTypeId.equals("1")) {

                txt_eventdate.text = getString(R.string.res_everyday)

            } else if (bookinginfo.data.eventData.eventHappeningTypeId.equals("2")) {
                txt_eventdate.text =
                    bookinginfo.data.eventData.startDate + " To " + bookinginfo.data.eventData.endDate
            }
            txt_eventime.text =
                bookinginfo.data.eventData.startTime + " To " + bookinginfo.data.eventData.endTime
            txt_eventlocation.text =
                bookinginfo.data.eventData.city + ", " + bookinginfo.data.eventData.district + ", " + bookinginfo.data.eventData.country

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txt_eventoverview.text = Html.fromHtml(
                    bookinginfo.data.eventData.detail,
                    Html.FROM_HTML_MODE_COMPACT
                )

            } else {
                txt_eventoverview.text = Html.fromHtml(bookinginfo.data.eventData.detail)

            }


        } else if (checkDetails == 3) {

            val gson = Gson()
            val bookinginfo = gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.VACATIONBOOKINGINFO, ""),
                ResponceBookinginfoRecreation::class.java
            )

            lay_tour.visibility = View.GONE
            lay_event.visibility = View.GONE
            lay_vacation.visibility = View.VISIBLE

            txt_vacationname.text = bookinginfo.data.recreationData.placeName
            txt_vacationtlocation.text =
                bookinginfo.data.recreationData.city + ", " + bookinginfo.data.recreationData.district + ", " + bookinginfo.data.recreationData.country


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txt_vacationoverview.text = Html.fromHtml(
                    bookinginfo.data.recreationData.detailDescription,
                    Html.FROM_HTML_MODE_COMPACT
                )

            } else {
                txt_vacationoverview.text = Html.fromHtml(bookinginfo.data.recreationData.detailDescription)

            }


        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt(AppConfig.BUNDLE.CHECKANIMATIES)?.let {
            checkDetails = it
        }
    }

    override fun onDetach() {
        super.onDetach()

    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }


}
