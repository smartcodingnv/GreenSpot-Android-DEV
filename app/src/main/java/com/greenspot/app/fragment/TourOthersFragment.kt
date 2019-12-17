package com.greenspot.app.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.TourOtherAdapter
import com.greenspot.app.model.PlaceSubItem
import com.greenspot.app.responce.tourdetail.ResponceTourDetails
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.fragment_place_availabality.*
import kotlinx.android.synthetic.main.fragment_place_availabality.view.*
import java.util.*


class TourOthersFragment : Fragment() {


    private var importantNote: String = ""
    private var termsandCondition: String = ""
    private lateinit var mView: View


    var toursubeData: ArrayList<PlaceSubItem> = ArrayList()

    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null


    fun newInstance(): TourOthersFragment {

        val args = Bundle()
        val fragment = TourOthersFragment()

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
        return inflater.inflate(R.layout.fragment_place_anemities, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mView = view


        val gson = Gson()
        val tourdetails =
            gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.TOURDETAILSRESPONCE, ""),
                ResponceTourDetails::class.java
            )

        importantNote = tourdetails.data.others.importantNote
        termsandCondition = tourdetails.data.others.termAndCondition

        initView()

    }

    private fun initView() {


        toursubeData.clear()



        setTourSubData()

        val visiterPlaceAdapter = TourOtherAdapter(activity)

        Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
        visiterPlaceAdapter.swapData(toursubeData)
        rv_placemenu.adapter = visiterPlaceAdapter


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)


    }

    override fun onDetach() {
        super.onDetach()

    }


    private fun setTourSubData() {
        if (importantNote.isNotEmpty()) {
            toursubeData.add(
                PlaceSubItem(
                    "1",
                    "Important Notes",
                    importantNote
                )
            )
        }

        if (termsandCondition.isNotEmpty()) {
            toursubeData.add(
                PlaceSubItem(
                    "1",
                    "Terms & Conditions",
                    termsandCondition
                )
            )

        }


    }


}
