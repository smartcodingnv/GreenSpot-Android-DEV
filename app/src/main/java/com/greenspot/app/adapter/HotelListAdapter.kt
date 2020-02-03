package com.greenspot.app.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenspot.app.R
import com.greenspot.app.activity.HotelDetailsActivity
import com.greenspot.app.model.ItineraryImg
import com.greenspot.app.responce.hotellist.IncludedInHotelItem
import com.greenspot.app.responce.hotellist.RecordsItem

import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.Common
import com.greenspot.app.utils.PreferenceHelper
import hk.ids.gws.android.sclick.SClick
import kotlinx.android.synthetic.main.dialog_contry.*
import kotlinx.android.synthetic.main.item_hotellist.view.*
import java.util.*


class HotelListAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<HotelListAdapter.FeatureViewHolder>() {

    private var data: List<RecordsItem> = ArrayList()
    private val mContext: Context? = context

    private var helper: PreferenceHelper? =
        PreferenceHelper(this.context!!, AppConfig.PREFERENCE.PREF_FILE)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_hotellist, parent, false)
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
             val intent = Intent(mContext, HotelDetailsActivity::class.java)
                 .putExtra(AppConfig.EXTRA.HOTELID, data[position].masterId)
                 .putExtra(AppConfig.EXTRA.HOTELNAME, data[position].name);
             mContext!!.startActivity(intent)

         })

    }


    fun swapData(data: List<RecordsItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var itineraryImg: ArrayList<ItineraryImg> = ArrayList()

        fun bind(
            item: RecordsItem,
            context: FragmentActivity?
        ) = with(itemView) {

            //            Glide.with(itemView)
//                .load(item.itemimg)
//
//                .into(itemView.img_itemm)


            val helperlang: PreferenceHelper? =
                PreferenceHelper(context!!, AppConfig.PREFERENCE.PREF_FILE_LANG)
            val currency = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")

            val rating: RatingBar
            rating = itemView.findViewById(R.id.rt_hotel)
            Glide.with(itemView)
                .load(item.mainImage)
                .dontTransform()
                .placeholder(R.drawable.travel)
                .into(itemView.imageView1)
            rating.rating = item.avgReviews.toFloat()


            itemView.findViewById<TextView>(R.id.txt_hotelname).text = item.name
            itemView.findViewById<TextView>(R.id.txt_hotelreivews).text =
                item.totalReviews.toString() + " " + context.getString(R.string.str_reviews)
            itemView.findViewById<TextView>(R.id.txt_location).text =
                item.city + ", " + item.district + ", " + item.country
            itemView.findViewById<TextView>(R.id.txt_hotelprice).text =
                context.getString(R.string.txt_from) + " " + currency + " " + item.price + "*"

            val imgOtherServiceAdapter = TourImgOtherServiceAdapter(context)
            itineraryImg.clear()
            setSubImgData(context, item.includedInHotel)
            Common.setHorizontalRecyclerView(context, rv_otherservicetourimg)
            imgOtherServiceAdapter.swapData(itineraryImg)
            rv_otherservicetourimg.adapter = imgOtherServiceAdapter


        }


        private fun setSubImgData(
            context: FragmentActivity?,
            includedInHotel: List<IncludedInHotelItem>?
        ) {
            for (name in includedInHotel!!) {

                if (name.masterId.equals("1")) {

                    itineraryImg.add(
                        ItineraryImg(
                            "1",
                            context!!.getString(R.string.str_wifi),
                            getURLForResource(R.drawable.ic_otherservicewifi)
                        )
                    )
                }

                if (name.masterId.equals("2")) {

                    itineraryImg.add(
                        ItineraryImg(
                            "2",
                            context!!.getString(R.string.str_parking),
                            getURLForResource(R.drawable.ic_parking)
                        )
                    )
                }

                if (name.masterId.equals("3")) {

                    itineraryImg.add(
                        ItineraryImg(
                            "3",
                            context!!.getString(R.string.str_pool),
                            getURLForResource(R.drawable.ic_pool)
                        )
                    )
                }
                if (name.masterId.equals("4")) {

                    itineraryImg.add(
                        ItineraryImg(
                            "4",
                            context!!.getString(R.string.str_meals),
                            getURLForResource(R.drawable.ic_othermeal)
                        )
                    )
                }
                if (name.masterId.equals("5")) {

                    itineraryImg.add(
                        ItineraryImg(
                            "5",
                            context!!.getString(R.string.str_transportation),
                            getURLForResource(R.drawable.ic_othertransport)
                        )
                    )
                }


            }


        }

        fun getURLForResource(resourceId: Int): String {
            //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
            return Uri.parse("android.resource://" + com.greenspot.app.R::class.java.getPackage()!!.getName() + "/" + resourceId)
                .toString()
        }


    }



}