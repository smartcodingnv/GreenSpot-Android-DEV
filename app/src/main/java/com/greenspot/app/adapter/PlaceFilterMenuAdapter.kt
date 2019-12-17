package com.greenspot.app.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.responce.FilterRecordsItem
import java.util.*

class PlaceFilterMenuAdapter(
    val context: FragmentActivity?,
    val itemClickListener: ItemClickListener,
    val checkFilter: Int
) :
    RecyclerView.Adapter<PlaceFilterMenuAdapter.FeatureViewHolder>() {


    private var data: List<FilterRecordsItem> = ArrayList()

    var lstselecteditem = ArrayList<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_filtermenu, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {


        holder.bind(data[position], context)


        holder.filteriTem.isChecked = data[position].check
//        lstselecteditem.clear()

        if (data[position].check == true) {
            holder.filteriTem.isChecked = true
            lstselecteditem.add(data[position].masterId)
        } else {
            holder.filteriTem.isChecked = false
            lstselecteditem.remove(data[position].masterId)
        }

        holder.filteriTem.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                Log.e("check state", " " + isChecked)
                data[position].check = true
                lstselecteditem.add(data[position].masterId)

            } else {

                data[position].check = false
                lstselecteditem.remove(data[position].masterId)

//                lstselecteditem.remove(data[position].masterId)

            }

            itemClickListener.recyclerViewListClicked(buttonView, position, 2)
        }
//        holder.itemView.setOnClickListener(View.OnClickListener {
//
//
//
//        })

    }

    fun getSelectedItem(): ArrayList<String> {
        return lstselecteditem
    }


//    fun getExter(): List<String>? {
//        return listselectedID
//    }

    fun swapData(data: List<FilterRecordsItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var filteriTem: CheckBox

        fun bind(
            item: FilterRecordsItem,
            context: FragmentActivity?
        ) = with(itemView) {


            filteriTem = itemView.findViewById(R.id.chk_filter)


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

            if(item.check==true){
                filteriTem.isChecked = true
            }else{
                filteriTem.isChecked = false
            }

            filteriTem.setText(item.name)


        }
    }


}



