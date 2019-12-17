package com.greenspot.app.responce


import com.google.gson.annotations.SerializedName

data class ComanResponce(@SerializedName("data")
                         val data: Any,
                         @SerializedName("message")
                         val message: String = "",
                         @SerializedName("status")
                         val status: Int = 0)





