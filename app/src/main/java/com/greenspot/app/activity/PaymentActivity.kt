package com.greenspot.app.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonArray
import com.greenspot.app.R
import com.greenspot.app.adapter.BankAdapter
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.model.BankList
import com.greenspot.app.network.ApiClient
import com.greenspot.app.network.ApiInterface
import com.greenspot.app.responce.idealpayment.ResponceIdealPayment
import com.greenspot.app.responce.payment.ResponcePayment
import com.greenspot.app.utils.*
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Source
import com.stripe.android.model.SourceParams
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.content_payment.*
import kotlinx.android.synthetic.main.dialog_contry.*
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList


class PaymentActivity : AppCompatActivity(), ItemClickListener {


    private var sourceid: String? = null
    private var clinetsecret: String? = null

    private var giropaySource: Source? = null
    private var checkPaymentMethod: Int = 1
    private lateinit var jsonArray: JSONArray
    private lateinit var jsonOBject: JSONObject

    private var bankData: ArrayList<BankList> = ArrayList()
    private var progress: Progress? = null
    private var viewDialog: ViewDialog? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    private var helperlang: PreferenceHelper? = null
    private var currncyCode: String? = ""
    private var countryID: String? = ""
    private var bankID: String? = ""
    private var langCode: String? = ""
    private var formDate: String? = ""
    private var single_price: String? = ""
    private var final_price: String? = ""
    private var tour_id: String? = ""
    private var stripe_token: String? = ""
    private var no_of_person: String? = ""
    private var persons: String? = ""
    private var token: String = ""

    private lateinit var dialog: Dialog


    private var meMap: HashMap<String, String> = hashMapOf()

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        progress = Progress(this)
        viewDialog = ViewDialog(this)
        utils = Utils(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)
        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")
        countryID = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRYID, "")

        formDate = helper!!.LoadStringPref(AppConfig.PREFERENCE.FROM_DATE, "")
        single_price = helper!!.LoadStringPref(AppConfig.PREFERENCE.START_PRICE, "")
        final_price = helper!!.LoadStringPref(AppConfig.PREFERENCE.FINAL_PRICE, "")
        no_of_person = helper!!.LoadStringPref(AppConfig.PREFERENCE.NO_OF_PERSON, "")
        persons = helper!!.LoadStringPref(AppConfig.PREFERENCE.PERSONDETAILS, "")

        tour_id = helper!!.LoadStringPref(AppConfig.PREFERENCE.PLACEID, "")
        token = "Bearer " + helper!!.LoadStringPref(AppConfig.PREFERENCE.AUTHTOKEN, "")

        Log.e("finalPrice", "  " + this.final_price!!)
        val payButton = findViewById<Button>(R.id.btn_done)
        val weakActivity = WeakReference<PaymentActivity>(this)

        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        et_bank.setOnClickListener(View.OnClickListener {

            openBankDialog()
        })

        jsonArray = JSONArray(persons)


        rg_payment.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                Log.e("bookingdetails", "radio " + radio.id)

                if (radio.text.equals("Pay with card")) {

                    checkPaymentMethod = 1
                    cardInputWidget.visibility = View.VISIBLE
                    et_bank.visibility = View.GONE
                }
                if (radio.text.equals("Ideal")) {
                    checkPaymentMethod = 2
                    cardInputWidget.visibility = View.GONE
                    et_bank.visibility = View.GONE
                }


            })


