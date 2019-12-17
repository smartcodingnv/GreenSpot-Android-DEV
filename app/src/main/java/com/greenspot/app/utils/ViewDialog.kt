package com.greenspot.app.utils


import android.app.Activity
import android.app.Dialog
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cunoraz.gifview.library.GifView
import com.greenspot.app.R


class ViewDialog //..we need the context else we can not create the dialog so get context in constructor
    (var activity: Activity) {
    private var gifView1: GifView? = null
    var dialog: Dialog? = null
    fun showDialog() {
        dialog = Dialog(activity)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog!!.setCancelable(false)

        dialog!!.setContentView(R.layout.dialog_progress)
        dialog!!.window?.setBackgroundDrawableResource(R.color.fiftyblack)
        dialog!!.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog!!.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )


         gifView1 = dialog!!.findViewById(R.id.gif1) as GifView

        gifView1!!.gifResource = R.drawable.flight_gif

        gifView1!!.play();

//        Glide.with(activity)
//            .asGif()
//            .load(R.raw.flight_gif_greennn)
//            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//            .into(gifImageView);

//        Glide.with(activity)
//            .load("")
//            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
//            .into(DrawableImageViewTarget(gifImageView))

        dialog!!.show()


    }

    fun hideDialog() {
        gifView1!!.pause()

        dialog?.dismiss()
    }

}