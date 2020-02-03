package com.greenspot.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.greenspot.app.R
import com.greenspot.app.responce.bookinginfo.AllUsersInfoItem
import java.util.*

class BookingAllpersonTitleAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<BookingAllpersonTitleAdapter.FeatureViewHolder>() {

    private var data: List<AllUsersInfoItem> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_allperson, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)
        val aminites = data[position]


        val personno = position + 1

        holder.title.text = context!!.getString(R.string.data_person)+" " + personno.toString()



        holder.itemView.setOnClickListener(View.OnClickListener {
            val expanded = aminites.isExpanded()
            aminites.setExpanded(!expanded)
            notifyItemChanged(position)
        })

    }


    fun swapData(data: List<AllUsersInfoItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public lateinit var title: TextView

        fun bind(
            item: AllUsersInfoItem,
            context: FragmentActivity?
        ) = with(itemView) {


            val laysubitem: LinearLayout

            title = itemView.findViewById(R.id.txt_listTitle)
            laysubitem = itemView.findViewById(R.id.lay_subitem)


            val expanded = item.isExpanded()
            laysubitem.visibility = if (expanded) View.VISIBLE else View.GONE

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




            if(item.firstName.isNotEmpty() && item.lastName.isNotEmpty()){
                itemView.findViewById<TextView>(R.id.txt_name).text = item.title+ ". " + item.firstName + " " +item.lastName
            }else{
                itemView.findViewById<TextView>(R.id.txt_name).text = context.getString(R.string.res_na)
            }

            if(item.dob.isNotEmpty()){
                itemView.findViewById<TextView>(R.id.txt_dob).text = item.dob
            }else{
                itemView.findViewById<TextView>(R.id.txt_dob).text = context.getString(R.string.res_na)
            }

            if(item.gender.isNotEmpty()){
                if(item.gender.equals("M"))
                {
                    itemView.findViewById<TextView>(R.id.txt_gender).text ="Male"
                }else if(item.gender.equals("F")){
                    itemView.findViewById<TextView>(R.id.txt_gender).text ="Female"
                }
            }else{
                itemView.findViewById<TextView>(R.id.txt_gender).text = context.getString(R.string.res_na)
            }

            if(item.gender.isNotEmpty()){
                itemView.findViewById<TextView>(R.id.txt_email).text = item.email
            }else{
                itemView.findViewById<TextView>(R.id.txt_email).text = context.getString(R.string.res_na)
            }


            if(item.gender.isNotEmpty()){
                itemView.findViewById<TextView>(R.id.txt_contactno).text = item.contactNumber
            }else{
                itemView.findViewById<TextView>(R.id.txt_contactno).text = context.getString(R.string.res_na)
            }



        }
    }


}



