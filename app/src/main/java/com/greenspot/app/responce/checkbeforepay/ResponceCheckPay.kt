package com.greenspot.app.responce.checkbeforepay


import com.google.gson.annotations.SerializedName

data class ResponceCheckPay(@SerializedName("data")
                            val data: Data,
                            @SerializedName("message")
                            val message: String = "",
                            @SerializedName("status")
                            val status: Int = 0)


data class Data(@SerializedName("new_price")
                val newPrice: Double = 0.0)


