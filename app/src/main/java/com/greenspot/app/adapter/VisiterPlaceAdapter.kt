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
import com.greenspot.app.activity.PlaceDetailsActivity
import com.greenspot.app.responce.RecordsItem
import com.greenspot.app.responce.home.ResponeHome
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import com.greenspot.app.utils.Progress
import com.greenspot.app.utils.Utils
import hk.ids.gws.android.sclick.SClick
import kotlinx.android.synthetic.main.item_place.view.*
import java.util.*

class VisiterPlaceAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<VisiterPlaceAdapter.FeatureViewHolder>() {

    private var data: List<ResponeHome.DataBean.RecreationBean.RecordsBeanPlace> = ArrayList()
    private val mContext: Context? = context
    private var progress: Progress? = Progress(this.context!!)
    private var utils: Utils? = Utils(this.context!!)
    private var helper: PreferenceHelper? =
        PreferenceHelper(this.context!!, AppConfig.PREFERENCE.PREF_FILE)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_place, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)

        holder.itemView.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.PLACEID, data[position].master_id!!)
            helper!!.ApplyPref()
            val intent = Intent(mContext, PlaceDetailsActivity::class.java)
                .putExtra(AppConfig.EXTRA.RECREATIONITEMID, data[position].master_id)
                .putExtra(AppConfig.EXTRA.RECREATIONITEMNAME, data[position].place_name);
            mContext!!.startActivity(intent)

        })

    }


    fun swapData(data: List<ResponeHome.DataBean.RecreationBean.RecordsBeanPlace>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: ResponeHome.DataBean.RecreationBean.RecordsBeanPlace,
            context: FragmentActivity?
        ) = with(itemView) {
            var helperlang: PreferenceHelper? =
                PreferenceHelper(this!!.context!!, AppConfig.PREFERENCE.PREF_FILE_LANG)
            var currency = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")

            Glide.with(itemView)
                .load(item.main_image)
                .placeholder(R.drawable.travel)
                .centerCrop()
                .into(itemView.imageView1)
            itemView.findViewById<TextView>(R.id.txt_place).text = item.place_name
            itemView.findViewById<TextView>(R.id.txt_location).text =
                item.city + ", " + item.district + ", " + item.country

            if (item.min_price_range.equals("0") ) {
                itemView.findViewById<TextView>(R.id.txt_price).text =
                    "From " + currency + " " + item.min_price_range
                itemView.findViewById<TextView>(R.id.txt_price).visibility = View.GONE
            }else if (item.min_price_range!!.isEmpty()) {
                itemView.findViewById<TextView>(R.id.txt_price).text =
                    "From " + currency + " " + item.min_price_range
                itemView.findViewById<TextView>(R.id.txt_price).visibility = View.GONE
            } else {
                itemView.findViewById<TextView>(R.id.txt_price).text =
                    "From " + currency + " " + item.min_price_range
                itemView.findViewById<TextView>(R.id.txt_price).visibility = View.VISIBLE
            }

        }
    }


}