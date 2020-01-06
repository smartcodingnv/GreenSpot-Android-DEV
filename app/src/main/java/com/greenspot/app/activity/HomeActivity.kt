package com.greenspot.app.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.greenspot.app.R
import com.greenspot.app.fragment.AccountFragment
import com.greenspot.app.fragment.BookingFragment
import com.greenspot.app.fragment.HomeFragment
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import com.greenspot.app.utils.Progress
import com.greenspot.app.utils.Utils
import kotlinx.android.synthetic.main.content_home.*

class HomeActivity : AppCompatActivity() {
    private val TAG = "HomeActivity"
    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        progress = Progress(this@HomeActivity)
        utils = Utils(this@HomeActivity)
        helper = PreferenceHelper(this@HomeActivity, AppConfig.PREFERENCE.PREF_FILE)
        bottom_navigation.inflateMenu(R.menu.menu_bottom_navigation)
        bottom_navigation.getMenu().getItem(0).setChecked(true)
        setHomeFragmnet(HomeFragment().newInstance())

        Log.e("checkkk "," onCreate  checkkk" )
        Log.e("checkkk "," onCreate  checkkk"+helper!!.LoadIntPref(AppConfig.PREFERENCE.CHECKPAYMENT,0) )



        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            val id = item.itemId
            when (id) {
                R.id.action_home -> setHomeFragmnet(HomeFragment().newInstance())
                R.id.action_booking ->
                    if(helper!!.LoadIntPref(AppConfig.PREFERENCE.CHECK_LOGINAS,0)==2){
                        setBookingFragmnet(BookingFragment().newInstance())
                    }else{
                        alertLogin()
                    }

                R.id.action_account -> setAccountFragmnet(AccountFragment().newInstance())
            }
            true
        }

    }

    private fun alertLogin() {
        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage("Please login to use this feature")
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            "OK",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                helper!!.clearAllPrefs()
                startActivity(
                    Intent(this, LoginActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()

            })

        builder1.setNegativeButton(
            "CANCEL",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        val alert11 = builder1.create()
        alert11.show()

    }


    private fun setHomeFragmnet(homeFragment: HomeFragment) {
        val t = supportFragmentManager.beginTransaction()
        t.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        t.replace(R.id.main_container, homeFragment)
        t.commitAllowingStateLoss()
    }

    private fun setBookingFragmnet(bookingFragment: BookingFragment) {
        val t = supportFragmentManager.beginTransaction()
        t.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        t.replace(R.id.main_container, bookingFragment)
        t.commitAllowingStateLoss()
    }

    private fun setAccountFragmnet(accountFragment: AccountFragment) {
        val t = supportFragmentManager.beginTransaction()
        t.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        t.replace(R.id.main_container, accountFragment)
        t.commitAllowingStateLoss()
    }

    override fun onResume() {
        super.onResume()


    }


}
