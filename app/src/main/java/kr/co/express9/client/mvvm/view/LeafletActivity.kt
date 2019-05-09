package kr.co.express9.client.mvvm.view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import com.github.chrisbanes.photoview.PhotoViewAttacher
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.base.BaseApplication.Companion.context
import kr.co.express9.client.databinding.ActivityLeafletBinding
import kr.co.express9.client.mvvm.model.data.Mart
import kr.co.express9.client.mvvm.viewModel.LeafletViewModel
import kr.co.express9.client.util.extension.launchActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LeafletActivity : BaseActivity<ActivityLeafletBinding>(R.layout.activity_leaflet), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var attacher: PhotoViewAttacher
    private lateinit var latLng: LatLng
    private lateinit var mart: Mart

    override fun initStartView(isRestart: Boolean) {
        setSupportActionBar(dataBinding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        if (intent.getStringExtra("type") == "마트 위치 보기") {
            dataBinding.ivLeaflet.visibility = View.GONE
            dataBinding.flLeafletMap.visibility = View.VISIBLE
            dataBinding.tvLeafletTitle.text = "마트 위치 보기"
            mart = intent.getSerializableExtra("mart") as Mart
            dataBinding.mart = mart
            latLng = LatLng(mart.latitude, mart.longitude)
            val mapFragment = supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        } else {
            dataBinding.tvLeafletTitle.text = "전단지 보기"
            dataBinding.ivLeaflet.visibility = View.VISIBLE
            dataBinding.flLeafletMap.visibility = View.GONE
            attacher = PhotoViewAttacher(dataBinding.ivLeaflet)
            val imageUrl = intent.getStringExtra("imageUrl")
            dataBinding.imageUrl = imageUrl
            attacher.update()
        }

        dataBinding.ivLeafletLeaflet.setOnClickListener {
            context.launchActivity<LeafletActivity> {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("type", "전단지 보기")
                putExtra("imageUrl", mart.leafletImageUrl)
            }
        }

        dataBinding.ivLeafletFavorite.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

            } else {

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        this.map.uiSettings.isScrollGesturesEnabled = false
        this.map.uiSettings.isZoomGesturesEnabled = false
        map.addMarker(MarkerOptions().icon(bitmapDescriptorFromVector(this, R.drawable.ic_place_favorite_big)).position(latLng))
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        map.animateCamera(CameraUpdateFactory.zoomTo(16f))
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
}