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
import com.greenspot.app.R
import com.greenspot.app.adapter.BookingHotelPolicyAdapter
import com.greenspot.app.adapter.HotelPolicyAdapter
import com.greenspot.app.adapter.TourOtherAdapter
import com.greenspot.app.model.PlaceSubItem
import com.greenspot.app.responce.bookinginfohotel.ResponceBookinginfoHotel
import com.greenspot.app.responce.hoteldetails.RecordsItem
import com.greenspot.app.responce.hoteldetails.ResponceHotelDetails
import com.greenspot.app.responce.tourdetail.ResponceTourDetails
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.fragment_place_anemities.*
import kotlinx.android.synthetic.main.fragment_place_availabality.rv_placemenu
import kotlinx.android.synthetic.main.fragment_place_availabality.view.*
import java.util.*


class TourOthersFragment : Fragment() {


    private var policyRecordsMy: ArrayList<RecordsItem>? = ArrayList()
    private var bookpolicyRecordsMy: ArrayList<com.greenspot.app.responce.bookinginfohotel.RecordsItem>? = ArrayList()

    private var importantNote: String = ""
    private var termsandCondition: String = ""
    private lateinit var mView: View
    private var checkOther: Int = 0


    var toursubeData: ArrayList<PlaceSubItem> = ArrayList()

    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null


    fun newInstance(checkflag: Int): TourOthersFragment {

        arguments = Bundle()
        val fragment = TourOthersFragment()
        arguments!!.putInt(AppConfig.BUNDLE.CHECKTOUROTHER, checkflag)
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

            val gson = Gson()
            val tourdetails =
                gson.fromJson(
                    helper!!.LoadStringPref(AppConfig.PREFERENCE.TOURDETAILSRESPONCE, ""),
                    ResponceTourDetails::class.java
                )
            txt_otheractivity.visibility = View.GONE
            txt_labelother.visibility = View.GONE
            importantNote = tourdetails.data.others.importantNote
            termsandCondition = tourdetails.data.others.termAndCondition


        } else if (checkOther == 2) {

            val gson = Gson()
            val hoteldetails =
                gson.fromJson(
                    helper!!.LoadStringPref(AppConfig.PREFERENCE.HOTELDETAILSRESPONCE, ""),
                    ResponceHotelDetails::class.java
                )


            if(hoteldetails.data.policies.termAndPolicy.isNotEmpty()){

                txt_labelother.visibility = View.GONE
                txt_otheractivity.visibility = View.GONE

                txt_labetermspolicy.visibility = View.VISIBLE
                txt_termspolicy.visibility = View.VISIBLE

                txt_labetermspolicy.text = getString(R.string.str_termsandpolicy)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    txt_termspolicy.text = Html.fromHtml(
                        hoteldetails.data.policies.termAndPolicy,
                        Html.FROM_HTML_MODE_COMPACT
                    )
                } else {
                    txt_termspolicy.text = Html.fromHtml(hoteldetails.data.policies.termAndPolicy)
                }

            }
            policyRecordsMy!!.clear()
            for (otherdata in hoteldetails.data.policies.records!!) {
                if (otherdata.value != "") {
                    policyRecordsMy!!.add(otherdata)
                }

            }
        }else if (checkOther == 3) {

            val gson = Gson()
            val hoteldetails =
                gson.fromJson(
                    helper!!.LoadStringPref(AppConfig.PREFERENCE.HOTELBOOKINGINFO, ""),
                    ResponceBookinginfoHotel::class.java
                )


            if(hoteldetails.data.policies.termAndPolicy.isNotEmpty()){

                txt_labelother.visibility = View.GONE
                txt_otheractivity.visibility = View.GONE

                txt_labetermspolicy.visibility = View.VISIBLE
                txt_termspolicy.visibility = View.VISIBLE

                txt_labetermspolicy.text = getString(R.string.str_termsandpolicy)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    txt_termspolicy.text = Html.fromHtml(
                        hoteldetails.data.policies.termAndPolicy,
                        Html.FROM_HTML_MODE_COMPACT
                    )
                } else {
                    txt_termspolicy.text = Html.fromHtml(hoteldetails.data.policies.termAndPolicy)
                }

            }
            bookpolicyRecordsMy!!.clear()
            for (otherdata in hoteldetails.data.policies.records!!) {
                if (otherdata.value != "") {
                    bookpolicyRecordsMy!!.add(otherdata)
                }

            }
        }

        initView()

    }

    private fun initView() {


        if (checkOther == 1) {

            toursubeData.clear()
            setTourSubData()
            val visiterPlaceAdapter = TourOtherAdapter(activity)
            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            visiterPlaceAdapter.swapData(toursubeData)
            rv_placemenu.adapter = visiterPlaceAdapter

        } else if (checkOther == 2) {

            val hotelPolicyAdapter = HotelPolicyAdapter(activity)
            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            hotelPolicyAdapter.swapData(this.policyRecordsMy!!)
            rv_placemenu.adapter = hotelPolicyAdapter

        }else if (checkOther == 3) {

            val hotelPolicyAdapter = BookingHotelPolicyAdapter(activity)
            Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
            hotelPolicyAdapter.swapData(this.bookpolicyRecordsMy!!)
            rv_placemenu.adapter = hotelPolicyAdapter

        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt(AppConfig.BUNDLE.CHECKTOUROTHER)?.let {
            checkOther = it
        }
    }

    override fun onDetach() {
        super.onDetach()

    }


    private fun setTourSubData() {
        if (importantNote.isNotEmpty()) {
            toursubeData.add(
                PlaceSubItem(
                    "1",
                    getString(R.string.item_importantnote),
                    importantNote
                )
            )
        }

        if (termsandCondition.isNotEmpty()) {
            toursubeData.add(
                PlaceSubItem(
                    "1",
                    getString(R.string.res_terms_amp_conditions),
                    termsandCondition
                )
            )

        }


    }


}
