package com.greenspot.app.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.greenspot.app.R
import com.greenspot.app.activity.EventListActivity
import com.greenspot.app.activity.ListPlaceActivity
import com.greenspot.app.activity.TourListActivity
import com.greenspot.app.adapter.*
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.network.ApiClient
import com.greenspot.app.network.ApiInterface
import com.greenspot.app.responce.CountriesItem
import com.greenspot.app.responce.CurrenciesItem
import com.greenspot.app.responce.LanguagesItem
import com.greenspot.app.responce.ResponceLocation
import com.greenspot.app.responce.home.ResponeHome
import com.greenspot.app.utils.*
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.dialog_contry.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), ItemClickListener {


    private var checkDialog: Int = 0
    private lateinit var eventAdapter: EventAdapter
    private lateinit var tourPlaceAdapter: TourPlaceAdapter
    private lateinit var visiterPlaceAdapter: VisiterPlaceAdapter
    private var viewDialog: ViewDialog? = null
    private var bannerList: ArrayList<ResponeHome.DataBean.BannerBean> = ArrayList()
    private var eList: ArrayList<ResponeHome.DataBean.EventBean.RecordsBeanEvent> = ArrayList()
    private var tList: ArrayList<ResponeHome.DataBean.TourBean.RecordsBeanTour> = ArrayList()
    private var rList: ArrayList<ResponeHome.DataBean.RecreationBean.RecordsBeanPlace> = ArrayList()
    private lateinit var language: Array<String>
    private var contryData: List<CountriesItem>? = null
    private var currancyData: List<CurrenciesItem>? = null
    private var languageData: List<LanguagesItem>? = null

    private lateinit var mView: View
    private lateinit var txtContryhome: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var txtCurrency: TextView
    private lateinit var txtLanguage: TextView
    private lateinit var txt_vacation: TextView
    private lateinit var txt_vacationsub: TextView
    private lateinit var txt_tour: TextView
    private lateinit var txt_toursub: TextView
    private lateinit var txt_event: TextView
    private lateinit var txt_eventsub: TextView
    private lateinit var lay_vacationview: LinearLayout
    private lateinit var lay_tourview: LinearLayout
    private lateinit var lay_evenview: LinearLayout
    private lateinit var lay_mainhome: LinearLayout
    private lateinit var sliderView: SliderView
    private lateinit var rv_vationplace: RecyclerView
    private lateinit var rv_tour: RecyclerView
    private lateinit var rv_event: RecyclerView
    private lateinit var dialog: Dialog
    private var meMap: HashMap<String, String> = hashMapOf()

    private var currncyCode: String? = null
    private var countryID: String? = null
    private var langCode: String? = null

//  var contryData: ArrayList<CountriesItem> = ArrayList()

//    var visiterList: ArrayList<VisiterList> = ArrayList()
//    var tourList: ArrayList<VisiterList> = ArrayList()
//    var eventList: ArrayList<VisiterList> = ArrayList()

    var pass =
        arrayOf("10 Des, 2019", "15 Des, 2019", "20 Des, 2019", "25 Des, 2019")

    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun newInstance(): HomeFragment {

        val args = Bundle()

        val fragment = HomeFragment()
        fragment.setArguments(args)
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        progress = Progress(this.activity!!)
        utils = Utils(this.activity!!)
        helper = PreferenceHelper(this.activity!!, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this.activity!!, AppConfig.PREFERENCE.PREF_FILE_LANG)
        viewDialog = ViewDialog(this.activity!!);
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view

        txtContryhome = mView.findViewById(R.id.txt_contryhome)
        txtCurrency = mView.findViewById(R.id.txt_currency)
        txtLanguage = mView.findViewById(R.id.txt_language)
        swipeRefreshLayout = mView.findViewById(R.id.swipeRefreshLayouthome)
        txt_vacation = mView.findViewById(R.id.txt_vacation)
        txt_vacationsub = mView.findViewById(R.id.txt_vacationsub)
        txt_tour = mView.findViewById(R.id.txt_tour)
        txt_toursub = mView.findViewById(R.id.txt_toursub)
        txt_event = mView.findViewById(R.id.txt_event)
        txt_eventsub = mView.findViewById(R.id.txt_eventsub)
        lay_vacationview = mView.findViewById(R.id.lay_vacationview)
        lay_tourview = mView.findViewById(R.id.lay_tourview)
        lay_evenview = mView.findViewById(R.id.lay_evenview)
        sliderView = mView.findViewById(R.id.imageSlider)
        rv_vationplace = mView.findViewById(R.id.rv_vationplace)
        rv_tour = mView.findViewById(R.id.rv_tour)
        rv_event = mView.findViewById(R.id.rv_event)
        lay_mainhome = mView.findViewById(R.id.lay_mainhome)


        val username = helper!!.LoadStringPref(AppConfig.PREFERENCE.USER_FNAME, "")
         if(username!!.isEmpty()){
             txt_username.text = "Hey Guest" + ", \nWhere you want to go?"
         }else{
             txt_username.text = "Hey " + username + ", \nWhere you want to go?"
         }


        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")

        visiterPlaceAdapter = VisiterPlaceAdapter(activity)
        tourPlaceAdapter = TourPlaceAdapter(activity)
        eventAdapter = EventAdapter(activity)
        Common.setHorizontalRecyclerView(context!!, rv_vationplace)
        Common.setHorizontalRecyclerView(context!!, rv_tour)
        Common.setHorizontalRecyclerView(context!!, rv_event)



        swipeRefreshLayout.setOnRefreshListener {


            checkDialog = 1
            getHomeData(
                contryID = countryID!!,
                selectCurrency = currncyCode!!,
                langCode = this.langCode!!
            )
        }

        initviews()

//
//        for (name in this!!.languageData!!) {
//            language = arrayOf(name.languageName)
//        }
//
//        val langadapter: ArrayAdapter<String> =
//            ArrayAdapter<String>(
//                mView.context, R.layout.spinner_item,
//                this.language!!
//            )
//        sp_language.setAdapter(langadapter);

//        val currencyadapter: ArrayAdapter<CurrenciesItem> =
//            ArrayAdapter<CurrenciesItem>(mView.context, R.layout.spinner_item,
//                this.currancyData!!
//            )
//        sp_currency.setAdapter(currencyadapter);


        txtContryhome.setOnClickListener(View.OnClickListener {
            openContryDialog()
        })

        lay_currency.setOnClickListener(View.OnClickListener {
            openCurrencyDialog()
        })

        lay_language.setOnClickListener(View.OnClickListener {
            openLanguageDialog()
        })






        lay_vacationview.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, ListPlaceActivity::class.java))

        })

        lay_vacation.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, ListPlaceActivity::class.java))

        })

        lay_event.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, EventListActivity::class.java))

        })

        lay_tour.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, TourListActivity::class.java))

        })

        lay_tourview.setOnClickListener(View.OnClickListener {

            startActivity(Intent(activity, TourListActivity::class.java))

        })

        lay_evenview.setOnClickListener(View.OnClickListener {

            startActivity(Intent(activity, EventListActivity::class.java))

        })

