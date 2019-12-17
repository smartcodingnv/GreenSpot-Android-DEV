package com.greenspot.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenspot.app.R
import com.greenspot.app.model.ItineraryImg
import kotlinx.android.synthetic.main.item_otherservice.view.*
import java.util.*


class ImgOtherServiceAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<ImgOtherServiceAdapter.FeatureViewHolder>() {

    private var data: List<ItineraryImg> = ArrayList()
    private val mContext: Context? = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(com.greenspot.app.R.layout.item_otherservice, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)

//        holder.itemView.setOnClickListener(View.OnClickListener {
//            val intent = Intent(mContext, ProductDetailsActivity::class.java);
//            intent.putExtra(AppConfig.EXTRA.PRODTITLE, data[position].itemname)
//            intent.putExtra(AppConfig.EXTRA.PRODCATEGORY, data[position].itemcategoruy)
//            mContext!!.startActivity(intent)
//
//        })


    }


    fun swapData(data: List<ItineraryImg>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: ItineraryImg,
            context: FragmentActivity?
        ) = with(itemView) {


            //            val requestBuilder = GlideToVectorYou
//                .init()
//                .with(context)
//                .requestBuilder
//
//
//            requestBuilder
//                .load(item.itineraryimg)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .apply(
//                    RequestOptions()
//                        .centerCrop()
//                )
//                .into(itemView.img_otherservice)


            itemView.findViewById<TextView>(R.id.txt_otherservice).text = item.otherservicename

            Glide.with(itemView)
                .load(item.otherserviceimg)
                .centerCrop()
                .into(itemView.img_otherservice)



        }
    }


}