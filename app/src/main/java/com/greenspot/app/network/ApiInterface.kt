package com.greenspot.app.network

import com.greenspot.app.responce.*
import com.greenspot.app.responce.checkbeforepay.ResponceCheckPay
import com.greenspot.app.responce.currencyconvert.CurrencyConvert
import com.greenspot.app.responce.eventdetails.EvnetDetailsResponce
import com.greenspot.app.responce.home.ResponeHome
import com.greenspot.app.responce.idealpayment.ResponceIdealPayment
import com.greenspot.app.responce.login.LoginResponce
import com.greenspot.app.responce.payment.ResponcePayment
import com.greenspot.app.responce.recreationdetails.ResponceRecDetails
import com.greenspot.app.responce.tourdetail.ResponceTourDetails
import com.greenspot.app.responce.tourlist.ResponceTourList
import com.greenspot.app.utils.AppConfig.URL.URL_CHANGEPWD
import com.greenspot.app.utils.AppConfig.URL.URL_CHECKBEFOREPAY
import com.greenspot.app.utils.AppConfig.URL.URL_CURRENCYCONVERT
import com.greenspot.app.utils.AppConfig.URL.URL_DETAILS
import com.greenspot.app.utils.AppConfig.URL.URL_EDITPROFILE
import com.greenspot.app.utils.AppConfig.URL.URL_EVENTDETAILS
import com.greenspot.app.utils.AppConfig.URL.URL_EVENTLIIST
import com.greenspot.app.utils.AppConfig.URL.URL_EVENTREVIEWLIST
import com.greenspot.app.utils.AppConfig.URL.URL_FILTERCOUNT
import com.greenspot.app.utils.AppConfig.URL.URL_FORGOTPWD
import com.greenspot.app.utils.AppConfig.URL.URL_HOME
import com.greenspot.app.utils.AppConfig.URL.URL_LISTMASTER
import com.greenspot.app.utils.AppConfig.URL.URL_LOCATION
import com.greenspot.app.utils.AppConfig.URL.URL_LOGIN
import com.greenspot.app.utils.AppConfig.URL.URL_PAYMENT
import com.greenspot.app.utils.AppConfig.URL.URL_PAYMENTMOLIE
import com.greenspot.app.utils.AppConfig.URL.URL_POSTREVIEW
import com.greenspot.app.utils.AppConfig.URL.URL_RECREATIONDETAILS
import com.greenspot.app.utils.AppConfig.URL.URL_RECREATIONLIST
import com.greenspot.app.utils.AppConfig.URL.URL_REVIEWLIST
import com.greenspot.app.utils.AppConfig.URL.URL_SIGNUP
import com.greenspot.app.utils.AppConfig.URL.URL_TOURDETAILS
import com.greenspot.app.utils.AppConfig.URL.URL_TOURLIST
import com.greenspot.app.utils.AppConfig.URL.URL_TOURREVIEW
import com.greenspot.app.utils.AppConfig.URL.URL_TOURSPAYMENT
import retrofit2.Call
import retrofit2.http.*
import java.util.*


interface ApiInterface {
    //
    //    @FormUrlEncoded
    //    @POST(AppConfig.URL.URL_REGISTER)
    //    Call<ResponceRegister> RESPONCE_REGISTER_CALL(@Field("email") String email, @Field("password") String password,
    //                                                  @Field("termcon") String term, @Field("accept") String condition,
    //                                                  @Field("firstname") String fname, @Field("lastname") String lname,
    //                                                  @Field("country") String contrycode, @Field("gender") String gender,
    //                                                  @Field("profile_pic") String profilepic, @Field("customerDOB") String coutdb);
    //

    @FormUrlEncoded
    @POST(URL_LOGIN)
    fun LOGIN_CALL(
        @Field("email") email: String, @Field("password") passsord: String,
        @Field("lang_code") langcode: String
    ): Call<LoginResponce>


    @FormUrlEncoded
    @POST(URL_FORGOTPWD)
    fun FORGOTPWD_CALL(
        @Field("email") email: String, @Field("lang_code") langcode: String
    ): Call<ResponceForgotpwd>

    @FormUrlEncoded
    @POST(URL_SIGNUP)
    fun SIGNUP_CALL(
        @Field("first_name") clientid: String, @Field("last_name") name: String,
        @Field("email") email: String, @Field("password") password: String,
        @Field("phone") phone: String, @Field("lang_code") langcode: String
    ): Call<LoginResponce>

    @FormUrlEncoded
    @POST(URL_DETAILS)
    fun USERDETAILS_CALL(
        @Header("Authorization") token: String, @Field("lang_code") name: String
    ): Call<UserDetailsResponce>

    @FormUrlEncoded
    @POST(URL_EDITPROFILE)
    fun UPDATE_PROFILE(
        @Header("Authorization") token: String, @Field("first_name") fname: String,
        @Field("last_name") lname: String, @Field("email") email: String,
        @Field("phone") phone: String, @Field("lang_code") langcode: String
    ): Call<UserDetailsResponce>

