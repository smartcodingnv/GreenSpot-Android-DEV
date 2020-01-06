package com.greenspot.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.model.RecBookingList
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import java.util.*

class BookingPlaceAdapter(
    val context: FragmentActivity?,
    val itemClickListener: ItemClickListener
) :
    RecyclerView.Adapter<BookingPlaceAdapter.FeatureViewHolder>() {

    private var data: List<RecBookingList> = ArrayList()

    private var helperlang: PreferenceHelper? =
        PreferenceHelper(this.context!!, AppConfig.PREFERENCE.PREF_FILE_LANG)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recreationbooking, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)
        val movie = data[position]
        var currncyCode = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")

        var adultcount: Int = data[position].count.toInt()

        if (position == 0) {

            holder.txt_adultcount!!.setText(data[position].count)
            holder.txt_price!!.text = currncyCode + " " + (data[position].price.toFloat() * 2).toString()
            data[position].finalPrice = (data[position].price.toFloat() * 2).toString()

        }else{
            holder.txt_price!!.text = currncyCode + " 0.0"
        }

        holder.ib_nexttt!!.setOnClickListener(View.OnClickListener {

            adultcount++
            holder.txt_adultcount!!.setText(adultcount.toString())
            val totalprice = data[position].price.toFloat() * adultcount
            holder.txt_price!!.text = currncyCode + " " +  totalprice.toString()
            data[position].count = adultcount.toString()
            data[position].finalPrice = totalprice.toString()

            itemClickListener.recyclerViewListClicked(it, position, 1)


        })

        holder.ib_prev!!.setOnClickListener(View.OnClickListener {


            if (adultcount == 0) {
                holder.txt_price!!.text = currncyCode + " 0.0"
                return@OnClickListener
            }
            adultcount--
            holder.txt_adultcount!!.setText(adultcount.toString())

            val totalprice = data[position].price.toFloat() * adultcount
            holder.txt_price!!.text = currncyCode + " " + totalprice.toString()
            data[position].count = adultcount.toString()
            data[position].finalPrice = totalprice.toString()

            itemClickListener.recyclerViewListClicked(it, position, 1)


        })


    }


    fun swapData(data: List<RecBookingList>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        public var ib_nexttt: ImageButton? = null
        public var ib_prev: ImageButton? = null
        public var txt_price: TextView? = null
        public var txt_adultcount: TextView? = null

        fun bind(
            item: RecBookingList,
            context: FragmentActivity?
        ) = with(itemView) {


            val title: TextView

            title = itemView.findViewById(R.id.txt_listTitle)
            txt_price = itemView.findViewById(R.id.txt_price)
            txt_adultcount = itemView.findViewById(R.id.txt_adultcount)
            ib_nexttt = itemView.findViewById(R.id.ib_adultadd)
            ib_prev = itemView.findViewById(R.id.ib_adultminus)
            title.setText(item.itemname)

        }
    }


}



