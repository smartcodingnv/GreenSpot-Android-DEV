package com.greenspot.app.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.BookingAllpersonEventTitleAdapter
import com.greenspot.app.adapter.BookingAllpersonTitleAdapter
import com.greenspot.app.responce.bookinginfo.ResponceBookinginfo
import com.greenspot.app.responce.bookinginfoevent.ResponceBookinginfoEvent
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.fragment_booking_allperson_info.*
import kotlinx.android.synthetic.main.fragment_booking_allperson_info.view.*


class BookingAllpersonInfo : Fragment() {
    // TODO: Rename and change types of parameters

    private val TAG = BookingTourDetailsFragment::class.java.simpleName


    private var progress: Progress? = null
    private var viewDialog: ViewDialog? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null
    private lateinit var mView: View
    private var checkAllperson: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    fun newInstance(checkflag: Int): BookingAllpersonInfo {

        arguments = Bundle()
        val fragment = BookingAllpersonInfo()
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


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_allperson_info, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view

        if(checkAllperson==1){

            val gson = Gson()
            val bookinginfo = gson.fromJson(helper!!.LoadStringPref(AppConfig.PREFERENCE.BOOKINGINFO, ""),
                ResponceBookinginfo::class.java
            )


            val bookingAllpersonTitleAdapter = BookingAllpersonTitleAdapter(activity)
            Common.setVerticalRecyclerView(context!!, mView.rv_allperson)
            bookingAllpersonTitleAdapter.swapData(bookinginfo.data.allUsersInfo!!)
            bookinginfo.data.allUsersInfo!![0].setExpanded(true)
            rv_allperson.adapter = bookingAllpersonTitleAdapter

        }else if(checkAllperson==2){

            val gson = Gson()
            val bookinginfo = gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.EVENTBOOKINGINFO, ""),
                ResponceBookinginfoEvent::class.java
            )

            val bookingAllpersonTitleAdapter = BookingAllpersonEventTitleAdapter(activity)
            Common.setVerticalRecyclerView(context!!, mView.rv_allperson)
            bookingAllpersonTitleAdapter.swapData(bookinginfo.data.allUsersInfo!!)
            rv_allperson.adapter = bookingAllpersonTitleAdapter

        }




    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt(AppConfig.BUNDLE.CHECKANIMATIES)?.let {
            checkAllperson = it
        }
    }

    override fun onDetach() {
        super.onDetach()

    }


}
