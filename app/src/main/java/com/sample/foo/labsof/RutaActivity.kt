package com.sample.foo.labsof

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY

import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.sample.foo.labsof.Coneccion.QuintaConeccion
import com.sample.foo.labsof.DataClass.Quinta
import com.sample.foo.labsof.databinding.ActivityRutaQuintaBinding
import kotlinx.coroutines.launch
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask


class RutaActivity: AppCompatActivity() {
    lateinit var binding: ActivityRutaQuintaBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRutaQuintaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "1")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)
        FT.commit()
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            val permiso = arrayOf<String>(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, permiso, 1)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        var mapView = (binding.mapview) as MapView
        initMap(mapView)
        val taskStart = fusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

            override fun isCancellationRequested() = false
        }).addOnSuccessListener { location: Location? ->
                if (location == null)
                    Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()
                else {
                    val lat = location.latitude
                    val lon = location.longitude
                    val start = GeoPoint(lat,lon)
                    val quinta_id:Int = intent.getIntExtra("quinta",-1)
                    var quinta: Quinta? = null
                    lifecycleScope.launch {
                        quinta = QuintaConeccion.getSingle(quinta_id)
                        var center = quinta?.geoImg
                        route(mapView,center!!,start)
                }
        }

        }
    }

    private fun initMap(mapView: MapView) {
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID)
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        mapView.setUseDataConnection(true)
        mapView.isNestedScrollingEnabled = false
        mapView.isClickable = true
        mapView.setMultiTouchControls(true)
        val mapViewController = mapView.controller
        mapViewController.setCenter(GeoPoint(-34.922078, -57.954025))
        mapViewController.setZoom(16)
    }
    private fun route(mapView: MapView, center: String,start:GeoPoint){
        val mapViewController = mapView.controller
        mapViewController.animateTo(start)
        val markerStart = Marker(mapView)
        markerStart.position = start
        markerStart.icon = resources.getDrawable(R.drawable.person)
        mapView.overlays.add(markerStart)
        var coords = center.split(',')
        var lat = coords[0].toDoubleOrNull()
        var lon = coords[1].toDoubleOrNull()
        var endPoint: GeoPoint? = null
        if(lat!=null && lon!= null){
            endPoint= GeoPoint(lat,lon)
        }

        var marker = Marker(mapView)
        marker.position = endPoint
        marker.icon = resources.getDrawable(R.drawable.home)
        mapView.overlays.add(marker)
        val executor: ExecutorService = Executors.newFixedThreadPool(1)
        val futureTask: FutureTask<String> = FutureTask(Runnable {
                val roadManager: RoadManager = OSRMRoadManager(this@RutaActivity, "")
                val waypoints = ArrayList<GeoPoint>()
                var Somewhere: GeoPoint? = start
                waypoints.add(Somewhere!!)
                waypoints.add(endPoint!!)
                val road = roadManager.getRoad(waypoints)
                if (road.mStatus != Road.STATUS_OK) Toast.makeText(
                    this@RutaActivity,
                    "Error!!!",
                    Toast.LENGTH_SHORT
                ).show()
                val roadOverlay: Polyline = RoadManager.buildRoadOverlay(road)
                mapView.getOverlays().add(roadOverlay)
            }, "")

            executor.submit(futureTask)

    }

}