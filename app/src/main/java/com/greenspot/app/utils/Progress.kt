package com.greenspot.app.utils

import android.app.ProgressDialog
import android.content.Context

class Progress(private val activity: Context) {

    private var pDialog: ProgressDialog? = null

    fun createDialog(cancelable: Boolean) {
        pDialog = ProgressDialog(activity)
        pDialog!!.setCancelable(cancelable)

    }

    fun DialogMessage(message: String) {
        pDialog!!.setMessage(message)
    }

    fun showDialog() {
        if (!pDialog!!.isShowing)
            pDialog!!.show()
    }

    fun hideDialog() {
        if (pDialog!!.isShowing)
            pDialog!!.dismiss()
    }
}