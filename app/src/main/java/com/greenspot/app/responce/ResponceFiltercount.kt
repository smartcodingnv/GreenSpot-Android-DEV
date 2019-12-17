package com.greenspot.app.responce


import com.google.gson.annotations.SerializedName

data class ResponceFilterCount(@SerializedName("data")
                               val data: DataResponce,
                               @SerializedName("message")
                               val message: String = "",
                               @SerializedName("status")
                               val status: Int = 0)


data class DataResponce(@SerializedName("count")
                val count: Int = 0)


