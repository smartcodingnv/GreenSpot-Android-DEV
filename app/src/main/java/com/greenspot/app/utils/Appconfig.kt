package com.greenspot.app.utils

class AppConfig {

    object URL {

//        val BASE_URL = "https://promo.visitgreenspot.com/api/"
        val BASE_URL = "http://greenspot.hvjstest.com/api/"
//         val BASE_URL = "http://192.168.0.109:8080/LDorado/api/"
        val PAYMENT_URL = "https://api.mollie.com/v2/"
        val BASE_IMAGEURL = "http://159.203.105.17"

        const val URL_PAYMENT = "payments"
        const val URL_LOGIN = "login"
        const val URL_SIGNUP = "reg-user"
        const val URL_DETAILS = "details"
        const val URL_HOME = "home"
        const val URL_EDITPROFILE = "editprofile"
        const val URL_FORGOTPWD = "forgotpassword"
        const val URL_CHANGEPWD = "changepassword"
        const val URL_LOCATION = "locations"
        const val URL_LISTMASTER = "list-masters"
        const val URL_FILTERCOUNT = "recreations/filter-count"
        const val URL_RECREATIONLIST = "recreations/list"
        const val URL_RECREATIONDETAILS = "recreations/detail"
        const val URL_POSTREVIEW = "post-review"
        const val URL_REVIEWLIST = "recreations/review-list"
        const val URL_EVENTLIIST = "events/list"
        const val URL_EVENTDETAILS = "events/detail"
        const val URL_EVENTREVIEWLIST = "events/review-list"
        const val URL_TOURLIST = "tours/list"
        const val URL_TOURDETAILS = "tours/detail"
        const val URL_TOURREVIEW = "tours/review-list"
        const val URL_TOURSPAYMENT = "tours/payment-stripe"
        const val URL_CURRENCYCONVERT = "currencyconvert"
        const val URL_CHECKBEFOREPAY = "check-before-pay"
        const val URL_TOURPAYMENTMOLIE = "tours/payment-mollie"
        const val URL_BOOKINGLIST = "bookings/list"
        const val URL_BOOKINGDETAILS = "bookings/detail"
        const val URL_EVENTPAYMENTMOLIE = "events/payment-mollie"
        const val URL_RECREATIONPAYMENTMOLIE = "recreations/payment-mollie"
        const val URL_HOTELLIST = "hotels/list"
        const val URL_HOTELDETAILS = "hotels/detail"
        const val URL_HOTELREVIEWLIST = "hotels/review-list"
        const val URL_HOTELPAYMENTMOLLIE = "hotels/payment-mollie"


        val SUCCESS = 200
        val TOKEN_EXPIRE = 400

    }

    object PREFERENCE {

        val PREF_FILE = "greenspot"
        val PREF_FILE_LANG = "greenspotlang"
        val USER_LOGIN_CHECK = "login"
        val CHECK_LOGINAS = "checklogin"
        val AUTHTOKEN = "authtoken"
        val USERID = "userid"
        val USER_FNAME = "firstname"
        val USER_LNAME = "lastname"
        val USER_EMAIL = "email"
        val USER_CONTACTNO = "contactno"
        val USERNAME = "username"
        val SELECTCONTRY = "selectcontry"
        val SELECTCONTRYID = "selectcontryid"

        val SELECTCURRENCYNAME = "selectcurrencyname"
        val SELECTLANGCODE = "selectlancode"
        val SELECTLANGNAME = "selectlanname"
        val SELECTSORTTITAL = "selectsorttitle"
        val PLACEDETAILSRESPONCE = "placedetailsresponce"

        val PLACESEARCHTEXT = "placesearchtext"
        val TOURSEARCHTEXT = "toursearchtext"

        val PLACEFILTERCHECK = "placefiltertxt"
        val TOURFILTERCHECK = "tourfilter"
        val PLACETITLE = "placetitle"
        val PLACEID = "placeID"
        val SERVICEPROVIDERID = "serviceproviderid" //created by

        val EVENTID = "eventID"
        val EVENTDETAILSRESPONCE = "eventdetailsresponce"
        val TOURDETAILSRESPONCE = "tourdetailsresponce"
        val HOTELDETAILSRESPONCE = "hoteldetailsresponce"


        val TOUR_START_PRICE = "tourstartprice"
        val TOUR_FINAL_PRICE = "tourfinalprice"
        val TOUR_FROM_DATE = "tourfromdate"
        val TOUR_NO_OF_PERSON = "tournoofperson"

        val EVENT_START_PRICE = "eventstartprice"
        val EVENT_FINAL_PRICE = "eventfinalprice"
        val EVENT_FROM_DATE = "eventfromdate"
        val EVENT_NO_OF_PERSON = "eventnoofperson"


        val RECREATION_START_PRICE = "recreationstartprice"
        val RECREATION_FINAL_PRICE = "recreationfinalprice"
        val RECREATION_FROM_DATE = "recreationfromdate"
        val RECREATION_NO_OF_PERSON = "recreationnoofperson"


        val mid_week_day_pass_adult_price = "mid_week_day_pass_adult_price"
        val mid_week_day_pass_adult_nop = "mid_week_day_pass_adult_nop"

