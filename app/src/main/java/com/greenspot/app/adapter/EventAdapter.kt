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
import com.greenspot.app.activity.EventDetailsActivity
import com.greenspot.app.responce.home.ResponeHome
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import hk.ids.gws.android.sclick.SClick
import kotlinx.android.synthetic.main.item_event.view.*
import java.util.*

class EventAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<EventAdapter.FeatureViewHolder>() {

    private var data: List<ResponeHome.DataBean.EventBean.RecordsBeanEvent> = ArrayList()
    private val mContext: Context? = context
    private var helper: PreferenceHelper? =
        PreferenceHelper(this.context!!, AppConfig.PREFERENCE.PREF_FILE)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_event, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)

//        holder.itemView.setOnClickListener(View.OnClickListener {
//            val intent = Intent(mContext, EventDetailsActivity::class.java);
//            mContext!!.startActivity(intent)
//
//        })

        holder.itemView.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener
            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.PLACEID, data[position].master_id!!)
            helper!!.ApplyPref()
            val intent = Intent(mContext, EventDetailsActivity::class.java)
                .putExtra(AppConfig.EXTRA.EVENTID,data[position].master_id)
                .putExtra(AppConfig.EXTRA.EVENTNAME,data[position].title);
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


    fun swapData(data: List<ResponeHome.DataBean.EventBean.RecordsBeanEvent>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: ResponeHome.DataBean.EventBean.RecordsBeanEvent,
            context: FragmentActivity?
        ) = with(itemView) {

            val helperlang: PreferenceHelper? =
                PreferenceHelper(context!!, AppConfig.PREFERENCE.PREF_FILE_LANG)
            val currency = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")

            val rating: RatingBar


            rating = itemView.findViewById(R.id.rt_event)
            Glide.with(itemView)
                .load(item.main_image)
                .placeholder(R.drawable.travel)
                .into(itemView.img_event)

            rating.rating = item.avg_reviews.toFloat()

            if(item.start_date!!.isEmpty()){

                itemView.findViewById<TextView>(R.id.txt_eventtime).text = item.start_time + "  "+context.getString(R.string.res_to)  +" " +item.end_time


            }else{
                itemView.findViewById<TextView>(R.id.txt_eventtime).text = item.start_date + "  "+context.getString(R.string.res_to)  +" "  +item.end_date

            }
            if(item.final_price.equals("0")){

                itemView.findViewById<TextView>(R.id.txt_eventprice).text = context.getString(R.string.txt_free)
            }else{
                itemView.findViewById<TextView>(R.id.txt_eventprice).text = context.getString(R.string.txt_from)+" " + currency + " " + item.final_price
            }

            itemView.findViewById<TextView>(R.id.txt_eventname).text = item.title
            itemView.findViewById<TextView>(R.id.txt_eventlocation).text = item.location


        }
    }


}