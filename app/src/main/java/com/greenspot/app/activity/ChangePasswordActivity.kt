package com.greenspot.app.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.greenspot.app.R
import com.greenspot.app.network.ApiClient
import com.greenspot.app.network.ApiInterface
import com.greenspot.app.responce.ComanResponce
import com.greenspot.app.responce.ResponceForgotpwd
import com.greenspot.app.utils.*
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.content_change_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var token: String
    private lateinit var helper: PreferenceHelper
    private lateinit var helperlang: PreferenceHelper
    private lateinit var utils: Utils
    private lateinit var progress: Progress
    private lateinit var viewDialog: ViewDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        utils = Utils(this)
        progress = Progress(this)
        viewDialog = ViewDialog(this)
        helper = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE)
        helperlang = PreferenceHelper(this, AppConfig.PREFERENCE.PREF_FILE_LANG)

        token = "Bearer " + helper.LoadStringPref(AppConfig.PREFERENCE.AUTHTOKEN, "")

        ib_back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        btn_updatepwd.setOnClickListener(View.OnClickListener {
            updatepwd()
        })


    }

    private fun updatepwd() {

        val currentpwd = et_oldpwd.getText().toString()
        val newpwd = et_newpwd.getText().toString()
        val confpwd = et_confpwd.getText().toString()
        if (currentpwd.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.alert_currentpwd),
                Toast.LENGTH_SHORT
            ).show()
            return

        }
        if (newpwd.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.alert_newpwd),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (confpwd.isEmpty()) {

            Toast.makeText(
                applicationContext,
                getString(R.string.alert_confpwd),
                Toast.LENGTH_SHORT
            ).show()

            return
        }


//        if (isValidMobile(number)) {
//            Toast.makeText(
//                applicationContext,
//                getString(R.string.res_validnumber),
//                Toast.LENGTH_SHORT
//            ).show()
//
//            return
//
//        }
        if (!confpwd.equals(newpwd)) {
//            password mismatch
            Toast.makeText(
                applicationContext,
                getString(R.string.alert_pwdmismatch),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        changePwd(
            token = token,
            oldpwd = currentpwd,
            newPwd = newpwd,
            langCode = helperlang.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")!!
        )


    }

    private fun changePwd(token: String, oldpwd: String, newPwd: String, langCode: String) {

//        progress.createDialog(false)
//        progress.DialogMessage(getString(R.string.please_wait))
//        progress.showDialog()
        viewDialog.showDialog()
        utils.hideKeyboard()

        val apiService = ApiClient.client?.create(ApiInterface::class.java)
        val forgotpwd = apiService?.CHANGEPWD_CALL(token,oldpwd,newPwd,langCode)
        forgotpwd?.enqueue(object : Callback<ComanResponce> {
            override fun onResponse(@NonNull call: Call<ComanResponce>, @NonNull response: Response<ComanResponce>) {
                viewDialog.hideDialog()
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
                } else if (response.code() == AppConfig.URL.TOKEN_EXPIRE) {

                    login()

                }else {

                    Toast.makeText(
                        applicationContext,
                        getString(R.string.msg_unexpected_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(@NonNull call: Call<ComanResponce>, @NonNull t: Throwable) {
                viewDialog.hideDialog()
                Toast.makeText(
                    applicationContext,
                    getString(R.string.msg_internet_conn),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }
    private fun login() {

        helper.clearAllPrefs()
        startActivity(
            Intent(this, MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        finish()
    }

}
