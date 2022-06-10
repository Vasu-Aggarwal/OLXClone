package com.example.olx.ui.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.olx.R
import com.example.olx.ui.baseFragment
import com.example.olx.ui.login.LoginActivity
import com.example.olx.utilities.Constants
import com.example.olx.utilities.SharedPref
import com.facebook.login.LoginManager
import com.facebook.share.Share
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : baseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setData()
        listener()
    }

    private fun listener() {
        ll_settings.setOnClickListener(this)
        ll_logout.setOnClickListener(this)
    }

    private fun setData() {
        tvName.text = SharedPref(activity!!).getString(Constants.USER_NAME)
        tvEmail.text = SharedPref(activity!!).getString(Constants.USER_EMAIL)
        Glide.with(activity!!).load(SharedPref(activity!!).getString(Constants.USER_PHONE))
            .placeholder(R.drawable.user)
            .into(imageviewUser)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ll_settings->{
                findNavController().navigate(R.id.action_profile_to_settings)
            }

            R.id.ll_logout->{
                showDialogBox()
            }
        }
    }

    private fun showDialogBox() {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout ?")
        builder.setIcon(R.drawable.img)
        builder.setPositiveButton("Yes"){
            dialog: DialogInterface?, which: Int -> FirebaseAuth.getInstance().signOut()
            LoginManager.getInstance().logOut()

            clearSession()
            startActivity(Intent(activity!!, LoginActivity::class.java))
            activity!!.finish()
            dialog?.dismiss()
        }
        builder.setNegativeButton("No"){
            dialog, which-> dialog.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun clearSession() {
        SharedPref(activity!!).setString(Constants.USER_EMAIL, "")
        SharedPref(activity!!).setString(Constants.USER_ID, "")
        SharedPref(activity!!).setString(Constants.USER_NAME, "")
        SharedPref(activity!!).setString(Constants.USER_PHONE, "")
        SharedPref(activity!!).setString(Constants.USER_PHOTO, "")
    }
}