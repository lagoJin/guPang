package kr.co.express9.client.mvvm.view

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kr.co.express9.client.R

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            this.map = map
        }
    }

    @SuppressLint("MissingPermission")
    fun initLocation() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 10f, locationListener)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000, 10f, locationListener)


    }

    private val locationListener = object : LocationListener{
        override fun onLocationChanged(location: Location?) {

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }
    }

}