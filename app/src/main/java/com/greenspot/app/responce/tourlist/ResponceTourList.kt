package com.greenspot.app.responce.tourlist


import com.google.gson.annotations.SerializedName

data class RecordsItem(@SerializedName("master_id")
                       val masterId: String = "",
                       @SerializedName("avg_reviews")
                       val avgReviews: Double = 0.0,
                       @SerializedName("included_in_tour_package")
                       val includedInTourPackage: List<IncludedInTourPackageItem>?,
                       @SerializedName("main_image")
                       val mainImage: String = "",
                       @SerializedName("nights")
                       val nights: Int = 0,
                       @SerializedName("price")
                       val price: String = "",
                       @SerializedName("tour_type")
                       val tourType: String = "",
                       @SerializedName("package_name")
                       val packageName: String = "",
                       @SerializedName("total_reviews")
                       val totalReviews: Int = 0,
                       @SerializedName("days")
                       val days: Int = 0,
                       @SerializedName("locations")
                       val locations: String = "",
                       @SerializedName("id")
                       val id: Int = 0)


data class IncludedInTourPackageItem(@SerializedName("master_id")
                                     val masterId: String = "",
                                     @SerializedName("value")
                                     val value: String = "")


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


data class ResponceTourList(@SerializedName("data")
                            val data: Data,
                            @SerializedName("message")
                            val message: String = "",
                            @SerializedName("status")
                            val status: Int = 0)


