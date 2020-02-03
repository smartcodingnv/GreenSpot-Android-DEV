package com.greenspot.app.activity

import android.app.Dialog
import android.content.Intent
import android.net.Uri
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
import com.greenspot.app.adapter.HotelListAdapter
import com.greenspot.app.adapter.PlaceSortAdapter
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.model.ListAddGuestDetails
import com.greenspot.app.network.ApiClient
import com.greenspot.app.network.ApiInterface
import com.greenspot.app.responce.ResponceListMaster
import com.greenspot.app.responce.SortingItem
import com.greenspot.app.responce.hotellist.RecordsItem
import com.greenspot.app.responce.hotellist.ResponceHotelList
import com.greenspot.app.utils.*
import hk.ids.gws.android.sclick.SClick
import kotlinx.android.synthetic.main.activity_hotel_list.*
import kotlinx.android.synthetic.main.activity_tour_list.ib_back
import kotlinx.android.synthetic.main.activity_tour_list.ib_filter
import kotlinx.android.synthetic.main.activity_tour_list.ib_search
import kotlinx.android.synthetic.main.activity_tour_list.ib_sort
import kotlinx.android.synthetic.main.activity_tour_list.txt_title
import kotlinx.android.synthetic.main.content_hotel_list.*
import kotlinx.android.synthetic.main.dialog_placesort.*
import kotlinx.android.synthetic.main.dialog_search.*
import kotlinx.android.synthetic.main.dialog_search.btn_btnapply
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class HotelListActivity : AppCompatActivity(), ItemClickListener {


    private var placeTitle: java.util.ArrayList<ListAddGuestDetails?> = ArrayList()
    private lateinit var listaddGuestdetails: ListAddGuestDetails
    private var roomGuestresponce: String = ""
    private var checkinDate: String = ""
    private var checkoutDate: String = ""
    private var totalRoom: String = ""
    private var roomGuest: String = ""
    val TAG = "HotelListActivity"

    private var loading: Boolean = true
    private var previousTotal = 0


    private var searchText: String = ""
    private var sortOrderby: String = ""
    private var sortOrderType: String = ""
    private var sortOrderTitle: String = ""
    private var priceRangeMax: String = ""
    private var priceRangeMin: String = ""

    private var isLastpage: Int = 0
    private var pageNumber: Int = 1
    private var hotelListAdapter: HotelListAdapter? = null
    private val hotelList: LinkedList<RecordsItem> =
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
    private var checkindateApply: Int = 3000
    private var checkFilterBack: Int = 2000


    private var meMap: HashMap<String, String> = hashMapOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_list)

        progress = Progress(this)
        viewDialog = ViewDialog(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)

        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")


        txt_title.setText(getString(R.string.str_hotelsin) + " " + helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRY, ""))


        if(checkinDate.isEmpty() ){
            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.CHECKINDATE,"")
            helper!!.ApplyPref()
        }
        if(checkoutDate.isEmpty() ){
            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.CHECKOUTDATE,"")
            helper!!.ApplyPref()
        }

        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        ib_search.setOnClickListener(View.OnClickListener {
            openSearchDialog()
        })

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
                    .putExtra(AppConfig.EXTRA.FILTERPRICEMAX, priceRangeMax), checkFilterApply
            )

        })
        ib_sort.setOnClickListener(View.OnClickListener {
            openSortDialog()

        })

        initviews()

        swipeRefreshLayout.setOnRefreshListener {
            pageNumber = 1
            previousTotal = 0
            hotelList.clear()
            hotelListAdapter!!.notifyDataSetChanged()
            getHotelData(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!,
                flag = 0

            )
        }

        val bundle = Bundle()

        lay_rooms.setOnClickListener(View.OnClickListener {

            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;

            startActivityForResult(
                Intent(this, HotelDateandGuestActivity::class.java)
                    .putExtra(AppConfig.EXTRA.CHECKDATEGUESTSCREEN, 1)
                    .putExtra(AppConfig.EXTRA.CHECKDATEGUEST, 3)
                    .putExtra(AppConfig.EXTRA.CHECKINDATE, checkinDate)
                    .putExtra(AppConfig.EXTRA.CHECKOUTDATE, checkoutDate)
                    .putExtra(AppConfig.EXTRA.TOTALROOM, totalRoom)
                    .putExtra(AppConfig.EXTRA.ROOMTOTALGUEST, roomGuest)
                    .putExtra(AppConfig.EXTRA.ROOMGUESTRESPONCE, placeTitle), checkindateApply
            )
        })

        lay_checkin.setOnClickListener(View.OnClickListener {

            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivityForResult(
                Intent(this, HotelDateandGuestActivity::class.java)
                    .putExtra(AppConfig.EXTRA.CHECKDATEGUESTSCREEN, 1)
                    .putExtra(AppConfig.EXTRA.CHECKDATEGUEST, 1)
                    .putExtra(AppConfig.EXTRA.CHECKINDATE, checkinDate)
                    .putExtra(AppConfig.EXTRA.CHECKOUTDATE, checkoutDate)
                    .putExtra(AppConfig.EXTRA.TOTALROOM, totalRoom)
                    .putExtra(AppConfig.EXTRA.ROOMTOTALGUEST, roomGuest)
                    .putExtra(AppConfig.EXTRA.ROOMGUESTRESPONCE, placeTitle), checkindateApply
            )

        })


        lay_checkout.setOnClickListener(View.OnClickListener {

            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            startActivityForResult(
                Intent(this, HotelDateandGuestActivity::class.java)
                    .putExtra(AppConfig.EXTRA.CHECKDATEGUESTSCREEN, 1)
                    .putExtra(AppConfig.EXTRA.CHECKDATEGUEST, 2)
                    .putExtra(AppConfig.EXTRA.CHECKINDATE, checkinDate)
                    .putExtra(AppConfig.EXTRA.CHECKOUTDATE, checkoutDate)
                    .putExtra(AppConfig.EXTRA.TOTALROOM, totalRoom)
                    .putExtra(AppConfig.EXTRA.ROOMTOTALGUEST, roomGuest)
                    .putExtra(AppConfig.EXTRA.ROOMGUESTRESPONCE, placeTitle), checkindateApply
            )

        })


    }

    private fun initviews() {


        getFillterData(
            contryID = this.countryID!!,
            selectCurrency = this.currncyCode!!,
            langCode = this.langCode!!
        )
        getHotelData(
            contryID = this.countryID!!,
            selectCurrency = this.currncyCode!!,
            langCode = this.langCode!!,
            flag = 0
        )

        setVisitorplace()


    }

    private fun setVisitorplace() {


        mLayoutManager = LinearLayoutManager(this)
        rv_hotellist.setLayoutManager(mLayoutManager)
        rv_hotellist.setHasFixedSize(true)
        hotelListAdapter = HotelListAdapter(this)
//        Common.setVerticalRecyclerView(this, rv_tourlist)
        hotelListAdapter!!.swapData(this.hotelList)
        rv_hotellist.adapter = hotelListAdapter
        setRecyclerViewScrollListener()


    }

    private fun setRecyclerViewScrollListener() {

        rv_hotellist.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        getHotelData(
                            contryID = countryID!!,
                            selectCurrency = currncyCode!!,
                            langCode = langCode!!,
                            flag = 1
                        )

                        loading = true;
                    }

                }

                /*    if (dy > 0) {
                        val visibleItemCount = mLayoutManager?.childCount
                        val totalItemCount = mLayoutManager?.itemCount
                        val firstVisibleItemPosition = mLayoutManager?.findFirstVisibleItemPosition()
                        val itemcount = visibleItemCount!! + firstVisibleItemPosition!!
                        if (isLastpage == 1) {
                            return
                        }
                        if (itemcount >= totalItemCount!! && firstVisibleItemPosition >= 0) {
                            Log.e("itemcount", " item")
                            viewDialog!!.hideDialog()
                            pageNumber++

                            getTourData(
                                contryID = countryID!!,
                                selectCurrency = currncyCode!!,
                                langCode = langCode!!
                            )
                        }
                    }*/


            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

            }

        })

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
                hotelList.clear()
                hotelListAdapter!!.notifyDataSetChanged()
                getHotelData(
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
            hotelList.clear()
            hotelListAdapter!!.notifyDataSetChanged()
            getHotelData(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!,
                flag = 0
            )

            dialog.dismiss()

        })

        dialog.show()
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
            hotelList.clear()
            hotelListAdapter!!.notifyDataSetChanged()
            getHotelData(
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
            hotelList.clear()
            hotelListAdapter!!.notifyDataSetChanged()
            getHotelData(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!,
                flag = 0
            )

            dialog.dismiss()
        }

        dialog.show()
    }


    private fun getHotelData(
        contryID: String,
        selectCurrency: String,
        langCode: String,
        flag: Int
    ) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))

        viewDialog!!.showDialog()
        utils!!.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val responcelistmaster = apiService?.HOTEL_LIST(
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

        responcelistmaster?.enqueue(object : Callback<ResponceHotelList> {
            override fun onResponse(@NonNull call: Call<ResponceHotelList>, @NonNull response: Response<ResponceHotelList>) {
                viewDialog!!.hideDialog()
                swipeRefreshLayout.isRefreshing = false
                val recreationList = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (recreationList!!.status == 1) {


                        isLastpage = recreationList.data.isLastPage

                        Log.e("lastpage", "" + isLastpage)
                        Log.e("pageNumber", "" + pageNumber)
                        hotelList.addAll(recreationList.data.records!!)
                        hotelListAdapter!!.notifyDataSetChanged()

                    } else {

                        if (hotelList == null || hotelList.isEmpty()) {
                            Toast.makeText(
                                this@HotelListActivity,
                                recreationList.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    }
                } else {
                    Toast.makeText(
                        this@HotelListActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponceHotelList>, @NonNull t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)
                Toast.makeText(
                    this@HotelListActivity,
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
            masterType = "H",
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


                    } else {

                        Toast.makeText(
                            this@HotelListActivity,
                            responceListMaster.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Toast.makeText(
                        this@HotelListActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


            override fun onFailure(@NonNull call: Call<ResponceListMaster>, @NonNull t: Throwable) {
//                progress!!.hideDialog()
                Toast.makeText(
                    this@HotelListActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        Log.e(TAG, resultCode.toString()) ave e
//        Log.e(TAG, requestCode.toString()) jemoklo e


        if (resultCode == checkFilterApply) {

            hotelList.clear()
            hotelListAdapter!!.notifyDataSetChanged()
            checkfilterData = data?.getStringExtra(AppConfig.EXTRA.CHECKFILTERDATA)!!
            priceRangeMin = data.getStringExtra(AppConfig.EXTRA.FILTERPRICEMIN)!!
            priceRangeMax = data.getStringExtra(AppConfig.EXTRA.FILTERPRICEMAX)!!
            meMap =
                data.getSerializableExtra(AppConfig.EXTRA.FILTERCHECKDATA) as HashMap<String, String>

            pageNumber = 1
            previousTotal = 0
            getHotelData(
                contryID = this.countryID!!,
                selectCurrency = this.currncyCode!!,
                langCode = this.langCode!!,
                flag = 0
            )

//            Log.e(TAG,checkfilterData)

        } else if (resultCode == checkFilterBack) {


        } else if (resultCode == checkindateApply) {

            val bundle = data?.extras
            checkinDate = data?.getStringExtra(AppConfig.EXTRA.CHECKINDATE)!!
            checkoutDate = data.getStringExtra(AppConfig.EXTRA.CHECKOUTDATE)!!
            totalRoom = data.getStringExtra(AppConfig.EXTRA.TOTALROOM)!!
            roomGuest = data.getStringExtra(AppConfig.EXTRA.ROOMTOTALGUEST)!!
            placeTitle =
                data.getSerializableExtra(AppConfig.EXTRA.ROOMGUESTRESPONCE) as ArrayList<ListAddGuestDetails?>

            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.CHECKINDATE,checkinDate)
            helper!!.SaveStringPref(AppConfig.PREFERENCE.CHECKOUTDATE,checkoutDate)
            helper!!.ApplyPref()

            txt_checkin.text = checkinDate
            txt_checkout.text = checkoutDate
            txt_roomss.text = totalRoom + " " + getString(R.string.str_rooms)
            txt_guest.text = roomGuest + " " + getString(R.string.str_guest)


//            questions =
//                intent.getSerializableExtra("QuestionListExtra") as ArrayList<Questions?>


        }
    }

    override fun recyclerViewListClicked(v: View, position: Int, flag: Int) {


        if (flag == 1) {

            sortOrderby = sortData[position].column
            sortOrderType = sortData[position].order
            sortOrderTitle = sortData[position].title

        }

    }


    fun getURLForResource(resourceId: Int): String {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://" + com.greenspot.app.R::class.java.getPackage()!!.getName() + "/" + resourceId)
            .toString()
    }


}
