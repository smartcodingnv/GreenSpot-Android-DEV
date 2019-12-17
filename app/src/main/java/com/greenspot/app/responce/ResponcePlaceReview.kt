package com.greenspot.app.responce


import com.google.gson.annotations.SerializedName

data class PlaceReivewRecordsItem(@SerializedName("master_id")
                                  val masterId: String = "",
                                  @SerializedName("front_user_first_name")
                                  val frontUserFirstName: String = "",
                                  @SerializedName("front_user_last_name")
                                  val frontUserLastName: String = "",
                                  @SerializedName("review_description")
                                  val reviewDescription: String = "",
                                  @SerializedName("review_star")
                                  val reviewStar: Double = 0.0,
                                  @SerializedName("operation_date")
                                  val operationDate: String = "",
                                  @SerializedName("id")
                                  val id: Int = 0)


data class PlaceReivewData(@SerializedName("records")
                           val records: List<PlaceReivewRecordsItem>?,
                           @SerializedName("total_records")
                           val totalRecords: Int = 0,
                           @SerializedName("next_page_number")
                           val nextPageNumber: Int = 0,
                           @SerializedName("page_length")
                           val pageLength: Int = 0,
                           @SerializedName("is_last_page")
                           val isLastPage: Int = 0)


data class ResponcePlaceReview(@SerializedName("data")
                               val data: PlaceReivewData,
                               @SerializedName("message")
                               val message: String = "",
                               @SerializedName("status")
                               val status: Int = 0)


