package com.greenspot.app.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.greenspot.app.model.ItineraryImg
import com.greenspot.app.responce.tourlist.IncludedInTourPackageItem
import com.greenspot.app.responce.tourlist.RecordsItem
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.Common
import com.greenspot.app.utils.PreferenceHelper
import hk.ids.gws.android.sclick.SClick
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






        holder.itemView.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.PLACEID, data[position].masterId)
            helper!!.ApplyPref()
            val intent = Intent(mContext, TourDetailsActivity::class.java)
                .putExtra(AppConfig.EXTRA.TOURID, data[position].masterId)
                .putExtra(AppConfig.EXTRA.TOURNAME, data[position].packageName);
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

            val imgOtherServiceAdapter = TourImgOtherServiceAdapter(context)
            itineraryImg.clear()
            setSubImgData(item.includedInTourPackage)
            Common.setHorizontalRecyclerView(context, rv_otherservicetourimg)
            imgOtherServiceAdapter.swapData(itineraryImg)
            rv_otherservicetourimg.adapter = imgOtherServiceAdapter


        }


        private fun setSubImgData(includedInTourPackage: List<IncludedInTourPackageItem>?) {
            for (name in includedInTourPackage!!) {

                if (name.masterId.equals("1")) {

                    itineraryImg.add(
                        ItineraryImg(
                            "1",
                            "Flight",
                            getURLForResource(R.drawable.ic_otherflight)
                        )
                    )
                }

                if (name.masterId.equals("2")) {

                    itineraryImg.add(
                        ItineraryImg(
                            "2",
                            "Hotel",
                            getURLForResource(R.drawable.ic_ohterhotel)
                        )
                    )
                }

                if (name.masterId.equals("3")) {

                    itineraryImg.add(
                        ItineraryImg(
                            "3",
                            "Sightseeing",
                            getURLForResource(R.drawable.ic_othersigghtsign)
                        )
                    )
                }
                if (name.masterId.equals("4")) {

                    itineraryImg.add(
                        ItineraryImg(
                            "4",
                            "Meals",
                            getURLForResource(R.drawable.ic_othermeal)
                        )
                    )
                }
                if (name.masterId.equals("5")) {

                    itineraryImg.add(
                        ItineraryImg(
                            "5",
                            "Transportation",
                            getURLForResource(R.drawable.ic_othertransport)
                        )
                    )
                }
                if (name.masterId.equals("6")) {

                    itineraryImg.add(
                        ItineraryImg(
                            "6",
                            "Wifi",
                            getURLForResource(R.drawable.ic_otherservicewifi)
                        )
                    )
                }

            }


        }

        fun getURLForResource(resourceId: Int): String {
            //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
            return Uri.parse("android.resource://" + com.greenspot.app.R::class.java!!.getPackage()!!.getName() + "/" + resourceId)
                .toString()
        }


    }


}