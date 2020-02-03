package com.greenspot.app.model

import java.io.Serializable


data class ListSelectRoom(
    var id: String,
    var room: String,
    var adult: String,
    var child: String,
    var roomPrice: String

) : Serializable