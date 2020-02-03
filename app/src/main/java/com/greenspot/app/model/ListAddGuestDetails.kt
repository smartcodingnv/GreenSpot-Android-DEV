package com.greenspot.app.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


data class ListAddGuestDetails(
    var id: String,
    var room: String,
    var adult: String,
    var child: String,
    var child1: String,
    var child2: String,
    var child3: String,
    var child4: String


) : Serializable {

    private var expanded: Boolean = false

    fun setExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    fun isExpanded(): Boolean {
        return expanded
    }
}