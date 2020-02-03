package com.greenspot.app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.greenspot.app.R
import com.greenspot.app.activity.PlaceDetailsActivity
import com.greenspot.app.responce.RecordsItem
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import com.greenspot.app.utils.Progress
import com.greenspot.app.utils.Utils
import hk.ids.gws.android.sclick.SClick
import kotlinx.android.synthetic.main.item_listplace.view.*
import java.util.*

class VisiterListPlaceAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<VisiterListPlaceAdapter.FeatureViewHolder>() {

    private var data: List<RecordsItem> = ArrayList()
    private val mContext: Context? = context
    private var progress: Progress? = Progress(this.context!!)
    private var utils: Utils? = Utils(this.context!!)
    private var helper: PreferenceHelper? =
        PreferenceHelper(this.context!!, AppConfig.PREFERENCE.PREF_FILE)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_listplace, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)



        holder.itemView.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.PLACEID, data[position].masterId)
            helper!!.ApplyPref()
            val intent = Intent(mContext, PlaceDetailsActivity::class.java)
                .putExtra(AppConfig.EXTRA.RECREATIONITEMID,data[position].masterId)
                .putExtra(AppConfig.EXTRA.RECREATIONITEMNAME,data[position].placeName);
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


    fun swapData(data: List<RecordsItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        //        var lay_imggg: ImageView? = null
        fun bind(
            item: RecordsItem,
            context: FragmentActivity?
        ) = with(itemView) {

            var lay_imggg: ImageView
            var helperlang: PreferenceHelper? =
                PreferenceHelper(this!!.context!!, AppConfig.PREFERENCE.PREF_FILE_LANG)
            var currency = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")

            var rating: RatingBar
            rating = itemView.findViewById(R.id.recreation_rating)
            Glide.with(itemView)
                .load(item.mainImage)
                .placeholder(R.drawable.travel)
                .dontTransform()
                .into(itemView.imageView1)


            rating.rating = item.avgReviews.toFloat()

            itemView.findViewById<TextView>(R.id.txt_recreationname).text = item.placeName
            itemView.findViewById<TextView>(R.id.txt_location).text = item.city + ", " + item.district + ", " + item.country

            if (item.minPriceRange.isEmpty()) {
                itemView.findViewById<TextView>(R.id.txt_price).text = context!!.getString(R.string.txt_from)+" " + currency + " " + item.minPriceRange
                itemView.findViewById<TextView>(R.id.txt_price).visibility = View.GONE
            }else if (item.minPriceRange.equals("0")) {
                itemView.findViewById<TextView>(R.id.txt_price).text = context!!.getString(R.string.txt_from)+" " + currency + " " + item.minPriceRange
                itemView.findViewById<TextView>(R.id.txt_price).visibility = View.GONE
            } else {
                itemView.findViewById<TextView>(R.id.txt_price).text = context!!.getString(R.string.txt_from)+" " + currency + " " + item.minPriceRange
                itemView.findViewById<TextView>(R.id.txt_price).visibility = View.VISIBLE
            }

        }
    }


}