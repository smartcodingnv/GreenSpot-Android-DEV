package com.greenspot.app.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
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
import com.greenspot.app.responce.LanguagesItem
import com.greenspot.app.responce.ResponceLocation
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_contry.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), ItemClickListener {

    private lateinit var helper: PreferenceHelper
    private lateinit var helperlang: PreferenceHelper
    private lateinit var utils: Utils
    private lateinit var progress: Progress
    private lateinit var viewDialog: ViewDialog
    private lateinit var dialog: Dialog
    private var languageData: List<LanguagesItem>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        if (PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
                .LoadBooleanPref(AppConfig.PREFERENCE.USER_LOGIN_CHECK, false)
        ) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }
        setContentView(R.layout.activity_main)


        utils = Utils(this)
        progress = Progress(this)
        viewDialog = ViewDialog(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)

        if (helperlang.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")!!.isEmpty()) {
            getLocationDate(helperlang.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")!!)
        } else {

            getLocationDate(getString(R.string.default_language))
        }


        btn_login.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(this@MainActivity, LoginActivity::class.java)
                    .putExtra(AppConfig.EXTRA.CHECKLOGINSIGNUP, 1)
            )
            finish()
        })

        btn_signup.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(this@MainActivity, LoginActivity::class.java)
                    .putExtra(AppConfig.EXTRA.CHECKLOGINSIGNUP, 2)
            )
            finish()
        })

        btn_guest.setOnClickListener(View.OnClickListener {
            helper.initPref()
            helper.SaveBooleanPref(AppConfig.PREFERENCE.USER_LOGIN_CHECK, true)
            helper.SaveIntPref(AppConfig.PREFERENCE.CHECK_LOGINAS, 1)
            helper.ApplyPref()
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        })



        lay_language.setOnClickListener(View.OnClickListener {
            openLanguageDialog()
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


    private fun getLocationDate(langCode: String) {

//        progress.createDialog(false)
//        progress.DialogMessage(getString(R.string.please_wait))
//        progress.showDialog()
        viewDialog.showDialog()
        utils.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val getLocation = apiService?.LOCATION_CALL(langCode)


        getLocation?.enqueue(object : Callback<ResponceLocation> {
            override fun onResponse(@NonNull call: Call<ResponceLocation>, @NonNull response: Response<ResponceLocation>) {
                viewDialog.hideDialog()
                val location = response.body()




                if (response.code() == AppConfig.URL.SUCCESS) {
                    if (location!!.status == 1) {


                        languageData = location.data.languages


                        val selectLanguageName =
                            helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGNAME, "")
                        if (selectLanguageName.isNullOrEmpty()) {
                            txt_languagee.setText(languageData!!.get(0).languageName)

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
                            txt_languagee.setText(selectLanguageName)
                        }


//                        Toast.makeText(
//                            activity,
//                            location.message,
//                            Toast.LENGTH_SHORT
//                        ).show()

                    } else {

                        Toast.makeText(
                            this@MainActivity,
                            location.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ResponceLocation>, @NonNull t: Throwable) {
                viewDialog.hideDialog()
                Toast.makeText(
                    this@MainActivity,
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
            txt_languagee.setText(languageData!!.get(position).languageName)
        }
    }


}
