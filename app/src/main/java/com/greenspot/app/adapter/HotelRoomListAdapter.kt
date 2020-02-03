package com.greenspot.app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenspot.app.R
import com.greenspot.app.activity.HotelRoomTabActivity
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.responce.hoteldetails.RoomsItem
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import hk.ids.gws.android.sclick.SClick
import java.util.*


class HotelRoomListAdapter(
    val context: FragmentActivity?,
    val itemClickListener: ItemClickListener
) :
    RecyclerView.Adapter<HotelRoomListAdapter.FeatureViewHolder>() {

    private var data: List<RoomsItem> = ArrayList()
    private val mContext: Context? = context

    private var helper: PreferenceHelper? =
        PreferenceHelper(this.context!!, AppConfig.PREFERENCE.PREF_FILE)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_roomlist, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)


        holder.btnselectRoom.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            itemClickListener.recyclerViewListClicked(it, position, 1)

        })



        holder.txtmoreaminities.setOnClickListener(View.OnClickListener {

            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener
            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.ROOMMASTERID, data[position].masterId)
            helper!!.ApplyPref()
            val intent = Intent(mContext, HotelRoomTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 1)
                .putExtra(AppConfig.EXTRA.ROOMTABFLAG, 1)
            mContext!!.startActivity(intent)

        })

        holder.txtmoreimg.setOnClickListener(View.OnClickListener {

            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener
            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.ROOMMASTERID, data[position].masterId)
            helper!!.ApplyPref()
            val intent = Intent(mContext, HotelRoomTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 0)
                .putExtra(AppConfig.EXTRA.ROOMTABFLAG, 1)
            mContext!!.startActivity(intent)

        })

        holder.imgRoom.setOnClickListener(View.OnClickListener {

            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener
            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.ROOMMASTERID, data[position].masterId)
            helper!!.ApplyPref()
            val intent = Intent(mContext, HotelRoomTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 0)
                .putExtra(AppConfig.EXTRA.ROOMTABFLAG, 1)
            mContext!!.startActivity(intent)

        })

        holder.txtmoreoption.setOnClickListener(View.OnClickListener {

            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener
            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.ROOMMASTERID, data[position].masterId)
            helper!!.ApplyPref()
            val intent = Intent(mContext, HotelRoomTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 2)
                .putExtra(AppConfig.EXTRA.ROOMTABFLAG, 1)
            mContext!!.startActivity(intent)

        })


        holder.txtmoreinclude.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener
            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.ROOMMASTERID, data[position].masterId)
            helper!!.ApplyPref()
            val intent = Intent(mContext, HotelRoomTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 3)
                .putExtra(AppConfig.EXTRA.ROOMTABFLAG, 1)
            mContext!!.startActivity(intent)

        })


//        holder.itemView.setOnClickListener(View.OnClickListener {
//            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
//            helper!!.initPref()
//            helper!!.SaveStringPref(AppConfig.PREFERENCE.PLACEID, data[position].masterId)
//            helper!!.ApplyPref()
//            val intent = Intent(mContext, HotelDetailsActivity::class.java)
//                .putExtra(AppConfig.EXTRA.HOTELID, data[position].masterId)
//                .putExtra(AppConfig.EXTRA.HOTELNAME, data[position].);
//            mContext!!.startActivity(intent)
//
//        })

    }


    fun swapData(data: List<RoomsItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public lateinit var btnselectRoom: Button
        public lateinit var txtmoreaminities: TextView
        public lateinit var txtmoreoption: TextView
        public lateinit var txtmoreinclude: TextView
        public lateinit var txtmoreimg: TextView
        public lateinit var imgRoom: ImageView

        fun bind(
            item: RoomsItem,
            context: FragmentActivity?
        ) = with(itemView) {

            //            Glide.with(itemView)
//                .load(item.itemimg)
//
//                .into(itemView.img_itemm)
            btnselectRoom = itemView.findViewById(R.id.btn_select)
            txtmoreaminities = itemView.findViewById(R.id.txt_moreaminities)
            txtmoreoption = itemView.findViewById(R.id.txt_moreoption)
            txtmoreinclude = itemView.findViewById(R.id.txt_moreinclude)
            txtmoreimg = itemView.findViewById(R.id.txt_moreimg)
            imgRoom = itemView.findViewById(R.id.img_room)


            val helperlang: PreferenceHelper? =
                PreferenceHelper(context!!, AppConfig.PREFERENCE.PREF_FILE_LANG)
            val currency = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")


            Glide.with(itemView)
                .load(item.roomImages!![0].imageName)
                .dontTransform()
                .centerCrop()
                .placeholder(R.drawable.travel)
                .into(imgRoom)

            btnselectRoom.visibility = View.VISIBLE
            itemView.findViewById<FrameLayout>(R.id.lay_baseprice).visibility = View.VISIBLE


            itemView.findViewById<TextView>(R.id.txt_roomtype).text = item.roomType
            if (item.basePrice.isEmpty()) {
                itemView.findViewById<TextView>(R.id.txt_baseprice).visibility = View.GONE
            } else {
                itemView.findViewById<TextView>(R.id.txt_baseprice).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.txt_baseprice).text =
                    currency + " " + item.basePrice
            }

            if (item.discount.isEmpty()) {
                itemView.findViewById<TextView>(R.id.txt_offer).visibility = View.GONE
            } else {
                itemView.findViewById<TextView>(R.id.txt_offer).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.txt_offer).text = item.discount
            }
            if (item.saving.isEmpty()) {
                itemView.findViewById<TextView>(R.id.txt_saving).visibility = View.GONE
            } else {
                itemView.findViewById<TextView>(R.id.txt_saving).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.txt_saving).text = item.saving
            }

            itemView.findViewById<TextView>(R.id.txt_price).text = currency + " " + item.finalPrice


//            if(item.other!=null && item.other.size>0){
//
//            }else{
//
//            }

            if (item.other.isNullOrEmpty()) {
                itemView.findViewById<TextView>(R.id.txt_aminities).visibility = View.GONE
                txtmoreaminities.visibility = View.GONE

            } else {
                itemView.findViewById<TextView>(R.id.txt_aminities).visibility = View.VISIBLE
                txtmoreaminities.visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.txt_aminities).text =
                    item.other.toString().replace("[", "").replace("]", "")

            }

            if (item.options.isNullOrEmpty()) {
                itemView.findViewById<TextView>(R.id.txt_option).visibility = View.GONE
                txtmoreoption.visibility = View.GONE

            } else {
                itemView.findViewById<TextView>(R.id.txt_option).visibility = View.VISIBLE
                txtmoreoption.visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.txt_option).text =
                    item.options.toString().replace("[", "").replace("]", "")

            }

            if (item.includeThisPrice.isNullOrEmpty()) {
                itemView.findViewById<TextView>(R.id.txt_included).visibility = View.GONE
                txtmoreinclude.visibility = View.GONE

            } else {
                itemView.findViewById<TextView>(R.id.txt_included).visibility = View.VISIBLE
                txtmoreinclude.visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.txt_included).text =
                    item.includeThisPrice.toString().replace("[", "").replace("]", "")

            }

            if (item.roomImages != null && item.roomImages.size - 1 >= 1) {
                txtmoreimg.visibility = View.VISIBLE
                val totalimg = item.roomImages.size - 1
                txtmoreimg.text = totalimg.toString() + " " + context.getString(R.string.str_more)

            } else {
                txtmoreimg.visibility = View.GONE

            }

        }

    }


}