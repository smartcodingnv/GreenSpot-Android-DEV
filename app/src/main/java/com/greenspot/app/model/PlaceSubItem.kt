package com.greenspot.app.model

data class PlaceSubItem(
    var id: String,
    var subtitleavailablity: String,
    var subdesceavailablity: String


){
    private var expanded: Boolean = false

    fun setExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    fun isExpanded(): Boolean {
        return expanded
    }
}