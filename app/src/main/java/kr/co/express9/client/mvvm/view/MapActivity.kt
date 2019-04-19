package kr.co.express9.client.mvvm.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tedpark.tedpermission.rx2.TedRx2Permission
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityMapBinding
import kr.co.express9.client.mvvm.viewModel.MapViewModel
import kr.co.express9.client.util.extension.AnyTostring
import kr.co.express9.client.util.extension.toast
import org.koin.android.ext.android.inject


class MapActivity : BaseActivity<ActivityMapBinding>(R.layout.activity_map), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val viewModel: MapViewModel by inject()

    override fun initStartView() {
        dataBinding.model = viewModel
        dataBinding.lifecycleOwner = this

        viewModel.event.observe(this, Observer { event ->

        })
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            this.map = it
            //addMarker(it)
        }
    }

    fun addMarker(map: GoogleMap) {
        map.addMarker(MarkerOptions().position(LatLng(0.0, 0.0))
        ).apply { this.tag = "" }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    fun initLocation() {
        val mapFragment =
                supportFragmentManager.findFragmentById(kr.co.express9.client.R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                && PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            initPermission()
            return
        }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 10f, locationListener)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10f, locationListener)
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }
    }

    @SuppressLint("MissingPermission")
    private fun initPermission() {
        TedRx2Permission.with(this)
                .setRationaleTitle(kr.co.express9.client.R.string.permission_title)
                .setRationaleMessage(kr.co.express9.client.R.string.permission_content) // "we need permission for read contact and find your location"
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .request()
                .subscribe({ tedPermissionResult ->
                    if (tedPermissionResult.isGranted) {
                        initLocation()
                        toast("Permission Granted")
                    } else {
                        toast("Permission Denied\n" + tedPermissionResult.getDeniedPermissions().toString())
                    }
                }, { throwable -> })
    }

}