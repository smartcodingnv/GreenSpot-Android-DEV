package com.greenspot.app.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.greenspot.app.R
import com.greenspot.app.adapter.LanguageAdapter
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.network.ApiClient
import com.greenspot.app.network.ApiInterface
import com.greenspot.app.premissionmanager.PermissionsManager
import com.greenspot.app.premissionmanager.PermissionsResultAction
import com.greenspot.app.responce.LanguagesItem
import com.greenspot.app.responce.ResponceForgotpwd
import com.greenspot.app.responce.ResponceLocation
import com.greenspot.app.responce.login.LoginResponce
import com.greenspot.app.utils.*
import hk.ids.gws.android.sclick.SClick
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_login.*
import kotlinx.android.synthetic.main.dialog_contry.*
import kotlinx.android.synthetic.main.fragment_home.lay_language
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity(), ItemClickListener {
    private val TAG = "LoginActivity"

    private var checkLogin: Int? = 0


    private lateinit var helper: PreferenceHelper
    private lateinit var helperlang: PreferenceHelper
    private lateinit var utils: Utils
    private lateinit var progress: Progress
    private lateinit var viewdialog: ViewDialog
    private lateinit var dialog: Dialog
    private var languageData: List<LanguagesItem>? = null

    private var langCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.greenspot.app.R.layout.activity_login)

        askPermissions()

        utils = Utils(this)
        progress = Progress(this)
        viewdialog = ViewDialog(this);
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)
        val intent = getIntent();
        checkLogin = intent.getIntExtra(AppConfig.EXTRA.CHECKLOGINSIGNUP, 0)
        langCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")
        if(langCode!!.isEmpty()){
            getLocationDate(langCode!!)
        }else{
            getLocationDate(getString(R.string.default_language))
        }


        if (checkLogin == 1) {

            card_login!!.visibility = View.VISIBLE
            card_signup!!.visibility = View.GONE
            card_forgotpwd!!.visibility = View.GONE

        } else if (checkLogin == 2) {

            card_login!!.visibility = View.GONE
            card_signup!!.visibility = View.VISIBLE
            card_forgotpwd!!.visibility = View.GONE

        }
        txt_forgotpwd.setOnClickListener(View.OnClickListener {

            card_login!!.visibility = View.GONE
            card_signup!!.visibility = View.GONE
            card_forgotpwd!!.visibility = View.VISIBLE
        })

        txt_flogin.setOnClickListener(View.OnClickListener {
            card_login!!.visibility = View.VISIBLE
            card_signup!!.visibility = View.GONE
            card_forgotpwd!!.visibility = View.GONE

        })

        txt_login.setOnClickListener(View.OnClickListener {
            card_login!!.visibility = View.VISIBLE
            card_signup!!.visibility = View.GONE

        })

        txt_singup.setOnClickListener(View.OnClickListener {
            card_login!!.visibility = View.GONE
            card_signup!!.visibility = View.VISIBLE

        })

        btn_forgotpwd.setOnClickListener(View.OnClickListener {
            forgotpwd()
        })

        btn_login.setOnClickListener(View.OnClickListener {
            login()
        })

        btn_singup.setOnClickListener(View.OnClickListener {
            singup()
        })

        lay_language.setOnClickListener(View.OnClickListener {
            openLanguageDialog()
        })

        lay_skipp.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            helper.initPref()
            helper.SaveBooleanPref(AppConfig.PREFERENCE.USER_LOGIN_CHECK, true)
            helper.SaveIntPref(AppConfig.PREFERENCE.CHECK_LOGINAS, 1)
            helper.ApplyPref()
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        })

    }

    private fun openLanguageDialog() {

        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_contry)
        dialog.window?.setBackgroundDrawableResource(R.color.colorIdolDetailDialogBackground)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        dialog.txt_title.setText(getString(R.string.str_language))
        val languageAdapter = LanguageAdapter(this, this)

