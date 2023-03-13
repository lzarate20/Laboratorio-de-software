package com.sample.foo.labsof

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.RondaConeccion
import com.sample.foo.labsof.DataClass.Ronda
import com.sample.foo.labsof.databinding.ActivityCrearRondaBinding
import com.sample.foo.labsof.helpers.ConversorDate
import com.sample.foo.labsof.helpers.DatePickerHelper
import com.sample.foo.labsof.helpers.DialogHelper
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditarRondaActivity : AppCompatActivity() {

    lateinit var binding: ActivityCrearRondaBinding
    lateinit var datePickerInicio: DatePickerHelper
    lateinit var datePickerFin: DatePickerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearRondaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "2")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)
        FT.commit()
        binding.title.text = "Editar Ronda"
        binding.submit.text = "Editar Ronda"

        val ronda_id = intent.getIntExtra("ronda", -1)
        datePickerInicio = DatePickerHelper(this)
        datePickerFin = DatePickerHelper(this)

        var ronda: Ronda?
        val dCreate = DialogHelper.espera(this@EditarRondaActivity)
        dCreate.show()
        lifecycleScope.launch {
            ronda = RondaConeccion.getRonda(ronda_id)
            var result_rondas = RondaConeccion.getRondas()!!.toMutableList()
            dCreate.dismiss()
            result_rondas.removeAll { it.id_ronda == ronda_id }
            binding.fechaInicio.setText(ConversorDate.convertToInput(ronda!!.fecha_inicio))
            binding.fechaFin.setText(ConversorDate.convertToInput(ronda!!.fecha_fin))
            var fechaMin = LocalDateTime.now()
            datePickerInicio.setMinDate(ConversorDate.toLong(fechaMin))
            datePickerFin.setMinDate(ConversorDate.toLong(fechaMin))
            binding.fechaInicio.setOnClickListener {

                var date = ConversorDate.getCurrentDate(binding.fechaInicio.text.toString())
                showDatePicker(datePickerInicio, binding.fechaInicio, date)
            }
            binding.fechaFin.setOnClickListener {
                var date = ConversorDate.getCurrentDate(binding.fechaInicio.text.toString())
                showDatePicker(datePickerFin, binding.fechaFin, date)
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
                    ronda_id,
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
                    } else {
                        val dCreate= DialogHelper.espera(this@EditarRondaActivity)
                        dCreate.show()
                        lifecycleScope.launch {
                            var res = RondaConeccion.putRonda(ronda)
                            dCreate.dismiss()
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
    }

    private fun showDatePicker(
        datePicker: DatePickerHelper,
        fecha: EditText,
        fechaDialogo: LocalDate
    ) {
        datePicker.showDialog(
            fechaDialogo.dayOfMonth,
            fechaDialogo.monthValue - 1,
            fechaDialogo.year,
            object : DatePickerHelper.Callback {
                override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {
                    val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                    val mon = month + 1
                    val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                    fecha.setText("${dayStr}/${monthStr}/${year}")
                }
            })

    }
}