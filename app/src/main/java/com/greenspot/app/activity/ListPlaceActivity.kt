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
import com.greenspot.app.adapter.PlaceSortAdapter
import com.greenspot.app.adapter.VisiterListPlaceAdapter
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.network.ApiClient
import com.greenspot.app.network.ApiInterface
import com.greenspot.app.responce.RecordsItem
import com.greenspot.app.responce.ResponceListMaster
import com.greenspot.app.responce.ResponceRecreationList
import com.greenspot.app.responce.SortingItem
import com.greenspot.app.utils.*
import hk.ids.gws.android.sclick.SClick
import kotlinx.android.synthetic.main.activity_list_place.*
import kotlinx.android.synthetic.main.content_list_place.*
import kotlinx.android.synthetic.main.dialog_placesort.*
import kotlinx.android.synthetic.main.dialog_placesort.btn_btnapply
import kotlinx.android.synthetic.main.dialog_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ListPlaceActivity : AppCompatActivity(), ItemClickListener {

    private var loading: Boolean = true
    private var previousTotal = 0
//    private var firstVisibleItem = 0
//    private var visibleItemCount = 0
//    private var totalItemCount = 0

    private var searchText: String = ""
    private var sortOrderby: String = ""
    private var sortOrderType: String = ""
    private var sortOrderTitle: String = ""
    private var priceRangeMax: String = ""
    private var priceRangeMin: String = ""
    val TAG = "ListPlaceActivity"

    private var isLastpage: Int = 0
    private var pageNumber: Int = 1
    private var visiterPlaceAdapter: VisiterListPlaceAdapter? = null
    private val recreateList: LinkedList<RecordsItem> =
        LinkedList<RecordsItem>()


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
        setContentView(R.layout.activity_list_place)

        progress = Progress(this)
        viewDialog = ViewDialog(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)


        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")

        txt_title.setText(
           getString(R.string.res_vacationin) +" " + helperlang!!.LoadStringPref(
                AppConfig.PREFERENCE.SELECTCONTRY,
                ""
            )
        )

        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        ib_search.setOnClickListener(View.OnClickListener {
            openSearchDialog()
        })


        initviews()


        swipeRefreshLayout.setOnRefreshListener {
            pageNumber = 1
            previousTotal = 0
            recreateList.clear()
            visiterPlaceAdapter!!.notifyDataSetChanged()
            getRecratetionData(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!,
                flag = 0
            )
        }

        ib_filter.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivityForResult(
                Intent(this, PlaceFilterActivity::class.java).putExtra(
                    AppConfig.EXTRA.FILTERCHECK,
                    1
                )
                    .putExtra(AppConfig.EXTRA.FILTERRESPONCE, filterResponce)
                    .putExtra(AppConfig.EXTRA.CHECKFILTERDATA, checkfilterData)
                    .putExtra(AppConfig.EXTRA.FILTERPRICEMIN, priceRangeMin)
                    .putExtra(AppConfig.EXTRA.FILTERPRICEMAX, priceRangeMax)
                , checkFilterApply
            )

        })

        ib_sort.setOnClickListener(View.OnClickListener {
            openSortDialog()

        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        Log.e(TAG, resultCode.toString()) ave e
//        Log.e(TAG, requestCode.toString()) jemoklo e


        if (resultCode == checkFilterApply) {

            recreateList.clear()
            visiterPlaceAdapter!!.notifyDataSetChanged()
            checkfilterData = data?.getStringExtra(AppConfig.EXTRA.CHECKFILTERDATA)!!
            priceRangeMin = data.getStringExtra(AppConfig.EXTRA.FILTERPRICEMIN)!!
            priceRangeMax = data.getStringExtra(AppConfig.EXTRA.FILTERPRICEMAX)!!
            meMap = data.getSerializableExtra(AppConfig.EXTRA.FILTERCHECKDATA) as HashMap<String, String>

            pageNumber = 1
            previousTotal = 0
            getRecratetionData(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!,
                flag = 0
            )

//            Log.e(TAG,checkfilterData)

        } else if (requestCode == checkFilterBack) {


        }
    }

    private fun initviews() {

        getFillterData(
            contryID = this.countryID!!,
            selectCurrency = this.currncyCode!!,
            langCode = this.langCode!!
        )

        getRecratetionData(
            contryID = this.countryID!!,
            selectCurrency = this.currncyCode!!,
            langCode = this.langCode!!,
            flag = 0
        )

        setVisitorplace()


    }

    private fun openSortDialog() {

        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_placesort)
        dialog.window?.setBackgroundDrawableResource(R.color.colorIdolDetailDialogBackground)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.setCancelable(true)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )


        val placeSortAdapter = PlaceSortAdapter(this, this)
        Common.setVerticalRecyclerView(this, dialog.rv_placesort)
        placeSortAdapter.swapData(sortData)
        dialog.rv_placesort.adapter = placeSortAdapter


        dialog.lay_dialg.setOnClickListener {
            dialog.dismiss()
        }

        dialog.btn_canel.setOnClickListener {

            helper?.initPref()
            helper?.SaveStringPref(AppConfig.PREFERENCE.SELECTSORTTITAL, "")
            helper?.ApplyPref()

            pageNumber = 1
            previousTotal = 0

            sortOrderType = ""
            sortOrderby = ""
            recreateList.clear()
            visiterPlaceAdapter!!.notifyDataSetChanged()
            getRecratetionData(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!,
                flag = 0
            )

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
            previousTotal = 0
            recreateList.clear()
            visiterPlaceAdapter!!.notifyDataSetChanged()
            getRecratetionData(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!,
                flag = 0
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

        if (searchText.isNullOrEmpty()) {
            dialog.et_search.hint = getString(R.string.res_search_title)
            cancel.text = getString(R.string.res_cancel)
        } else {
            dialog.et_search.setText(searchText)
            cancel.text = getString(R.string.res_clear)
        }

        dialog.lay_dialogsearch.setOnClickListener {
            dialog.dismiss()
        }


        cancel.setOnClickListener(View.OnClickListener {
            searchText = dialog.et_search.text.toString()
            Log.e("searchtext", " " + searchText)
            if (searchText.isEmpty()) {
                dialog.dismiss()
            } else {
                helper!!.initPref()
                helper!!.SaveStringPref(AppConfig.PREFERENCE.PLACESEARCHTEXT, "")
                helper!!.ApplyPref()
                searchText = ""
                pageNumber = 1
                previousTotal = 0

                recreateList.clear()
                visiterPlaceAdapter!!.notifyDataSetChanged()
                getRecratetionData(
                    contryID = this.countryID!!,
                    selectCurrency = this.currncyCode!!,
                    langCode = this.langCode!!,
                    flag = 0
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
            previousTotal = 0
            recreateList.clear()
            visiterPlaceAdapter!!.notifyDataSetChanged()
            getRecratetionData(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!,
                flag = 0
            )

            dialog.dismiss()

        })

        dialog.show()
    }


    private fun setVisitorplace() {

        mLayoutManager = LinearLayoutManager(this)
        rv_vationplace.setLayoutManager(mLayoutManager)


        visiterPlaceAdapter = VisiterListPlaceAdapter(this)
//        Common.setVerticalRecyclerView(this, rv_vationplace)
//        rv_vationplace.setItemAnimator(SlideInUpAnimator())
        visiterPlaceAdapter!!.swapData(this.recreateList)
        rv_vationplace.adapter = visiterPlaceAdapter
        setRecyclerViewScrollListener()
    }


    private fun setRecyclerViewScrollListener() {

        rv_vationplace.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        getRecratetionData(
                            contryID = countryID!!,
                            selectCurrency = currncyCode!!,
                            langCode = langCode!!,
                            flag = 1
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

    private fun getRecratetionData(
        contryID: String,
        selectCurrency: String,
        langCode: String,
        flag: Int
    ) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
//        progress!!.showDialog()

        viewDialog!!.showDialog()
        utils!!.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val responcelistmaster = apiService?.RECREATTION_LIST(
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

        responcelistmaster?.enqueue(object : Callback<ResponceRecreationList> {
            override fun onResponse(@NonNull call: Call<ResponceRecreationList>, @NonNull response: Response<ResponceRecreationList>) {
                viewDialog!!.hideDialog()
                swipeRefreshLayout.isRefreshing = false
                val recreationList = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (recreationList!!.status == 1) {


                        isLastpage = recreationList.data.isLastPage

                        Log.e("pageNumberrr", " " + pageNumber)
                        Log.e("lastpage", "" + isLastpage)
                        recreateList.addAll(recreationList.data.records!!)
                        visiterPlaceAdapter!!.notifyDataSetChanged()

                    } else {

                        if (recreateList == null || recreateList.isEmpty()) {
                            Toast.makeText(
                                this@ListPlaceActivity,
                                recreationList.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                } else {

                    Toast.makeText(
                        this@ListPlaceActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(@NonNull call: Call<ResponceRecreationList>, @NonNull t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)
                Toast.makeText(
                    this@ListPlaceActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun getFillterData(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
//        progress!!.showDialog()
//        utils!!.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val responcelistmaster = apiService?.LIST_MASTER(
            masterType = "R",
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

                        for (name in responceListMaster.sorting) {

                            if (name.isDefault == 1) {
                                sortOrderby = name.column
                                sortOrderType = name.order
                                sortOrderTitle = name.title

                                helper?.initPref()
                                helper?.SaveStringPref(
                                    AppConfig.PREFERENCE.SELECTSORTTITAL,
                                    sortOrderTitle
                                )
                                helper?.ApplyPref()
                            }
                        }


//                        filterTitle = responceListMaster.data!![0].records!![0].id = 0


                    } else {

                        Toast.makeText(
                            this@ListPlaceActivity,
                            responceListMaster.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Toast.makeText(
                        this@ListPlaceActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


            override fun onFailure(@NonNull call: Call<ResponceListMaster>, @NonNull t: Throwable) {
//                progress!!.hideDialog()
                Toast.makeText(
                    this@ListPlaceActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    override fun recyclerViewListClicked(v: View, position: Int, flag: Int) {


        if (flag == 1) {

            sortOrderby = sortData[position].column
            sortOrderType = sortData[position].order
            sortOrderTitle = sortData[position].title
        }

    }


}
