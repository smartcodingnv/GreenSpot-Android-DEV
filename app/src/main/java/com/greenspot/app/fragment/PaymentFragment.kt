package com.greenspot.app.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.PlacePaymnetOtherAdapter
import com.greenspot.app.adapter.PlacePaymnetTitleAdapter
import com.greenspot.app.responce.recreationdetails.OtherRecordsItem
import com.greenspot.app.responce.recreationdetails.PaymentRecordsItem
import com.greenspot.app.responce.recreationdetails.RecordsItem
import com.greenspot.app.responce.recreationdetails.ResponceRecDetails
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.fragment_place_anemities.*
import kotlinx.android.synthetic.main.fragment_place_anemities.view.*
import kotlinx.android.synthetic.main.fragment_place_availabality.rv_placemenu
import kotlinx.android.synthetic.main.fragment_place_availabality.view.rv_placemenu
import java.util.*


class PaymentFragment : Fragment() {

    private lateinit var mView: View
    private var paymnetRecordsMy: ArrayList<PaymentRecordsItem>? = ArrayList()

    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var paymnetotherRecordsMy: ArrayList<OtherRecordsItem>? = ArrayList()

    fun newInstance(): PaymentFragment {

        arguments = Bundle()
        val fragment = PaymentFragment()
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

        val gson = Gson()
        val respncedetails =
            gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.PLACEDETAILSRESPONCE, ""),
                ResponceRecDetails::class.java
            )

        paymnetRecordsMy!!.clear()
        paymnetotherRecordsMy!!.clear()

        for (payment in respncedetails.data.payments.paymentRecords!!) {
            val paymnetsubRecordsMy: ArrayList<RecordsItem>? = ArrayList()
            for (temp1 in payment.records!!) {
                if (temp1.value != "") {
                    paymnetsubRecordsMy!!.add(temp1)
                }
            }
            if (paymnetsubRecordsMy!!.size > 0) {
                val temp2 = payment
                temp2.records = paymnetsubRecordsMy
                paymnetRecordsMy!!.add(temp2)
            }
        }

        for (otherdata in respncedetails.data.payments.otherRecords!!) {
            if (otherdata.value != "") {
                paymnetotherRecordsMy!!.add(otherdata)
            }

        }


        initView()

    }

    private fun initView() {


        val PlacePaymnetTitleAdapter = PlacePaymnetTitleAdapter(activity)
        Common.setVerticalRecyclerView(context!!, mView.rv_placemenu)
        PlacePaymnetTitleAdapter.swapData(this.paymnetRecordsMy!!)
        rv_placemenu.adapter = PlacePaymnetTitleAdapter

        val PlacePaymnetOtherAdapter = PlacePaymnetOtherAdapter(activity)
        Common.setVerticalRecyclerView(context!!, mView.rv_paymentother)
        PlacePaymnetOtherAdapter.swapData(this.paymnetotherRecordsMy!!)
        rv_paymentother.adapter = PlacePaymnetOtherAdapter


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)


    }

    override fun onDetach() {
        super.onDetach()

    }


}
