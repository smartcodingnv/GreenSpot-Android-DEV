package com.greenspot.app

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import com.stripe.android.PaymentConfiguration
import java.util.*


class MyApp : Application() {


    var context: Context? = null
    private var helperlang: PreferenceHelper? = null

    override fun onCreate() {
        super.onCreate()
        context = applicationContext;
        mInstance = this
        helperlang = PreferenceHelper(context!!, AppConfig.PREFERENCE.PREF_FILE_LANG)


        PaymentConfiguration.init(applicationContext, "pk_test_eVxkRYgEQ98kdUzYhoKhQ12q00YeyU1NxU")

        updateLanguage(this)

    }



    companion object {
        lateinit var mInstance: MyApp
        fun getContext(): Context? {
            return mInstance.applicationContext
        }

        fun updateLanguage(ctx: Context) {

            val prefs = PreferenceHelper(ctx, AppConfig.PREFERENCE.PREF_FILE_LANG)
            val lang = prefs.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGCODE, "")!!
            if(lang.isEmpty()){
                updateLanguage(ctx,ctx.getString(R.string.default_language))
            }else{

                updateLanguage(ctx, lang)
            }

        }

        private fun updateLanguage(ctx: Context, lang: String) {

            val cfg = Configuration()
            cfg.setLocale(Locale(lang))
            ctx.resources.updateConfiguration(cfg, null)

        }
    }

}