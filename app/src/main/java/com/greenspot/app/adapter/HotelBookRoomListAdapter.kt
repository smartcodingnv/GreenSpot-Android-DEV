package com.greenspot.app.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greenspot.app.MyApp
import com.greenspot.app.R
import com.greenspot.app.activity.HotelRoomTabActivity
import com.greenspot.app.interfaces.ItemClickListener
import com.greenspot.app.responce.bookinginfohotel.RoomInfoItem
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.Common
import com.greenspot.app.utils.PreferenceHelper
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView
import hk.ids.gws.android.sclick.SClick
import it.sephiroth.android.library.imagezoom.ImageViewTouch
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.layoutInflater
import java.util.*


class HotelBookRoomListAdapter(
    val context: FragmentActivity?

) :
    RecyclerView.Adapter<HotelBookRoomListAdapter.FeatureViewHolder>() {

    private var data: List<RoomInfoItem> = ArrayList()
    private val mContext: Context? = context

    private var helper: PreferenceHelper? =
        PreferenceHelper(this.context!!, AppConfig.PREFERENCE.PREF_FILE)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_roomlist, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {

        holder.bind(data[position], context)


        holder.txtmoreaminities.setOnClickListener(View.OnClickListener {

            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener
            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.ROOMMASTERID, data[position].masterId)
            helper!!.ApplyPref()
            val intent = Intent(mContext, HotelRoomTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 0)
                .putExtra(AppConfig.EXTRA.ROOMTABFLAG, 2)
            mContext!!.startActivity(intent)

        })



        holder.imgRoom.setOnClickListener(View.OnClickListener {

            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener
            showLargeImage(context = mContext!!, url = data[position].hotelRoomImage, type = "I")

//            helper!!.initPref()
//            helper!!.SaveStringPref(AppConfig.PREFERENCE.ROOMMASTERID, data[position].masterId)
//            helper!!.ApplyPref()
//            val intent = Intent(mContext, HotelRoomTabActivity::class.java)
//                .putExtra(AppConfig.EXTRA.TABCHECK, 0)
//                .putExtra(AppConfig.EXTRA.ROOMTABFLAG, 2)
//            mContext!!.startActivity(intent)

        })

        holder.txtmoreoption.setOnClickListener(View.OnClickListener {

            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener
            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.ROOMMASTERID, data[position].masterId)
            helper!!.ApplyPref()
            val intent = Intent(mContext, HotelRoomTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 1)
                .putExtra(AppConfig.EXTRA.ROOMTABFLAG, 2)
            mContext!!.startActivity(intent)

        })


        holder.txtmoreinclude.setOnClickListener(View.OnClickListener {
            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener
            helper!!.initPref()
            helper!!.SaveStringPref(AppConfig.PREFERENCE.ROOMMASTERID, data[position].masterId)
            helper!!.ApplyPref()
            val intent = Intent(mContext, HotelRoomTabActivity::class.java)
                .putExtra(AppConfig.EXTRA.TABCHECK, 2)
                .putExtra(AppConfig.EXTRA.ROOMTABFLAG, 2)
            mContext!!.startActivity(intent)

        })