        val mid_week_day_pass_child_price = "mid_week_day_pass_child_price"
        val mid_week_day_pass_child_nop = "mid_week_day_pass_child_nop"

        val mid_week_night_pass_adult_price = "mid_week_night_pass_adult_price"
        val mid_week_night_pass_adult_nop = "mid_week_night_pass_adult_nop"

        val mid_week_night_pass_child_price = "mid_week_night_pass_child_price"
        val mid_week_night_pass_child_nop = "mid_week_night_pass_child_nop"


        val weekend_day_pass_adult_price = "weekend_day_pass_adult_price"
        val weekend_day_pass_adult_nop = "weekend_day_pass_adult_nop"

        val weekend_day_pass_child_price = "weekend_day_pass_child_price"
        val weekend_day_pass_child_nop = "weekend_day_pass_child_nop"

        val weekend_night_pass_adult_price = "weekend_night_pass_adult_price"
        val weekend_night_pass_adult_nop = "weekend_night_pass_adult_nop"

        val weekend_night_pass_child_price = "weekend_night_pass_child_price"
        val weekend_night_pass_child_nop = "weekend_night_pass_child_nop"


        val SELECTTOURMONTH = "selecttourmonth"
        val SELECTTOURDATE = "selecttourdate"




        val PERSONDETAILS = "persondetails"
        val FINALROOMDATA = "finalroomdata"
        val FINALEURPRICE = "finaleurprice"
        val TRANSACTIONID = "transactionid"


        val BANKNAME = "bankname"
        val CHECKPAYMENT = "checkpayment"

        val BOOKINGINFO = "ResponceTourBookinginfo"
        val EVENTBOOKINGINFO = "ResponceBookinginfoEvent"
        val VACATIONBOOKINGINFO = "ResponceBookinginforvcation"
        val HOTELBOOKINGINFO = "ResponceBookinginforHotel"

        val ROOMMASTERID = "roommasterid"
        val CHECKINDATE = "checkindate"
        val CHECKOUTDATE = "checkoutdate"
        val HOTEL_FINAL_PRICE = "hotelfinalprice"
        val HOTEL_NO_OF_ROOM = "hotelnoofroom"



    }

    object BUNDLE {

        val CHECKDISCRIPTION = "checkdiscription" // 1-place , = 2- tour, 3-contact us
        val CHECKANIMATIES = "checkanimaties" // 1-place , = 2- tour, 3-contact us , 4 - bookinfo touramintes, 5- bookinfo events amintes
        val CHECKAOTHER = "checkother" //1 = placeother , 2= event contact us , 3-tourcontact us , 4- bookinginfo , 5 - eventbooking enfo
        val CHECKAITINERARY = "checkitinerary" //1 = tourdata iteineary , 2= booking iteinerary
        val CHECKTOUROTHER = "checktourother" //1 = TOUROTHER , 2= HotelPolicy
        val RING = "Ring"
        val END = "end"
        val CALL_END = "call_end"

    }

    object EXTRA {
        val FCM_GROUP = "rydemate"
        val CHECKLOGIN = "checklogin"
        val CHECKLOGINSIGNUP = "checkloginsignup"
        val FILTERCHECK = "filtercheck"  // flag check to filter place, tour ,event
        val FILTERRESPONCE = "filterresponce" // Responce without filter
        val CHECKFILTERDATA = "filterdata" // Responce with filter
        val FILTERPRICEMIN = "filterpricemin"
        val FILTERPRICEMAX = "filterpricemax"
        val FILTERCHECKDATA = "filtercheckdata"

        val TABCHECK = "tabcheck"

        val RECREATIONITEMID = "recreationitemid"
        val RECREATIONITEMNAME = "recreationitemname"

        val EVENTID = "eventid"
        val EVENTNAME = "eventname"

        val TOURID = "tourid"
        val TOURNAME = "tourname"

        val HOTELID = "hotelid"
        val HOTELNAME = "hotelname"

        val CHECKDATEGUESTSCREEN = "checkdateguestscreen"
        val CHECKDATEGUEST = "checkdateguest"
        val CHECKINDATE = "checkingdate"
        val CHECKOUTDATE = "checkoutgdate"
        val TOTALROOM = "totalroom"
        val ROOMTOTALGUEST = "roomtotalguest"
        val ROOMGUESTRESPONCE = "roomguestresponce"
        val SELECTROOMLIST = "selectroomlist"
        val HOTELROOMPRICE = "hotelroomprice"
        val ROOMDATA = "roomdata"

        val TOTALGUEST = "totalguest"
        val TYPE = "type"  // passed in booking details api
        val BOOKINGID = "bookingid"
        val BOOKINGTYPE = "bookingtype"   //tour, event , recreation
        val CHECKBOOKINGINFO = "checkbookinginfo"
        val ROOMTABFLAG = "roomtabflag" //1 list room , 2 bookroomlist

        val TOURTITLE = "toutTitle"


    }

    object Constant {
        val LANGUAGE = "en"
        val LISTLIMIT = "10"
    }
}