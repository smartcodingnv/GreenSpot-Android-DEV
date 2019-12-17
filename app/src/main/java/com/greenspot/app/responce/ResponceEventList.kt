package com.greenspot.app.responce


import com.google.gson.annotations.SerializedName

data class EventListRecordsItem(@SerializedName("master_id")
                                val masterId: String = "",
                                @SerializedName("end_date")
                                val endDate: String = "",
                                @SerializedName("avg_reviews")
                                val avgReviews: Double = 0.0,
                                @SerializedName("main_image")
                                val mainImage: String = "",
                                @SerializedName("end_time")
                                val endTime: String = "",
                                @SerializedName("discount")
                                val discount: String = "",
                                @SerializedName("event_happening_type")
                                val eventHappeningType: String = "",
                                @SerializedName("title")
                                val title: String = "",
                                @SerializedName("booking_option_id")
                                val bookingOptionId: String = "",
                                @SerializedName("event_type_name")
                                val eventTypeName: String = "",
                                @SerializedName("start_time")
                                val startTime: String = "",
                                @SerializedName("final_price")
                                val finalPrice: String = "",
                                @SerializedName("total_reviews")
                                val totalReviews: Int = 0,
                                @SerializedName("base_price")
                                val basePrice: String = "",
                                @SerializedName("location")
                                val location: String = "",
                                @SerializedName("id")
                                val id: Int = 0,
                                @SerializedName("paid_event")
                                val paidEvent: String = "",
                                @SerializedName("start_date")
                                val startDate: String = "")


data class EventData(@SerializedName("records")
                     val records: List<EventListRecordsItem>?,
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


data class ResponceEventList(@SerializedName("data")
                             val data: EventData,
                             @SerializedName("message")
                             val message: String = "",
                             @SerializedName("status")
                             val status: Int = 0)


