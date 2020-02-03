package com.greenspot.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenspot.app.R
import com.greenspot.app.responce.hoteldetails.RoomImagesItem


import kotlinx.android.synthetic.main.item_placeimg.view.*
import java.util.*

class SliderHotelRoomAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<SliderHotelRoomAdapter.FeatureViewHolder>() {

    private var data: List<RoomImagesItem> = ArrayList()
    private val mContext: Context? = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_placeimg, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)

//        holder.itemView.setOnClickListener(View.OnClickListener {
//            val intent = Intent(mContext, ProductDetailsActivity::class.java);
//            intent.putExtra(AppConfig.EXTRA.PRODTITLE, data[position].itemname)
//            intent.putExtra(AppConfig.EXTRA.PRODCATEGORY, data[position].itemcategoruy)
//            mContext!!.startActivity(intent)
//
//        })


    }


    fun swapData(data: List<RoomImagesItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: RoomImagesItem,
            context: FragmentActivity?
        ) = with(itemView) {

            Glide.with(itemView)
                .load(item.imageName)
                .placeholder(R.drawable.travel)
                .centerCrop()
                .into(itemView.img_item)


        }
    }


}