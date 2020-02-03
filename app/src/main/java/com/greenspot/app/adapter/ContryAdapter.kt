package com.greenspot.app.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
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


class ContryAdapter(
    val mContext: FragmentActivity?,
    val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<ContryAdapter.ProductViewHolder>() {
    private var count: Int = 0
    private var data: List<CountriesItem> = ArrayList()
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
                .inflate(R.layout.item_dialog, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        holder.bind(data[position], mContext)

        var selectcontryName = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCONTRY, "")


        if (selectcontryName.equals(data.get(position).name) ) {

            holder.rb_sort!!.setChecked(true)

        }

        holder.itemView.setOnClickListener {
            lastSelectedPosition = position
            notifyDataSetChanged()
            itemClickListener.recyclerViewListClicked(it, position,1)
        }
        holder.rb_sort!!.setOnClickListener {
            lastSelectedPosition = position
            notifyDataSetChanged()
            itemClickListener.recyclerViewListClicked(it, position,1)
        }



    }


    fun swapData(data: List<CountriesItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        public var rb_sort: RadioButton? = null
        public var rb_group: RadioGroup? = null

        fun bind(
            item: CountriesItem,
            onClick: FragmentActivity?
        ) = with(itemView) {
            rb_sort = itemView.findViewById<RadioButton>(R.id.rb_contry)
//            rb_group = itemView.findViewById<RadioGroup>(R.id.rb_gorup)
            itemView.findViewById<RadioButton>(R.id.rb_contry).text =
                item.name


        }
    }


    fun getItem(i: Int): CountriesItem {

        return data.get(i)
    }


}