//        contryData.clear()
//        setContryData()
        Common.setVerticalRecyclerView(this, dialog.rv_bestseller)
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


    private fun forgotpwd() {

        val email = et_femail.getText().toString()
        if (email.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.res_enteremail),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (!isValidEmail(email)) {
            Toast.makeText(
                applicationContext,
                getString(R.string.res_validemail),
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        forgotPassword(email = email, langCode = langCode!!)

    }

    private fun singup() {

        val fname = et_sfirstname.getText().toString()
        val lname = et_slastname.getText().toString()
        val phoneno = et_sphoneno.getText().toString()
        val email = et_semail.getText().toString()
        val password = et_spassword.getText().toString()
        if (fname.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.res_enterfname),
                Toast.LENGTH_SHORT
            ).show()
            return

        }
        if (lname.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.res_enterlname),
                Toast.LENGTH_SHORT
            ).show()
            return

        }
        if (phoneno.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.res_enterphonno),
                Toast.LENGTH_SHORT
            ).show()
            return

        }
        if (email.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.res_enteremail),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (password.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.enter_the_password),
                Toast.LENGTH_SHORT
            ).show()

            return
        }
        if (!isValidEmail(email)) {
            Toast.makeText(
                applicationContext,
                getString(R.string.res_validemail),
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        createAccount(fname, lname, phoneno, email, password, this.langCode!!)

    }

    private fun login() {

        val email = et_lemail.getText().toString()
        val password = et_lpassword.getText().toString()
        if (email.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.res_enteremail),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (password.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.enter_the_password),
                Toast.LENGTH_SHORT
            ).show()

            return
        }
        if (!isValidEmail(email)) {
            Toast.makeText(
                applicationContext,
                getString(R.string.res_validemail),
                Toast.LENGTH_SHORT
            ).show()

            return
        }
