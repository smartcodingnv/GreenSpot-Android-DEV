package com.greenspot.app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.model.PlaceSubItem
import com.greenspot.app.utils.AppConfig
import java.util.*

class PlaceDetailsAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<PlaceDetailsAdapter.FeatureViewHolder>() {

    private var data: List<PlaceSubItem> = ArrayList()
    private val mContext: Context? = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)


    }


    fun swapData(data: List<PlaceSubItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: PlaceSubItem,
            context: FragmentActivity?
        ) = with(itemView) {


            itemView.findViewById<TextView>(R.id.txt_listitem).text = item.subtitleavailablity
            itemView.findViewById<TextView>(R.id.txt_itemdesc).text = item.subdesceavailablity


        }
    }





}