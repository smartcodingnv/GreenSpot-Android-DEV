package com.greenspot.app.responce.bookinginfohotel


import com.google.gson.annotations.SerializedName

data class AmenitiesItem(@SerializedName("id")
                         val id: Int = 0,
                         @SerializedName("title")
                         val title: String = "",
                         @SerializedName("value")
                         val value: String = "")
{
    private var expanded: Boolean = false

    fun setExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    fun isExpanded(): Boolean {
        return expanded
    }
}


data class RoomInfoItem(@SerializedName("other")
                        val other: List<String>?,
                        @SerializedName("room_facilitie")
                        val roomFacilitie: String = "",
                        @SerializedName("hotel_room_image")
                        val hotelRoomImage: String = "",
                        @SerializedName("master_id")
                        val masterId: String = "",
                        @SerializedName("bed_type")
                        val bedType: String = "",
                        @SerializedName("extra_bed_charge")
                        val extraBedCharge: String = "",
                        @SerializedName("discount")
                        val discount: String = "",
                        @SerializedName("total_room_selected")
                        val totalRoomSelected: String = "",
                        @SerializedName("maximum_child_allow")
                        val maximumChildAllow: String = "",
                        @SerializedName("price_per_night")
                        val pricePerNight: String = "",
                        @SerializedName("discount_type_id")
                        val discountTypeId: String = "",
                        @SerializedName("options")
                        val options: List<String>?,
                        @SerializedName("maximum_adult_allow")
                        val maximumAdultAllow: String = "",
                        @SerializedName("include_this_price")
                        val includeThisPrice: List<String>?,
                        @SerializedName("room_type")
                        val roomType: String = "")


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
                       @SerializedName("no_of_rooms")
                       val noOfRooms: Int = 0)


data class ResponceBookinginfoHotel(@SerializedName("data")
                                    val data: Data,
                                    @SerializedName("message")
                                    val message: String = "",
                                    @SerializedName("status")
                                    val status: Int = 0)





data class RecordsItem(@SerializedName("id")
                       val id: Int = 0,
                       @SerializedName("title")
                       val title: String = "",
                       @SerializedName("value")
                       val value: String = "")


data class Policies(@SerializedName("records")
                    val records: List<RecordsItem>?,
                    @SerializedName("term_and_policy")
                    val termAndPolicy: String = "")




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


data class Data(@SerializedName("amenities")
                val amenities: List<AmenitiesItem>?,
                @SerializedName("hotel_data")
                val hotelData: HotelData,
                @SerializedName("user_info")
                val userInfo: UserInfo,
                @SerializedName("policies")
                val policies: Policies,
                @SerializedName("room_info")
                val roomInfo: List<RoomInfoItem>?,
                @SerializedName("payment_info")
                val paymentInfo: PaymentInfo)


data class HotelData(@SerializedName("country")
                     val country: String = "",
                     @SerializedName("avg_reviews")
                     val avgReviews: Int = 0,
                     @SerializedName("sp_email")
                     val spEmail: String = "",
                     @SerializedName("suitable_fors")
                     val suitableFors: String = "",
                     @SerializedName("sp_first_name")
                     val spFirstName: String = "",
                     @SerializedName("sp_address")
                     val spAddress: String = "",
                     @SerializedName("entertainments")
                     val entertainments: String = "",
                     @SerializedName("detail_description")
                     val detailDescription: String = "",
                     @SerializedName("contact_person_name")
                     val contactPersonName: String = "",
                     @SerializedName("term_and_policy")
                     val termAndPolicy: String = "",
                     @SerializedName("attractions")
                     val attractions: String = "",
                     @SerializedName("landmark")
                     val landmark: String = "",
                     @SerializedName("fax")
                     val fax: String = "",
                     @SerializedName("longitude")
                     val longitude: String = "",
                     @SerializedName("whatsapp_number")
                     val whatsappNumber: String = "",
                     @SerializedName("children_free_age")
                     val childrenFreeAge: Int = 0,
                     @SerializedName("accomodations")
                     val accomodations: String = "",
                     @SerializedName("check_out_time")
                     val checkOutTime: String = "",
                     @SerializedName("sp_last_name")
                     val spLastName: String = "",
                     @SerializedName("room_booking_limit_per_transaction")
                     val roomBookingLimitPerTransaction: Int = 0,
                     @SerializedName("district")
                     val district: String = "",
                     @SerializedName("name")
                     val name: String = "",
                     @SerializedName("meals")
                     val meals: String = "",
                     @SerializedName("pets_allow")
                     val petsAllow: String = "",
                     @SerializedName("city")
                     val city: String = "",
                     @SerializedName("main_image")
                     val mainImage: String = "",
                     @SerializedName("check_in_time")
                     val checkInTime: String = "",
                     @SerializedName("latitude")
                     val latitude: String = "",
                     @SerializedName("mobile1")
                     val mobile: String = "",
                     @SerializedName("booking_option_id")
                     val bookingOptionId: String = "",
                     @SerializedName("landline1")
                     val landline: String = "",
                     @SerializedName("sp_phone")
                     val spPhone: String = "",
                     @SerializedName("room_types")
                     val roomTypes: String = "",
                     @SerializedName("alias")
                     val alias: String = "",
                     @SerializedName("currency")
                     val currency: String = "",
                     @SerializedName("shopping_types")
                     val shoppingTypes: String = "",
                     @SerializedName("email")
                     val email: String = "",
                     @SerializedName("views")
                     val views: String = "",
                     @SerializedName("transportations")
                     val transportations: String = "",
                     @SerializedName("address")
                     val address: String = "",
                     @SerializedName("sports")
                     val sports: String = "",
                     @SerializedName("weathers")
                     val weathers: String = "",
                     @SerializedName("availabilities")
                     val availabilities: String = "",
                     @SerializedName("spoken_languages")
                     val spokenLanguages: String = "",
                     @SerializedName("property_types")
                     val propertyTypes: String = "",
                     @SerializedName("total_reviews")
                     val totalReviews: Int = 0,
                     @SerializedName("postal_code")
                     val postalCode: String = "")