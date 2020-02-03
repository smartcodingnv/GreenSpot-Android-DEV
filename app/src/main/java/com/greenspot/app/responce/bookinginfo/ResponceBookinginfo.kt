package com.greenspot.app.responce.bookinginfo


import com.google.gson.annotations.SerializedName

data class PaymentInfo(
    @SerializedName("transaction_id")
    val transactionId: String = "",
    @SerializedName("payment_price")
    val paymentPrice: String = "",
    @SerializedName("original_payment_currency")
    val originalPaymentCurrency: String = "",
    @SerializedName("payment_mode")
    val paymentMode: String = "",
    @SerializedName("from_date")
    val fromDate: String = "",
    @SerializedName("payment_status")
    val paymentStatus: String = "",
    @SerializedName("original_single_price")
    val originalSinglePrice: String = "",
    @SerializedName("no_of_person")
    val noOfPerson: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("original_payment_price")
    val originalPaymentPrice: String = "",
    @SerializedName("payment_currency")
    val paymentCurrency: String = "",
    @SerializedName("booking_id")
    val bookingId: String = "",
    @SerializedName("single_price")
    val singlePrice: String = "",
    @SerializedName("payment_type")
    val paymentType: String = "",
    @SerializedName("to_date")
    val toDate: String = ""
)


data class TourAmenitiesItem(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("value")
    val value: String = ""
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


data class TourData(
    @SerializedName("country")
    val country: String = "",
    @SerializedName("sp_email")
    val spEmail: String = "",
    @SerializedName("city")
    val city: String = "",
    @SerializedName("main_image")
    val mainImage: String = "",
    @SerializedName("sp_first_name")
    val spFirstName: String = "",
    @SerializedName("sp_address")
    val spAddress: String = "",
    @SerializedName("tour_detail")
    val tourDetail: String = "",
    @SerializedName("entertainments")
    val entertainments: String = "",
    @SerializedName("departure_city")
    val departureCity: String = "",
    @SerializedName("included_tour_package_id")
    val includedTourPackageId: String = "",
    @SerializedName("main_image_name")
    val mainImageName: String = "",
    @SerializedName("sp_phone")
    val spPhone: String = "",
    @SerializedName("important_note")
    val importantNote: String = "",
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("contact_detail_mobile")
    val contactDetailMobile: String = "",
    @SerializedName("tour_type")
    val tourType: String = "",
    @SerializedName("alias")
    val alias: String = "",
    @SerializedName("currency")
    val currency: String = "",
    @SerializedName("contact_detail_phone")
    val contactDetailPhone: String = "",
    @SerializedName("attractions")
    val attractions: String = "",
    @SerializedName("shopping_types")
    val shoppingTypes: String = "",
    @SerializedName("recreations")
    val recreations: String = "",
    @SerializedName("contact_detail_address")
    val contactDetailAddress: String = "",
    @SerializedName("transportations")
    val transportations: String = "",
    @SerializedName("accomodations")
    val accomodations: String = "",
    @SerializedName("sports")
    val sports: String = "",
    @SerializedName("weathers")
    val weathers: String = "",
    @SerializedName("contact_detail_email")
    val contactDetailEmail: String = "",
    @SerializedName("sp_last_name")
    val spLastName: String = "",
    @SerializedName("term_and_condition")
    val termAndCondition: String = "",
    @SerializedName("tour_month")
    val tourMonth: String = "",
    @SerializedName("nights")
    val nights: Int = 0,
    @SerializedName("district")
    val district: String = "",
    @SerializedName("package_name")
    val packageName: String = "",
    @SerializedName("tour_operator_name")
    val tourOperatorName: String = "",
    @SerializedName("itineraries")
    val itineraries: List<ItinerariesItem>?,
    @SerializedName("days")
    val days: Int = 0,
    @SerializedName("locations")
    val locations: String = "",
    @SerializedName("category")
    val category: String = ""
)


data class ItinerariesItem(
    @SerializedName("master_id")
    val masterId: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("day")
    val day: String = "",
    @SerializedName("hotel_inclusion")
    val hotelInclusion: String = "",
    @SerializedName("hotel_info")
    val hotelInfo: String = ""
)


data class UserInfo(
    @SerializedName("gender")
    val gender: String = "",
    @SerializedName("dob")
    val dob: String = "",
    @SerializedName("last_name")
    val lastName: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("first_name")
    val firstName: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("contact_number")
    val contactNumber: String = ""
)


data class Data(
    @SerializedName("user_info")
    val userInfo: UserInfo,
    @SerializedName("all_users_info")
    val allUsersInfo: List<AllUsersInfoItem>?,
    @SerializedName("payment_info")
    val paymentInfo: PaymentInfo,
    @SerializedName("tour_data")
    val tourData: TourData,
    @SerializedName("tour_amenities")
    val tourAmenities: List<TourAmenitiesItem>?
)


data class ResponceBookinginfo(
    @SerializedName("data")
    val data: Data,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
)


data class AllUsersInfoItem(
    @SerializedName("gender")
    val gender: String = "",
    @SerializedName("dob")
    val dob: String = "",
    @SerializedName("last_name")
    val lastName: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("first_name")
    val firstName: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("contact_number")
    val contactNumber: String = ""
) {
    private var expanded: Boolean = false

    fun setExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    fun isExpanded(): Boolean {
        return expanded
    }
}


