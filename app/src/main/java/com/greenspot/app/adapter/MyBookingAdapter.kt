package com.greenspot.app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenspot.app.R
import com.greenspot.app.activity.BookingInfoActvity
import com.greenspot.app.responce.bookinglist.RecordsItem
import com.greenspot.app.utils.AppConfig
import kotlinx.android.synthetic.main.item_listbooking.view.*
import java.util.*


class MyBookingAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<MyBookingAdapter.FeatureViewHolder>() {

    private var data: List<RecordsItem> = ArrayList()
    private val mContext: Context? = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(com.greenspot.app.R.layout.item_listbooking, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)

        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, BookingInfoActvity::class.java);
            intent.putExtra(AppConfig.EXTRA.BOOKINGID, data[position].masterId)
            intent.putExtra(AppConfig.EXTRA.BOOKINGTYPE, data[position].type)
            intent.putExtra(AppConfig.EXTRA.CHECKBOOKINGINFO,2)
            mContext!!.startActivity(intent)

        })


    }


    fun swapData(data: List<RecordsItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: RecordsItem,
            context: FragmentActivity?
        ) = with(itemView) {


            itemView.findViewById<TextView>(R.id.txt_itemname).text = item.title
            itemView.findViewById<TextView>(R.id.txt_location).text = item.type + " In "+ item.locations
            itemView.findViewById<TextView>(R.id.txt_noofperson).text = item.noOfPerson
            itemView.findViewById<TextView>(R.id.txt_personname).text = item.personFullName
            itemView.findViewById<TextView>(R.id.txt_date).text =
                item.fromDate + " To " + item.toDate
            itemView.findViewById<TextView>(R.id.txt_price).text =
                item.originalPaymentCurrency + " " + item.originalPaymentPrice.toString()

            Glide.with(itemView)
                .load(item.mainImage)
                .placeholder(R.drawable.travel)
                .centerCrop()
                .into(itemView.img_itemimg)


        }
    }


}