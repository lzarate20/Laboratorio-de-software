package com.sample.foo.labsof

import android.Manifest

import android.content.pm.PackageManager

import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.QuintaConeccion
import com.sample.foo.labsof.DataClass.Quinta
import com.sample.foo.labsof.databinding.ActivityRutaQuintaBinding
import kotlinx.coroutines.launch
import okhttp3.internal.wait
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
    lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

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
        val quinta_id:Int = intent.getIntExtra("quinta",-1)
        var quinta: Quinta? = null
        lifecycleScope.launch {
            quinta = QuintaConeccion.getSingle(quinta_id)
            var center = quinta?.geoImg
            var mapView = (binding.mapview) as MapView
            initMap(mapView,center!!)

        }
    }

    private fun initMap(mapView: MapView, center: String){
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID)
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        mapView.setUseDataConnection(true)
        mapView.isNestedScrollingEnabled = false
        mapView.isClickable = true
        mapView.setMultiTouchControls(true)
        var myLocationOverlay: MyLocationNewOverlay =  MyLocationNewOverlay(GpsMyLocationProvider(this), mapView)
        myLocationOverlay.enableMyLocation()
        myLocationOverlay.enableFollowLocation()
        val mapViewController = mapView.controller
        mapViewController.setZoom(16)
        mapViewController.setCenter( GeoPoint(-34.921257, -57.954384))

        mapView.overlays.add(myLocationOverlay)


        var coords = center.split(',')
        var lat = coords[0].toDoubleOrNull()
        var lon = coords[1].toDoubleOrNull()
        var endPoint: GeoPoint? = null
        if(lat!=null && lon!= null){
            endPoint= GeoPoint(lat,lon)
        }

        var marker = Marker(mapView)
        marker.position = endPoint
        mapView.overlays.add(marker)


        val executor: ExecutorService = Executors.newFixedThreadPool(1)
        val futureTask: FutureTask<String> = FutureTask( Runnable {
            val roadManager: RoadManager = OSRMRoadManager(this@RutaActivity, "")
            val waypoints = ArrayList<GeoPoint>()
            var Somewhere:GeoPoint? = myLocationOverlay.myLocation
            waypoints.add(Somewhere!!)
            waypoints.add(endPoint!!)
            val road = roadManager.getRoad(waypoints)
            if (road.mStatus != Road. STATUS_OK) Toast.makeText(
                this@RutaActivity,
                "Error!!!",
                Toast. LENGTH_SHORT
            ).show()
            val roadOverlay: Polyline = RoadManager.buildRoadOverlay(road)
            mapView.getOverlays().add(roadOverlay)
        }, "")
        executor.submit(futureTask)
    }
    private fun checkPermissions(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }



}