package com.greenspot.app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenspot.app.R
import com.greenspot.app.activity.TourDetailsActivity
import com.greenspot.app.responce.home.ResponeHome
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import kotlinx.android.synthetic.main.item_tour.view.*
import kotlinx.android.synthetic.main.item_tourlist.view.*
import java.util.*

class TourPlaceAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<TourPlaceAdapter.FeatureViewHolder>() {

    private var data: List<ResponeHome.DataBean.TourBean.RecordsBeanTour> = ArrayList()
    private val mContext: Context? = context
    private var helper: PreferenceHelper? =
        PreferenceHelper(this.context!!, AppConfig.PREFERENCE.PREF_FILE)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_tour, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)



        holder.itemView.setOnClickListener(View.OnClickListener {

            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.PLACEID, data[position].master_id!!)
            helper!!.ApplyPref()
            val intent = Intent(mContext, TourDetailsActivity::class.java)
                .putExtra(AppConfig.EXTRA.TOURID, data[position].master_id)
                .putExtra(AppConfig.EXTRA.TOURNAME, data[position].package_name);
            mContext!!.startActivity(intent)

        })

//        holder.itemView.setOnClickListener(View.OnClickListener {
//            val intent = Intent(mContext, ProductDetailsActivity::class.java);
//            intent.putExtra(AppConfig.EXTRA.PRODTITLE, data[position].itemname)
//            intent.putExtra(AppConfig.EXTRA.PRODCATEGORY, data[position].itemcategoruy)
//            mContext!!.startActivity(intent)
//
//        })

    }


    fun swapData(data: List<ResponeHome.DataBean.TourBean.RecordsBeanTour>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: ResponeHome.DataBean.TourBean.RecordsBeanTour,
            context: FragmentActivity?
        ) = with(itemView) {

            //            Glide.with(itemView)
//                .load(item.itemimg)
//
//                .into(itemView.img_itemm)

            var helperlang: PreferenceHelper? =
                PreferenceHelper(context!!, AppConfig.PREFERENCE.PREF_FILE_LANG)
            var currency = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")

            var rating: RatingBar
            rating = itemView.findViewById(R.id.rt_tourrating)
            Glide.with(itemView)
                .load(item.main_image)
                .placeholder(R.drawable.travel)
                .into(itemView.img_tourimg)

            rating.rating = item.avg_reviews.toFloat()

            itemView.findViewById<TextView>(R.id.txt_tourname).text = item.package_name
            itemView.findViewById<TextView>(R.id.txt_tourlocation).text = item.locations
            itemView.findViewById<TextView>(R.id.txt_daynight).text = item.nights.toString() +" Nights | " +  item.days.toString() +" Days"
            itemView.findViewById<TextView>(R.id.txt_tourprice).text =
                "From " + currency + " " + item.price + "*"

        }
    }


}