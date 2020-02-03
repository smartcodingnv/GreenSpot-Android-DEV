package com.greenspot.app.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.activity.LoginActivity
import com.greenspot.app.activity.MainActivity
import com.greenspot.app.adapter.GuestReviewAdapter
import com.greenspot.app.network.ApiClient
import com.greenspot.app.network.ApiInterface
import com.greenspot.app.responce.ComanResponce
import com.greenspot.app.responce.PlaceReivewRecordsItem
import com.greenspot.app.responce.ResponcePlaceReview
import com.greenspot.app.responce.recreationdetails.ResponceRecDetails
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.fragment_place_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class PlaceReviewFragment : Fragment() {

    private var loading: Boolean = true
    private var previousTotal = 0

    private lateinit var guestReviewAdapter: GuestReviewAdapter
    private var userRateing: Int = 0
    // TODO: Rename and change types of parameters
    private lateinit var mView: View

    private var progress: Progress? = null
    private var viewDialog: ViewDialog? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null

    private var placeID: String = ""
    private var token: String = ""
    private var serviceProviderid: String = ""
    private var isLastpage: Int = 0
    private var pageNumber: Int = 1
    private var currncyCode: String? = ""
    private var countryID: String? = ""
    private var langCode: String? = ""
    private var mLayoutManager: LinearLayoutManager? = null
    private var txt_guestreview: TextView? = null
    private var et_reivew: EditText? = null
    private var rbgiverstar: RatingBar? = null

    private val guestreviewData: LinkedList<PlaceReivewRecordsItem> =
        LinkedList<PlaceReivewRecordsItem>()

    fun newInstance(): PlaceReviewFragment {

        val args = Bundle()

        val fragment = PlaceReviewFragment()
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
        viewDialog = ViewDialog(this.activity!!)
        utils = Utils(this.activity!!)
        helper = PreferenceHelper(this.activity!!, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this.activity!!, AppConfig.PREFERENCE.PREF_FILE_LANG)
        return inflater.inflate(com.greenspot.app.R.layout.fragment_place_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view

        txt_guestreview = mView.findViewById(R.id.txt_guestreview)
        et_reivew = mView.findViewById(R.id.et_reivew)
        rbgiverstar = mView.findViewById(R.id.rb_giverstar)


        placeID = helper!!.LoadStringPref(AppConfig.PREFERENCE.PLACEID, "")!!
        serviceProviderid = helper!!.LoadStringPref(AppConfig.PREFERENCE.SERVICEPROVIDERID, "")!!
        token = "Bearer " + helper!!.LoadStringPref(AppConfig.PREFERENCE.AUTHTOKEN, "")

        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")

        val gson = Gson()
        val respncedetails =
            gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.PLACEDETAILSRESPONCE, ""),
                ResponceRecDetails::class.java
            )

        initView()


        val taxtrating =
            respncedetails.data.mainRecords.totalReviews.toString() + " " + getString(R.string.str_reviews) + "  " +
                    respncedetails.data.mainRecords.avgReviews.toFloat() + "/5.0"
        txt_rating.text = taxtrating
        rt_placereivew.rating = respncedetails.data.mainRecords.avgReviews.toFloat()




        rb_giverstar.setOnTouchListener(View.OnTouchListener { v, event ->

            rb_giverstar.onTouchEvent(event)
        })



        rb_giverstar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->

            userRateing = ratingBar.rating.toInt()
        }

        btn_submitrating.setOnClickListener(View.OnClickListener {

            if (helper!!.LoadIntPref(AppConfig.PREFERENCE.CHECK_LOGINAS, 0) == 2) {
                submitRivew()
            } else {
                alertLogin()
            }
        })
    }

    private fun submitRivew() {

        if (userRateing == 0) {
            Toast.makeText(
                activity,
                getString(R.string.res_selectrating),
                Toast.LENGTH_SHORT
            ).show()
            return
        } else if (et_reivew!!.text.isNullOrEmpty()) {
            Toast.makeText(
                activity,
                getString(R.string.res_reivewvalidation),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        submitReviewData(
            contryID = countryID!!,
            selectCurrency = currncyCode!!,
            langCode = langCode!!
        )


    }


    private fun initView() {

        guestreviewData.clear()
        getReviewData(
            contryID = countryID!!,
            selectCurrency = currncyCode!!,
            langCode = langCode!!
        )

        mLayoutManager = LinearLayoutManager(activity)
        rv_guestreview.setLayoutManager(mLayoutManager)
        guestReviewAdapter = GuestReviewAdapter(activity)
        guestReviewAdapter.swapData(guestreviewData)
        rv_guestreview.adapter = guestReviewAdapter
        setRecyclerViewScrollListener()


    }


    private fun setRecyclerViewScrollListener() {

        rv_guestreview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {

                    val visibleItemCount = mLayoutManager!!.childCount
                    val totalItemCount = mLayoutManager!!.itemCount
                    val firstVisibleItem = mLayoutManager!!.findFirstVisibleItemPosition()
                    val itemcount = visibleItemCount + firstVisibleItem

                    if (isLastpage == 1) {
                        return
                    }
                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }

                    if (!loading && itemcount >= totalItemCount && firstVisibleItem >= 0) {
                        // End has been reached

                        viewDialog!!.hideDialog()
                        pageNumber++
                        getReviewData(
                            contryID = countryID!!,
                            selectCurrency = currncyCode!!,
                            langCode = langCode!!
                        )

                        loading = true;
                    }

                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

            }

        })

    }


    private fun alertLogin() {
        val builder1 = AlertDialog.Builder(this.activity!!)
        builder1.setMessage(getString(R.string.alert_reviewlogin))
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            getString(R.string.res_ok),
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                helper!!.clearAllPrefs()
                startActivity(
                    Intent(activity, LoginActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                activity!!.finish()
            })

        builder1.setNegativeButton(
            getString(R.string.res_cancel),
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        val alert11 = builder1.create()
        alert11.show()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()

    }


    fun getURLForResource(resourceId: Int): String {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://" + com.greenspot.app.R::class.java.getPackage()!!.getName() + "/" + resourceId)
            .toString()
    }

    private fun getReviewData(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
//        viewDialog!!.showDialog()
        utils!!.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val responcePlaceReview = apiService?.REVIEW_DETAILS(
            contryID = contryID,
            selectCurrency = selectCurrency,
            langcode = langCode,
            limit = AppConfig.Constant.LISTLIMIT,
            pageNumber = pageNumber.toString(),
            id = placeID

        )

        Log.e("contry", contryID)
        Log.e("currency", selectCurrency)
        Log.e("langcode", langCode)
        Log.e("placeID", placeID)

        Log.e("pageNumber", pageNumber.toString())

        responcePlaceReview?.enqueue(object : Callback<ResponcePlaceReview> {
            override fun onResponse(@NonNull call: Call<ResponcePlaceReview>, @NonNull response: Response<ResponcePlaceReview>) {
//                viewDialog!!.hideDialog()

                val reivewData = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (reivewData!!.status == 1) {


                        isLastpage = reivewData.data.isLastPage
//                        Log.e("lastpage", "" + isLastpage)
                        guestreviewData.addAll(reivewData.data.records!!)
                        if (guestreviewData.size >= 0) {
                            txt_guestreview!!.visibility = View.VISIBLE
                        } else {
                            txt_guestreview!!.visibility = View.GONE
                        }
                        guestReviewAdapter.notifyDataSetChanged()

                    } else {


//
//                        if(guestreviewData ==null || guestreviewData.isEmpty()){
//                            Toast.makeText(
//                                activity,
//                                reivewData.message,
//                                Toast.LENGTH_SHORT
//                            ).show()
//
//                        }


                    }
                } else {

                    Log.e("rview", " " + response.code())
                    Log.e("rview", " " + response.errorBody())


                    Toast.makeText(
                        activity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponcePlaceReview>, @NonNull t: Throwable) {

//                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)
                Toast.makeText(
                    activity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    private fun submitReviewData(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
        viewDialog!!.showDialog()
        utils!!.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val responcePlaceReview = apiService?.POSTREIVEW_CALL(
            token = token,
            contryID = contryID,
            selectCurrency = selectCurrency,
            langcode = langCode,
            id = placeID,
            serviceproiver = serviceProviderid,
            type = "R",
            applystar = userRateing.toString(),
            reviwdesc = et_reivew!!.text.toString()

        )

        Log.e("contry", contryID)
        Log.e("currency", selectCurrency)
        Log.e("langcode", langCode)
        Log.e("placeID", placeID)

        Log.e("pageNumber", pageNumber.toString())

        responcePlaceReview?.enqueue(object : Callback<ComanResponce> {
            override fun onResponse(@NonNull call: Call<ComanResponce>, @NonNull response: Response<ComanResponce>) {
                viewDialog!!.hideDialog()

                val postreivewData = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (postreivewData!!.status == 1) {

                        et_reivew!!.setText("")
                        rb_giverstar.rating = 0.0F
                        Toast.makeText(
                            activity,
                            postreivewData.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {


//


                        Toast.makeText(
                            activity,
                            postreivewData.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else if (response.code() == AppConfig.URL.TOKEN_EXPIRE) {

                    login()

                } else {


                    Toast.makeText(
                        activity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ComanResponce>, @NonNull t: Throwable) {

                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)
                Toast.makeText(
                    activity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun login() {

        helper!!.clearAllPrefs()
        startActivity(
            Intent(activity, MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        activity!!.finish()
    }

}
