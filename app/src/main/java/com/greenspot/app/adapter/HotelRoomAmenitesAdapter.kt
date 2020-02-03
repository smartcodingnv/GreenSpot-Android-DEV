package com.greenspot.app.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.responce.CountriesItem
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import com.greenspot.app.utils.Progress
import com.greenspot.app.utils.Utils
import java.util.*
import kotlin.concurrent.thread


class HotelRoomAmenitesAdapter(
    val mContext: FragmentActivity?
) : RecyclerView.Adapter<HotelRoomAmenitesAdapter.ProductViewHolder>() {
    private var count: Int = 0
    private var data: List<String> = ArrayList()
    private var lastSelectedPosition = -1


    private var progress: Progress? = Progress(this!!.mContext!!)
    private var utils: Utils? = Utils(this!!.mContext!!)
    private var helperlang: PreferenceHelper? =
        PreferenceHelper(this!!.mContext!!, AppConfig.PREFERENCE.PREF_FILE_LANG)

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
                .inflate(R.layout.item_roomaminites, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        holder.bind(data[position], mContext)


    }


    fun swapData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            item: String,
            onClick: FragmentActivity?
        ) = with(itemView) {

            itemView.findViewById<TextView>(R.id.txt_itemaminites).text = item.toString()

        }
    }

}


