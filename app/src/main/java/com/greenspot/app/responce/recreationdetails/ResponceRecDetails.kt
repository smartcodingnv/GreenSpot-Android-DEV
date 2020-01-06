package com.greenspot.app.responce.recreationdetails


import com.google.gson.annotations.SerializedName

data class Availability(
    @SerializedName("address")
    val address: String = "",
    @SerializedName("office_address")
    val officeAddress: String = "",
    @SerializedName("place_open_time")
    val placeOpenTime: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("place_close_time")
    val placeCloseTime: String = "",
    @SerializedName("known_for")
    val knownFor: String = "",
    @SerializedName("mobile")
    val mobile: String = "",
    @SerializedName("office_open_time")
    val officeOpenTime: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("office_close_time")
    val officeCloseTime: String = ""
)


data class AmenitiesRecordsItem(
    @SerializedName("records")
    var records: List<RecordsItem>?,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = ""
)
{
    private var expanded: Boolean = false

    fun setExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    fun isExpanded(): Boolean {
        return expanded
    }
}



data class NearbyPlacesItem(
    @SerializedName("master_id")
    val masterId: String = "",
    @SerializedName("place_name")
    val placeName: String = "",
    @SerializedName("country")
    val country: String = "",
    @SerializedName("city")
    val city: String = "",
    @SerializedName("main_image")
    val mainImage: String = "",
    @SerializedName("district")
    val district: String = ""
)


data class RecordsItem(
    @SerializedName("id")
    val id: Double = 0.0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("value")
    val value: String = ""
)


data class Description(
    @SerializedName("description")
    val description: String = ""
)


data class OtherRecordsItem(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("value")
    val value: String = ""
)


data class Payments(
    @SerializedName("payment_records")
    val paymentRecords: List<PaymentRecordsItem>?,
    @SerializedName("other_records")
    val otherRecords: List<OtherRecordsItem>?
)


data class StayItem(
    @SerializedName("records")
    var records: List<RecordsItem>?,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = ""
) {
    private var expanded: Boolean = false

    fun setExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    fun isExpanded(): Boolean {
        return expanded
    }
}

data class Data(
    @SerializedName("amenities")
    val amenities: Amenities,
    @SerializedName("nearby_places")
    val nearbyPlaces: List<NearbyPlacesItem>?,
    @SerializedName("other")
    val other: List<OtherItem>?,
    @SerializedName("social_links")
    val socialLinks: List<SocialLinksItem>?,
    @SerializedName("payments")
    val payments: Payments,
    @SerializedName("description")
    val description: Description,
    @SerializedName("availability")
    val availability: Availability,
    @SerializedName("contact_us")
    val contactUs: ContactUs,
    @SerializedName("stay")
    val stay: List<StayItem>?,
    @SerializedName("main_records")
    val mainRecords: MainRecords,
    @SerializedName("prices")
    val prices: Prices,
    @SerializedName("suggested_tours")
    val suggestedTours: List<SuggestedToursItem>?,
    @SerializedName("gallery_images")
    val galleryImages: List<GalleryImagesItem>?
)

data class SuggestedToursItem(@SerializedName("master_id")
                              val masterId: String = "",
                              @SerializedName("avg_reviews")
                              val avgReviews: Int = 0,
                              @SerializedName("main_image")
                              val mainImage: String = "",
                              @SerializedName("package_name")
                              val packageName: String = "",
                              @SerializedName("total_reviews")
                              val totalReviews: Int = 0)


data class ContactUs(
    @SerializedName("address")
    val address: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("email")
    val email: String = ""
)


data class Prices(
    @SerializedName("mid_week_day_pass_adult")
    val midWeekDayPassAdult: String = "",
    @SerializedName("mid_week_day_pass_child")
    val midWeekDayPassChild: String = "",
    @SerializedName("weekend_night_pass_child")
    val weekendNightPassChild: String = "",
    @SerializedName("weekend_night_pass_adult")
    val weekendNightPassAdult: String = "",
    @SerializedName("mid_week_night_pass_adult")
    val midWeekNightPassAdult: String = "",
    @SerializedName("mid_week_night_pass_child")
    val midWeekNightPassChild: String = "",
    @SerializedName("weekend_day_pass_adult")
    val weekendDayPassAdult: String = "",
    @SerializedName("weekend_day_pass_child")
    val weekendDayPassChild: String = ""
)


data class OtherItem(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("value")
    val value: String = ""
)


data class ResponceRecDetails(
    @SerializedName("data")
    val data: Data,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)


data class MainRecords(
    @SerializedName("place_name")
    val placeName: String = "",
    @SerializedName("country")
    val country: String = "",
    @SerializedName("short_description")
    val shortDescription: String = "",
    @SerializedName("min_price_range")
    val minPriceRange: String = "",
    @SerializedName("is_booking")
    val isBooking: Int = 0,
    @SerializedName("avg_reviews")
    val avgReviews: Double = 0.0,
    @SerializedName("booking_option")
    val bookingOption: Int = 0,
    @SerializedName("district")
    val district: String = "",
    @SerializedName("latitude")
    val latitude: String = "",
    @SerializedName("total_reviews")
    val totalReviews: Int = 0,
    @SerializedName("created_by")
    val createdBy: String = "",
    @SerializedName("longitude")
    val longitude: String = ""
)


data class PaymentRecordsItem(
    @SerializedName("records")
    var records: List<RecordsItem>?,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = ""
) {
    private var expanded: Boolean = false

    fun setExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    fun isExpanded(): Boolean {
        return expanded
    }
}

data class SocialLinksItem(
    @SerializedName("master_id")
    val masterId: String = "",
    @SerializedName("video_name")
    val videoName: String = "",
    @SerializedName("order_id")
    val orderId: Int = 0
)


data class Amenities(
    @SerializedName("other_activities")
    val otherActivities: String = "",
    @SerializedName("amenities_records")
    val amenitiesRecords: List<AmenitiesRecordsItem>?
) {
    private var expanded: Boolean = false

    fun setExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    fun isExpanded(): Boolean {
        return expanded
    }
}


data class GalleryImagesItem(
    @SerializedName("master_id")
    val masterId: String = "",
    @SerializedName("image_name")
    val imageName: String = "",
    @SerializedName("order_id")
    val orderId: Int = 0
)


