package com.greenspot.app.utils

class AppConfig {

    object URL {

        val BASE_URL = "http://greenspot.hvjstest.com/api/"
//        val BASE_URL = "http://192.168.0.103/ldorado/api/"
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
        const val URL_PAYMENTMOLIE = "tours/payment-mollie"





        val SUCCESS = 200

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
        val USERNAME = "username"
        val SELECTCONTRY = "selectcontry"
        val SELECTCONTRYID = "selectcontryid"
        val SELECTCURRENCYID = "selectcurrencyid"
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

        val START_PRICE = "startprice"
        val FINAL_PRICE = "finalprice"
        val FROM_DATE = "fromdate"
        val NO_OF_PERSON = "noofperson"
        val PERSONDETAILS = "persondetails"
        val FINALEURPRICE = "finaleurprice"
        val TRANSACTIONID = "transactionid"
        val BANKNAME = "bankname"
        val CHECKPAYMENT = "checkpayment"


    }

    object BUNDLE {

        val CHECKDISCRIPTION = "checkdiscription" // 1-place , = 2- tour, 3-contact us
        val CHECKANIMATIES = "checkanimaties" // 1-place , = 2- tour, 3-contact us
        val CHECKAOTHER = "checkother" //1 = placeother , 2= event contact us
        val CHECKREIVEW = "checkreivew" //1 = placeother , 2= event contact us
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

        val TOTALGUEST = "totalguest"


    }

    object Constant {
        val LANGUAGE = "en"
        val LISTLIMIT = "10"
    }
}