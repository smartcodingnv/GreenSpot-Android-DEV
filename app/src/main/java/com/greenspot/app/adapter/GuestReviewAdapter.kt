package com.greenspot.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.responce.PlaceReivewRecordsItem
import java.util.*

class GuestReviewAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<GuestReviewAdapter.FeatureViewHolder>() {

    private var data: List<PlaceReivewRecordsItem> = ArrayList()
    private val mContext: Context? = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gustreview, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)


    }


    fun swapData(data: List<PlaceReivewRecordsItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: PlaceReivewRecordsItem,
            context: FragmentActivity?
        ) = with(itemView) {


            itemView.findViewById<TextView>(R.id.txt_username).text = item.frontUserFirstName + " " +item.frontUserLastName
            itemView.findViewById<TextView>(R.id.txt_ratingdate).text = item.operationDate
            itemView.findViewById<TextView>(R.id.txt_reivew).text = item.reviewDescription
            itemView.findViewById<RatingBar>(R.id.rt_reivew).rating = item.reviewStar.toFloat()
//            itemView.findViewById<TextView>(R.id.txt_reivew).text = item.itemreview


        }
    }


}