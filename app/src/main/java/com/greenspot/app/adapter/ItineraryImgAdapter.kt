package com.greenspot.app.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenspot.app.R
import com.greenspot.app.model.ItineraryImg
import com.greenspot.app.responce.tourdetail.GalleryImagesItem
import hk.ids.gws.android.sclick.SClick
import kotlinx.android.synthetic.main.item_itenerayimg.view.*
import kotlinx.android.synthetic.main.item_placeimg.view.*
import java.util.*


class ItineraryImgAdapter(
    val context: FragmentActivity?

) :
    RecyclerView.Adapter<ItineraryImgAdapter.FeatureViewHolder>() {

    private var count: Int = 0
    private var data: List<GalleryImagesItem> = ArrayList()
    private val mContext: Context? = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(com.greenspot.app.R.layout.item_itenerayimg, parent, false)
        )
    }

    override fun getItemCount() = 1

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)



        Log.e("check size", "" + data.size)
        if(data.size==0||data.size==1){
            holder.lay_nexttt!!.visibility = View.GONE
            holder.lay_prevvv!!.visibility = View.GONE
        }else{
            holder.lay_nexttt!!.visibility = View.VISIBLE
            holder.lay_prevvv!!.visibility = View.VISIBLE
        }

        count = position
        holder.lay_nexttt!!.setOnClickListener(View.OnClickListener {

            if (count == data.size - 1) {
                return@OnClickListener
            }
            count++

            Glide.with(this.mContext!!)
                .load(data[count].imageName)
                .placeholder(R.drawable.travel)
                .centerCrop()
                .into(holder.lay_imggg!!)
            Log.e("count", "" + count)
        })

        holder.lay_prevvv!!.setOnClickListener(View.OnClickListener {

            if (count == 0) {
                return@OnClickListener

            }
            count--
            Glide.with(this.mContext!!)
                .load(data[count].imageName)
                .placeholder(R.drawable.travel)
                .centerCrop()
                .into(holder.lay_imggg!!)
            Log.e("count", "" + count)

        })


//
//        holder.itemView.setOnClickListener(View.OnClickListener {
//            val expanded = movie.isExpanded()
//            movie.setExpanded(!expanded)
//            notifyItemChanged(position)
//        })

//        holder.itemView.setOnClickListener {
//
////            notifyDataSetChanged()
//            itemClickListener.recyclerViewListClicked(it, position)
//        }


    }


    fun swapData(data: List<GalleryImagesItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        public var lay_nexttt: LinearLayout? = null
        public var lay_prevvv: LinearLayout? = null
        public var lay_imggg: ImageView? = null


        fun bind(
            item: GalleryImagesItem,
            context: FragmentActivity?
        ) = with(itemView) {




            lay_nexttt = itemView.findViewById<LinearLayout>(R.id.lay_next)
            lay_prevvv = itemView.findViewById<LinearLayout>(R.id.lay_prev)
            lay_imggg = itemView.findViewById<ImageView>(R.id.img_itinearary)

            Glide.with(itemView)
                .load(item.imageName)
                .placeholder(R.drawable.travel)
                .centerCrop()
                .into(itemView.img_itinearary)
        }
    }


}



