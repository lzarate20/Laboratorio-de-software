package com.sample.foo.labsof


import android.content.DialogInterface
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.FamiliaProductoraConeccion
import com.sample.foo.labsof.Coneccion.QuintaConeccion
import com.sample.foo.labsof.DataClass.FamiliaProductora
import com.sample.foo.labsof.DataClass.Quinta
import com.sample.foo.labsof.databinding.ActivityCrearQuintaBinding
import com.sample.foo.labsof.helpers.ConversorDate
import com.sample.foo.labsof.helpers.DatePickerHelper
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants

import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class CrearQuintaActivity:AppCompatActivity() {
    lateinit var binding: ActivityCrearQuintaBinding
    lateinit var datePicker: DatePickerHelper
    lateinit var scrollview: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityCrearQuintaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "1")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)
        FT.commit()
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PERMISSION_GRANTED) {
            val permiso = arrayOf<String>(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, permiso, 1)
        }
        var mapView = (binding.mapview) as MapView
        initMap(mapView)
        scrollview = binding.scrollView
        mapView.setOnTouchListener { view, motionEvent ->
            var action = motionEvent.action

            when(action){
                MotionEvent.ACTION_DOWN ->  { scrollview.requestDisallowInterceptTouchEvent(true); false}
                MotionEvent.ACTION_UP -> { scrollview.requestDisallowInterceptTouchEvent(false); true}
                MotionEvent.ACTION_MOVE ->{ scrollview.requestDisallowInterceptTouchEvent(true); false}
                else -> false
            }

        }
        datePicker = DatePickerHelper(this)
        var year: Int
        var month: Int
        var day: Int
        LocalDate.now().let { now ->

            year = now.year
            month = now.monthValue
            day = now.dayOfMonth
        }
        binding.fecha.setText(ConversorDate.formatDate(year, month - 1, day))
        binding.fecha.setOnClickListener {
            showDatePicker(binding.fecha)
        }

        binding.submit.setOnClickListener {
            if(binding.nombreQuinta.text == null){
                binding.error.visibility = View.VISIBLE
                binding.error.text ="Se debe ingresar el nombre de la quinta"
            }
            else if(binding.direccion.text == null){
                binding.error.visibility = View.VISIBLE
                binding.error.text = "Se debe ingresar una direccion"
            }
            else if (binding.nombreFamilia.text == null){
                binding.error.visibility = View.VISIBLE
                binding.error.text ="Se debe ingresar el nombre de la familia"
            }
            else {
                val date = LocalDate.parse(binding.fecha.text.toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                if(!date.isBefore(LocalDate.now())){
                    binding.error.visibility = View.VISIBLE
                    binding.error.text = "Se debe ingresar una fecha anterior al dia de hoy"
                }
                else{
                    year = date.year
                    month = date.monthValue
                    day = date.dayOfMonth
                    var list = listOf(year,month,day)
                    var fp = FamiliaProductora(binding.nombreFamilia.text.toString(),list)
                    lifecycleScope.launch{
                        var result_fam = FamiliaProductoraConeccion.post(fp)
                        if(result_fam != null){
                            var quinta = Quinta(binding.nombreQuinta.text.toString(),binding.direccion.text.toString(),mapView.mapCenter.toString(),result_fam.id_fp)
                            var res_quinta = QuintaConeccion.post(quinta)
                            if(res_quinta != null){
                                val builder: android.app.AlertDialog.Builder =
                                    android.app.AlertDialog.Builder(it.context)
                                builder.setTitle("Guardado Exitoso")
                                builder.setMessage("Se Guardo exitosamente la quinta")
                                builder.setPositiveButton("Listo",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        finish()
                                    })
                                builder.create()?.show()
                            }
                        }
                    }
                }
            }
        }

    }
    private fun showDatePicker(fecha: EditText) {
        val hoy = LocalDateTime.now()
        datePicker.setMaxDate(ConversorDate.toLong(hoy))
        datePicker.showDialog(hoy.dayOfMonth, hoy.monthValue,hoy.year, object : DatePickerHelper.Callback {
            override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                fecha.setText("${dayStr}/${monthStr}/${year}")
            }
        })

    }
    private fun initMap(mapView: MapView){
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID)
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        mapView.setUseDataConnection(true)
        mapView.isNestedScrollingEnabled = false
        mapView.isClickable = true
        mapView.setMultiTouchControls(true)
        val mapViewController = mapView.controller
        mapViewController.setZoom(16.5)
        val Somewhere = GeoPoint(-34.9193, -57.9547)
        mapViewController.setCenter(Somewhere)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PERMISSION_GRANTED)
                Toast.makeText(this@CrearQuintaActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this@CrearQuintaActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
}