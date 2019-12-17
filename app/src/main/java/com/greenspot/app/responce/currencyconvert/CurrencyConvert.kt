package com.greenspot.app.responce.currencyconvert


import com.google.gson.annotations.SerializedName

data class Data(@SerializedName("convert_price")
                val convertPrice: String = "",
                @SerializedName("output_currency")
                val outputCurrency: String = "")


data class CurrencyConvert(@SerializedName("data")
                           val data: Data,
                           @SerializedName("message")
                           val message: String = "",
                           @SerializedName("status")
                           val status: Int = 0)


