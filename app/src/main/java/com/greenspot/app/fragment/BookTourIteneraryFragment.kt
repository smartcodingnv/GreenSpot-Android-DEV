package com.greenspot.app.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.BookItineraryTitleAdapter
import com.greenspot.app.adapter.ItineraryImgAdapter
import com.greenspot.app.adapter.ItineraryTitleAdapter
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.responce.bookinginfo.ItinerariesItem
import com.greenspot.app.responce.bookinginfo.ResponceBookinginfo
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.fragment_tour_itenerary.*
import kotlinx.android.synthetic.main.fragment_tour_itenerary.view.*


class BookTourIteneraryFragment : Fragment(), ItemClickListener {


    private var itineraryData: ArrayList<ItinerariesItem> = ArrayList()
    private lateinit var mView: View
    private var checkitinerary: Int = 0


    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null


    fun newInstance(): BookTourIteneraryFragment {

        val args = Bundle()
        val fragment = BookTourIteneraryFragment()

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

        return inflater.inflate(R.layout.fragment_tour_itenerary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view

        val gson = Gson()
        val bookinginfo = gson.fromJson(
            helper!!.LoadStringPref(AppConfig.PREFERENCE.BOOKINGINFO, ""),
            ResponceBookinginfo::class.java
        )


        itineraryData = bookinginfo.data.tourData.itineraries as ArrayList<ItinerariesItem>

        initView()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

    }

    private fun initView() {


        val itineraryTitleAdapter = BookItineraryTitleAdapter(activity, this)

        Common.setHorizontalRecyclerView(context!!, mView.rv_itenearyday)
        itineraryTitleAdapter.swapData(itineraryData)
        rv_itenearyday.adapter = itineraryTitleAdapter


        if (itineraryData[0].title.isEmpty()) {
            txt_itinearytitle.visibility = View.GONE
        } else {
            txt_itinearytitle.visibility = View.VISIBLE
            txt_itinearytitle.setText(itineraryData[0].title)
        }

        if (itineraryData[0].hotelInfo.isEmpty()) {
            txt_itinearyinfo.visibility = View.GONE
        } else {
            txt_itinearyinfo.visibility = View.VISIBLE
            txt_itinearyinfo.setText(itineraryData[0].hotelInfo)
        }

        if (itineraryData[0].hotelInclusion.isEmpty()) {
            txt_itinearyincusion.visibility = View.GONE
        } else {
            txt_itinearyincusion.visibility = View.VISIBLE
            txt_itinearyincusion.setText(itineraryData[0].hotelInclusion)
        }

        if (itineraryData[0].description.isEmpty()) {
            txt_itinearydesc.visibility = View.GONE
        } else {
            txt_itinearydesc.visibility = View.VISIBLE
            txt_itinearydesc.setText(itineraryData[0].description)
        }




    }


    override fun recyclerViewListClicked(v: View, position: Int, flag: Int) {


        if (itineraryData[position].title.isEmpty()) {
            txt_itinearytitle.visibility = View.GONE
        } else {
            txt_itinearytitle.visibility = View.VISIBLE
            txt_itinearytitle.setText(itineraryData[position].title)
        }

        if (itineraryData[position].hotelInfo.isEmpty()) {
            txt_itinearyinfo.visibility = View.GONE
        } else {
            txt_itinearyinfo.visibility = View.VISIBLE
            txt_itinearyinfo.setText(itineraryData[position].hotelInfo)
        }

        if (itineraryData[position].hotelInclusion.isEmpty()) {
            txt_itinearyincusion.visibility = View.GONE
        } else {
            txt_itinearyincusion.visibility = View.VISIBLE
            txt_itinearyincusion.setText(itineraryData[position].hotelInclusion)
        }

        if (itineraryData[position].description.isEmpty()) {
            txt_itinearydesc.visibility = View.GONE
        } else {
            txt_itinearydesc.visibility = View.VISIBLE
            txt_itinearydesc.setText(itineraryData[position].description)
        }



    }


    fun getURLForResource(resourceId: Int): String {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://" + com.greenspot.app.R::class.java!!.getPackage()!!.getName() + "/" + resourceId)
            .toString()
    }

}




