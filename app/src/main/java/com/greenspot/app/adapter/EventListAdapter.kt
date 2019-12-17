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
import com.greenspot.app.responce.EventListRecordsItem
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import com.greenspot.app.utils.Progress
import com.greenspot.app.utils.Utils
import kotlinx.android.synthetic.main.item_listevent.view.*
import java.util.*

class EventListAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<EventListAdapter.FeatureViewHolder>() {

    private var data: List<EventListRecordsItem> = ArrayList()
    private val mContext: Context? = context
    private var progress: Progress? = Progress(this.context!!)
    private var utils: Utils? = Utils(this.context!!)
    private var helper: PreferenceHelper? =
        PreferenceHelper(this.context!!, AppConfig.PREFERENCE.PREF_FILE)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_listevent, parent, false)
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
                .putExtra(AppConfig.EXTRA.EVENTID,data[position].masterId)
                .putExtra(AppConfig.EXTRA.EVENTNAME,data[position].title);
            mContext!!.startActivity(intent)

        })

//        holder.itemView.setOnClickListener(View.OnClickListener {
//            val intent = Intent(mContext, EventDetailsActivity::class.java);
//            mContext!!.startActivity(intent)
//
//        })

//        holder.itemView.setOnClickListener(View.OnClickListener {
//            val intent = Intent(mContext, ProductDetailsActivity::class.java);
//            intent.putExtra(AppConfig.EXTRA.PRODTITLE, data[position].itemname)
//            intent.putExtra(AppConfig.EXTRA.PRODCATEGORY, data[position].itemcategoruy)
//            mContext!!.startActivity(intent)
//
//        })

    }


    fun swapData(data: List<EventListRecordsItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: EventListRecordsItem,
            context: FragmentActivity?
        ) = with(itemView) {




            val helperlang: PreferenceHelper? =
                PreferenceHelper(context!!, AppConfig.PREFERENCE.PREF_FILE_LANG)
            val currency = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")

            val rating: RatingBar

            rating = itemView.findViewById(R.id.rt_event)
            Glide.with(itemView)
                .load(item.mainImage)
                .placeholder(R.drawable.travel)
                .into(itemView.img_event)




            rating.rating = item.avgReviews.toFloat()

            if(item.startDate.isEmpty()){

                itemView.findViewById<TextView>(R.id.txt_eventstartdate).text = "Every Day"
                itemView.findViewById<TextView>(R.id.txt_eventenddate).visibility = View.GONE

            }else{
                itemView.findViewById<TextView>(R.id.txt_eventenddate).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.txt_eventstartdate).text = item.startDate
                itemView.findViewById<TextView>(R.id.txt_eventenddate).text = item.endDate
            }
            if(item.finalPrice.equals("0")){

                itemView.findViewById<TextView>(R.id.txt_eventprice).text = "Free"
                itemView.findViewById<TextView>(R.id.txt_eventoffer).visibility = View.GONE

            }else{
                itemView.findViewById<TextView>(R.id.txt_eventoffer).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.txt_eventoffer).text = item.discount
                itemView.findViewById<TextView>(R.id.txt_eventprice).text = "From " + currency + " " + item.finalPrice
            }

            itemView.findViewById<TextView>(R.id.txt_eventtitle).text = item.title

            itemView.findViewById<TextView>(R.id.txt_eventstarttime).text = item.startTime
            itemView.findViewById<TextView>(R.id.txt_eventendtime).text = item.endTime
            itemView.findViewById<TextView>(R.id.txt_eventlocation).text = item.location






        }
    }


}