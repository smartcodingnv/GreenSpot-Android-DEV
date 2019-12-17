package com.greenspot.app.responce


import com.google.gson.annotations.SerializedName

data class RecordsItem(@SerializedName("master_id")
                       val masterId: String = "",
                       @SerializedName("place_name")
                       val placeName: String = "",
                       @SerializedName("min_price_range")
                       val minPriceRange: String = "",
                       @SerializedName("country")
                       val country: String = "",
                       @SerializedName("is_booking")
                       val isBooking: Int = 0,
                       @SerializedName("avg_reviews")
                       val avgReviews: Double = 0.0,
                       @SerializedName("city")
                       val city: String = "",
                       @SerializedName("main_image")
                       val mainImage: String = "",
                       @SerializedName("district")
                       val district: String = "",
                       @SerializedName("total_reviews")
                       val totalReviews: Int = 0,
                       @SerializedName("id")
                       val id: Int = 0)


data class ResponceRecreationList(@SerializedName("data")
                                  val data: DataRecreation,
                                  @SerializedName("message")
                                  val message: String = "",
                                  @SerializedName("status")
                                  val status: Int = 0)


data class DataRecreation(@SerializedName("records")
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


