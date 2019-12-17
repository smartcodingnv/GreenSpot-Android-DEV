package com.greenspot.app.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.responce.recreationdetails.ResponceRecDetails
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import com.greenspot.app.utils.Progress
import com.greenspot.app.utils.Utils
import kotlinx.android.synthetic.main.fragment_place_contactus_fragmenet.*


class PlaceContactusFragmenet : Fragment() {
    private lateinit var mView: View

    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null

    fun newInstance(): PlaceContactusFragmenet {

        val args = Bundle()

        val fragment = PlaceContactusFragmenet()
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
        return inflater.inflate(R.layout.fragment_place_contactus_fragmenet, container, false)
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

        

        txt_address.text = respncedetails.data.contactUs.address
        txt_contactno.text = respncedetails.data.contactUs.phone
        txt_email.text = respncedetails.data.contactUs.email

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

    }


}
