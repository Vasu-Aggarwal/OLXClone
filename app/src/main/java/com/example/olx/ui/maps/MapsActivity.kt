package com.example.olx.ui.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Camera
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.olx.R
import com.example.olx.databinding.ActivityMapsBinding
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.olx.model.DataItemModel
import com.example.olx.ui.baseFragment
import com.example.olx.utilities.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException


class MapsActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    var latitude: Double = 0.0
    var longitude: Double = 0.0

    var geocodeMatches: List<Address>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val address: String? = intent.getStringExtra("Ad")
        Log.d("LOC", "location: "+ address.toString())
        try {
            geocodeMatches = Geocoder(this).getFromLocationName(address, 1)

        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (geocodeMatches != null) {
            latitude = geocodeMatches!![0].latitude
            longitude = geocodeMatches!![0].longitude
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val zoomLevel = 16.0f
        val loc = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(loc).title("Seller Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, zoomLevel))

        val mapSettings = mMap?.uiSettings
        mapSettings?.isZoomGesturesEnabled = true
        mapSettings?.isRotateGesturesEnabled = true
        mapSettings?.isScrollGesturesEnabled = true
    }

}
