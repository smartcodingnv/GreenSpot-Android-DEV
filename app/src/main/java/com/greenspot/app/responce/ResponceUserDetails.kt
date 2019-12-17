package com.greenspot.app.responce


import com.google.gson.annotations.SerializedName

data class UserData(@SerializedName("is_deleted")
                val isDeleted: String = "",
                @SerializedName("updated_at")
                val updatedAt: String = "",
                @SerializedName("phone")
                val phone: String = "",
                @SerializedName("last_name")
                val lastName: String = "",
                @SerializedName("created_at")
                val createdAt: String = "",
                @SerializedName("_id")
                val Id: String = "",
                @SerializedName("id")
                val id: Int = 0,
                @SerializedName("language_id")
                val languageId: String = "",
                @SerializedName("default_date_format")
                val defaultDateFormat: String = "",
                @SerializedName("first_name")
                val firstName: String = "",
                @SerializedName("email")
                val email: String = "",
                @SerializedName("status")
                val status: String = "")


data class UserDetailsResponce(@SerializedName("data")
                               val data: UserData,
                               @SerializedName("message")
                               val message: String = "",
                               @SerializedName("status")
                               val status: Int = 0)


