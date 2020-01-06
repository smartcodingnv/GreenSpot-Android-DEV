package com.greenspot.app.responce.bookinginforecreation


import com.google.gson.annotations.SerializedName

data class AmenitiesItem(@SerializedName("records")
                         var records: List<RecordsItem>?,
                         @SerializedName("id")
                         val id: Int = 0,
                         @SerializedName("title")
                         val title: String = "")
{
    private var expanded: Boolean = false

    fun setExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    fun isExpanded(): Boolean {
        return expanded
    }
}


data class PaymentInfo(@SerializedName("transaction_id")
                       val transactionId: String = "",
                       @SerializedName("payment_price")
                       val paymentPrice: String = "",
                       @SerializedName("original_payment_currency")
                       val originalPaymentCurrency: String = "",
                       @SerializedName("payment_mode")
                       val paymentMode: String = "",
                       @SerializedName("night_pass_adult_person")
                       val nightPassAdultPerson: String = "",
                       @SerializedName("from_date")
                       val fromDate: String = "",
                       @SerializedName("payment_status")
                       val paymentStatus: String = "",
                       @SerializedName("original_single_price")
                       val originalSinglePrice: String = "",
                       @SerializedName("no_of_person")
                       val noOfPerson: Int = 0,
                       @SerializedName("created_at")
                       val createdAt: String = "",
                       @SerializedName("night_pass_child_person")
                       val nightPassChildPerson: String = "",
                       @SerializedName("day_pass_child_person")
                       val dayPassChildPerson: String = "",
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
                       @SerializedName("day_pass_adult_person")
                       val dayPassAdultPerson: String = "",
                       @SerializedName("to_date")
                       val toDate: String = "",
                       @SerializedName("day_pass_adult_person_price")
                       val dayPassAdultPersonPrice: String = "",
                       @SerializedName("night_pass_adult_person_price")
                       val nightPassAdultPersonPrice: String = "",
                       @SerializedName("night_pass_child_person_price")
                       val nightPassChildPersonPrice: String = "",
                       @SerializedName("day_pass_child_person_price")
                       val dayPassChildPersonPrice: String = "")


data class RecreationData(@SerializedName("country")
                          val country: String = "",
                          @SerializedName("midweek_adult_daypass")
                          val midweekAdultDaypass: String = "",
                          @SerializedName("vegitation_types")
                          val vegitationTypes: String = "",
                          @SerializedName("sp_email")
                          val spEmail: String = "",
                          @SerializedName("sp_first_name")
                          val spFirstName: String = "",
                          @SerializedName("internet_coverages")
                          val internetCoverages: String = "",
                          @SerializedName("sp_address")
                          val spAddress: String = "",
                          @SerializedName("phone2")
                          val phone2: String = "",
                          @SerializedName("entertainments")
                          val entertainments: String = "",
                          @SerializedName("accessibilities")
                          val accessibilities: String = "",
                          @SerializedName("weekend_child_nightpass")
                          val weekendChildNightpass: String = "",
                          @SerializedName("recreation_types")
                          val recreationTypes: String = "",
                          @SerializedName("phone1")
                          val phone1: String = "",
                          @SerializedName("detail_description")
                          val detailDescription: String = "",
                          @SerializedName("renting_options")
                          val rentingOptions: String = "",
                          @SerializedName("weekend_adult_daypass")
                          val weekendAdultDaypass: String = "",
                          @SerializedName("animals")
                          val animals: String = "",
                          @SerializedName("attractions")
                          val attractions: String = "",
                          @SerializedName("longitude")
                          val longitude: String = "",
                          @SerializedName("water_remarks")
                          val waterRemarks: String = "",
                          @SerializedName("accomodations")
                          val accomodations: String = "",
                          @SerializedName("free_wifi")
                          val freeWifi: String = "",
                          @SerializedName("sp_last_name")
                          val spLastName: String = "",
                          @SerializedName("midweek_adult_nightpass")
                          val midweekAdultNightpass: String = "",
                          @SerializedName("weekend_child_daypass")
                          val weekendChildDaypass: String = "",
                          @SerializedName("weekend_adult_nightpass")
                          val weekendAdultNightpass: String = "",
                          @SerializedName("district")
                          val district: String = "",
                          @SerializedName("special_features")
                          val specialFeatures: String = "",
                          @SerializedName("short_description")
                          val shortDescription: String = "",
                          @SerializedName("other")
                          val other: String = "",
                          @SerializedName("city")
                          val city: String = "",
                          @SerializedName("main_image")
                          val mainImage: String = "",
                          @SerializedName("latitude")
                          val latitude: String = "",
                          @SerializedName("safeties")
                          val safeties: String = "",
                          @SerializedName("mobile1")
                          val mobile1: String = "",
                          @SerializedName("midweek_child_nightpass")
                          val midweekChildNightpass: String = "",
                          @SerializedName("sp_phone")
                          val spPhone: String = "",
                          @SerializedName("mobile2")
                          val mobile2: String = "",
                          @SerializedName("alias")
                          val alias: String = "",
                          @SerializedName("currency")
                          val currency: String = "",
                          @SerializedName("food_and_beverages")
                          val foodAndBeverages: String = "",
                          @SerializedName("email")
                          val email: String = "",
                          @SerializedName("transportations")
                          val transportations: String = "",
                          @SerializedName("suitables")
                          val suitables: String = "",
                          @SerializedName("place_name")
                          val placeName: String = "",
                          @SerializedName("address")
                          val address: String = "",
                          @SerializedName("weathers")
                          val weathers: String = "",
                          @SerializedName("animal_names")
                          val animalNames: String = "",
                          @SerializedName("water_types")
                          val waterTypes: String = "",
                          @SerializedName("other_activities")
                          val otherActivities: String = "",
                          @SerializedName("midweek_child_daypass")
                          val midweekChildDaypass: String = "",
                          @SerializedName("animal_types")
                          val animalTypes: String = "",
                          @SerializedName("mobile_coverages")
                          val mobileCoverages: String = "",
                          @SerializedName("radio_coverages")
                          val radioCoverages: String = "",
                          @SerializedName("type_of_stays")
                          val typeOfStays: String = "")


data class RecordsItem(@SerializedName("id")
                       val id: Double = 0.0,
                       @SerializedName("title")
                       val title: String = "",
                       @SerializedName("value")
                       val value: String = "")


data class ResponceBookinginfoRecreation(@SerializedName("data")
                                         val data: Data,
                                         @SerializedName("message")
                                         val message: String = "",
                                         @SerializedName("status")
                                         val status: Int = 0)


data class UserInfo(@SerializedName("gender")
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
                    val contactNumber: String = "")


data class StayItem(@SerializedName("records")
                    var records: List<RecordsItem>?,
                    @SerializedName("id")
                    val id: Int = 0,
                    @SerializedName("title")
                    val title: String = "")
{
    private var expanded: Boolean = false

    fun setExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    fun isExpanded(): Boolean {
        return expanded
    }
}

data class Data(@SerializedName("other_activities")
                val otherActivities: String = "",
                @SerializedName("amenities")
                val amenities: List<AmenitiesItem>?,
                @SerializedName("user_info")
                val userInfo: UserInfo,
                @SerializedName("payment_info")
                val paymentInfo: PaymentInfo,
                @SerializedName("recreation_data")
                val recreationData: RecreationData,
                @SerializedName("stay")
                val stay: List<StayItem>?)


