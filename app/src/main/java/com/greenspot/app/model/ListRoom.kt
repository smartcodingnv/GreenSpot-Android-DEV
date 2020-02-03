package com.greenspot.app.model

import java.io.Serializable


data class ListRoom(
    var master_id: String,
    var room_selected: String,
    var single_price: String

) : Serializable