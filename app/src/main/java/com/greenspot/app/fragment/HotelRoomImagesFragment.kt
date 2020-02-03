package com.greenspot.app.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.greenspot.app.MyApp
import com.greenspot.app.R
import com.greenspot.app.adapter.SliderHotelRoomAdapter
import com.greenspot.app.responce.hoteldetails.ResponceHotelDetails
import com.greenspot.app.responce.hoteldetails.RoomImagesItem
import com.greenspot.app.utils.*
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView
import it.sephiroth.android.library.imagezoom.ImageViewTouch
import kotlinx.android.synthetic.main.content_hotel_details.*
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.layoutInflater


class HotelRoomImagesFragment : Fragment() {


    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    var count: Int = 0

    private var imgURl: String? = ""
    private var gallaryImageList: ArrayList<RoomImagesItem> = ArrayList()
    private var roomId: String? = ""

    fun newInstance(): HotelRoomImagesFragment {

        val args = Bundle()
        val fragment = HotelRoomImagesFragment()
        fragment.setArguments(args)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        progress = Progress(this.activity!!)
        utils = Utils(this.activity!!)
        helper = PreferenceHelper(this.activity!!, AppConfig.PREFERENCE.PREF_FILE)
        return inflater.inflate(R.layout.fragment_hotel_room_images, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val gson = Gson()
        val hoteldetails =
            gson.fromJson(
                helper!!.LoadStringPref(AppConfig.PREFERENCE.HOTELDETAILSRESPONCE, ""),
                ResponceHotelDetails::class.java
            )



        roomId = helper!!.LoadStringPref(AppConfig.PREFERENCE.ROOMMASTERID, "")

        for (room in hoteldetails.data.rooms!!) {

            if (room.masterId.equals(roomId)) {

                gallaryImageList = room.roomImages as ArrayList<RoomImagesItem>
                gallaryImageList.sortBy {
                    it.orderId
                }


            }
        }

        if(gallaryImageList.size==1){
            lay_next.visibility = View.GONE
            lay_prev.visibility = View.GONE

        }else{
            lay_next.visibility = View.VISIBLE
            lay_prev.visibility = View.VISIBLE

        }

        imgURl = gallaryImageList.get(0).imageName
        Glide.with(this.activity!!)
            .load(imgURl)
            .placeholder(R.drawable.travel)
            .into(img_hotel)


        setVisitorplace()

        img_hotel.setOnClickListener(View.OnClickListener {
            if (imgURl!!.isEmpty()) {

                return@OnClickListener
            }
            showLargeImage(context = this.activity!!, url = imgURl, type = "I")

        })

        lay_prev.setOnClickListener(View.OnClickListener {
            if (count == 0) {
                return@OnClickListener
            }
            count--
            imgURl = gallaryImageList[count].imageName
            Glide.with(this)
                .load(imgURl)
                .dontTransform()
                .into(img_hotel)

            rv_hotelplaceimg.scrollToPosition(count)
        })

        lay_next.setOnClickListener(View.OnClickListener {
            val position = gallaryImageList.size - 1
            if (position == count) {
                return@OnClickListener
            }
            count++
            imgURl = gallaryImageList[count].imageName

            Glide.with(this)
                .load(imgURl)

                .into(img_hotel)

            rv_hotelplaceimg.scrollToPosition(count)
        })

        rv_hotelplaceimg.affectOnItemClicks() { position, view ->

            count = position

            imgURl = gallaryImageList[position].imageName

            Glide.with(this)
                .load(imgURl)

                .into(img_hotel)


        }


    }

    private fun setVisitorplace() {

        val sliderTourAdapter = SliderHotelRoomAdapter(activity)
        Common.setHorizontalRecyclerView(activity!!, rv_hotelplaceimg)
        sliderTourAdapter.swapData(gallaryImageList)
        rv_hotelplaceimg.adapter = sliderTourAdapter
    }

    fun RecyclerView.affectOnItemClicks(
        onClick: ((position: Int, view: View) -> Unit)? = null,
        onLongClick: ((position: Int, view: View) -> Unit)? = null
    ) {
        this.addOnChildAttachStateChangeListener(
            RecyclerItemClickListener(
                this,
                onClick,
                onLongClick
            )
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

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
