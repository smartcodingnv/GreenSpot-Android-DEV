package com.greenspot.app.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.EventListAdapter
import com.greenspot.app.adapter.PlaceSortAdapter
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.network.ApiClient
import com.greenspot.app.network.ApiInterface
import com.greenspot.app.responce.EventListRecordsItem
import com.greenspot.app.responce.ResponceEventList
import com.greenspot.app.responce.ResponceListMaster
import com.greenspot.app.responce.SortingItem
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.activity_tour_list.*
import kotlinx.android.synthetic.main.content_event_list.*
import kotlinx.android.synthetic.main.content_list_place.*
import kotlinx.android.synthetic.main.dialog_placesort.*
import kotlinx.android.synthetic.main.dialog_placesort.btn_btnapply
import kotlinx.android.synthetic.main.dialog_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EventListActivity : AppCompatActivity(), ItemClickListener {

//    var sortData: ArrayList<PlaceFiltermenuItem> = ArrayList()
//    private lateinit var dialog: Dialog
//    var eventList: ArrayList<VisiterList> = ArrayList()
//
//    private var progress: Progress? = null
//    private var utils: Utils? = null
//    private var helper: PreferenceHelper? = null
//    private var helperlang: PreferenceHelper? = null

    private var loading: Boolean = true
    private var previousTotal = 0

    private var searchText: String = ""
    private var sortOrderby: String = ""
    private var sortOrderType: String = ""
    private var sortOrderTitle: String = ""
    private var priceRangeMax: String = ""
    private var priceRangeMin: String = ""
    val TAG = "ListPlaceActivity"

    private var isLastpage: Int = 0
    private var pageNumber: Int = 1
    private var evenlistAdapter: EventListAdapter? = null
    private val eventList: LinkedList<EventListRecordsItem> =
        LinkedList<EventListRecordsItem>()


    private lateinit var filterResponce: String
    var sortData: ArrayList<SortingItem> = ArrayList()
    private var progress: Progress? = null
    private var viewDialog: ViewDialog? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null
    private lateinit var dialog: Dialog

    private var currncyCode: String? = null
    private var countryID: String? = null
    private var langCode: String? = null


    private var checkfilterData: String = ""
    private var mLayoutManager: LinearLayoutManager? = null

    private var checkFilterApply: Int = 1000
    private var checkFilterBack: Int = 2000

    private var meMap: HashMap<String, String> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
        progress = Progress(this)
        viewDialog = ViewDialog(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)

        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")
        txt_title.setText(
            getString(R.string.res_eventin)+" " + helperlang!!.LoadStringPref(
                AppConfig.PREFERENCE.SELECTCONTRY,
                ""
            )
        )

        initviews()

        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        ib_search.setOnClickListener(View.OnClickListener {
            openSearchDialog()
        })
        ib_filter.setOnClickListener(View.OnClickListener {
            startActivityForResult(
                Intent(this, PlaceFilterActivity::class.java).putExtra(AppConfig.EXTRA.FILTERCHECK, 1)
                    .putExtra(AppConfig.EXTRA.FILTERRESPONCE, filterResponce)
                    .putExtra(AppConfig.EXTRA.CHECKFILTERDATA, checkfilterData)
                    .putExtra(AppConfig.EXTRA.FILTERPRICEMIN, priceRangeMin)
                    .putExtra(AppConfig.EXTRA.FILTERPRICEMAX, priceRangeMax)
                , checkFilterApply
            )


        })

        swipeRefreshLayoutevent.setOnRefreshListener {
            pageNumber = 1
            previousTotal =0
            eventList.clear()
            evenlistAdapter!!.notifyDataSetChanged()
            getEventData(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!
            )
        }


        ib_sort.setOnClickListener(View.OnClickListener {
            openSortDialog()

        })


    }

    private fun initviews() {

        setEventListData()
        getFillterData(
            contryID = this.countryID!!,
            selectCurrency = this.currncyCode!!,
            langCode = this.langCode!!
        )

        getEventData(
            contryID = this.countryID!!,
            selectCurrency = this.currncyCode!!,
            langCode = this.langCode!!
        )

    }

    private fun setEventListData() {


        mLayoutManager = LinearLayoutManager(this)
        rv_eventlist.setLayoutManager(mLayoutManager)

        evenlistAdapter = EventListAdapter(this)
        Common.setVerticalRecyclerView(this, rv_eventlist)
        evenlistAdapter!!.swapData(eventList)
        rv_eventlist.adapter = evenlistAdapter
        setRecyclerViewScrollListener()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        Log.e(TAG, resultCode.toString()) ave e
//        Log.e(TAG, requestCode.toString()) jemoklo e


        if (resultCode == checkFilterApply) {

            eventList.clear()
            evenlistAdapter!!.notifyDataSetChanged()
            checkfilterData = data?.getStringExtra(AppConfig.EXTRA.CHECKFILTERDATA)!!
            priceRangeMin = data.getStringExtra(AppConfig.EXTRA.FILTERPRICEMIN)!!
            priceRangeMax = data.getStringExtra(AppConfig.EXTRA.FILTERPRICEMAX)!!
            meMap =
                data.getSerializableExtra(AppConfig.EXTRA.FILTERCHECKDATA) as HashMap<String, String>

            pageNumber = 1
            previousTotal =0
            getEventData(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!
            )

//            Log.e(TAG,checkfilterData)

        } else if (requestCode == checkFilterBack) {


        }
    }

    private fun setRecyclerViewScrollListener() {

        rv_eventlist.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        getEventData(
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


    private fun openSortDialog() {

        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_placesort)
        dialog.window?.setBackgroundDrawableResource(R.color.colorIdolDetailDialogBackground)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )


        val placeSortAdapter = PlaceSortAdapter(this, this)

        Common.setVerticalRecyclerView(this, dialog.rv_placesort)
        placeSortAdapter.swapData(sortData)
        dialog.rv_placesort.adapter = placeSortAdapter



        dialog.btn_canel.setOnClickListener {

            helper?.initPref()
            helper?.SaveStringPref(AppConfig.PREFERENCE.SELECTSORTTITAL, "")
            helper?.ApplyPref()
            pageNumber = 1
            previousTotal =0
            sortOrderType = ""
            sortOrderby = ""
            eventList.clear()
            evenlistAdapter!!.notifyDataSetChanged()
            getEventData(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!
            )

            dialog.dismiss()
        }

        dialog.lay_dialg.setOnClickListener {
            dialog.dismiss()
        }



        dialog.btn_btnapply.setOnClickListener {

            //            if(sortOrderType.isNullOrEmpty()){
//
//                Toast.makeText(
//                    this@ListPlaceActivity,
//                    "Select at least one",
//                    Toast.LENGTH_SHORT
//                ).show()
//
//            }

            helper?.initPref()
            helper?.SaveStringPref(AppConfig.PREFERENCE.SELECTSORTTITAL, sortOrderTitle)
            helper?.ApplyPref()
            pageNumber = 1
            previousTotal =0
            eventList.clear()
            evenlistAdapter!!.notifyDataSetChanged()
            getEventData(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!
            )

            dialog.dismiss()
        }

        dialog.show()
    }


    private fun openSearchDialog() {

        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_search)
        dialog.window?.setBackgroundDrawableResource(R.color.colorIdolDetailDialogBackground)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        val cancel = dialog.findViewById<Button>(R.id.btn_scanel)

