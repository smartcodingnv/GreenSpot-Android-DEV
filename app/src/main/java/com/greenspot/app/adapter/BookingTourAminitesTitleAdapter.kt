package com.greenspot.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.responce.bookinginfo.TourAmenitiesItem
import com.greenspot.app.responce.tourdetail.AmenitiesItem

import com.greenspot.app.utils.Common
import java.util.*

class BookingTourAminitesTitleAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<BookingTourAminitesTitleAdapter.FeatureViewHolder>() {

    private var data: List<TourAmenitiesItem> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_groupsubtext, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)
        val aminites = data[position]


        holder.itemView.setOnClickListener(View.OnClickListener {
            val expanded = aminites.isExpanded()
            aminites.setExpanded(!expanded)
            notifyItemChanged(position)
        })

    }


    fun swapData(data: List<TourAmenitiesItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(
            item: TourAmenitiesItem,
            context: FragmentActivity?
        ) = with(itemView) {


            val title: TextView
            val laysubitem: LinearLayout

            title = itemView.findViewById(R.id.txt_listTitle)
            laysubitem = itemView.findViewById(R.id.lay_subitem)

            val expanded = item.isExpanded()
            laysubitem.visibility = if (expanded) View.VISIBLE else View.GONE

            if (expanded) {

                title.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, ContextCompat.getDrawable(
                        context!!, R.drawable.ic_ncollops
                    ), null
                )
            } else {

                title.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, ContextCompat.getDrawable(
                        context!!, R.drawable.ic_nexpand
                    ), null
                )
            }


            title.setText(item.title)


            itemView.findViewById<TextView>(R.id.txt_itemdesc).text = item.value


        }
    }


}