//        txt_hotelall.setOnClickListener(View.OnClickListener {
//            startActivity(Intent(activity, ListPlaceActivity::class.java))
//
//        })

    }

    override fun recyclerViewListClicked(v: View, position: Int, flag: Int) {

        Log.e(tag, " " + flag)
        if (flag == 1) {
            helperlang!!.initPref()
            helperlang!!.SaveStringPref(
                AppConfig.PREFERENCE.SELECTCONTRY,
                contryData!!.get(position).name
            )
            helperlang!!.SaveStringPref(
                AppConfig.PREFERENCE.SELECTCONTRYID,
                contryData!!.get(position).masterId
            )
            helperlang!!.ApplyPref()
            dialog.dismiss()
            txtContryhome.setText(contryData!!.get(position).name)
            countryID = contryData!!.get(position).masterId
            initviews()

        } else if (flag == 2) {
            helperlang!!.initPref()
            helperlang!!.SaveStringPref(
                AppConfig.PREFERENCE.SELECTCURRENCYNAME,
                currancyData!!.get(position).currency
            )
            helperlang!!.ApplyPref()
            dialog.dismiss()
            txtCurrency.setText(currancyData!!.get(position).currency)
            currncyCode = currancyData!!.get(position).currency
            initviews()
        } else if (flag == 3) {
            helperlang!!.initPref()
            helperlang!!.SaveStringPref(
                AppConfig.PREFERENCE.SELECTLANGNAME,
                languageData!!.get(position).languageName
            )

            helperlang!!.SaveStringPref(
                AppConfig.PREFERENCE.SELECTLANGCODE,
                languageData!!.get(position).languageCode
            )
            helperlang!!.ApplyPref()
            dialog.dismiss()
            txtLanguage.setText(languageData!!.get(position).languageName)
            langCode = languageData!!.get(position).languageCode
            initviews()
        }


    }

    private fun initviews() {

        getLocationDate(langCodee = this.langCode!!)

//        setVisitorplace()

//        setHotelData()
    }

    // TODO: Rename method, update argument and hook method into UI event
    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

    }


    private fun openContryDialog() {

        dialog = Dialog(this!!.activity!!)
        dialog.setContentView(R.layout.dialog_contry)
        dialog.window?.setBackgroundDrawableResource(R.color.colorIdolDetailDialogBackground)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.txt_title.setText(getString(R.string.str_selectcontry))
        val contryAdapter = ContryAdapter(activity, this)

//        contryData.clear()
//        setContryData()
        Common.setVerticalRecyclerView(context!!, dialog.rv_bestseller)
        contryAdapter.swapData(this.contryData!!)

//        dialog.rv_bestseller.affectOnItemClicks() { position, view ->
//
//
//
//        }

        dialog.rv_bestseller.adapter = contryAdapter

        dialog.lay_dialog.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun openCurrencyDialog() {

        dialog = Dialog(this!!.activity!!)
        dialog.setContentView(R.layout.dialog_contry)
        dialog.window?.setBackgroundDrawableResource(R.color.colorIdolDetailDialogBackground)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.txt_title.setText(getString(R.string.str_choosecurrency))
        val currancyAdapter = CurrancyAdapter(activity, this)

//        contryData.clear()
//        setContryData()
        Common.setVerticalRecyclerView(context!!, dialog.rv_bestseller)
        currancyAdapter.swapData(this.currancyData!!)

//        dialog.rv_bestseller.affectOnItemClicks() { position, view ->
//
//
//
//        }

        dialog.rv_bestseller.adapter = currancyAdapter

        dialog.lay_dialog.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun openLanguageDialog() {

        dialog = Dialog(this!!.activity!!)
        dialog.setContentView(R.layout.dialog_contry)
        dialog.window?.setBackgroundDrawableResource(R.color.colorIdolDetailDialogBackground)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.txt_title.setText(getString(R.string.str_language))
        val languageAdapter = LanguageAdapter(activity, this)

//        contryData.clear()
//        setContryData()

        Common.setVerticalRecyclerView(context!!, dialog.rv_bestseller)
        languageAdapter.swapData(this.languageData!!)

//        dialog.rv_bestseller.affectOnItemClicks() { position, view ->
//
//
//
//        }

        dialog.rv_bestseller.adapter = languageAdapter

        dialog.lay_dialog.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun setVisitorplace() {


        visiterPlaceAdapter.swapData(rList)
        rv_vationplace.adapter = visiterPlaceAdapter
    }

    private fun setTourplace() {


        tourPlaceAdapter.swapData(tList)
        rv_tour.adapter = tourPlaceAdapter
    }

    private fun setEvenetData() {


        eventAdapter.swapData(eList)
        rv_event.adapter = eventAdapter
    }

    private fun setHotelData() {

//        val visiterPlaceAdapter = VisiterPlaceAdapter(activity)
//        visiterList.clear()
//        setUpPlaceData()
//        Common.setHorizontalRecyclerView(context!!, mView.rv_hotel)
//        visiterPlaceAdapter.swapData(visiterList)
//        rv_hotel.adapter = visiterPlaceAdapter
    }


//    private fun setContryData() {
//        contryData.add(
//            CountryList(
//                "Suriname"
//            )
//        )
//        contryData.add(
//            CountryList(
//                "Guyana"
//            )
//        )
//        contryData.add(
//            CountryList(
//                "French Guyana"
//            )
//        )
//        contryData.add(
//            CountryList(
//                "Holland"
//            )
//        )
//
//    }


    /* private fun setUpPlaceData() {
         visiterList.add(
             VisiterList(
                 "1",
                 "Goa",
                 "Goa, India",
                 "https://images.unsplash.com/photo-1517282009859-f000ec3b26fe?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1834&q=80"
             )
         )
         visiterList.add(
             VisiterList(
                 "2",
                 "Paldi",
                 "Ahemdabad, Gujarat, India",
                 "https://images.unsplash.com/photo-1517282009859-f000ec3b26fe?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1834&q=80"
             )
         )
         visiterList.add(
             VisiterList(
                 "3",
                 "Paldi",
                 "Ahemdabad, Gujarat, India",
                 "https://images.unsplash.com/photo-1517282009859-f000ec3b26fe?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1834&q=80"
             )
         )
         visiterList.add(
             VisiterList(
                 "4",
                 "Paldi",
                 "Ahemdabad, Gujarat, India",
                 "https://images.unsplash.com/photo-1517282009859-f000ec3b26fe?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1834&q=80"
             )
         )
         visiterList.add(
             VisiterList(
                 "5",
                 "Paldi",
                 "Ahemdabad, Gujarat, India",
                 "https://images.unsplash.com/photo-1517282009859-f000ec3b26fe?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1834&q=80"
             )
         )
     }

     private fun setUpTourData() {
         tourList.add(
             VisiterList(
                 "1",
                 "Ahemdabad to Dubai",
                 "Burj Khalifa, Palm Jumeirah, Burj Al Arab.",
                 "https://images.unsplash.com/photo-1517282009859-f000ec3b26fe?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1834&q=80"
             )
         )
         tourList.add(
             VisiterList(
                 "2",
                 "Ahemdabad to Goa",
                 "Baga Beach, Grand Island, Fort Aguada",
                 "https://images.unsplash.com/photo-1517282009859-f000ec3b26fe?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1834&q=80"
             )
         )
         tourList.add(
             VisiterList(
                 "3",
                 "Ahemdabad to Dubai",
                 "Burj Khalifa, Palm Jumeirah, Burj Al Arab.",
                 "https://images.unsplash.com/photo-1517282009859-f000ec3b26fe?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1834&q=80"
             )
         )
         tourList.add(
             VisiterList(
                 "4",
                 "Ahemdabad to Goa",
                 "Baga Beach, Grand Island, Fort Aguada",
                 "https://images.unsplash.com/photo-1517282009859-f000ec3b26fe?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1834&q=80"
             )
         )
         tourList.add(
             VisiterList(
                 "5",
                 "Ahemdabad to Dubai",
                 "Burj Khalifa, Palm Jumeirah, Burj Al Arab.",
                 "https://images.unsplash.com/photo-1517282009859-f000ec3b26fe?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1834&q=80"
             )
         )
     }


     private fun setUpEventData() {
         eventList.add(
             VisiterList(
                 "1",
                 "India vs Bangladesh, 1st Test Match at Ahmedabad, India",
                 "Motera Stadium, Ahmedabad, India, 364001",
                 getURLForResource(com.greenspot.app.R.drawable.travel)
             )
         )
         eventList.add(
             VisiterList(
                 "2",
                 "Arijit singh Concert Ahmedabad",
                 "GMDC gorund, Ahmdebad",
                 getURLForResource(com.greenspot.app.R.drawable.traveltwo)
             )
         )
         eventList.add(
             VisiterList(
                 "3",
                 "India vs Bangladesh, 1st Test Match at Ahmedabad, India",
                 "Motera Stadium, Ahmedabad, India, 364001",
                 getURLForResource(com.greenspot.app.R.drawable.travelfour)
             )
         )
         eventList.add(
             VisiterList(
                 "4",
                 "Arijit singh Concert Ahmedabad",
                 "GMDC gorund, Ahmdebad",
                 getURLForResource(com.greenspot.app.R.drawable.travlethree)
             )
         )
         eventList.add(
             VisiterList(
                 "3",
                 "India vs Bangladesh, 1st Test Match at Ahmedabad, India",
                 "Motera Stadium, Ahmedabad, India, 364001",
                 getURLForResource(com.greenspot.app.R.drawable.travlefive)
             )
         )
     }*/


    fun RecyclerView.affectOnItemClicks(
        onClick: ((position: Int, view: View) -> Unit)? = null,
        onLongClick: ((position: Int, view: View) -> Unit)? = null
    ) {
        this.addOnChildAttachStateChangeListener(
            RecyclerItemClickListener(
                this,
                onClick,
                onLongClick
            )
        )
    }

    fun getURLForResource(resourceId: Int): String {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://" + com.greenspot.app.R::class.java!!.getPackage()!!.getName() + "/" + resourceId)
            .toString()
    }

    private fun getLocationDate(langCodee: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))
//        progress!!.showDialog()

        viewDialog!!.showDialog()
        utils!!.hideKeyboard()
        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val getLocation = apiService?.LOCATION_CALL(langCodee)
        getLocation?.enqueue(object : Callback<ResponceLocation> {
            override fun onResponse(@NonNull call: Call<ResponceLocation>, @NonNull response: Response<ResponceLocation>) {


                val location = response.body()

                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (location!!.status == 1) {

                        contryData = location.data.countries
                        currancyData = location.data.currencies
                        languageData = location.data.languages


                        val selectcontryName =
                            helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRY, "")
                        if (selectcontryName.isNullOrEmpty()) {
                            helperlang!!.initPref()
                            helperlang!!.SaveStringPref(
                                AppConfig.PREFERENCE.SELECTCONTRY,
                                contryData!!.get(0).name
                            )

                            helperlang!!.SaveStringPref(
                                AppConfig.PREFERENCE.SELECTCONTRYID,
                                contryData!!.get(0).masterId
                            )
                            helperlang!!.ApplyPref()
                            countryID = contryData!!.get(0).masterId
                            txtContryhome.setText(contryData!!.get(0).name)
                        } else {
                            txtContryhome.setText(selectcontryName)
                        }

                        val selectcurrencyName =
                            helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
                        if (selectcurrencyName.isNullOrEmpty()) {
                            helperlang!!.initPref()
                            helperlang!!.SaveStringPref(
                                AppConfig.PREFERENCE.SELECTCURRENCYNAME,
                                currancyData!!.get(0).currency
                            )
                            helperlang!!.ApplyPref()
                            currncyCode = currancyData!!.get(0).currency
                            txtCurrency.setText(currancyData!!.get(0).currency)
                        } else {
                            txtCurrency.setText(selectcurrencyName)
                        }

                        val selectLanguageName =
                            helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGNAME, "")
                        if (selectLanguageName.isNullOrEmpty()) {
                            helperlang!!.initPref()
                            helperlang!!.SaveStringPref(
                                AppConfig.PREFERENCE.SELECTLANGNAME,
                                languageData!!.get(0).languageName
                            )

                            helperlang!!.SaveStringPref(
                                AppConfig.PREFERENCE.SELECTLANGCODE,
                                languageData!!.get(0).languageCode
                            )
                            helperlang!!.ApplyPref()
                            txtLanguage.setText(languageData!!.get(0).languageName)
                        } else {
                            txtLanguage.setText(selectLanguageName)
                        }




                        getHomeData(
                            contryID = countryID!!,
                            selectCurrency = currncyCode!!,
                            langCode = langCode!!
                        )


//                        Toast.makeText(
//                            activity,
//                            location.message,
//                            Toast.LENGTH_SHORT
//                        ).show()

                    } else {

                        Toast.makeText(
                            activity,
                            location.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponceLocation>, @NonNull t: Throwable) {
                viewDialog!!.hideDialog()

                Toast.makeText(
                    activity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun getHomeData(contryID: String, selectCurrency: String, langCode: String) {

        if (checkDialog == 1) {

            checkDialog = 0

            viewDialog!!.showDialog()
        }

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val responcelistmaster = apiService?.HOME_DETAILS(
            contryID = contryID,
            selectCurrency = selectCurrency,
            langcode = langCode

        )


        responcelistmaster?.enqueue(object : Callback<ResponeHome> {
            override fun onResponse(@NonNull call: Call<ResponeHome>, @NonNull response: Response<ResponeHome>) {
                viewDialog!!.hideDialog()
                lay_mainhome.visibility = View.VISIBLE
                swipeRefreshLayout.isRefreshing = false
                val recreationList = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (recreationList!!.status == 1) {

                        txt_vacation.text = recreationList.data?.recreation!!.title
                        txt_vacationsub.text = recreationList.data!!.recreation!!.sub_title

                        txt_tour.text = recreationList.data?.tour!!.title
                        txt_toursub.text = recreationList.data!!.tour!!.sub_title

                        txt_event.text = recreationList.data?.event!!.title
                        txt_eventsub.text = recreationList.data!!.event!!.sub_title

                        bannerList =
                            recreationList.data!!.banner as ArrayList<ResponeHome.DataBean.BannerBean>

                        bannerList.sortBy {
                            it.order_id
                        }


                        val adapter = HomeSliderAdapter(activity, bannerList)
                        sliderView.sliderAdapter = adapter
                        sliderView.setIndicatorVisibility(false)

                        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                        sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_RIGHT
                        sliderView.indicatorSelectedColor = Color.TRANSPARENT
                        sliderView.indicatorUnselectedColor = Color.TRANSPARENT
                        sliderView.scrollTimeInSec = 4 //set scroll delay in seconds :
                        sliderView.startAutoCycle()

                        rList =
                            recreationList.data!!.recreation!!.records as ArrayList<ResponeHome.DataBean.RecreationBean.RecordsBeanPlace>
                        tList =
                            (recreationList.data!!.tour!!.records as ArrayList<ResponeHome.DataBean.TourBean.RecordsBeanTour>?)!!
                        eList =
                            (recreationList.data!!.event!!.records as ArrayList<ResponeHome.DataBean.EventBean.RecordsBeanEvent>?)!!

                        if (rList.size == 0) {
                            lay_vacationview.visibility = View.GONE
                        } else {
                            lay_vacationview.visibility = View.VISIBLE
                        }
                        if (tList.size == 0) {
                            lay_tourview.visibility = View.GONE
                        } else {
                            lay_tourview.visibility = View.VISIBLE
                        }
                        if (eList.size == 0) {
                            lay_evenview.visibility = View.GONE
                        } else {
                            lay_evenview.visibility = View.VISIBLE
                        }

                        setVisitorplace()
                        setTourplace()
                        setEvenetData()


                    } else {

                        Toast.makeText(
                            activity,
                            recreationList.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponeHome>, @NonNull t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)
//                Toast.makeText(
//                    activity,
//                    getString(R.string.msg_internet_conn),
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        })

    }


}
