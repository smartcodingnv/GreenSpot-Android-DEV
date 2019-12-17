package com.greenspot.app.responce.payment


import com.google.gson.annotations.SerializedName

data class ResponcePayment(@SerializedName("data")
                           val data: Data,
                           @SerializedName("message")
                           val message: String = "",
                           @SerializedName("status")
                           val status: Int = 0)


data class UserInfo(@SerializedName("transaction_id")
                    val transactionId: String = "",
                    @SerializedName("gender")
                    val gender: String = "",
                    @SerializedName("dob")
                    val dob: String = "",
                    @SerializedName("last_name")
                    val lastName: String = "",
                    @SerializedName("title")
                    val title: String = "",
                    @SerializedName("first_name")
                    val firstName: String = "",
                    @SerializedName("email")
                    val email: String = "",
                    @SerializedName("contact_number")
                    val contactNumber: String = "")


data class Data(@SerializedName("user_info")
                val userInfo: UserInfo)


