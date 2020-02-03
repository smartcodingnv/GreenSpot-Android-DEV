package com.greenspot.app.responce.hotellist


import com.google.gson.annotations.SerializedName

data class RecordsItem(@SerializedName("master_id")
                       val masterId: String = "",
                       @SerializedName("country")
                       val country: String = "",
                       @SerializedName("avg_reviews")
                       val avgReviews: Int = 0,
                       @SerializedName("city")
                       val city: String = "",
                       @SerializedName("main_image")
                       val mainImage: String = "",
                       @SerializedName("latitude")
                       val latitude: String = "",
                       @SerializedName("discount")
                       val discount: String = "",
                       @SerializedName("included_in_hotel")
                       val includedInHotel: List<IncludedInHotelItem>?,
                       @SerializedName("price")
                       val price: String = "",
                       @SerializedName("district")
                       val district: String = "",
                       @SerializedName("name")
                       val name: String = "",
                       @SerializedName("total_reviews")
                       val totalReviews: Int = 0,
                       @SerializedName("id")
                       val id: Int = 0,
                       @SerializedName("longitude")
                       val longitude: String = "")


data class ResponceHotelList(@SerializedName("data")
                             val data: Data,
                             @SerializedName("message")
                             val message: String = "",
                             @SerializedName("status")
                             val status: Int = 0)


data class Data(@SerializedName("records")
                val records: List<RecordsItem>?,
                @SerializedName("total_records")
                val totalRecords: Int = 0,
                @SerializedName("next_page_number")
                val nextPageNumber: Int = 0,
                @SerializedName("order_by")
                val orderBy: String = "",
                @SerializedName("page_length")
                val pageLength: Int = 0,
                @SerializedName("is_last_page")
                val isLastPage: Int = 0)


data class IncludedInHotelItem(@SerializedName("master_id")
                               val masterId: String = "",
                               @SerializedName("value")
                               val value: String = "")


