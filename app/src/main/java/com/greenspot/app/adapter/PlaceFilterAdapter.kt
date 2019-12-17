package com.greenspot.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.responce.DataItemFilterTitle
import java.util.*


class PlaceFilterAdapter(
    val context: FragmentActivity?,
    val itemClickListener: ItemClickListener,
    val checkFilter: Int
) :
    RecyclerView.Adapter<PlaceFilterAdapter.FeatureViewHolder>() {


    var list: List<String> = ArrayList()

    private var mContext = context
    private var row_index: Int = 0
    private var checkFiltervalue: Int = checkFilter
    private var data: List<DataItemFilterTitle> = ArrayList()
    var lstselecteditem = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_filterlist_title, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)


//        row_index = position;
//        notifyDataSetChanged()


        var x = 0

        for (name in data[position].records!!) {

            if (name.check == true) {

                x++
            } else if (name.check == false) {

//                data[position].lstselecteditem.removeAt(data[position].lstselecteditem.indexOf(name.masterId))
//                lstselecteditem.removeAt(name.masterId)
            }
            //list ne under add kari ne data api call karvi
        }

        if (x == 0) {
            holder.count!!.visibility = View.GONE
        } else {
            if (checkFiltervalue == 2 && position == 0) {
                holder.count!!.visibility = View.GONE
            } else {
                holder.count!!.visibility = View.VISIBLE
                holder.count!!.setText(x.toString())
            }

        }

        lstselecteditem.clear()
        holder.itemView.setOnClickListener {

            row_index = position;
            notifyDataSetChanged()
            itemClickListener.recyclerViewListClicked(it, position, 1)
        }




        if (row_index == position) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    this.mContext!!,
                    R.color.white
                )
            );
            holder.title!!.setTextColor(ContextCompat.getColor(this.mContext!!, R.color.black));
        } else {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    this.mContext!!,
                    R.color.lightgray
                )
            );
            holder.title!!.setTextColor(ContextCompat.getColor(this.mContext!!, R.color.gray));
        }


    }


    fun getSelectedItem(): ArrayList<String> {
        return lstselecteditem
    }

    fun swapData(data: List<DataItemFilterTitle>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var title: TextView? = null
        var count: TextView? = null

        fun bind(
            item: DataItemFilterTitle,
            context: FragmentActivity?
        ) = with(itemView) {


            title = itemView.findViewById(R.id.txt_listTitle)
            count = itemView.findViewById(R.id.txt_filtercount)
            itemView.findViewById<TextView>(R.id.txt_listTitle).text = item.title
//            itemView.findViewById<TextView>(R.id.txt_filtercount).text = item.count


//            val expanded = item.isExpanded()
//            rvPlacedetails.visibility = if (expanded) View.VISIBLE else View.GONE

//            if (expanded) {
//
//                title.setCompoundDrawablesWithIntrinsicBounds(
//                    null, null, ContextCompat.getDrawable(
//                        context!!, R.drawable.drawable_collops
//                    ), null
//                )
//            } else {
//
//                title.setCompoundDrawablesWithIntrinsicBounds(
//                    null, null, ContextCompat.getDrawable(
//                        context!!, R.drawable.drawable_add
//                    ), null
//                )
//            }
//


        }
    }


}