    @FormUrlEncoded
    @POST(URL_CHANGEPWD)
    fun CHANGEPWD_CALL(
        @Header("Authorization") token: String, @Field("old_pass") oldpwd: String,
        @Field("new_pass") newpwd: String, @Field("lang_code") lancode: String
    ): Call<ComanResponce>

    @FormUrlEncoded
    @POST(URL_LOCATION)
    fun LOCATION_CALL(
        @Field("lang_code") lancode: String
    ): Call<ResponceLocation>

    @FormUrlEncoded
    @POST(URL_LISTMASTER)
    fun LIST_MASTER(
        @Field("master_type") masterType: String, @Field("global_country_id") contryID: String,
        @Field("selected_currency") selectCurrency: String, @Field("lang_code") lancode: String
    ): Call<ResponceListMaster>

    @FormUrlEncoded
    @POST(URL_FILTERCOUNT)
    fun FILTER_COUNT(
        @Field("global_country_id") contryID: String, @Field("lang_code") langcode: String, @Field("selected_currency") selectCurrency: String,
        @Field("range_slider_min") minRange: String, @Field("range_slider_max") maxRange: String,
        @FieldMap params: HashMap<String, String>
    ): Call<ResponceFilterCount>


    @FormUrlEncoded
    @POST(URL_RECREATIONLIST)
    fun RECREATTION_LIST(
        @Field("global_country_id") contryID: String, @Field("lang_code") langcode: String, @Field("selected_currency") selectCurrency: String,
        @Field("page_number") pageNumber: String, @Field("limit") limit: String, @Field("search_text") searchText: String,
        @Field("order_by") orderBy: String, @Field("order_type") orderType: String,
        @Field("range_slider_min") minRange: String, @Field("range_slider_max") maxRange: String,
        @FieldMap params: HashMap<String, String>
    ): Call<ResponceRecreationList>

    @FormUrlEncoded
    @POST(URL_RECREATIONDETAILS)
    fun RECREATION_DETAILS(
        @Field("global_country_id") contryID: String, @Field("lang_code") langcode: String, @Field("selected_currency") selectCurrency: String,
        @Field("recreation_id") recreationID: String
    ): Call<ResponceRecDetails>

    @FormUrlEncoded
    @POST(URL_REVIEWLIST)
    fun REVIEW_DETAILS(
        @Field("global_country_id") contryID: String, @Field("lang_code") langcode: String, @Field("selected_currency") selectCurrency: String,
        @Field("id") id: String, @Field("limit") limit: String, @Field("page_number") pageNumber: String
    ): Call<ResponcePlaceReview>

    @FormUrlEncoded
    @POST(URL_POSTREVIEW)
    fun POSTREIVEW_CALL(
        @Header("Authorization") token: String, @Field("global_country_id") contryID: String, @Field(
            "lang_code"
        ) langcode: String,
        @Field("selected_currency") selectCurrency: String, @Field("id") id: String, @Field("type") type: String,
        @Field("service_provider_id") serviceproiver: String, @Field("review_star") applystar: String,
        @Field("review_description") reviwdesc: String
    ): Call<ComanResponce>

    @FormUrlEncoded
    @POST(URL_EVENTLIIST)
    fun EVENT_LIST(
        @Field("global_country_id") contryID: String, @Field("lang_code") langcode: String, @Field("selected_currency") selectCurrency: String,
        @Field("page_number") pageNumber: String, @Field("limit") limit: String, @Field("search_text") searchText: String,
        @Field("order_by") orderBy: String, @Field("order_type") orderType: String,
        @Field("range_slider_min") minRange: String, @Field("range_slider_max") maxRange: String,
        @FieldMap params: HashMap<String, String>
    ): Call<ResponceEventList>

    @FormUrlEncoded
    @POST(URL_EVENTDETAILS)
    fun EVENT_DETAILS(
        @Field("global_country_id") contryID: String, @Field("lang_code") langcode: String, @Field("selected_currency") selectCurrency: String,
        @Field("event_id") eventid: String
    ): Call<EvnetDetailsResponce>

    @FormUrlEncoded
    @POST(URL_EVENTREVIEWLIST)
    fun EVENTREVIEW_DETAILS(
        @Field("global_country_id") contryID: String, @Field("lang_code") langcode: String, @Field("selected_currency") selectCurrency: String,
        @Field("id") id: String, @Field("limit") limit: String, @Field("page_number") pageNumber: String
    ): Call<ResponcePlaceReview>

    @FormUrlEncoded
    @POST(URL_TOURLIST)
    fun TOUR_LIST(
        @Field("global_country_id") contryID: String, @Field("lang_code") langcode: String, @Field("selected_currency") selectCurrency: String,
        @Field("page_number") pageNumber: String, @Field("limit") limit: String, @Field("search_text") searchText: String,
        @Field("order_by") orderBy: String, @Field("order_type") orderType: String,
        @Field("range_slider_min") minRange: String, @Field("range_slider_max") maxRange: String,
        @FieldMap params: HashMap<String, String>
    ): Call<ResponceTourList>


