package com.greenspot.app.model

import java.util.*


class PlaceAvaliblityTitle(
    val id: String,
    val titleavailablity: String,
    var subItem: ArrayList<PlaceSubItem>
) {

    private var expanded: Boolean = false

    fun setExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    fun isExpanded(): Boolean {
        return expanded
    }
}