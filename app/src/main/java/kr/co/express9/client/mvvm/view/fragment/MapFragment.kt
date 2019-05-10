package kr.co.express9.client.mvvm.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.jakewharton.rxbinding3.widget.textChanges
import com.tedpark.tedpermission.rx2.TedRx2Permission
import io.reactivex.android.schedulers.AndroidSchedulers
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseFragment
import kr.co.express9.client.databinding.FragmentMapBinding
import kr.co.express9.client.mvvm.model.data.Address
import kr.co.express9.client.mvvm.model.data.Mart
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.mvvm.view.LeafletActivity
import kr.co.express9.client.mvvm.view.MainActivity
import kr.co.express9.client.mvvm.viewModel.KakaoAddressViewModel
import kr.co.express9.client.mvvm.viewModel.MapViewModel
import kr.co.express9.client.util.extension.hideKeyboard
import kr.co.express9.client.util.extension.launchActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val mapViewModel: MapViewModel by viewModel()
    private val kakaoAddressViewModel: KakaoAddressViewModel by viewModel()

    private lateinit var locationManager: LocationManager
    private lateinit var location: Location
    val latlng = LatLng(37.5088255, 127.0631105)
    private lateinit var mart: Mart

    @SuppressLint("MissingPermission")
    override fun initStartView(isRestart: Boolean) {
        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_dropdown_item_1line, kakaoAddressViewModel.addressResult.value!!)
        initLocation()
        dataBinding.mapViewModel = mapViewModel
        dataBinding.kakaoViewModel = kakaoAddressViewModel
        dataBinding.lifecycleOwner = this

        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        mapViewModel.event.observe(this, Observer { event ->
            when (event) {
                MapViewModel.Event.MART_LIST -> {
                    map.clear()
                    mapViewModel.marts.value!!.forEach { Mart ->
                        var bitmapDescription: BitmapDescriptor? = bitmapDescriptorFromVector(activity!!, R.drawable.ic_place_non_favorite_big)
                        if (User.getFavoriteMarts().contains(Mart)) {
                            bitmapDescription = bitmapDescriptorFromVector(activity!!, R.drawable.ic_place_favorite_big)
                        }
                        val marker = map.addMarker(MarkerOptions().icon(bitmapDescription).position(LatLng(Mart.latitude, Mart.longitude)))
                        marker.tag = Mart.martSeq
                    }
                }
                MapViewModel.Event.FAVORITE_REFRESH -> {
                    map.clear()
                    mapViewModel.marts.value!!.forEach { Mart ->
                        var bitmapDescription: BitmapDescriptor? =
                                bitmapDescriptorFromVector(activity!!, R.drawable.ic_place_non_favorite_big)
                        if (User.getFavoriteMarts().contains(Mart)) {
                            bitmapDescription = bitmapDescriptorFromVector(activity!!, R.drawable.ic_place_favorite_big)
                        }
                        val marker = map.addMarker(
                                MarkerOptions().icon(bitmapDescription).position(LatLng(Mart.latitude, Mart.longitude)))
                        marker.tag = Mart.martSeq
                    }
                    if (User.getFavoriteMarts().size > 0) {
                        dataBinding.tvMapCheck.setBackgroundColor(activity!!.resources.getColor(R.color.colorPrimary))
                        dataBinding.tvMapCheck.isClickable = true
                    } else {
                        dataBinding.tvMapCheck.setBackgroundColor(activity!!.resources.getColor(R.color.fontGray))
                        dataBinding.tvMapCheck.isClickable = false
                    }
                }
            }
        })

        kakaoAddressViewModel.event.observe(this, Observer { event ->
            when (event) {
                KakaoAddressViewModel.Event.WRITE_SEARCH_ADDRESS -> {

                }
                KakaoAddressViewModel.Event.SEARCH_SUCCESS -> {
                    adapter.clear()
                    adapter.addAll(kakaoAddressViewModel.addressResult.value!!)
                    adapter.notifyDataSetChanged()
                }
                KakaoAddressViewModel.Event.NO_ADDRESS -> {

                }
                KakaoAddressViewModel.Event.NETWORK_ERROR -> {

                }
            }
        })

        compositeDisposable.add(dataBinding.actvMapSearch.textChanges()
                .filter { it.isNotEmpty() }
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { kakaoAddressViewModel.getAddressList(it as Editable) })

        dataBinding.actvMapSearch.setAdapter(adapter)
        dataBinding.actvMapSearch.setOnItemClickListener { parent, view, position, id ->
            val document = kakaoAddressViewModel.realAdress.value!!.documents[position]
            map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(document.y, document.x)))
            map.animateCamera(CameraUpdateFactory.zoomTo(16f))
            this.hideKeyboard()
        }

        dataBinding.ivMapLocation.setOnClickListener {
            initLocation()
            if (::map.isInitialized && ::location.isInitialized) {
                map.moveCamera(CameraUpdateFactory.newLatLng(latlng))
                map.animateCamera(CameraUpdateFactory.zoomTo(16f))
            }
        }

        dataBinding.tvMapCheck.setOnClickListener {
            activity!!.launchActivity<MainActivity>()
            activity!!.finish()
        }

        dataBinding.ivMarketLeaflet.setOnClickListener {
            activity!!.launchActivity<LeafletActivity> {
                putExtra("imageUrl", dataBinding.mart!!.leafletImageUrl)
            }
        }

        if (User.getFavoriteMarts().size != 0) {
            dataBinding.tvMapCheck.setBackgroundColor(activity!!.resources.getColor(R.color.colorPrimary))
        } else {
            dataBinding.tvMapCheck.isClickable = false
        }

    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.minimumHeight)
        val bitmap =
                Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    @SuppressLint("ResourceAsColor")
    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            this.map = it
            if (arguments != null) {
                val address = Gson().fromJson(arguments!!.getString("location"), Address.Document::class.java)
                map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(address.y, address.x)))
                map.animateCamera(CameraUpdateFactory.zoomTo(16f))
            } else {
                this.map.moveCamera(CameraUpdateFactory.newLatLng(latlng))
                this.map.animateCamera(CameraUpdateFactory.zoomTo(16f))
            }
            this.map.setOnCameraIdleListener {
                it.projection.visibleRegion.latLngBounds.apply {
                    mapViewModel.getMarts(northeast, southwest)
                }
            }
            this.map.setOnMarkerClickListener {
                dataBinding.flMarketInfo.visibility = View.VISIBLE
                mapViewModel.marts.value!!.forEach { Mart ->
                    if (it.tag == Mart.martSeq) {
                        dataBinding.mart = Mart
                        dataBinding.ivMarketFavorite.setOnCheckedChangeListener { buttonView, isChecked ->
                            if (isChecked) {
                                mapViewModel.addFavoriteMart(User.getUser().userSeq, Mart)
                            } else {
                                mapViewModel.deleteFavoriteMart(User.getUser().userSeq, Mart)
                            }
                        }
                        dataBinding.ivMarketFavorite.isChecked = User.getFavoriteMarts().contains(Mart)
                    }
                }
                true
            }
            this.map.setOnCameraMoveListener {
                dataBinding.flMarketInfo.visibility = View.GONE
            }
        }
    }

    @SuppressLint("CheckResult")
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    fun initLocation() {
        val mapFragment = activity!!.supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                        activity!!,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                && PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                        activity!!,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
        ) {
            TedRx2Permission.with(activity!!)
                    .setRationaleTitle(R.string.permission_title)
                    .setRationaleMessage(R.string.permission_content) // "we need permission for read contact and find your location"
                    .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .request()
                    .subscribe({ tedPermissionResult ->
                        if (tedPermissionResult.isGranted) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER,
                                    3000,
                                    10f,
                                    locationListener
                            )
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    3000,
                                    10f,
                                    locationListener
                            )
                        }
                    }, { throwable -> })
            return
        }
    }


    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            location?.let {
                this@MapFragment.location = it
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }
    }
}