//        val searchText = helper!!.LoadStringPref(AppConfig.PREFERENCE.PLACESEARCHTEXT, "")

        dialog.lay_dialogsearch.setOnClickListener {
            dialog.dismiss()
        }


        if (searchText.isNullOrEmpty()) {
            dialog.et_search.hint = getString(R.string.res_search_title)
            cancel.text = getString(R.string.res_cancel)
        } else {
            dialog.et_search.setText(searchText)
            cancel.text = getString(R.string.res_clear)
        }

        dialog.btn_scanel.setOnClickListener(View.OnClickListener {
            searchText = dialog.et_search.text.toString()
            Log.e("searchtext"," " +searchText)
            if(searchText.isEmpty()){
                dialog.dismiss()
            }else{

                helper!!.initPref()
                helper!!.SaveStringPref(AppConfig.PREFERENCE.PLACESEARCHTEXT, "")
                helper!!.ApplyPref()
                searchText = ""
                pageNumber = 1
                previousTotal =0
                eventList.clear()
                evenlistAdapter!!.notifyDataSetChanged()
                getEventData(
                    contryID = this.countryID!!,
                    selectCurrency = this.currncyCode!!,
                    langCode = this.langCode!!
                )
                dialog.dismiss()

            }

        })

        dialog.btn_btnapply.setOnClickListener(View.OnClickListener {
            //            helper!!.initPref()
//            helper!!.SaveStringPref(
//                AppConfig.PREFERENCE.PLACESEARCHTEXT,
//                dialog.et_search.text.toString()
//            )
//            helper!!.ApplyPref()

            searchText = dialog.et_search.text.toString()
            pageNumber = 1
            previousTotal =0
            eventList.clear()
            evenlistAdapter!!.notifyDataSetChanged()
            getEventData(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!
            )

            dialog.dismiss()

        })

        dialog.show()
    }


    override fun recyclerViewListClicked(v: View, position: Int, flag: Int) {


        if (flag == 1) {

            sortOrderby = sortData[position].column
            sortOrderType = sortData[position].order
            sortOrderTitle = sortData[position].title
        }

    }


    private fun getFillterData(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
//        progress!!.showDialog()
//        utils!!.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val responcelistmaster = apiService?.LIST_MASTER(
            masterType = "E",
            contryID = contryID,
            selectCurrency = selectCurrency,
            lancode = langCode
        )


        responcelistmaster?.enqueue(object : Callback<ResponceListMaster> {
            override fun onResponse(@NonNull call: Call<ResponceListMaster>, @NonNull response: Response<ResponceListMaster>) {
//                progress!!.hideDialog()
                val responceListMaster = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (responceListMaster!!.status == 1) {


                        val gson = Gson()
                        filterResponce = gson.toJson(responceListMaster)

                        sortData = responceListMaster.sorting as ArrayList<SortingItem>

//                        filterTitle = responceListMaster.data!![0].records!![0].id = 0
                        for(name in responceListMaster.sorting){

                            if(name.isDefault==1){
                                sortOrderby = name.column
                                sortOrderType = name.order
                                sortOrderTitle = name.title

                                helper?.initPref()
                                helper?.SaveStringPref(AppConfig.PREFERENCE.SELECTSORTTITAL, sortOrderTitle)
                                helper?.ApplyPref()
                            }
                        }

                    } else {

                        Toast.makeText(
                            this@EventListActivity,
                            responceListMaster.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Toast.makeText(
                        this@EventListActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


            override fun onFailure(@NonNull call: Call<ResponceListMaster>, @NonNull t: Throwable) {
//                progress!!.hideDialog()
                Toast.makeText(
                    this@EventListActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    private fun getEventData(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
        viewDialog!!.showDialog()
        utils!!.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val responceEventList = apiService?.EVENT_LIST(
            contryID = contryID,
            selectCurrency = selectCurrency,
            langcode = langCode,
            limit = AppConfig.Constant.LISTLIMIT,
            maxRange = priceRangeMax,
            minRange = priceRangeMin,
            orderBy = sortOrderby,
            orderType = sortOrderType,
            pageNumber = pageNumber.toString(),
            params = meMap,
            searchText = searchText
        )

        Log.e("contry", contryID)
        Log.e("currency", selectCurrency)
        Log.e("langcode", langCode)

        responceEventList?.enqueue(object : Callback<ResponceEventList> {
            override fun onResponse(@NonNull call: Call<ResponceEventList>, @NonNull response: Response<ResponceEventList>) {
                viewDialog!!.hideDialog()
                swipeRefreshLayoutevent.isRefreshing = false
                val eventListresponce = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (eventListresponce!!.status == 1) {


                        isLastpage = eventListresponce.data.isLastPage
                        Log.e("lastpage", "" + isLastpage)
                        eventList.addAll(eventListresponce.data.records!!)
                        evenlistAdapter!!.notifyDataSetChanged()

                    } else {

                        if(eventList==null || eventList.isEmpty()){
                            Toast.makeText(
                                this@EventListActivity,
                                eventListresponce.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }



                    }
                } else {
                    Toast.makeText(
                        this@EventListActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponceEventList>, @NonNull t: Throwable) {
                swipeRefreshLayoutevent.isRefreshing = false
                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)
                Toast.makeText(
                    this@EventListActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

}
