package com.greenspot.app.responce


import com.google.gson.annotations.SerializedName

data class LoginResponce(@SerializedName("data")
                         val data: Data,
                         @SerializedName("message")
                         val message: String = "",
                         @SerializedName("status")
                         val status: Int = 0)


data class Data(@SerializedName("user_id")
                val userId: Int = 0,
                @SerializedName("detail")
                val detail: Detail,
                @SerializedName("token")
                val token: String = "")


data class Detail(@SerializedName("last_name")
                  val lastName: String = "",
                  @SerializedName("created_at")
                  val createdAt: String = "",
                  @SerializedName("language_id")
                  val languageId: Int = 0,
                  @SerializedName("default_date_format")
                  val defaultDateFormat: String = "",
                  @SerializedName("is_deleted")
                  val isDeleted: String = "",
                  @SerializedName("updated_at")
                  val updatedAt: String = "",
                  @SerializedName("phone")
                  val phone: String = "",
                  @SerializedName("_id")
                  val Id: String = "",
                  @SerializedName("id")
                  val id: Int = 0,
                  @SerializedName("time_zone_id")
                  val timeZoneId: String = "",
                  @SerializedName("first_name")
                  val firstName: String = "",
                  @SerializedName("email")
                  val email: String = "",
                  @SerializedName("status")
                  val status: String = "")


