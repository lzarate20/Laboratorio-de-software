package com.sample.foo.labsof

import android.os.Build
import android.os.Bundle
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class CrearVisita : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_visita)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "2")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)

        FT.commit()

        val fecha: EditText = findViewById(R.id.fecha)
        var year: Int
        var month: Int
        var day: Int
        LocalDate.now().let { now ->

            year = now.year
            month = now.monthValue
            day = now.dayOfMonth
        }
        fecha.setText(formatDate(year, month - 1, day))
        fecha.setOnClickListener {
            showDatePicker(fecha)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePicker(fecha: EditText) {
        val date = getCurrentDate(fecha)
        val hoy = ConversorLocalDateTime.toLong(LocalDateTime.now())
        FechaDialog.newInstance(
            date.year,
            date.monthValue,
            date.dayOfMonth, minDate = hoy
        ) { _, year, month, day ->
            fecha.setText(formatDate(year, month, day))
        }.show(supportFragmentManager, "date-picker")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(fecha: EditText): LocalDate {
        val date = fecha.text.toString()
        return LocalDate.parse(date, formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDate(year: Int, month: Int, day: Int): String {
        val monthAux: Int = month + 1
        return LocalDate.of(year, monthAux, day).format(formatter)
    }

}