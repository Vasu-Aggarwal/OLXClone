package com.example.olx.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.olx.R
import com.example.olx.ui.baseFragment
import com.example.olx.utilities.Constants
import com.example.olx.utilities.SharedPref
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : baseFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_settings, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        edName.setText(SharedPref(activity!!).getString(Constants.USER_NAME))
        edPhone.setText(SharedPref(activity!!).getString(Constants.USER_PHONE))
        edEmail.setText(SharedPref(activity!!).getString(Constants.USER_EMAIL))
        edAddress.setText(SharedPref(activity!!).getString(Constants.USER_ADDRESS))

        listener()
    }

    private fun listener() {
        save.setOnClickListener{
            saveData()
        }
    }

    private fun saveData() {
        if(edName.text.toString().isEmpty())
            edName.setError("Enter Full Name")

        else if(edEmail.text.toString().isEmpty())
            edEmail.setError("Enter Email")
        else{
            SharedPref(activity!!).setString(Constants.USER_PHONE, edPhone.text.toString())
            SharedPref(activity!!).setString(Constants.USER_ADDRESS, edAddress.text.toString())
            SharedPref(activity!!).setString(Constants.USER_EMAIL, edEmail.text.toString())
            SharedPref(activity!!).setString(Constants.USER_NAME, edName.text.toString())
            Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_settings_to_profile)
        }

    }
}