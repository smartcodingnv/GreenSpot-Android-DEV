package com.greenspot.app.model

import java.util.*


class PlaceFilterTitle(
    val id: String,
    val titleavailablity: String,
    val count: String,
    var subItem: ArrayList<PlaceFiltermenuItem>
) {

    private var expanded: Boolean = false
    private  var countcheck: Int =0


    fun setCount(count: Int){
        this.countcheck = count
    }

    fun getCount(): Int{
        return countcheck
    }

    fun setExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    fun isExpanded(): Boolean {
        return expanded
    }
}