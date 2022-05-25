package com.example.olx.ui

import android.app.Dialog
import android.view.Window
import androidx.fragment.app.Fragment
import com.example.olx.R

open class baseFragment: Fragment() {

    lateinit var mDialog: Dialog

    open fun showProgressBar(){
        mDialog = Dialog(activity!!)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(R.layout.dialog_progressbar)
        mDialog.setCancelable(true)
        mDialog.show()
    }

    open fun hideProgressBar(){
        mDialog.dismiss()
    }

}