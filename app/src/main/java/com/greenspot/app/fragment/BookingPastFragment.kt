package com.greenspot.app.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.greenspot.app.R
import com.greenspot.app.activity.MainActivity
import com.greenspot.app.adapter.MyBookingAdapter
import com.greenspot.app.network.ApiClient
import com.greenspot.app.network.ApiInterface
import com.greenspot.app.responce.bookinglist.RecordsItem
import com.greenspot.app.responce.bookinglist.ResponceBookingList
import com.greenspot.app.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class BookingPastFragment : Fragment() {
    private var loading: Boolean = true
    private var previousTotal = 0
    private lateinit var myBookingAdapter: MyBookingAdapter
    private lateinit var mView: View
    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null

    private val upcomingBookingList: LinkedList<RecordsItem> =
        LinkedList<RecordsItem>()

    private var countryID: String? = ""
    private var langCode: String? = ""
    private var currncyCode: String? = ""
    private var token: String = ""
    private var helperlang: PreferenceHelper? = null
    private var viewDialog: ViewDialog? = null
    private var isLastpage: Int = 0
    private var pageNumber: Int = 1
    private var mLayoutManager: LinearLayoutManager? = null

    private lateinit var rv_pastbooking: RecyclerView
    private lateinit var sr_pastcomingbooking: SwipeRefreshLayout

    fun newInstance(): BookingPastFragment {

        val args = Bundle()

        val fragment = BookingPastFragment()
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
        viewDialog = ViewDialog(this.activity!!)
        helperlang = PreferenceHelper(this.activity!!, AppConfig.PREFERENCE.PREF_FILE_LANG)
        return inflater.inflate(R.layout.fragment_booking_past, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        rv_pastbooking = mView.findViewById(R.id.rv_pastbooking)
        sr_pastcomingbooking = mView.findViewById(R.id.sr_pastcomingbooking)
        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")
        token = "Bearer " + helper!!.LoadStringPref(AppConfig.PREFERENCE.AUTHTOKEN, "")

        intiView()

        sr_pastcomingbooking.setOnRefreshListener {
            pageNumber = 1
            previousTotal = 0
            upcomingBookingList.clear()
            myBookingAdapter.notifyDataSetChanged()
            upcomingBooking(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!
            )
        }


    }

    private fun intiView() {

        upcomingBooking(
            contryID = this.countryID!!,
            selectCurrency = this.currncyCode!!,
            langCode = this.langCode!!
        )

        setMyBookingData()
    }

    private fun setMyBookingData() {


        mLayoutManager = LinearLayoutManager(activity)
        rv_pastbooking.setLayoutManager(mLayoutManager)
        myBookingAdapter = MyBookingAdapter(activity)
        myBookingAdapter.swapData(upcomingBookingList)
        rv_pastbooking.adapter = myBookingAdapter
        setRecyclerViewScrollListener()


    }

    private fun setRecyclerViewScrollListener() {

        rv_pastbooking.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        upcomingBooking(
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
//                val visibleItemCount = mLayoutManager?.childCount
//                val totalItemCount = mLayoutManager?.itemCount
//                val firstVisibleItemPosition = mLayoutManager?.findFirstVisibleItemPosition()
//                val itemcount = visibleItemCount!! + firstVisibleItemPosition!!
//
//
//                if (isLastpage == 1) {
//                    return
//                } else if (isLastpage == 0) {
//                    if (itemcount >= totalItemCount!! && firstVisibleItemPosition >= 0) {
//                        Log.e("itemcount", " item")
//                        pageNumber++
//                        viewDialog!!.hideDialog()
//
//                        getRecratetionData(
//                            contryID = countryID!!,
//                            selectCurrency = currncyCode!!,
//                            langCode = langCode!!
//                        )
//                    }
//
//                }
            }

        })

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)


    }

    override fun onDetach() {
        super.onDetach()

    }


    fun getURLForResource(resourceId: Int): String {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://" + com.greenspot.app.R::class.java!!.getPackage()!!.getName() + "/" + resourceId)
            .toString()
    }

    private fun upcomingBooking(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
        viewDialog!!.showDialog()
        utils!!.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val checkBookingResponce = apiService?.CALL_BOOKINGLIST(
            token = token,
            langcode = langCode,
            selectCurrency = selectCurrency,
            type = "P",
            pageNumber = pageNumber.toString(),
            limit = AppConfig.Constant.LISTLIMIT

        )

        checkBookingResponce?.enqueue(object : Callback<ResponceBookingList> {
            override fun onResponse(@NonNull call: Call<ResponceBookingList>, @NonNull response: Response<ResponceBookingList>) {
                viewDialog!!.hideDialog()
                sr_pastcomingbooking.isRefreshing = false
                val bookingListresponce = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (bookingListresponce!!.status == 1) {

//                        currencyData()
                        isLastpage = bookingListresponce.data.isLastPage
                        upcomingBookingList.addAll(bookingListresponce.data.records!!)
                        myBookingAdapter.notifyDataSetChanged()


                    } else {


                        if(upcomingBookingList==null || upcomingBookingList.isEmpty()){
                            Toast.makeText(
                                activity,
                                bookingListresponce.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }




                    }
                }else if (response.code() == AppConfig.URL.TOKEN_EXPIRE) {

                    login()

                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponceBookingList>, @NonNull t: Throwable) {
                sr_pastcomingbooking.isRefreshing = false
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
