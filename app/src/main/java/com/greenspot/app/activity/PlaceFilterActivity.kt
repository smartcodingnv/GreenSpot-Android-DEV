package com.greenspot.app.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar
import com.google.gson.Gson
import com.greenspot.app.R
import com.greenspot.app.adapter.PlaceFilterAdapter
import com.greenspot.app.adapter.PlaceFilterMenuAdapter
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.responce.DataItemFilterTitle
import com.greenspot.app.responce.FilterRecordsItem
import com.greenspot.app.responce.ResponceListMaster
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.activity_place_filter.*
import kotlinx.android.synthetic.main.content_place_filter.*
import java.util.*
import com.greenspot.app.model.PlaceFiltermenuItem as PlaceFiltermenuItem1


class PlaceFilterActivity : AppCompatActivity(), ItemClickListener {
    private var checkData: String? = ""
    private var priceMinRange: String? = ""
    private var priceMaxRange: String? = ""
    private var filterdata: ResponceListMaster? = null
    val TAG = "PlaceFilterActivity"

    private var filterResponce: String? = null
    private var meMap: HashMap<String, String> = hashMapOf()
    private lateinit var selectReqKey: String
    private lateinit var tvMin: TextView
    private lateinit var tvMax: TextView
    private lateinit var rangeSeekbar: CrystalRangeSeekbar


