package com.greenspot.app.responce.bookinglist


import com.google.gson.annotations.SerializedName

data class RecordsItem(@SerializedName("master_id")
                       val masterId: String = "",
                       @SerializedName("original_payment_currency")
                       val originalPaymentCurrency: String = "",
                       @SerializedName("from_date")
                       val fromDate: String = "",
                       @SerializedName("main_image")
                       val mainImage: String = "",
                       @SerializedName("person_full_name")
                       val personFullName: String = "",
                       @SerializedName("no_of_person")
                       val noOfPerson: String = "",
                       @SerializedName("transaction_at")
                       val transactionAt: String = "",
                       @SerializedName("title")
                       val title: String = "",
                       @SerializedName("type")
                       val type: String = "",
                       @SerializedName("original_payment_price")
                       val originalPaymentPrice: String = "",
                       @SerializedName("to_date")
                       val toDate: String = "",
                       @SerializedName("locations")
                       val locations: String = "",
                       @SerializedName("id")
                       val id: Int = 0)


data class Data(@SerializedName("records")
                val records: List<RecordsItem>?,
                @SerializedName("total_records")
                val totalRecords: Int = 0,
                @SerializedName("next_page_number")
                val nextPageNumber: Int = 0,
                @SerializedName("page_length")
                val pageLength: Int = 0,
                @SerializedName("is_last_page")
                val isLastPage: Int = 0)


data class ResponceBookingList(@SerializedName("data")
                               val data: Data,
                               @SerializedName("message")
                               val message: String = "",
                               @SerializedName("status")
                               val status: Int = 0)


