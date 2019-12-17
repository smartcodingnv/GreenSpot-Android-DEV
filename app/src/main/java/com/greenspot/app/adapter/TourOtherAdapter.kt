package com.greenspot.app.adapter

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.model.PlaceSubItem
import java.util.*

class TourOtherAdapter(val context: FragmentActivity?) :

    RecyclerView.Adapter<TourOtherAdapter.FeatureViewHolder>() {
    private var data: List<PlaceSubItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_groupsubtext, parent, false)
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


    fun swapData(data: List<PlaceSubItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(
            item: PlaceSubItem,
            context: FragmentActivity?
        ) = with(itemView) {



            val title: TextView
            val txtdesc: TextView
            val laysubitem: LinearLayout

            title = itemView.findViewById(R.id.txt_listTitle)
            txtdesc = itemView.findViewById(R.id.txt_itemdesc)
            laysubitem = itemView.findViewById(R.id.lay_subitem)

            val expanded = item.isExpanded()
            laysubitem.visibility = if (expanded) View.VISIBLE else View.GONE

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


            title.setText(item.subtitleavailablity)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txtdesc.text = Html.fromHtml(item.subdesceavailablity,
                    Html.FROM_HTML_MODE_COMPACT)
            } else {
                txtdesc.text = Html.fromHtml(item.subdesceavailablity)
            }

       }
    }


}



