package com.greenspot.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.responce.recreationdetails.StayItem
import com.greenspot.app.utils.Common
import java.util.*

class PlaceStayTitleAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<PlaceStayTitleAdapter.FeatureViewHolder>() {

    private var data: List<StayItem> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_group, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)
        val aminites = data[position]


        holder.itemView.setOnClickListener(View.OnClickListener {
            val expanded = aminites.isExpanded()
            aminites.setExpanded(!expanded)
            notifyItemChanged(position)
        })

    }


    fun swapData(data: List<StayItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(
            item: StayItem,
            context: FragmentActivity?
        ) = with(itemView) {


            val title: TextView
            val rvPlacedetails: RecyclerView

            title = itemView.findViewById(R.id.txt_listTitle)
            rvPlacedetails = itemView.findViewById(R.id.rv_placedetaillist)

            val expanded = item.isExpanded()
            rvPlacedetails.visibility = if (expanded) View.VISIBLE else View.GONE

            if (expanded) {

                title.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, ContextCompat.getDrawable(
                        context!!, R.drawable.ic_ncollops
                    ), null
                )
            } else {

                title.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, ContextCompat.getDrawable(
                        context!!, R.drawable.ic_nexpand
                    ), null
                )
            }



            title.setText(item.title)

            val placeDetailsAdapter = PlaceAminitesDetailsAdapter(context)
            Common.setVerticalRecyclerView(context, rvPlacedetails)
            placeDetailsAdapter.swapData(item.records!!)
            rvPlacedetails.adapter = placeDetailsAdapter
        }
    }


}