//               Toast.makeText(
//                   applicationContext,
//                   "Log in",
//                   Toast.LENGTH_SHORT
//               ).show()

        loginuser(
            email = email,
            password = password,
            langCode = langCode!!
        )


    }

    private fun askPermissions() {
        PermissionsManager.getInstance()
            .requestAllManifestPermissionsIfNecessary(this, object : PermissionsResultAction() {
                override fun onGranted() {

                }

                override fun onDenied(permission: String) {
                    val message = String.format(
                        Locale.getDefault(),
                        getString(R.string.message_denied),
                        permission
                    )
                    //                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            })
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun emailValidator(email: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }


    private fun loginuser(email: String, password: String, langCode: String) {

//        progress.createDialog(false)
//        progress.DialogMessage(getString(R.string.please_wait))
//        progress.showDialog()
        viewdialog.showDialog()
        utils.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val loginResponceCall = apiService?.LOGIN_CALL(email, password, langCode)


        loginResponceCall?.enqueue(object : Callback<LoginResponce> {
            override fun onResponse(@NonNull call: Call<LoginResponce>, @NonNull response: Response<LoginResponce>) {
                viewdialog.hideDialog()
                val loginResponce = response.body()


                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (response.body()?.status == 1) {

                        helper.initPref()
                        helper.SaveBooleanPref(AppConfig.PREFERENCE.USER_LOGIN_CHECK, true)
                        helper.SaveStringPref(
                            AppConfig.PREFERENCE.AUTHTOKEN, loginResponce!!.data.token
                        )
                        helper.SaveIntPref(AppConfig.PREFERENCE.USERID, loginResponce.data.userId)
                        helper.SaveStringPref(
                            AppConfig.PREFERENCE.USER_FNAME,
                            loginResponce.data.detail.firstName
                        )
                        helper.SaveStringPref(
                            AppConfig.PREFERENCE.USER_LNAME,
                            loginResponce.data.detail.lastName
                        )
                        helper.SaveStringPref(
                            AppConfig.PREFERENCE.USER_EMAIL,
                            loginResponce.data.detail.email
                        )
                        helper.SaveStringPref(
                            AppConfig.PREFERENCE.USER_CONTACTNO,
                            loginResponce.data.detail.phone
                        )
                        helper.SaveIntPref(AppConfig.PREFERENCE.CHECK_LOGINAS, 2)
                        helper.ApplyPref()
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                        finish()

                    } else {

                        Toast.makeText(
                            applicationContext,
                            loginResponce?.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Log.e(TAG, "fail: " + response.code())
                    Log.e(TAG, "fail: " + response.errorBody())
                    Log.e(TAG, "fail: " + response.message())
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<LoginResponce>, @NonNull t: Throwable) {
                viewdialog.hideDialog()
                Log.e(TAG, "onFailure: " + t.cause)
                Log.e(TAG, "onFailure: " + t.message)
                Log.e(TAG, "onFailure: " + t.stackTrace)
                Toast.makeText(
                    applicationContext,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    private fun forgotPassword(email: String, langCode: String) {

//        progress.createDialog(false)
//        progress.DialogMessage(getString(R.string.please_wait))
//        progress.showDialog()
        viewdialog.showDialog()
        utils.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val forgotpwd = apiService?.FORGOTPWD_CALL(email, langCode)


        forgotpwd?.enqueue(object : Callback<ResponceForgotpwd> {
            override fun onResponse(@NonNull call: Call<ResponceForgotpwd>, @NonNull response: Response<ResponceForgotpwd>) {
                viewdialog.hideDialog()
                val forgotpwd = response.body()


                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (forgotpwd!!.status == 1) {

                        Toast.makeText(
                            applicationContext,
                            forgotpwd?.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        Toast.makeText(
                            applicationContext,
                            forgotpwd?.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponceForgotpwd>, @NonNull t: Throwable) {
                viewdialog.hideDialog()
                Toast.makeText(
                    applicationContext,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    private fun createAccount(
        firstname: String,
        lastname: String,
        phonno: String,
        email: String,
        password: String,
        langCode: String
    ) {

//        progress.createDialog(false)
//        progress.DialogMessage(getString(R.string.please_wait))
//        progress.showDialog()
        viewdialog.showDialog()
        utils.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val loginResponceCall =
            apiService?.SIGNUP_CALL(firstname, lastname, email, password, phonno, langCode)


        loginResponceCall?.enqueue(object : Callback<LoginResponce> {
            override fun onResponse(@NonNull call: Call<LoginResponce>, @NonNull response: Response<LoginResponce>) {
                viewdialog.hideDialog()
                val loginResponce = response.body()


                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (response.body()?.status == 1) {

                        helper.initPref()
                        helper.SaveBooleanPref(AppConfig.PREFERENCE.USER_LOGIN_CHECK, true)
                        helper.SaveStringPref(
                            AppConfig.PREFERENCE.AUTHTOKEN,
                            loginResponce!!.data.token
                        )
                        helper.SaveIntPref(AppConfig.PREFERENCE.USERID, loginResponce.data.userId)
                        helper.SaveStringPref(
                            AppConfig.PREFERENCE.USER_FNAME,
                            loginResponce.data.detail.firstName
                        )
                        helper.SaveStringPref(
                            AppConfig.PREFERENCE.USER_LNAME,
                            loginResponce.data.detail.lastName
                        )
                        helper.SaveStringPref(
                            AppConfig.PREFERENCE.USER_EMAIL,
                            loginResponce.data.detail.email
                        )
                        helper.SaveStringPref(
                            AppConfig.PREFERENCE.USER_CONTACTNO,
                            loginResponce.data.detail.phone
                        )
                        helper.SaveIntPref(AppConfig.PREFERENCE.CHECK_LOGINAS, 2)
                        helper.ApplyPref()
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                        finish()

                    } else {

                        Toast.makeText(
                            applicationContext,
                            loginResponce?.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<LoginResponce>, @NonNull t: Throwable) {
                viewdialog.hideDialog()
                Log.e(TAG, "onFailure: " + t.cause)
                Toast.makeText(
                    applicationContext,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun getLocationDate(langCode: String) {

//        progress.createDialog(false)
//        progress.DialogMessage(getString(R.string.please_wait))
//        progress.showDialog()
        viewdialog.showDialog()
        utils.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val getLocation = apiService?.LOCATION_CALL(langCode)


        getLocation?.enqueue(object : Callback<ResponceLocation> {
            override fun onResponse(@NonNull call: Call<ResponceLocation>, @NonNull response: Response<ResponceLocation>) {
                viewdialog.hideDialog()
                val location = response.body()




                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (location!!.status == 1) {


                        languageData = location.data.languages


                        val selectLanguageName =
                            helperlang.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGNAME, "")
                        if (selectLanguageName.isNullOrEmpty()) {
                            txt_languagelogin.setText(languageData!!.get(0).languageName)
                            if (helperlang.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")!!.isEmpty()) {
                                helperlang.initPref()
                                helperlang.SaveStringPref(
                                    AppConfig.PREFERENCE.SELECTLANGNAME,
                                    languageData!!.get(0).languageName
                                )

                                helperlang.SaveStringPref(
                                    AppConfig.PREFERENCE.SELECTLANGCODE,
                                    languageData!!.get(0).languageCode
                                )
                                helperlang.ApplyPref()
                            }
                        } else {
                            txt_languagelogin.setText(selectLanguageName)
                        }


//                        Toast.makeText(
//                            activity,
//                            location.message,
//                            Toast.LENGTH_SHORT
//                        ).show()

                    } else {

                        Toast.makeText(
                            this@LoginActivity,
                            location.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponceLocation>, @NonNull t: Throwable) {
                viewdialog.hideDialog()
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    override fun recyclerViewListClicked(v: View, position: Int, flag: Int) {
        if (flag == 3) {
            helperlang.initPref()
            helperlang.SaveStringPref(
                AppConfig.PREFERENCE.SELECTLANGNAME,
                languageData!!.get(position).languageName
            )

            helperlang.SaveStringPref(
                AppConfig.PREFERENCE.SELECTLANGCODE,
                languageData!!.get(position).languageCode
            )
            helperlang.ApplyPref()
            dialog.dismiss()
            txt_languagelogin.setText(languageData!!.get(position).languageName)
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        if (viewdialog != null) {
            viewdialog.hideDialog();
        }
    }


}
