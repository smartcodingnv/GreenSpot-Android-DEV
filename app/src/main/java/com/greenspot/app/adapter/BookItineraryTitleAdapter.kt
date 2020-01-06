package com.greenspot.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.responce.bookinginfo.ItinerariesItem
import java.util.*

class BookItineraryTitleAdapter(
    val context: FragmentActivity?,
    val itemClickListener: ItemClickListener
) :
    RecyclerView.Adapter<BookItineraryTitleAdapter.FeatureViewHolder>() {

    private var data: List<ItinerariesItem> = ArrayList()
    private var mContext = context
    private var row_index: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_itineraryday, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)
        val movie = data[position]

//


        holder.itemView.setOnClickListener {
            row_index = position;
            notifyDataSetChanged()
            itemClickListener.recyclerViewListClicked(it, position, 0)
        }

        if (row_index == position) {


            holder.title!!.setTextColor(ContextCompat.getColor(this.mContext!!, R.color.white));
            holder.title!!.setBackgroundColor(ContextCompat.getColor(this.mContext!!, R.color.green));
        } else {
            holder.title!!.setBackgroundColor(ContextCompat.getColor(this.mContext!!, R.color.tansparnt));
            holder.title!!.setTextColor(ContextCompat.getColor(this.mContext!!, R.color.gray));
        }


    }


    fun swapData(data: List<ItinerariesItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        public var title: TextView? = null

        fun bind(
            item: ItinerariesItem,
            context: FragmentActivity?
        ) = with(itemView) {


            itemView.findViewById<TextView>(R.id.txt_listitem).text =
                context!!.getString(R.string.res_day) + item.day

            title = itemView.findViewById<TextView>(R.id.txt_listitem)


//            val expanded = item.isExpanded()
//            rvPlacedetails.visibility = if (expanded) View.VISIBLE else View.GONE

//            if (expanded) {
//
//                title.setCompoundDrawablesWithIntrinsicBounds(
//                    null, null, ContextCompat.getDrawable(
//                        context!!, R.drawable.drawable_collops
//                    ), null
//                )
//            } else {
//
//                title.setCompoundDrawablesWithIntrinsicBounds(
//                    null, null, ContextCompat.getDrawable(
//                        context!!, R.drawable.drawable_add
//                    ), null
//                )
//            }
//
//
//
//            title.setText(item.titleavailablity)
//
//            val placeDetailsAdapter = PlaceDetailsAdapter(context)
//            Common.setVerticalRecyclerView(context!!, rvPlacedetails)
//            placeDetailsAdapter.swapData(item.subItem)
//            rvPlacedetails.adapter = placeDetailsAdapter
        }
    }


}



