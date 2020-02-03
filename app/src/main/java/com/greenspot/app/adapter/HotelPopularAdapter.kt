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
import com.greenspot.app.activity.HotelDetailsActivity

import com.greenspot.app.activity.TourDetailsActivity
import com.greenspot.app.responce.hoteldetails.NearbyPlacesItem
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import com.greenspot.app.utils.Progress
import com.greenspot.app.utils.Utils

import kotlinx.android.synthetic.main.item_place.view.imageView1
import java.util.*

class HotelPopularAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<HotelPopularAdapter.FeatureViewHolder>() {

    private var data: List<NearbyPlacesItem> = ArrayList()
    private val mContext: Context? = context
    private var helper: PreferenceHelper? =
        PreferenceHelper(this.context!!, AppConfig.PREFERENCE.PREF_FILE)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_nearbyhotel, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)

        holder.itemView.setOnClickListener(View.OnClickListener {

            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.PLACEID, data[position].masterId)
            helper!!.ApplyPref()
            val intent = Intent(mContext, HotelDetailsActivity::class.java)
                .putExtra(AppConfig.EXTRA.HOTELID, data[position].masterId)
                .putExtra(AppConfig.EXTRA.HOTELNAME, data[position].name);
            mContext!!.startActivity(intent)

        })

    }


    fun swapData(data: List<NearbyPlacesItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: NearbyPlacesItem,
            context: FragmentActivity?
        ) = with(itemView) {
            val helperlang: PreferenceHelper? =
                PreferenceHelper(this.context!!, AppConfig.PREFERENCE.PREF_FILE_LANG)
            var currency = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")

            val providername: TextView
            providername = itemView.findViewById(R.id.txt_providername)

            Glide.with(itemView)
                .load(item.mainImage)
                .centerCrop()
                .into(itemView.imageView1)
            itemView.findViewById<TextView>(R.id.txt_place).text = item.name
            itemView.findViewById<TextView>(R.id.txt_location).text = currency + " " + item.price
            providername.visibility = View.VISIBLE
            providername.text = context!!.getString(R.string.res_per_night)


        }
    }


}