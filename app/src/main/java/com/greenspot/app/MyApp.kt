package com.greenspot.app

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import com.stripe.android.PaymentConfiguration

class MyApp : Application() {


    var context: Context? = null

    override fun onCreate() {
        super.onCreate()

        context = applicationContext;

        PaymentConfiguration.init(applicationContext, "pk_test_eVxkRYgEQ98kdUzYhoKhQ12q00YeyU1NxU")


    }
}