package com.example.olx.ui.includeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.olx.R
import com.example.olx.baseFragment
import com.example.olx.utilities.Constants
import kotlinx.android.synthetic.main.fragment_include_details.*

class IncludeDetailsFragment: baseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_include_details, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listener()
        if(arguments?.getString(Constants.KEY)!!.equals(Constants.CAR))
            llKMDriven.visibility = View.VISIBLE
    }

    private fun listener() {
        textViewNext.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.textViewNext->{
                sendData()
            }
        }
    }

    private fun sendData() {
        if(edBrand.text?.isEmpty()!!)
            edBrand.setError(getString(R.string.enter_brand_name))
        else if(edAddress.text?.isEmpty()!!)
            edAddress.setError(getString(R.string.enter_address))
        else if(edPhone.text?.isEmpty()!!)
            edPhone.setError(getString(R.string.enter_phone_number))
        else{
            val bundle = Bundle()
            bundle.putString(Constants.BRAND, edBrand.text.toString())
            bundle.putString(Constants.YEAR, edYear.text.toString())
            bundle.putString(Constants.AD_TITLE, edAdTitle.text.toString())
            bundle.putString(Constants.AD_DESCRIPTION, edDescribe.text.toString())
            bundle.putString(Constants.ADDRESS, edAddress.text.toString())
            bundle.putString(Constants.PHONE, edPhone.text.toString())
            bundle.putString(Constants.PRICE, edPrice.text.toString())
            bundle.putString(Constants.KM_DRIVEN, edKMDrven.text.toString())
            bundle.putString(Constants.KEY, arguments?.getString(Constants.KEY))
            findNavController().navigate(R.id.action_details_to_upload_photo, bundle)
        }
    }
}