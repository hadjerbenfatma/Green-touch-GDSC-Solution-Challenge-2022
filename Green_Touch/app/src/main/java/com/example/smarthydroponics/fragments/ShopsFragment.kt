package com.example.smarthydroponics.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smarthydroponics.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.fragment_shops.*

class ShopsFragment : Fragment() {
    private lateinit var mMap : GoogleMap
    private var mapReady=false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView= inflater.inflate(R.layout.fragment_shops, container, false)
        val mapFragment= childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap -> mMap= googleMap
            mapReady=true
            updateMap()
        }
        return rootView

    }



    private fun updateMap() {
    }

}