//        holder.itemView.setOnClickListener(View.OnClickListener {
//            if (!SClick.check(SClick.BUTTON_CLICK)) return@OnClickListener;
//            helper!!.initPref()
//            helper!!.SaveStringPref(AppConfig.PREFERENCE.PLACEID, data[position].masterId)
//            helper!!.ApplyPref()
//            val intent = Intent(mContext, HotelDetailsActivity::class.java)
//                .putExtra(AppConfig.EXTRA.HOTELID, data[position].masterId)
//                .putExtra(AppConfig.EXTRA.HOTELNAME, data[position].);
//            mContext!!.startActivity(intent)
//
//        })

    }


    fun swapData(data: List<RoomInfoItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public lateinit var btnselectRoom: Button
        public lateinit var txtmoreaminities: TextView
        public lateinit var txtmoreoption: TextView
        public lateinit var txtmoreinclude: TextView
        public lateinit var txtmoreimg: TextView
        public lateinit var imgRoom: ImageView

        fun bind(
            item: RoomInfoItem,
            context: FragmentActivity?
        ) = with(itemView) {

            //            Glide.with(itemView)
//                .load(item.itemimg)
//
//                .into(itemView.img_itemm)
            btnselectRoom = itemView.findViewById(R.id.btn_select)
            txtmoreaminities = itemView.findViewById(R.id.txt_moreaminities)
            txtmoreoption = itemView.findViewById(R.id.txt_moreoption)
            txtmoreinclude = itemView.findViewById(R.id.txt_moreinclude)
            txtmoreimg = itemView.findViewById(R.id.txt_moreimg)
            imgRoom = itemView.findViewById(R.id.img_room)

            txtmoreimg.visibility = View.GONE

            itemView.findViewById<FrameLayout>(R.id.lay_baseprice).visibility = View.GONE

            val helperlang: PreferenceHelper? =
                PreferenceHelper(context!!, AppConfig.PREFERENCE.PREF_FILE_LANG)
            val currency = helperlang!!.LoadStringPref(AppConfig.PREFERENCE.SELECTCURRENCYNAME, "")


            Glide.with(itemView)
                .load(item.hotelRoomImage)
                .dontTransform()
                .centerCrop()
                .placeholder(R.drawable.travel)
                .into(imgRoom)



            btnselectRoom.visibility = View.GONE

            itemView.findViewById<TextView>(R.id.txt_roomtype).text = item.roomType

            itemView.findViewById<TextView>(R.id.txt_offer).text =  context.getString(R.string.str_totalroomselected)+ " "+item.totalRoomSelected
         
            if (item.extraBedCharge.isEmpty()) {
                itemView.findViewById<TextView>(R.id.txt_saving).visibility = View.GONE
            } else {
                itemView.findViewById<TextView>(R.id.txt_saving).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.txt_saving).text = context.getString(R.string.str_extrabedcharge) +" " +currency + " " +item.extraBedCharge
            }

            itemView.findViewById<TextView>(R.id.txt_price).text = currency + " " + item.pricePerNight


//            if(item.other!=null && item.other.size>0){
//
//            }else{
//
//            }

            if (item.other.isNullOrEmpty()) {
                itemView.findViewById<TextView>(R.id.txt_aminities).visibility = View.GONE
                txtmoreaminities.visibility = View.GONE

            } else {
                itemView.findViewById<TextView>(R.id.txt_aminities).visibility = View.VISIBLE
                txtmoreaminities.visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.txt_aminities).text =
                    item.other.toString().replace("[", "").replace("]", "")

            }

            if (item.options.isNullOrEmpty()) {
                itemView.findViewById<TextView>(R.id.txt_option).visibility = View.GONE
                txtmoreoption.visibility = View.GONE

            } else {
                itemView.findViewById<TextView>(R.id.txt_option).visibility = View.VISIBLE
                txtmoreoption.visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.txt_option).text =
                    item.options.toString().replace("[", "").replace("]", "")

            }

            if (item.includeThisPrice.isNullOrEmpty()) {
                itemView.findViewById<TextView>(R.id.txt_included).visibility = View.GONE
                txtmoreinclude.visibility = View.GONE

            } else {
                itemView.findViewById<TextView>(R.id.txt_included).visibility = View.VISIBLE
                txtmoreinclude.visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.txt_included).text =
                    item.includeThisPrice.toString().replace("[", "").replace("]", "")

            }



        }

    }

    fun showLargeImage(
        context: Context,
        url: String? = null,
        bitmap: Bitmap? = null,
        type: String
    ) {
        val dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(context.layoutInflater.inflate(R.layout.layout_large_video, null))

        val btnClose = dialog.findViewById<ImageButton>(R.id.btnIvClose)
        val ivPreview = dialog.findViewById<ImageViewTouch>(R.id.iv_preview_image)
        val youtubeView = dialog.findViewById<YouTubePlayerView>(R.id.youtubeView)
        var youTubePlayer: YouTubePlayer? = null


        if (bitmap != null) {
            youtubeView.visibility = View.VISIBLE
            ivPreview.imageBitmap = bitmap
        }

        if (type == "I" && bitmap == null) {
            youtubeView.visibility = View.GONE
            btnClose.visibility = View.VISIBLE
            ivPreview.visibility = View.VISIBLE
            Glide.with(context)
                .load(url)
                .dontTransform()
                .into(ivPreview)
        } else if (type == "V") {
            ivPreview.visibility = View.GONE
            youtubeView.visibility = View.VISIBLE
            btnClose.visibility = View.VISIBLE
            youtubeView.initialize({
                it.addListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady() {
                        youTubePlayer = it
                        it.loadVideo(Common.getYoutubeId(url!!)!!, 0f)
                    }
                })
            }, true)
            youtubeView.enterFullScreen()
            youtubeView.addFullScreenListener(object : YouTubePlayerFullScreenListener {
                override fun onYouTubePlayerEnterFullScreen() {}

                override fun onYouTubePlayerExitFullScreen() {
                    dialog.dismiss()
                }
            })
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
            MyApp.updateLanguage(context)
        }

        dialog.setOnDismissListener {
            youTubePlayer?.pause()
            youTubePlayer = null
            MyApp.updateLanguage(context)
        }

        dialog.show()
    }


}