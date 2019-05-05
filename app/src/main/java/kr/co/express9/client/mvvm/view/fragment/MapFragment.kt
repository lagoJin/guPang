package kr.co.express9.client.mvvm.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jakewharton.rxbinding3.widget.textChanges
import com.tedpark.tedpermission.rx2.TedRx2Permission
import com.yarolegovich.discretescrollview.DiscreteScrollView
import io.reactivex.android.schedulers.AndroidSchedulers
import kr.co.express9.client.R
import kr.co.express9.client.adapter.ItemTransformer
import kr.co.express9.client.adapter.MapMarketAdapter
import kr.co.express9.client.base.BaseFragment
import kr.co.express9.client.databinding.FragmentMapBinding
import kr.co.express9.client.mvvm.viewModel.KakaoAddressViewModel
import kr.co.express9.client.mvvm.viewModel.MapViewModel
import kr.co.express9.client.util.Logger
import kr.co.express9.client.util.extension.toast
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val viewModel: MapViewModel by inject()
    private val kakaoAddressViewModel: KakaoAddressViewModel by inject()

    private lateinit var locationManager: LocationManager
    private lateinit var location: Location


    @SuppressLint("MissingPermission")
    override fun initStartView() {
        Logger.d("startView")
        initLocation()
        dataBinding.model = viewModel
        dataBinding.kakaoViewModel = kakaoAddressViewModel

        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        viewModel.event.observe(this, Observer { event ->

        })

        kakaoAddressViewModel.event.observe(this, Observer { event ->
            when (event) {
                KakaoAddressViewModel.Event.WRITE_SEARCH_ADDRESS -> {

                }
                KakaoAddressViewModel.Event.NO_ADDRESS -> {

                }
                KakaoAddressViewModel.Event.NETWORK_ERROR -> {

                }
            }
        })

        disposable.add(dataBinding.actvMapSearch.textChanges()
            .filter { it.isNotEmpty() }
            .debounce(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { kakaoAddressViewModel.getAddressList(it as Editable) })

        dataBinding.ivMapLocation.setOnClickListener {
            if (::map.isInitialized && ::location.isInitialized) {
                map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude, location.longitude)))
                map.animateCamera(CameraUpdateFactory.zoomTo(17f))
            }
        }

        initAdapter()
    }

    private val scrollListener =
        DiscreteScrollView.ScrollListener<MapMarketAdapter.ViewHolder> { scrollPosition, currentIndex, newIndex, currentHolder, newCurrentHolder ->
            Logger.d("scrollPosition :$scrollPosition\n currentIndex : $currentIndex\n newIndex : $newIndex")
        }

    private fun initAdapter() {
        dataBinding.vpMap.apply {
            dataBinding.vpMap.setItemTransformer(ItemTransformer())
            //adapter = InfiniteScrollAdapter.wrap(MapMarketAdapter())
            addScrollListener(scrollListener)
            adapter = MapMarketAdapter()
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            this.map = it
            this.map.setOnCameraIdleListener {
                val northeast = it.projection.visibleRegion.latLngBounds.northeast
                val southwest = it.projection.visibleRegion.latLngBounds.southwest

            }
        }
    }

    fun addMarker(map: GoogleMap) {
        map.addMarker(MarkerOptions().position(LatLng(0.0, 0.0))).apply { this.tag = "" }
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
                        activity!!.toast("Permission Granted")
                    } else {
                        activity!!.toast("Permission Denied\n" + tedPermissionResult.getDeniedPermissions().toString())
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

    override fun onDestroy() {
        super.onDestroy()
    }
}