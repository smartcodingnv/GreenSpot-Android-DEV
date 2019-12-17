package com.greenspot.app.model

data class ListBookingDetails(
    var id: String,
    var person: String,
    var title: String,
    var first_name: String,
    var last_name: String,
    var dob: String,
    var gender: String,
    var email: String,
    var contact_number: String

){

    private var expanded: Boolean = false

    fun setExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    fun isExpanded(): Boolean {
        return expanded
    }
}