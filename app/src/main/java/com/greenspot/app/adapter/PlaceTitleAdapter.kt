package com.greenspot.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.model.PlaceAvaliblityTitle
import com.greenspot.app.utils.Common
import java.util.*

class PlaceTitleAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<PlaceTitleAdapter.FeatureViewHolder>() {

    private var data: List<PlaceAvaliblityTitle> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_group, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)
        val movie = data[position]


        holder.itemView.setOnClickListener(View.OnClickListener {
            val expanded = movie.isExpanded()
            movie.setExpanded(!expanded)
            notifyItemChanged(position)
        })

    }


    fun swapData(data: List<PlaceAvaliblityTitle>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(
            item: PlaceAvaliblityTitle,
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
                        context!!, R.drawable.drawable_collops
                    ), null
                )
            } else {

                title.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, ContextCompat.getDrawable(
                        context!!, R.drawable.drawable_add
                    ), null
                )
            }



            title.setText(item.titleavailablity)

            val placeDetailsAdapter = PlaceDetailsAdapter(context)
            Common.setVerticalRecyclerView(context!!, rvPlacedetails)
            placeDetailsAdapter.swapData(item.subItem)
            rvPlacedetails.adapter = placeDetailsAdapter
        }
    }


}