    @FormUrlEncoded
    @POST(URL_TOURDETAILS)
    fun TOUR_DETAILS(
        @Field("global_country_id") contryID: String, @Field("lang_code") langcode: String, @Field("selected_currency") selectCurrency: String,
        @Field("tour_id") tourid: String
    ): Call<ResponceTourDetails>

    @FormUrlEncoded
    @POST(URL_TOURREVIEW)
    fun TOURVIEW_DETAILS(
        @Field("global_country_id") contryID: String, @Field("lang_code") langcode: String, @Field("selected_currency") selectCurrency: String,
        @Field("id") id: String, @Field("limit") limit: String, @Field("page_number") pageNumber: String
    ): Call<ResponcePlaceReview>

    @FormUrlEncoded
    @POST(URL_HOME)
    fun HOME_DETAILS(
        @Field("global_country_id") contryID: String, @Field("lang_code") langcode: String, @Field("selected_currency") selectCurrency: String
    ): Call<ResponeHome>

    @FormUrlEncoded
    @POST(URL_TOURSPAYMENT)
    fun PAYMENT_CALL(
        @Header("Authorization") token: String, @Field("global_country_id") contryID: String, @Field(
            "lang_code"
        ) langcode: String, @Field("selected_currency") selectCurrency: String,
        @Field("tour_id") tourid: String, @Field("single_price") singlePrice: String, @Field("final_price") finalPrice: String,
        @Field("stripe_token") stripeToken: String, @Field("from_date") formDate: String, @Field("no_of_person") noofperson: String,
        @Field("persons") person: Any
    ): Call<ResponcePayment>


    @Headers("Content-Type: application/json")
    @POST(URL_PAYMENT)
    fun PAYMENTBBODY_CALL(
        @Header("Authorization") token: String, @Body jsonBody: String
    ): Call<ResponceIdealPayment>


    @FormUrlEncoded
    @POST(URL_CURRENCYCONVERT)
    fun CURRENCY_CONVERT(
        @Field("price") price: String, @Field("current_currency") currentCurrency: String,
        @Field("output_currency") outputCurrency: String, @Field("lang_code") lancode: String
    ): Call<CurrencyConvert>

    @FormUrlEncoded
    @POST(URL_CHECKBEFOREPAY)
    fun CALL_CHECKBEFOREPAY(
        @Header("Authorization") token: String, @Field("id") tourid: String, @Field("single_price") singleprice: String,
        @Field("selected_currency") selectCurrency: String, @Field("master_type") masterType: String,
        @Field("lang_code") langcode: String
    ): Call<ComanResponce>

    @FormUrlEncoded
    @POST(URL_PAYMENTMOLIE)
    fun PAYMENTMOLIE_CALL(
        @Header("Authorization") token: String, @Field("global_country_id") contryID: String, @Field(
            "lang_code") langcode: String, @Field("selected_currency") selectCurrency: String,
        @Field("tour_id") tourid: String, @Field("transaction_id") tanstationID: String,
        @Field("single_price") singlePrice: String, @Field("original_single_price") originalsingleprice: String,
        @Field("original_payment_price") originalPaymentprice: String,@Field("original_payment_currency") originalPaymentcurrency: String,
        @Field("from_date") formDate: String, @Field("no_of_person") noofperson: String,
        @Field("persons") person: Any
    ): Call<ComanResponce>



//    @FormUrlEncoded
//    @POST(URL_PHARMACYLIST)
//    fun PHARMACY_LIST_CALL(
//        @Field("client_id") clientid: String, @Field("pharmacy_id") pharmacyid: String,
//        @Field("lang_code") langcode: String
//    ): Call<PharmacyResponce>
//
//
//    @FormUrlEncoded
//    @POST(URL_PHARMACYSEARCH)
//    fun PHARMACY_SEARCHLIST_CALL(
//        @Field("client_id") clientid: String, @Field("search_text") searchtxt: String,
//        @Field("page_no") pageno: String, @Field("lang_code") langcode: String
//    ): Call<PharmacySearchResponce>
//
//    @FormUrlEncoded
//    @POST(URL_MEDICINSEARCH)
//    fun MEDICEIN_SEARCHLIST_CALL(
//        @Field("client_id") clientid: String, @Field("search_text") searchtxt: String,
//        @Field("page_no") pageno: String, @Field("lang_code") langcode: String
//    ): Call<MedicinSearchResponce>
//
//    @FormUrlEncoded
//    @POST(URL_MEDICINWISEPHARMACY)
//    fun MEDICEINWISE_PHARMACYLIST_CALL(
//        @Field("client_id") clientid: String, @Field("medicine_id") medecinid: String,
//        @Field("page_no") pageno: String, @Field("insurance_id") insuraceid: String,
//        @Field("quantity") quantity: String, @Field("lang_code") langcode: String, @Field("user_id") userid: String
//    ): Call<MedicinewisePharemacyResponce>


}