//        stripe.createToken(card, object : TokenCallback {
//            override fun onSuccess(token: Token?) {
//                Log.v("Token!","Token Created!!"+ token!!.getId())
//                chargeCard(token!!.getId()); // Pass that token to your Server for further processing
//            }
//
//            override fun onError(error: Exception?) {
//                Log.v("Token!","Token Not Created!!")
//                error!!.printStackTrace()
//            }
//
//        })


        val stripe = Stripe(this, "pk_test_eVxkRYgEQ98kdUzYhoKhQ12q00YeyU1NxU")



        btn_done.setOnClickListener(View.OnClickListener {


            if (checkPaymentMethod == 1) {


                val card = cardInputWidget.card

                if (card == null) {
                    Toast.makeText(
                        applicationContext,
                        "Enter the card",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OnClickListener
                }

                if (!card.validateCard()) {

                    Toast.makeText(
                        applicationContext,
                        "Card is not valid",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OnClickListener
                }


                val cardSourceParams = SourceParams.createCardParams(card!!)
                // The asynchronous way to do it. Call this method on the main thread.
                viewDialog!!.showDialog()
                stripe.createSource(
                    cardSourceParams,
                    object : ApiResultCallback<Source> {
                        override fun onSuccess(source: Source) {
                            // Store the source somewhere, use it, etc


                            Log.e("stripetoken", "111 " + source.id)


                            stripe_token = source.id

                            //                        jsonOBject = JSONObject()
                            //                        jsonOBject.put("tour_id",tour_id)
                            //                        jsonOBject.put("lang_code",langCode)
                            //                        jsonOBject.put("single_price",single_price)
                            //                        jsonOBject.put("final_price",final_price)
                            //                        jsonOBject.put("stripe_token",stripe_token)
                            //                        jsonOBject.put("selected_currency",currncyCode)
                            //                        jsonOBject.put("from_date",formDate)
                            //                        jsonOBject.put("no_of_person",no_of_person)
                            //                        jsonArray = JSONArray(persons)
                            //
                            //                        jsonOBject.put("persons",jsonArray)
                            //
                            //                        Log.e("paymnet", "jsonOBject " + jsonOBject.toString())

                            sendPaymnetData(
                                contryID = countryID!!,
                                selectCurrency = currncyCode!!,
                                langCode = langCode!!
                            )


                        }

                        override fun onError(error: Exception) {
                            // Tell the user that something went wrong

                            Log.e("stripetoken", "22 " + error.message)
                            Log.e("stripetoken", "22 " + error.stackTrace)
                        }
                    })

            } else if (checkPaymentMethod == 2) {

                val amountt = Math.round(final_price!!.toFloat())

                val amount = amountt.toLong()
                val strrrr :String = amount.toString() + ".00"

                val json = JSONObject()
                json.put("currency","EUR")
                json.put("value",strrrr)
                jsonOBject = JSONObject()
                jsonOBject.put("amount", json)
                jsonOBject.put("description", "Order #12345")
                jsonOBject.put("redirectUrl", "com.greenspot.app://post-authentication-return-url")
                jsonOBject.put("webhookUrl", "https://webshop.example.org/payments/webhook/")

                Log.e("paymnet", "jsonOBject " + jsonOBject.toString())

                sendIdealPaymnetData()
/*

  ideal start

  if (bankID!!.isEmpty()) {

                    Toast.makeText(
                        applicationContext,
                        "Select the Bank",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OnClickListener
                }


                val amountt= Math.round(final_price!!.toFloat())
                val amount= amountt.toLong()


                Log.e("amount","  "+amount)


                val giropayParams = SourceParams.createIdealParams(
                    amount,
                    "Customer Name",
                    "com.greenspot.app://post-authentication-return-url",
                    "a purchase description",
                    bank = bankID

                )

//                     assertEquals
//                     val metamap: HashMap<String?, String?> =
//                         object : HashMap<String?, String?>() {
//                             init {
//                                 put("giro", "with chicken")
//                                 put("type", "wrap")
//                             }
//                         }
//                     giropayParams.metaData()

                doAsync {
                    giropaySource = stripe.createSourceSynchronous(giropayParams)

                    if (Source.SourceFlow.REDIRECT.equals(giropaySource!!.flow)) {

                        val redirectUrl = giropaySource!!.redirect!!.url

//                        val redirectUrl = "https://www.mollie.com/payscreen/select-method/Fvs3rWxcKj"
                        clinetsecret = giropaySource!!.clientSecret
                        sourceid = giropaySource!!.id

                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
                        startActivity(browserIntent)

                    }

                }

                ideal end

                */

            }


        })


    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.data != null && intent.data!!.query != null) {
            // The client secret and source ID found here is identical to
            // that of the source used to get the redirect URL.

            val host = intent.data!!.host


            Log.e("tag", " " + intent.scheme)
            Log.e("tag", " " + intent.action)
            Log.e("tag", " " + intent.extras)


            Log.e("tag", " " + host)


        }
    }


    private fun sendPaymnetData(contryID: String, selectCurrency: String, langCode: String) {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))

        utils!!.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val responcePlaceReview = apiService?.PAYMENT_CALL(
            token = token,
            contryID = contryID,
            selectCurrency = selectCurrency,
            langcode = langCode,
            person = jsonArray,
            tourid = this.tour_id!!,
            finalPrice = this.final_price!!,
            formDate = this.formDate!!,
            noofperson = this.no_of_person!!,
            singlePrice = this.single_price!!,
            stripeToken = this.stripe_token!!

        )

        Log.e("token", token)
        Log.e("contryID", contryID)
        Log.e("selectCurrency", selectCurrency)
        Log.e("langcode", langCode)
        Log.e("person", this.persons!!)
        Log.e("tourid", this.tour_id!!)
        Log.e("finalPrice", this.final_price!!)
        Log.e("formDate", this.formDate!!)
        Log.e("noofperson", this.no_of_person!!)
        Log.e("singlePrice", this.single_price!!)
        Log.e("stripeToken", this.stripe_token!!)


        responcePlaceReview?.enqueue(object : Callback<ResponcePayment> {

            override fun onResponse(@NonNull call: Call<ResponcePayment>, @NonNull response: Response<ResponcePayment>) {
                viewDialog!!.hideDialog()

                val postreivewData = response.body()
                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (postreivewData!!.status == 1) {


                        Toast.makeText(
                            this@PaymentActivity,
                            postreivewData.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(Intent(this@PaymentActivity, HomeActivity::class.java))
                        finish()


                    } else {

                        Toast.makeText(
                            this@PaymentActivity,
                            postreivewData.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {

                    Toast.makeText(
                        this@PaymentActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponcePayment>, @NonNull t: Throwable) {

                viewDialog!!.hideDialog()
                Log.e("fail", " " + t.message)
                Toast.makeText(
                    this@PaymentActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun openBankDialog() {

        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_contry)
        dialog.window?.setBackgroundDrawableResource(R.color.colorIdolDetailDialogBackground)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.txt_title.setText("Select Bank")
        val contryAdapter = BankAdapter(this, this)

        bankData.clear()
        setBankData()
        Common.setVerticalRecyclerView(this, dialog.rv_bestseller)
        contryAdapter.swapData(bankData)

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


    private fun setBankData() {
        bankData.add(
            BankList(
                "1",
                "ABN Amro",
                "abn_amro"
            )
        )
        bankData.add(
            BankList(
                "2",
                "ASN Bank",
                "asn_bank"
            )
        )
        bankData.add(
            BankList(
                "3",
                "Bunq B.V.",
                "bunq"
            )
        )
        bankData.add(
            BankList(
                "4",
                "Handelsbanken",
                "handelsbanken"
            )
        )

        bankData.add(
            BankList(
                "5",
                "ING Bank",
                "ing"
            )
        )
        bankData.add(
            BankList(
                "6",
                "Knab",
                "knab"
            )
        )

        bankData.add(
            BankList(
                "7",
                "Moneyou",
                "moneyou"
            )
        )

        bankData.add(
            BankList(
                "8",
                "Rabobank",
                "rabobank"
            )
        )

        bankData.add(
            BankList(
                "9",
                "RegioBank",
                "regiobank"
            )
        )

        bankData.add(
            BankList(
                "10",
                "SNS Bank",
                "sns_bank"
            )
        )

        bankData.add(
            BankList(
                "11",
                "Triodos Bank",
                "triodos_bank"
            )
        )

        bankData.add(
            BankList(
                "12",
                "Van Lanschot",
                "van_lanschot"
            )
        )

    }

    override fun recyclerViewListClicked(v: View, position: Int, flag: Int) {

//        helperlang!!.initPref()
//        helperlang!!.SaveStringPref(
//            AppConfig.PREFERENCE.SELECTCONTRY,
//            bankData.get(position).bankname
//        )
//        helperlang!!.ApplyPref()

        dialog.dismiss()
        et_bank.setText(bankData!!.get(position).bankname)
        bankID = bankData!!.get(position).bankid
    }


    private fun sendIdealPaymnetData() {

//        progress!!.createDialog(false)
//        progress!!.DialogMessage(getString(R.string.please_wait))

        utils!!.hideKeyboard()

        viewDialog!!.showDialog()

        val apiService = ApiClient.client2?.create(ApiInterface::class.java)
        val responcePlaceReview = apiService?.PAYMENTBBODY_CALL(
            token = "Bearer test_QUnmcf4JQEFQGk3d5BDGwNy5NcWvdh",
            jsonBody = jsonOBject.toString()
//          jsonBody = jsonOBject
        )

        responcePlaceReview?.enqueue(object : Callback<ResponceIdealPayment> {

            override fun onResponse(@NonNull call: Call<ResponceIdealPayment>, @NonNull response: Response<ResponceIdealPayment>) {
                viewDialog!!.hideDialog()

                Log.e("ideal"," "+response.code())

                if(response.code()==201 || response.code()==200){

                    val paymentSessionData = response.body()
                    val redirectUrl = paymentSessionData!!.Links.checkout.href
                    if(redirectUrl.isEmpty()){
                        Toast.makeText(
                            this@PaymentActivity,
                            "Try with again",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
                    startActivity(browserIntent)
                }

            }

            override fun onFailure(@NonNull call: Call<ResponceIdealPayment>, @NonNull t: Throwable) {

                viewDialog!!.hideDialog()

                Log.e("fail", " " + t.message)
                Log.e("fail", " " + t.localizedMessage)
                Log.e("fail", " " + t.stackTrace)
                Toast.makeText(
                    this@PaymentActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


}



















