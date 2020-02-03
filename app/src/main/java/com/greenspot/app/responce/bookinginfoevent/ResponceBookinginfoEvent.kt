package com.greenspot.app.responce.bookinginfoevent


import com.google.gson.annotations.SerializedName

data class PaymentInfo(@SerializedName("transaction_id")
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
                       val toDate: String = "",
                       @SerializedName("paid_event")
                       val paidEvent: String = "")



data class ResponceBookinginfoEvent(
    @SerializedName("data")
    val data: Data,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Int = 0
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
    @SerializedName("event_amenities")
    val eventAmenities: List<EventAmenitiesItem>?,
    @SerializedName("event_data")
    val eventData: EventData,
    @SerializedName("payment_info")
    val paymentInfo: PaymentInfo
)


data class EventAmenitiesItem(
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


data class EventData(
    @SerializedName("end_date")
    val endDate: String = "",
    @SerializedName("country")
    val country: String = "",
    @SerializedName("event_happening_type_id")
    val eventHappeningTypeId: String = "",
    @SerializedName("city")
    val city: String = "",
    @SerializedName("sp_email")
    val spEmail: String = "",
    @SerializedName("main_image")
    val mainImage: String = "",
    @SerializedName("sp_first_name")
    val spFirstName: String = "",
    @SerializedName("sp_address")
    val spAddress: String = "",
    @SerializedName("discount")
    val discount: Int = 0,
    @SerializedName("booking_required")
    val bookingRequired: String = "",
    @SerializedName("event_happening_type")
    val eventHappeningType: String = "",
    @SerializedName("organizer_mobile")
    val organizerMobile: String = "",
    @SerializedName("organizer_address")
    val organizerAddress: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("organizer_company_name")
    val organizerCompanyName: String = "",
    @SerializedName("main_image_name")
    val mainImageName: String = "",
    @SerializedName("end_date_time")
    val endDateTime: String = "",
    @SerializedName("event_type")
    val eventType: String = "",
    @SerializedName("sp_phone")
    val spPhone: String = "",
    @SerializedName("final_price")
    val finalPrice: Int = 0,
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("alias")
    val alias: String = "",
    @SerializedName("currency")
    val currency: String = "",
    @SerializedName("paid_event")
    val paidEvent: String = "",
    @SerializedName("organizer_phone")
    val organizerPhone: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("start_date")
    val startDate: String = "",
    @SerializedName("suitables")
    val suitables: String = "",
    @SerializedName("transportations")
    val transportations: String = "",
    @SerializedName("accomodations")
    val accomodations: String = "",
    @SerializedName("address")
    val address: String = "",
    @SerializedName("weathers")
    val weathers: String = "",
    @SerializedName("availabilities")
    val availabilities: String = "",
    @SerializedName("organizer_email")
    val organizerEmail: String = "",
    @SerializedName("term_and_condition")
    val termAndCondition: String = "",
    @SerializedName("sp_last_name")
    val spLastName: String = "",
    @SerializedName("end_time")
    val endTime: String = "",
    @SerializedName("paid_event_id")
    val paidEventId: String = "",
    @SerializedName("discount_type")
    val discountType: String = "",
    @SerializedName("start_time")
    val startTime: String = "",
    @SerializedName("booking_option")
    val bookingOption: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("district")
    val district: String = "",
    @SerializedName("detail")
    val detail: String = "",
    @SerializedName("start_date_time")
    val startDateTime: String = "",
    @SerializedName("office_open_time")
    val officeOpenTime: String = "",
    @SerializedName("office_close_time")
    val officeCloseTime: String = ""
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



