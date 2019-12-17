package com.greenspot.app.model

import java.util.ArrayList

data class TourList(

    var id: String,
    var itemname: String,
    var itemdesc: String,
    var subimage: ArrayList<ItineraryImg>
)