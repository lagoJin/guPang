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
import com.jakewharton.rxbinding3.widget.textChanges
import com.tedpark.tedpermission.rx2.TedRx2Permission
import com.yarolegovich.discretescrollview.DiscreteScrollView
import io.reactivex.android.schedulers.AndroidSchedulers
import kr.co.express9.client.R
import kr.co.express9.client.adapter.ItemTransformer
import kr.co.express9.client.adapter.MapMartAdapter
import kr.co.express9.client.base.BaseFragment
import kr.co.express9.client.databinding.FragmentMapBinding
import kr.co.express9.client.mvvm.model.data.Mart
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.mvvm.viewModel.KakaoAddressViewModel
import kr.co.express9.client.mvvm.viewModel.MapViewModel
import kr.co.express9.client.util.Logger
import kr.co.express9.client.util.extension.anyTostring
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val mapViewModel: MapViewModel by inject()
    private val kakaoAddressViewModel: KakaoAddressViewModel by inject()

    private lateinit var locationManager: LocationManager
    private lateinit var location: Location

    private val martList = ArrayList<Mart>()
    private lateinit var adapter: MapMartAdapter

    @SuppressLint("MissingPermission")
    override fun initStartView(isRestart: Boolean) {
        initLocation()
        dataBinding.mapViewModel = mapViewModel
        dataBinding.kakaoViewModel = kakaoAddressViewModel
        dataBinding.lifecycleOwner = this

        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        var bitmapDescription: BitmapDescriptor? = bitmapDescriptorFromVector(activity!!, R.drawable.ic_place_non_favorite)
        mapViewModel.event.observe(this, Observer { event ->
            when (event) {
                MapViewModel.Event.MART_LIST -> {
                    martList.clear()
                    map.clear()
                    mapViewModel.marts.value!!.forEach { Mart ->
                        martList.add(Mart)
                        Logger.d("좋아요 마트 ${User.getFavoriteMarts().anyTostring()}")
                        User.getFavoriteMarts().forEach { myMart ->
                            if (Mart.martSeq == myMart.martSeq) {
                                bitmapDescription = bitmapDescriptorFromVector(activity!!, R.drawable.ic_place_favorite)
                            }
                            Logger.d("좋아요 마트 ${myMart.martSeq}")
                        }
                        map.addMarker(MarkerOptions().icon(bitmapDescription).position(LatLng(Mart.latitude, Mart.longitude)))
                    }
                    Logger.d("마트리스트 ${martList.anyTostring()}")
                    adapter.notifyDataSetChanged()
                }
            }
        })

        kakaoAddressViewModel.event.observe(this, Observer { event ->
            when (event) {
                KakaoAddressViewModel.Event.WRITE_SEARCH_ADDRESS -> {

                }
                KakaoAddressViewModel.Event.SEARCH_SUCCESS -> {

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

        dataBinding.ivMapLocation.setOnClickListener {
            initLocation()
            if (::map.isInitialized && ::location.isInitialized) {
                map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude, location.longitude)))
                map.animateCamera(CameraUpdateFactory.zoomTo(16f))
            }
        }
        initAdapter()
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.minimumHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun addMarker(latlng: LatLng, type: Int) {
        var bitmapDescription: BitmapDescriptor? = null
        when (type) {
            TYPE.FAVORITE.ordinal -> {
                bitmapDescription = BitmapDescriptorFactory.fromResource(R.drawable.ic_place_favorite)
            }
            TYPE.NON_FAVORITE.ordinal -> {
                bitmapDescription = BitmapDescriptorFactory.fromResource(R.drawable.ic_place_non_favorite)
            }
            TYPE.FAVORITE_BIG.ordinal -> {
                bitmapDescription = BitmapDescriptorFactory.fromResource(R.drawable.ic_place_favorite_big)
            }
            TYPE.NON_FAVORITE_BIG.ordinal -> {
                bitmapDescription = BitmapDescriptorFactory.fromResource(R.drawable.ic_place_non_favorite_big)
            }
        }
        map.addMarker(MarkerOptions().icon(bitmapDescription).position(latlng))
    }

    private val scrollListener = DiscreteScrollView.ScrollListener<MapMartAdapter.ViewHolder> { scrollPosition, currentIndex, newIndex, currentHolder, newCurrentHolder ->
        martList[newIndex].apply {
            map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))
            map.animateCamera(CameraUpdateFactory.zoomTo(16f))
        }
    }

    private fun initAdapter() {
        adapter = MapMartAdapter(martList) { i ->

        }

        dataBinding.vpMap.apply {
            dataBinding.vpMap.setItemTransformer(ItemTransformer())
            addScrollListener(scrollListener)
            this@apply.adapter = this@MapFragment.adapter
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            this.map = it
            this.map.setOnCameraIdleListener {
                it.projection.visibleRegion.latLngBounds.apply {
                    mapViewModel.getMartSearch(northeast, southwest)
                }
            }
            this.map.setOnMarkerClickListener {

                true
            }
        }
    }

    @SuppressLint("CheckResult")
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    fun initLocation() {
        val mapFragment = activity!!.supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)
                && PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            TedRx2Permission.with(activity!!)
                    .setRationaleTitle(R.string.permission_title)
                    .setRationaleMessage(R.string.permission_content) // "we need permission for read contact and find your location"
                    .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .request()
                    .subscribe({ tedPermissionResult ->
                        if (tedPermissionResult.isGranted) {
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 10f, locationListener)
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10f, locationListener)
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

    enum class TYPE {
        NON_FAVORITE,
        FAVORITE,
        NON_FAVORITE_BIG,
        FAVORITE_BIG
    }
}