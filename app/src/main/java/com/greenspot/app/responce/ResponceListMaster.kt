package com.greenspot.app.responce


import com.google.gson.annotations.SerializedName
import java.util.*

data class FilterRecordsItem(
    @SerializedName("master_id")
    val masterId: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("id")
    val id: Int = 0,
    var check: Boolean



)


data class DataItemFilterTitle(
    @SerializedName("records")
    val records: List<FilterRecordsItem>?,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("request_key")
    val requestKey: String = ""
)


data class ResponceListMaster(
    @SerializedName("data")
    val data: List<DataItemFilterTitle>?,
    @SerializedName("slider_max")
    val sliderMax: Int = 0,
    @SerializedName("sorting")
    val sorting: List<SortingItem>?,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)


data class SortingItem(
    @SerializedName("column")
    val column: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("is_default")
    val isDefault: Int = 0,
    @SerializedName("order")
    val order: String = ""
)


