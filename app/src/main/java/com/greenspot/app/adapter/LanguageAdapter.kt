package com.greenspot.app.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.responce.LanguagesItem
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import com.greenspot.app.utils.Progress
import com.greenspot.app.utils.Utils
import java.util.*


class LanguageAdapter(
    val mContext: FragmentActivity?,
    val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<LanguageAdapter.ProductViewHolder>() {
    private var count: Int = 0
    private var data: List<LanguagesItem> = ArrayList()
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


        var selectLanguage = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTLANGNAME, "")


        if (selectLanguage.equals(data.get(position).languageName) ) {

            holder.rb_sort!!.setChecked(true)

        }



        holder.itemView.setOnClickListener {
            lastSelectedPosition = position
            notifyDataSetChanged()
            itemClickListener.recyclerViewListClicked(it, position, 3)
        }
        holder.rb_sort!!.setOnClickListener {
            lastSelectedPosition = position
            notifyDataSetChanged()
            itemClickListener.recyclerViewListClicked(it, position, 3)
        }




    }


    fun swapData(data: List<LanguagesItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        public var rb_sort: RadioButton? = null

        fun bind(
            item: LanguagesItem,
            onClick: FragmentActivity?
        ) = with(itemView) {
            rb_sort = itemView.findViewById<RadioButton>(R.id.rb_contry)
            itemView.findViewById<RadioButton>(R.id.rb_contry).text =
                item.languageName




        }
    }


    fun getItem(i: Int): LanguagesItem {

        return data.get(i)
    }

}


