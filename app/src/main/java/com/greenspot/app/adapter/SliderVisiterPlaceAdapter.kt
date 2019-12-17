package com.greenspot.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenspot.app.R
import com.greenspot.app.model.GalleryImgList

import com.greenspot.app.responce.recreationdetails.GalleryImagesItem
import kotlinx.android.synthetic.main.content_place_details.*

import kotlinx.android.synthetic.main.item_placeimg.view.*
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class SliderVisiterPlaceAdapter(val context: FragmentActivity?) :
    RecyclerView.Adapter<SliderVisiterPlaceAdapter.FeatureViewHolder>() {

    private var data: List<GalleryImgList> = ArrayList()
    private val mContext: Context? = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_placeimg, parent, false)
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


    fun swapData(data: List<GalleryImgList>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: GalleryImgList,
            context: FragmentActivity?
        ) = with(itemView) {




            if (item.flag == 1) {
                val query: String = URL(item.item_name).getQuery()
                val param = query.split("&").toTypedArray()
                var id: String? = null
                for (row in param) {
                    val param1 = row.split("=").toTypedArray()
                    if (param1[0] == "v") {
                        id = param1[1]
                    }
                }

                val imgURl = "http://img.youtube.com/vi/" + id + "/0.jpg"
                Glide.with(itemView)
                    .load(imgURl)
                    .placeholder(R.drawable.travel)
                    .centerCrop()
                    .into(itemView.img_item)
            } else {


                Glide.with(itemView)
                    .load(item.item_name)
                    .placeholder(R.drawable.travel)
                    .centerCrop()
                    .into(itemView.img_item)
            }




        }
    }




}