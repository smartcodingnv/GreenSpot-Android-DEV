package com.greenspot.app.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.responce.SortingItem
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import com.greenspot.app.utils.Progress
import com.greenspot.app.utils.Utils
import java.util.*


class PlaceSortAdapter(
    val mContext: FragmentActivity?,
    val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<PlaceSortAdapter.ProductViewHolder>() {
    private var selectplaceSort: String? = ""
    private var count: Int = 0
    private var data: List<SortingItem> = ArrayList()
    private var lastSelectedPosition = -1


    private var progress: Progress? = Progress(this!!.mContext!!)
    private var utils: Utils? = Utils(this!!.mContext!!)
    private var helper: PreferenceHelper? =
        PreferenceHelper(this!!.mContext!!, AppConfig.PREFERENCE.PREF_FILE)

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

        selectplaceSort = helper!!.LoadStringPref(AppConfig.PREFERENCE.SELECTSORTTITAL, "")
        Log.e(TAG, "" + selectplaceSort)


        if (selectplaceSort.equals(data[position].title)) {

            Log.e(TAG, "" + position)
            holder.rb_sort!!.setChecked(true)

        }else{
            holder.rb_sort!!.setChecked(lastSelectedPosition == position);
        }








        holder.itemView.setOnClickListener {
            helper?.initPref()
            helper?.SaveStringPref(AppConfig.PREFERENCE.SELECTSORTTITAL, "")
            helper?.ApplyPref()
            selectplaceSort =""
            lastSelectedPosition = position
            notifyDataSetChanged()
            itemClickListener.recyclerViewListClicked(it, position, 1)
        }
        holder.rb_sort!!.setOnClickListener {
            helper?.initPref()
            helper?.SaveStringPref(AppConfig.PREFERENCE.SELECTSORTTITAL, "")
            helper?.ApplyPref()
            selectplaceSort =""
            lastSelectedPosition = position
            notifyDataSetChanged()
            itemClickListener.recyclerViewListClicked(it, position, 1)
        }


    }


    fun swapData(data: List<SortingItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var rb_sort: RadioButton? = null

        fun bind(
            item: SortingItem,
            onClick: FragmentActivity?
        ) = with(itemView) {
            rb_sort = itemView.findViewById<RadioButton>(R.id.rb_contry)
            itemView.findViewById<RadioButton>(R.id.rb_contry).text = item.title


        }
    }


}


