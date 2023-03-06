package com.sample.foo.labsof

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doBeforeTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.RondaConeccion
import com.sample.foo.labsof.DataClass.Ronda
import com.sample.foo.labsof.databinding.ActivityCrearRondaBinding
import com.sample.foo.labsof.helpers.ConversorDate
import com.sample.foo.labsof.helpers.DatePickerHelper
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class CrearRondaActivity: AppCompatActivity() {

    lateinit var binding:ActivityCrearRondaBinding
    lateinit var datePickerInicio: DatePickerHelper
    lateinit var datePickerFin: DatePickerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityCrearRondaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "2")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)
        FT.commit()
        binding.title.text = "Crear Ronda"
        binding.submit.text = "Crear Ronda"
        datePickerInicio = DatePickerHelper(this)
        datePickerFin = DatePickerHelper(this)
        var year: Int
        var month: Int
        var day: Int
        LocalDate.now().let { now ->

            year = now.year
            month = now.monthValue
            day = now.dayOfMonth
        }
        var result_rondas: List<Ronda>? = null
        lifecycleScope.launch {
            result_rondas = RondaConeccion.getRondas()
        }
        binding.fechaInicio.setText(ConversorDate.formatDate(year, month-1, day))
        binding.fechaFin.setText(ConversorDate.formatDate(year, month-1, day))
        var fechaMin = LocalDateTime.now()
        datePickerInicio.setMinDate(ConversorDate.toLong(fechaMin))
        datePickerFin.setMinDate(ConversorDate.toLong(fechaMin))
        binding.fechaInicio.setOnClickListener {

           var date = ConversorDate.getCurrentDate(binding.fechaInicio.text.toString())
            showDatePicker(datePickerInicio,binding.fechaInicio,date)
        }
        binding.fechaFin.setOnClickListener {
            var date = ConversorDate.getCurrentDate(binding.fechaInicio.text.toString())
            showDatePicker(datePickerFin,binding.fechaFin,date)
        }
        binding.submit.setOnClickListener {

            var fecha_ini = LocalDate.parse(
                binding.fechaInicio.text.toString(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            )
            var fecha_fin = LocalDate.parse(
                binding.fechaFin.text.toString(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            )

            val ronda = Ronda(
                null,
                ConversorDate.formatDateListInt(fecha_ini),
                ConversorDate.formatDateListInt(fecha_fin)
            )
            if (result_rondas != null) {
                if (result_rondas!!.any { Ronda.Compare.betweenDate(it, ronda) }) {
                    binding.error.text =
                        "Ya existe una ronda que se superpone con las fechas ingresadas"
                    binding.error.visibility = View.VISIBLE
                } else if (fecha_fin.isBefore(fecha_ini)) {
                    binding.error.text =
                        "La fecha de inicio debe ser anterior a la fecha de fin."
                    binding.error.visibility = View.VISIBLE
                }
                else{
                    lifecycleScope.launch {
                        var res = RondaConeccion.postRonda(ronda)
                        if (res != null) {
                            val builder: android.app.AlertDialog.Builder =
                                android.app.AlertDialog.Builder(it.context)
                            builder.setTitle("Guardado Exitoso")
                            builder.setMessage("Se Guardo exitosamente la ronda")
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

    private fun showDatePicker(datePicker:DatePickerHelper,fecha: EditText,fechaDialogo:LocalDate) {
        datePicker.showDialog(fechaDialogo.dayOfMonth, fechaDialogo.monthValue-1,fechaDialogo.year, object : DatePickerHelper.Callback {
            override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month +1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                fecha.setText("${dayStr}/${monthStr}/${year}")
            }
        })

    }


}