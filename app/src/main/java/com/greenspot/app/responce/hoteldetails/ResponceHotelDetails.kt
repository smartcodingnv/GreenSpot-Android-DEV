package com.greenspot.app.responce.hoteldetails


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

data class NearbyPlacesItem(@SerializedName("master_id")
                            val masterId: String = "",
                            @SerializedName("main_image")
                            val mainImage: String = "",
                            @SerializedName("price")
                            val price: String = "",
                            @SerializedName("name")
                            val name: String = "")


data class ResponceHotelDetails(@SerializedName("data")
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


data class Description(@SerializedName("suitable_for")
                       val suitableFor: String = "",
                       @SerializedName("description")
                       val description: String = "",
                       @SerializedName("property_type")
                       val propertyType: String = "")


data class Data(@SerializedName("amenities")
                val amenities: List<AmenitiesItem>?,
                @SerializedName("nearby_places")
                val nearbyPlaces: List<NearbyPlacesItem>?,
                @SerializedName("rooms")
                val rooms: List<RoomsItem>?,
                @SerializedName("main_records")
                val mainRecords: MainRecords,
                @SerializedName("policies")
                val policies: Policies,
                @SerializedName("description")
                val description: Description,
                @SerializedName("contact_us")
                val contactUs: ContactUs,
                @SerializedName("gallery_images")
                val galleryImages: List<GalleryImagesItem>?)


data class IncludedInHotelItem(@SerializedName("master_id")
                               val masterId: String = "",
                               @SerializedName("value")
                               val value: String = "")


data class ContactUs(@SerializedName("address")
                     val address: String = "",
                     @SerializedName("landline1")
                     val landline: String = "",
                     @SerializedName("landline2")
                     val landline2: String = "",
                     @SerializedName("mobile2")
                     val mobile2: String = "",
                     @SerializedName("mobile1")
                     val mobile: String = "",
                     @SerializedName("contact_person_name")
                     val contactPersonName: String = "",
                     @SerializedName("fax")
                     val fax: String = "",
                     @SerializedName("email")
                     val email: String = "",
                     @SerializedName("whatsapp_number")
                     val whatsappNumber: String = "")


data class RoomsItem(@SerializedName("master_id")
                     val masterId: String = "",
                     @SerializedName("saving")
                     val saving: String = "",
                     @SerializedName("other")
                     val other: List<String>?,
                     @SerializedName("final_price")
                     val finalPrice: String = "",
                     @SerializedName("options")
                     val options: List<String>?,
                     @SerializedName("base_price")
                     val basePrice: String = "",
                     @SerializedName("discount")
                     val discount: String = "",
                     @SerializedName("include_this_price")
                     val includeThisPrice: List<String>?,
                     @SerializedName("room_images")
                     val roomImages: List<RoomImagesItem>?,
                     @SerializedName("maximum_adult_in_room")
                     val maximumAdultInRoom: Int = 0,
                     @SerializedName("room_type")
                     val roomType: String = "",
                     @SerializedName("maximum_child_in_room")
                     val maximumChildInRoom: Int = 0)


data class MainRecords(@SerializedName("country")
                       val country: String = "",
                       @SerializedName("short_description")
                       val shortDescription: String = "",
                       @SerializedName("room_booking_limit_per_transaction")
                       val roomBookingLimit: Int = 0,
                       @SerializedName("included_in_hotel")
                       val includedInHotel: List<IncludedInHotelItem>?,
                       @SerializedName("avg_reviews")
                       val avgReviews: Int = 0,
                       @SerializedName("city")
                       val city: String = "",
                       @SerializedName("district")
                       val district: String = "",
                       @SerializedName("address")
                       val address: String,
                       @SerializedName("latitude")
                       val latitude: String = "",
                       @SerializedName("name")
                       val name: String = "",
                       @SerializedName("total_reviews")
                       val totalReviews: Int = 0,
                       @SerializedName("created_by")
                       val createdBy: String = "",
                       @SerializedName("longitude")
                       val longitude: String = "")


data class RoomImagesItem(@SerializedName("master_id")
                          val masterId: String = "",
                          @SerializedName("image_name")
                          val imageName: String = "",
                          @SerializedName("order_id")
                          val orderId: Int = 0)


data class GalleryImagesItem(@SerializedName("master_id")
                             val masterId: String = "",
                             @SerializedName("image_name")
                             val imageName: String = "",
                             @SerializedName("order_id")
                             val orderId: Int = 0)


