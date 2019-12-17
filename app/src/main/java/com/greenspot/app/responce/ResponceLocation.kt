package com.greenspot.app.responce


import com.google.gson.annotations.SerializedName

data class CurrenciesItem(@SerializedName("master_id")
                          val masterId: String = "",
                          @SerializedName("name")
                          val name: String = "",
                          @SerializedName("currency")
                          val currency: String = "",
                          @SerializedName("id")
                          val id: Int = 0)


data class CountriesItem(@SerializedName("sortname")
                         val sortname: String = "",
                         @SerializedName("master_id")
                         val masterId: String = "",
                         @SerializedName("name")
                         val name: String = "",
                         @SerializedName("phonecode")
                         val phonecode: Int = 0)


data class DataLocation(@SerializedName("languages")
                val languages: List<LanguagesItem>?,
                @SerializedName("countries")
                val countries: List<CountriesItem>?,
                @SerializedName("currencies")
                val currencies: List<CurrenciesItem>?)


data class LanguagesItem(@SerializedName("master_id")
                         val masterId: String = "",
                         @SerializedName("language_code")
                         val languageCode: String = "",
                         @SerializedName("id")
                         val id: Int = 0,
                         @SerializedName("language_name")
                         val languageName: String = "")


data class ResponceLocation(@SerializedName("data")
                            val data: DataLocation,
                            @SerializedName("message")
                            val message: String = "",
                            @SerializedName("status")
                            val status: Int = 0)


