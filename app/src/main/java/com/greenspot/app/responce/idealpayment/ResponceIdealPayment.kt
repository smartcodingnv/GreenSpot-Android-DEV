package com.greenspot.app.responce.idealpayment


import com.google.gson.annotations.SerializedName

data class Checkout(@SerializedName("href")
                    val href: String = "",
                    @SerializedName("type")
                    val type: String = "")


data class Amount(@SerializedName("currency")
                  val currency: String = "",
                  @SerializedName("value")
                  val value: String = "")


data class Documentation(@SerializedName("href")
                         val href: String = "",
                         @SerializedName("type")
                         val type: String = "")


data class Links(@SerializedName("documentation")
                 val documentation: Documentation,
                 @SerializedName("self")
                 val self: Self,
                 @SerializedName("checkout")
                 val checkout: Checkout)


data class Self(@SerializedName("href")
                val href: String = "",
                @SerializedName("type")
                val type: String = "")


data class ResponceIdealPayment(@SerializedName("amount")
                                val amount: Amount,
                                @SerializedName("metadata")
                                val metadata: List<MetadataItem>?,
                                @SerializedName("redirectUrl")
                                val redirectUrl: String = "",
                                @SerializedName("method")
                                val method: Any = "",
                                @SerializedName("resource")
                                val resource: String = "",
                                @SerializedName("_links")
                                val Links: Links,
                                @SerializedName("description")
                                val description: String = "",
                                @SerializedName("expiresAt")
                                val expiresAt: String = "",
                                @SerializedName("webhookUrl")
                                val webhookUrl: String = "",
                                @SerializedName("mode")
                                val mode: String = "",
                                @SerializedName("createdAt")
                                val createdAt: String = "",
                                @SerializedName("profileId")
                                val profileId: String = "",
                                @SerializedName("id")
                                val id: String = "",
                                @SerializedName("isCancelable")
                                val isCancelable: Boolean = false,
                                @SerializedName("status")
                                val status: String = "",
                                @SerializedName("sequenceType")
                                val sequenceType: String = "")


data class MetadataItem(@SerializedName("order_id")
                        val orderId: String = "")


