package com.greenspot.app.responce.eventdetails


import com.google.gson.annotations.SerializedName

data class AmenitiesItem(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("value")
    val value: String = ""
) {
    private var expanded: Boolean = false

    fun setExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    fun isExpanded(): Boolean {
        return expanded
    }
}


data class EvnetDetailsResponce(
    @SerializedName("data")
    val data: Data,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)


data class PopularEventsItem(
    @SerializedName("master_id")
    val masterId: String = "",
    @SerializedName("main_image")
    val mainImage: String = "",
    @SerializedName("price")
    val price: String = "",
    @SerializedName("title")
    val title: String = ""
)


data class MainRecords(
    @SerializedName("end_date")
    val endDate: String = "",
    @SerializedName("suitable_for")
    val suitableFor: String = "",
    @SerializedName("address")
    val address: String = "",
    @SerializedName("avg_reviews")
    val avgReviews: Double = 0.0,
    @SerializedName("organizer_logo")
    val organizerLogo: String = "",
    @SerializedName("latitude")
    val latitude: String = "",
    @SerializedName("end_time")
    val endTime: String = "",
    @SerializedName("event_happening_type")
    val eventHappeningType: String = "",
    @SerializedName("discount")
    val discount: String = "",
    @SerializedName("organizer_address")
    val organizerAddress: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("booking_option_id")
    val bookingOptionId: String = "",
    @SerializedName("created_by")
    val createdBy: String = "",
    @SerializedName("organizer_company_name")
    val organizerCompanyName: String = "",
    @SerializedName("start_time")
    val startTime: String = "",
    @SerializedName("saving")
    val saving: String = "",
    @SerializedName("event_types")
    val eventTypes: String = "",
    @SerializedName("final_price")
    val finalPrice: String = "",
    @SerializedName("booking_required_id")
    val bookingRequiredId: String = "",
    @SerializedName("total_reviews")
    val totalReviews: Int = 0,
    @SerializedName("base_price")
    val basePrice: String = "",
    @SerializedName("paid_event")
    val paidEvent: String = "",
    @SerializedName("start_date")
    val startDate: String = "",
    @SerializedName("longitude")
    val longitude: String = ""
)


data class Ovierview(
    @SerializedName("ovierview")
    val ovierview: String = ""
)


data class Data(
    @SerializedName("amenities")
    val amenities: List<AmenitiesItem>?,
    @SerializedName("popular_events")
    val popularEvents: List<PopularEventsItem>?,
    @SerializedName("main_records")
    val mainRecords: MainRecords,
    @SerializedName("ovierview")
    val ovierview: Ovierview,
    @SerializedName("contact_us")
    val contactUs: ContactUs,
    @SerializedName("gallery_images")
    val galleryImages: List<GalleryImagesItem>?
)


data class ContactUs(
    @SerializedName("address")
    val address: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("office_open_time")
    val officeOpenTime: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("office_close_time")
    val officeCloseTime: String = ""
)


data class GalleryImagesItem(
    @SerializedName("master_id")
    val masterId: String = "",
    @SerializedName("image_name")
    val imageName: String = "",
    @SerializedName("order_id")
    val orderId: Int = 0
)


