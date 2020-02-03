package com.greenspot.app.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.greenspot.app.MyApp
import com.greenspot.app.R
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {
    private lateinit var timer: CountDownTimer
    private val SPLASH_DISPLAY_LENGTH = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
        MyApp.updateLanguage(this)

        img_splash.setBackground(ContextCompat.getDrawable(this@SplashActivity, R.drawable.img1))
//        Handler().postDelayed({
//            /* Create an Intent that will start the Menu-Activity. */
//
//
//        }, SPLASH_DISPLAY_LENGTH.toLong())

        timer = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                Log.e("timeee", " " + millisUntilFinished)

//                if (millisUntilFinished <= 5000 && millisUntilFinished >= 4000) {
//                    Log.e("timeee", " 1")
////                    img_splash.setBackground(ContextCompat.getDrawable(this@SplashActivity, R.drawable.img1))
//                    img_splash.setImageResource( R.drawable.img1)
//                }
//                if (millisUntilFinished < 4000 && millisUntilFinished >= 3000) {
//                    Log.e("timeee", " 2")
//
////                    img_splash.setBackground(ContextCompat.getDrawable(this@SplashActivity, R.drawable.img2))
//                    img_splash.setImageResource( R.drawable.img2)
//                }
//                if (millisUntilFinished < 3000 && millisUntilFinished >= 2000) {
//
//                    Log.e("timeee", " 3")
////                    img_splash.setBackground(ContextCompat.getDrawable(this@SplashActivity, R.drawable.img3))
//                    img_splash.setImageResource( R.drawable.img3)
//                }
//                if (millisUntilFinished < 2000 && millisUntilFinished >= 1000) {
//                    Log.e("timeee", " 4")
////                    img_splash.setBackgroundResource(R.drawable.img4)
////                    img_splash.setBackground(ContextCompat.getDrawable(this@SplashActivity, R.drawable.img4))
//                    img_splash.setImageResource( R.drawable.img4)
//                }
                if (millisUntilFinished <= 1000 && millisUntilFinished >= 0) {
                    Log.e("timeee", " 5")
//                    img_splash.setBackgroundResource(R.drawable.img5)
//                    img_splash.setBackground(ContextCompat.getDrawable(this@SplashActivity, R.drawable.img5))
                    img_splash.setImageResource( R.drawable.img5)
                }

            }

            override fun onFinish() {

                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
//                displayData()
            }
        }
        timer.start()

    }
}
