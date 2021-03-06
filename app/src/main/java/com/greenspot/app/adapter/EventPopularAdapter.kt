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
import com.greenspot.app.activity.TourDetailsActivity
import com.greenspot.app.responce.eventdetails.PopularEventsItem
import com.greenspot.app.responce.recreationdetails.SuggestedToursItem
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import kotlinx.android.synthetic.main.item_nearbytour.view.*
import kotlinx.android.synthetic.main.item_popularevent.view.*
import java.util.*

class EventPopularAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<EventPopularAdapter.FeatureViewHolder>() {

    private var data: List<PopularEventsItem> = ArrayList()
    private val mContext: Context? = context
    private var helper: PreferenceHelper? =
        PreferenceHelper(this.context!!, AppConfig.PREFERENCE.PREF_FILE)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_popularevent, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)



        holder.itemView.setOnClickListener(View.OnClickListener {

            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.PLACEID, data[position].masterId)
            helper!!.ApplyPref()
            val intent = Intent(mContext, EventDetailsActivity::class.java)
                .putExtra(AppConfig.EXTRA.EVENTID, data[position].masterId)
                .putExtra(AppConfig.EXTRA.EVENTNAME, data[position].title);
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


    fun swapData(data: List<PopularEventsItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: PopularEventsItem,
            context: FragmentActivity?
        ) = with(itemView) {

            //            Glide.with(itemView)
//                .load(item.itemimg)
//
//                .into(itemView.img_itemm)

            var helperlang: PreferenceHelper? =
                PreferenceHelper(context!!, AppConfig.PREFERENCE.PREF_FILE_LANG)
            var currency = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")



            Glide.with(itemView)
                .load(item.mainImage)
                .placeholder(R.drawable.travel)
                .into(itemView.img_eventpopular)



            itemView.findViewById<TextView>(R.id.txt_eventtitle).text = item.title

            if(item.price.equals("Free")){

                itemView.findViewById<TextView>(R.id.txt_eventprice).text =  context.getString(R.string.txt_free)


            }else{

                itemView.findViewById<TextView>(R.id.txt_eventprice).text =currency + " " + item.price
            }



        }
    }


}