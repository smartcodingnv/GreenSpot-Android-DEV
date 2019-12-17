package com.greenspot.app.responce.tourdetail


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


data class Others(@SerializedName("important_note")
                  val importantNote: String = "",
                  @SerializedName("term_and_condition")
                  val termAndCondition: String = "")


data class IncludedInTourPackageItem(@SerializedName("master_id")
                                     val masterId: String = "",
                                     @SerializedName("value")
                                     val value: String = "")


data class PopularToursItem(@SerializedName("master_id")
                            val masterId: String = "",
                            @SerializedName("main_image")
                            val mainImage: String = "",
                            @SerializedName("package_name")
                            val packageName: String = "",
                            @SerializedName("tour_operator_name")
                            val tourOperatorName: String = "",
                            @SerializedName("locations")
                            val locations: String = "")


data class MainRecords(@SerializedName("tour_category")
                       val tourCategory: String = "",
                       @SerializedName("avg_reviews")
                       val avgReviews: Double = 0.0,
                       @SerializedName("included_in_tour_package")
                       val includedInTourPackage: List<IncludedInTourPackageItem>?,
                       @SerializedName("price")
                       val price: String = "",
                       @SerializedName("nights")
                       val nights: Int = 0,
                       @SerializedName("tour_type")
                       val tourType: String = "",
                       @SerializedName("package_name")
                       val packageName: String = "",
                       @SerializedName("total_reviews")
                       val totalReviews: Int = 0,
                       @SerializedName("days")
                       val days: Int = 0,
                       @SerializedName("locations")
                       val locations: String = "",
                       @SerializedName("created_by")
                       val createdBy: String = "",
                       @SerializedName("depature_city")
                       val depatureCity: String = "")


data class ItinerariesItem(@SerializedName("master_id")
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
                           val hotelInfo: String = "",
                           @SerializedName("gallery_images")
                           val galleryImages: List<GalleryImagesItem>?)


data class Ovierview(@SerializedName("ovierview")
                     val ovierview: String = "")


data class Data(@SerializedName("amenities")
                val amenities: List<AmenitiesItem>?,
                @SerializedName("main_records")
                val mainRecords: MainRecords,
                @SerializedName("popular_tours")
                val popularTours: List<PopularToursItem>?,
                @SerializedName("ovierview")
                val ovierview: Ovierview,
                @SerializedName("itineraries")
                val itineraries: List<ItinerariesItem>?,
                @SerializedName("contact_us")
                val contactUs: ContactUs,
                @SerializedName("others")
                val others: Others,
                @SerializedName("gallery_images")
                val galleryImages: List<GalleryImagesItem>?)


data class ResponceTourDetails(@SerializedName("data")
                               val data: Data,
                               @SerializedName("message")
                               val message: String = "",
                               @SerializedName("status")
                               val status: Int = 0)


data class ContactUs(@SerializedName("address")
                     val address: String = "",
                     @SerializedName("phone")
                     val phone: String = "",
                     @SerializedName("email")
                     val email: String = "")


data class GalleryImagesItem(@SerializedName("master_id")
                             val masterId: String = "",
                             @SerializedName("image_name")
                             val imageName: String = "",
                             @SerializedName("order_id")
                             val orderId: Int = 0)