    private var currncyCode: String? = null
    private var countryID: String? = null
    private var langCode: String? = null
    private var checkFilter: Int = 0
    private lateinit var placeFilterAdapter: PlaceFilterAdapter
    private lateinit var placeDetailsAdapter: PlaceFilterMenuAdapter
    var filterTitle: ArrayList<DataItemFilterTitle>? = ArrayList()
    var filtersubdata: ArrayList<FilterRecordsItem>? = ArrayList()
    var placeData: ArrayList<DataItemFilterTitle> = ArrayList()
    var officeData: ArrayList<PlaceFiltermenuItem1> = ArrayList()
    var lstselected = ArrayList<String>()

    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_filter)

        progress = Progress(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)

        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")

        rangeSeekbar = findViewById(R.id.rangeSeekbar1) as CrystalRangeSeekbar

        tvMin = findViewById(R.id.textMin1) as TextView
        tvMax = findViewById(R.id.textMax1) as TextView

        val intent = getIntent();
        checkFilter = intent.getIntExtra(AppConfig.EXTRA.FILTERCHECK, 0)

        checkData = intent.getStringExtra(AppConfig.EXTRA.CHECKFILTERDATA);

        priceMinRange = intent.getStringExtra(AppConfig.EXTRA.FILTERPRICEMIN);
        priceMaxRange = intent.getStringExtra(AppConfig.EXTRA.FILTERPRICEMAX);

        Log.e(TAG, "" + checkData)

            if (checkData.isNullOrEmpty()) {

            filterResponce = intent.getStringExtra(AppConfig.EXTRA.FILTERRESPONCE)

        } else {

            filterResponce = intent.getStringExtra(AppConfig.EXTRA.CHECKFILTERDATA)
        }


        val gson = Gson()
        filterdata = gson.fromJson(filterResponce, ResponceListMaster::class.java)

        initView()


        ib_back.setOnClickListener(View.OnClickListener {
            priceMaxRange = ""
            priceMaxRange = ""
            val intent = Intent()
            intent.putExtra(AppConfig.EXTRA.CHECKFILTERDATA, gson.toJson(filterdata))
            setResult(2000, intent)
            finish()
//            onBackPressed()
        })


        txt_clearfilter.setOnClickListener(View.OnClickListener {


            if (checkFilter == 1) {

//                placeFilter()
                resetPlaceFilter()

            } else if (checkFilter == 2) {

//                resetTourFilter()

            }

        })


        btn_rating.setOnClickListener(View.OnClickListener {

            val intent = Intent()
            intent.putExtra(AppConfig.EXTRA.FILTERCHECKDATA, meMap);
            intent.putExtra(AppConfig.EXTRA.FILTERPRICEMIN, priceMinRange);
            intent.putExtra(AppConfig.EXTRA.FILTERPRICEMAX, priceMaxRange);
            intent.putExtra(AppConfig.EXTRA.CHECKFILTERDATA, gson.toJson(filterdata))
            setResult(1000, intent)
            finish()

        })


    }

    private fun initView() {


        filterTitle = filterdata?.data as ArrayList<DataItemFilterTitle>?

        if (checkData!!.isEmpty()) {

            filtersubdata!!.add(
                FilterRecordsItem(
                    "",
                    "",
                    1,
                    false

                )
            )

            filterTitle!!.add(
                0,
                DataItemFilterTitle(
                    filtersubdata,
                    1,
                    getString(R.string.txt_priceperperson),
                    ""
                )
            )

        }

        Log.e("seekmax", "see" + filterdata!!.sliderMax)

        if (priceMinRange!!.isEmpty()) {
            rangeSeekbar.setMinValue(0F)
            rangeSeekbar.setMinStartValue(0F);
        } else {
            rangeSeekbar.setMinValue(0F)
            rangeSeekbar.setMinStartValue(priceMinRange!!.toFloat());
        }
        if (priceMaxRange!!.isEmpty()) {
            rangeSeekbar.setMaxValue(filterdata!!.sliderMax.toFloat())
            rangeSeekbar.setMaxStartValue(filterdata!!.sliderMax.toFloat());
        } else {
            rangeSeekbar.setMaxValue(filterdata!!.sliderMax.toFloat())
            rangeSeekbar.setMaxStartValue(priceMaxRange!!.toFloat());
        }

        rangeSeekbar.apply();

        rangeSeekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->

            priceMinRange = minValue.toString()
            priceMaxRange = maxValue.toString()
            tvMin.text = currncyCode + " " + priceMinRange
            tvMax.text = currncyCode + " " + priceMaxRange
        }

        placeFilter()


    }

    private fun resetPlaceFilter() {

        meMap.clear()
        filterTitle?.clear()
        filterResponce = intent.getStringExtra(AppConfig.EXTRA.FILTERRESPONCE)
        val gson = Gson()
        filterdata = gson.fromJson(filterResponce, ResponceListMaster::class.java)

        filterTitle = filterdata?.data as ArrayList<DataItemFilterTitle>?

        filtersubdata!!.add(
            FilterRecordsItem(
                "",
                "",
                1,
                false

            )
        )

        filterTitle!!.add(
            0,
            DataItemFilterTitle(
                filtersubdata,
                1,
                getString(R.string.item_priceperperson),
                ""
            )
        )

        Log.e("seekmax", "see" + filterdata!!.sliderMax)




        rangeSeekbar.setMinValue(0F)
        rangeSeekbar.setMinStartValue(0F)
        rangeSeekbar.setMaxValue(filterdata!!.sliderMax.toFloat())
        rangeSeekbar.setMaxStartValue(filterdata!!.sliderMax.toFloat());
        rangeSeekbar.apply();


//        rangeSeekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
//            tvMin.text = currncyCode + " " + minValue.toString()
//            tvMax.text = currncyCode + " " + maxValue.toString()
//        }


        placeFilter()


    }



    private fun placeFilter() {

        placeFilterAdapter = PlaceFilterAdapter(this, this, checkFilter)
        Common.setVerticalRecyclerView(this, rv_filtertitle)
        placeFilterAdapter.swapData(this.filterTitle!!)
        rv_filtertitle.adapter = placeFilterAdapter

        txt_pricerange.visibility = View.VISIBLE
        lay_pricerange.visibility = View.VISIBLE
        rangeSeekbar1.visibility = View.VISIBLE
        rv_filter.visibility = View.GONE

//        filtertitle = this!!.filterTitle!![0].title.toString()

        placeDetailsAdapter = PlaceFilterMenuAdapter(this, this, checkFilter)
        placeDetailsAdapter.notifyDataSetChanged()
        Common.setVerticalRecyclerView(this, rv_filter)
        placeDetailsAdapter.swapData(filterTitle!![0].records!!)
        rv_filter.adapter = placeDetailsAdapter



    }

    override fun recyclerViewListClicked(v: View, position: Int, flag: Int) {


//        val id: String = visiterPlaceAdapter.getSelectedItem().toString().replace("\\[", "").replace("\\]", "")
//        Log.e("postion", "check" + id)
        if (flag == 2) {


            val id: String = placeDetailsAdapter.getSelectedItem().toString()
            val idddd = id.replace("[", "").replace("]", "").replace(" ", "")

            Log.e("postion", "check" + idddd)

            for (name in this.filterTitle!!) {

                if (name.requestKey.equals(selectReqKey)) {
                    meMap.put(selectReqKey, idddd)
                }

            }


            Log.e("checkmap", " " + meMap.toString())


        } else if (flag == 1) {

//            val id: String = visiterPlaceAdapter.getSelectedItem().toString().replace("\\[", "").replace("\\]", "")
//
//            Log.e("postion", "check" + id)

            selectReqKey = this.filterTitle!![position].requestKey.toString()

            if (checkFilter == 1) {

                if (position == 0) {
                    txt_pricerange.visibility = View.VISIBLE
                    lay_pricerange.visibility = View.VISIBLE
                    rangeSeekbar1.visibility = View.VISIBLE
                    rv_filter.visibility = View.GONE

                } else {
                    txt_pricerange.visibility = View.GONE
                    lay_pricerange.visibility = View.GONE
                    rangeSeekbar1.visibility = View.GONE
                    rv_filter.visibility = View.VISIBLE
                    placeFilterAdapter.notifyDataSetChanged()

                    placeDetailsAdapter = PlaceFilterMenuAdapter(this, this, checkFilter)
                    placeDetailsAdapter.notifyDataSetChanged()
                    Common.setVerticalRecyclerView(this, rv_filter)
                    placeDetailsAdapter.swapData(this.filterTitle!![position].records!!)
                    rv_filter.adapter = placeDetailsAdapter
                }


//            visiterPlaceAdapter.notifyDataSetChanged()
//            val placeDetailsAdapter = PlaceFilterMenuAdapter(this)
//            placeDetailsAdapter.notifyDataSetChanged()
//            Common.setVerticalRecyclerView(this, rv_filter)
//            placeDetailsAdapter.swapData(this.filterTitle!![position].records!!)
//            rv_filter.adapter = placeDetailsAdapter

            } else if (checkFilter == 2) {

                if (position == 0) {
                    lay_pricerange.visibility = View.VISIBLE
                    rangeSeekbar1.visibility = View.VISIBLE
                    rv_filter.visibility = View.GONE

                } else {
                    lay_pricerange.visibility = View.GONE
                    rangeSeekbar1.visibility = View.GONE
                    rv_filter.visibility = View.VISIBLE
                    placeFilterAdapter.notifyDataSetChanged()
                    placeDetailsAdapter = PlaceFilterMenuAdapter(this, this, checkFilter)
                    placeDetailsAdapter.notifyDataSetChanged()
                    Common.setVerticalRecyclerView(this, rv_filter)
                    placeDetailsAdapter.swapData(this.filterTitle!![position].records!!)
                    rv_filter.adapter = placeDetailsAdapter
                }

            }

        }


//        placeTitle[position-1].setCount(placeData[position].getCount())
    }




}





