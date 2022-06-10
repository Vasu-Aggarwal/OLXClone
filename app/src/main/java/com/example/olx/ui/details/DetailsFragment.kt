package com.example.olx.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.olx.R
import com.example.olx.model.DataItemModel
import com.example.olx.ui.PreviewImageActivity
import com.example.olx.ui.baseFragment
import com.example.olx.ui.details.adapter.DetailImagesAdapter
import com.example.olx.ui.maps.MapsActivity
import com.example.olx.utilities.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : baseFragment(), DetailImagesAdapter.onItemClick {
    private lateinit var dataItemModel: DataItemModel
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_details, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getItemDetails()

        clickListener()
        if(arguments?.getString(Constants.KEY).equals(Constants.CAR))
            llKMDriven.visibility = View.VISIBLE

    }

    private fun clickListener() {
        call.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel: " +dataItemModel.phone)
            startActivity(dialIntent)
        }

        maps.setOnClickListener {
//            Toast.makeText(context, "you click for map", Toast.LENGTH_SHORT).show()
            val mapIntent = Intent(context, MapsActivity::class.java)
            mapIntent.putExtra("Ad", dataItemModel.address)
            startActivity(mapIntent)
        }
    }

    private fun getItemDetails() {
        showProgressBar()

        db.collection(arguments?.getString(Constants.KEY)!!)
            .document(arguments?.getString(Constants.DOCUMENT_ID)!!)
            .get().addOnSuccessListener {
                hideProgressBar()
                dataItemModel = it.toObject(DataItemModel::class.java)!!
                setData()
                setPagerAdapter()
            }
    }

    private fun setPagerAdapter() {
        val detailImagesAdapter = DetailImagesAdapter(context!!, dataItemModel.images, this)
        viewPager.adapter = detailImagesAdapter
        viewPager.offscreenPageLimit = 1
    }

    private fun setData() {
        tvPrice.text = Constants.CURRENCY_SYMBOL + dataItemModel.price
        tvTitle.text = dataItemModel.adTitle
        tvAddress.text = dataItemModel.address
//        tvYear.text = dataItemModel.year
        tvBrand.text = dataItemModel.brand
        tvDescription.text = dataItemModel.description
        tvPhone.text = dataItemModel.phone
//        val dateFormat = SimpleDateFormat("DD-MMM", Locale.getDefault())
//        val date = dateFormat.format(Constants.CREATED_DATE)
//        tvDate.text = date
    }

    override fun onClick(position: Int) {
        startActivity(Intent(activity, PreviewImageActivity::class.java)
            .putExtra("imageUrl", dataItemModel.images.get(position)))
    }
}