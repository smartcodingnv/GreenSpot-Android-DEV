package com.greenspot.app.responce.paymentmollie


import com.google.gson.annotations.SerializedName

data class ResponcePaymentMollie(@SerializedName("data")
                                 val data: Data,
                                 @SerializedName("message")
                                 val message: String = "",
                                 @SerializedName("status")
                                 val status: Int = 0)


data class Data(@SerializedName("id")
                val id: String = "")


