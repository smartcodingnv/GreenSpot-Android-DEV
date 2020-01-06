package com.greenspot.app.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.greenspot.app.R
import com.greenspot.app.activity.ChangePasswordActivity
import com.greenspot.app.activity.EditProfileActivity
import com.greenspot.app.activity.LoginActivity
import com.greenspot.app.activity.MainActivity
import com.greenspot.app.utils.AppConfig
import com.greenspot.app.utils.PreferenceHelper
import com.greenspot.app.utils.Progress
import com.greenspot.app.utils.Utils
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.view.*

class AccountFragment : Fragment() {

    private var progress: Progress? = null
    private var utils: Utils? = null
    private var helper: PreferenceHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun newInstance(): AccountFragment {

        val args = Bundle()

        val fragment = AccountFragment()
        fragment.setArguments(args)
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        progress = Progress(this!!.activity!!)
        utils = Utils(this!!.activity!!)
        helper = PreferenceHelper(
            this!!.activity!!,
            AppConfig.PREFERENCE.PREF_FILE
        )     // Inflate the layout for this
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(helper!!.LoadIntPref(AppConfig.PREFERENCE.CHECK_LOGINAS,0)==2){

            lay_editprofile.visibility = View.VISIBLE
            lay_changepwd.visibility = View.VISIBLE
            lay_settings.visibility = View.VISIBLE
            lay_logout.visibility = View.VISIBLE
            lay_login.visibility = View.GONE
            view_editprofile.visibility = View.VISIBLE
            view_changepwd.visibility = View.VISIBLE
            view_setting.visibility = View.VISIBLE
            view_login.visibility = View.VISIBLE


        }else{


            lay_editprofile.visibility = View.GONE
            lay_changepwd.visibility = View.GONE
            lay_settings.visibility = View.VISIBLE
            lay_logout.visibility = View.GONE
            lay_login.visibility = View.VISIBLE
            view_editprofile.visibility = View.GONE
            view_changepwd.visibility = View.GONE
            view_setting.visibility = View.VISIBLE
            view_login.visibility = View.VISIBLE

        }

        lay_editprofile.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(activity, EditProfileActivity::class.java)
            )

        })

        lay_changepwd.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(activity, ChangePasswordActivity::class.java)
            )

        })

        lay_logout.setOnClickListener(View.OnClickListener {

            signout()
        })

        lay_login.setOnClickListener(View.OnClickListener {

            login()
        })



    }

    private fun login() {

        helper!!.clearAllPrefs()
        startActivity(
            Intent(activity, MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        activity!!.finish()
    }

    private fun signout() {
        val builder1 = AlertDialog.Builder(this!!.activity!!)
        builder1.setMessage("Are you sure you want to sign out?")
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            "OK",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                helper!!.clearAllPrefs()
                startActivity(
                    Intent(activity, LoginActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                activity!!.finish()

            })

        builder1.setNegativeButton(
            "CANCEL",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        val alert11 = builder1.create()
        alert11.show()

    }
    // TODO: Rename method, update argument and hook method into UI event

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

}
