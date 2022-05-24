package com.example.olx.ui.uploadPhoto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.olx.R
import com.example.olx.baseFragment

class UploadPhotoFragment : baseFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_upload_photo, container, false)
        return rootView
    }
}