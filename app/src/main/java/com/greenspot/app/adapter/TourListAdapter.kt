package com.greenspot.app.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenspot.app.R
import com.greenspot.app.activity.TourDetailsActivity
import com.greenspot.app.responce.tourlist.IncludedInTourPackageItem
import com.greenspot.app.responce.tourlist.RecordsItem
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import kotlinx.android.synthetic.main.item_tourlist.view.*
import java.util.*


class TourListAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<TourListAdapter.FeatureViewHolder>() {

    private var data: List<RecordsItem> = ArrayList()
    private val mContext: Context? = context
    private var helper: PreferenceHelper? =
        PreferenceHelper(this.context!!, AppConfig.PREFERENCE.PREF_FILE)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_tourlist, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)


        for (name in data[position].includedInTourPackage!!) {

            Log.e("List", " tour " + name.masterId + "   " + name.value)


            if (name.masterId.equals("1")) {

                holder.layFlight.visibility = View.VISIBLE
}
            if (name.masterId.equals("2")) {

                holder.layhotel.visibility = View.VISIBLE

            }

            if (name.masterId.equals("3")) {

                holder.laysightseen.visibility = View.VISIBLE
            }
            if (name.masterId.equals("4")) {

                holder.laymeals.visibility = View.VISIBLE
            }
            if (name.masterId.equals("5")) {

                holder.laytransportaion.visibility = View.VISIBLE

            }
            if (name.masterId.equals("6")) {

                holder.laywifi.visibility = View.VISIBLE

            }

        }



        holder.itemView.setOnClickListener(View.OnClickListener {

            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.PLACEID, data[position].masterId)
            helper!!.ApplyPref()
            val intent = Intent(mContext, TourDetailsActivity::class.java)
                .putExtra(AppConfig.EXTRA.TOURID, data[position].masterId)
                .putExtra(AppConfig.EXTRA.TOURNAME, data[position].packageName);
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

        public lateinit var layFlight: LinearLayout
        public lateinit var layhotel: LinearLayout
        public lateinit var laysightseen: LinearLayout
        public lateinit var laymeals: LinearLayout
        public lateinit var laytransportaion: LinearLayout
        public lateinit var laywifi: LinearLayout
        fun bind(
            item: RecordsItem,
            context: FragmentActivity?
        ) = with(itemView) {

            //            Glide.with(itemView)
//                .load(item.itemimg)
//
//                .into(itemView.img_itemm)

            layFlight = itemView.findViewById(R.id.lay_flighttt)
            layhotel = itemView.findViewById(R.id.lay_hotelll)
            laysightseen = itemView.findViewById(R.id.lay_sightseennn)
            laymeals = itemView.findViewById(R.id.lay_mealsss)
            laytransportaion = itemView.findViewById(R.id.lay_transportaionnn)
            laywifi = itemView.findViewById(R.id.lay_wifiii)


            var helperlang: PreferenceHelper? =
                PreferenceHelper(context!!, AppConfig.PREFERENCE.PREF_FILE_LANG)
            var currency = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")

            var rating: RatingBar
            rating = itemView.findViewById(R.id.rt_tour)
            Glide.with(itemView)
                .load(item.mainImage)
                .placeholder(R.drawable.travel)
                .into(itemView.img_tour)
            rating.rating = item.avgReviews.toFloat()


            itemView.findViewById<TextView>(R.id.txt_tourname).text = item.packageName
            itemView.findViewById<TextView>(R.id.txt_tourlocation).text = item.locations
            itemView.findViewById<TextView>(R.id.txt_day).text = item.days.toString()
            itemView.findViewById<TextView>(R.id.txt_night).text = item.nights.toString()
            itemView.findViewById<TextView>(R.id.txt_price).text =
                "From " + currency + " " + item.price + "*"




         /*   for (name in includePkgList) {

                Log.e("List", " tour " + name.masterId + "   " + name.value)


                if (name.masterId.equals("1")) {

                    layFlight.visibility = View.VISIBLE

                }
                if (name.masterId.equals("2")) {

                    layhotel.visibility = View.VISIBLE

                }

                if (name.masterId.equals("3")) {

                    laysightseen.visibility = View.VISIBLE
                }
                if (name.masterId.equals("4")) {

                    laymeals.visibility = View.VISIBLE
                }
                if (name.masterId.equals("5")) {

                    laytransportaion.visibility = View.VISIBLE

                }
                if (name.masterId.equals("6")) {

                    laywifi.visibility = View.VISIBLE
                }

            }
*/

//            val imgOtherServiceAdapter = ImgOtherServiceAdapter(context)
//            Common.setHorizontalRecyclerView(context!!, rv_otherservice)
//            imgOtherServiceAdapter.swapData(item.includedInTourPackage)
//            rv_otherservice.adapter = imgOtherServiceAdapter


        }
    }


}