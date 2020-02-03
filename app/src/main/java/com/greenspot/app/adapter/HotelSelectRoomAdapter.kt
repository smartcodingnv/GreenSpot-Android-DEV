package com.greenspot.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.model.ListSelectRoom
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import com.greenspot.app.utils.Progress
import com.greenspot.app.utils.Utils
import com.makeramen.roundedimageview.RoundedImageView
import hk.ids.gws.android.sclick.SClick


class HotelSelectRoomAdapter(
    val mContext: FragmentActivity?,
    val itemClickListener: ItemClickListener,
    val flag: Int
) : RecyclerView.Adapter<HotelSelectRoomAdapter.ProductViewHolder>() {
    private var count: Int = 0
    private var data: ArrayList<ListSelectRoom> = ArrayList()
    private var lastSelectedPosition = -1


    private var progress: Progress? = Progress(this.mContext!!)
    private var utils: Utils? = Utils(this.mContext!!)
    private var helperlang: PreferenceHelper? =
        PreferenceHelper(this.mContext!!, AppConfig.PREFERENCE.PREF_FILE_LANG)

//    fun SortAdapter(
//        activity: HomeFragment,
//        catagoriesEntities: ProductSortList,
//        clickListener: ItemClickListener
//    ) {
//        this.data = listOf(catagoriesEntities)
//        this.mContext = activity
//        this.itemClickListener = clickListener
//
//        //        progress = new Progress(mContext);
//        //        helper = new PreferenceHelper(mContext, AppConfig.PREFERENCE.PREF_FILE);
//
//
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_selectroom, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        holder.bind(data[position], mContext)
        if(flag==2){
            holder.ivDeleteroom.visibility = View.GONE
        }else{
            holder.ivDeleteroom.visibility = View.VISIBLE
        }

        holder.ivDeleteroom.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
            removeItem(position)
            itemClickListener.recyclerViewListClicked(it, position, 2)

        })

    }


    fun swapData(data: ArrayList<ListSelectRoom>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public lateinit var ivDeleteroom: RoundedImageView
        fun bind(
            item: ListSelectRoom,
            onClick: FragmentActivity?
        ) = with(itemView) {

            

            val helperlang: PreferenceHelper? =
                PreferenceHelper(context!!, AppConfig.PREFERENCE.PREF_FILE_LANG)
            val currency = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")

            ivDeleteroom = itemView.findViewById(R.id.iv_delteroom)
            itemView.findViewById<TextView>(R.id.txt_roomtype).text = item.room
            itemView.findViewById<TextView>(R.id.txt_noofperson).text =
                item.adult + " " + context.getString(R.string.txt_adults) + ", " +
                        item.child + " " + context.getString(R.string.txt_child)
            itemView.findViewById<TextView>(R.id.txt_roomprice).text =
                currency + " " + item.roomPrice

        }
    }

    private fun removeItem(position: Int) {
        this.data.removeAt(position)
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.data.size);
    }
}


