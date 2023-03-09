package com.sample.foo.labsof

import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.QuintaConeccion
import com.sample.foo.labsof.Coneccion.UserConeccion
import com.sample.foo.labsof.Coneccion.VisitaConeccion
import com.sample.foo.labsof.DataClass.Visita
import com.sample.foo.labsof.Listados.ListQuintas
import com.sample.foo.labsof.Listados.ListUsers
import com.sample.foo.labsof.helpers.ConversorDate
import com.sample.foo.labsof.helpers.DialogHelper
import com.sample.foo.labsof.helpers.FechaDialog
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime


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

        val spinnerQ = findViewById<Spinner>(R.id.spinnerQ)
        val spinnerT = findViewById<Spinner>(R.id.spinnerT)
        val fecha: EditText = findViewById(R.id.fecha)
        val desc: EditText = findViewById(R.id.descripcion)
        var listTec:ListUsers?=null
        var listQ: ListQuintas?=null
        val dCreate = DialogHelper.espera(this)
        dCreate.show()
        lifecycleScope.launch {
            listTec = UserConeccion.get().getTecnicos()
            listQ = QuintaConeccion.get()
            dCreate.dismiss()
            if (listTec!!.error == null) {
                val lista =
                    listTec!!.users!!.map { x -> "${(x.nombre)} ${(x.apellido)}" }.toTypedArray()

                creacionSpinner(spinnerT, lista)
            } else {
                DialogHelper.dialogo(this@CrearVisita, "Error",
                    listTec!!.error!!, true, false, { finish() }, {}
                )
            }
            if (listQ!!.error == null) {
                val lista =
                    listQ!!.quintas!!.map { x -> "${(x.nombre)}" }.toTypedArray()

                creacionSpinner(spinnerQ, lista)
            } else {
                DialogHelper.dialogo(this@CrearVisita, "Error", listQ!!.error!!,
                    true, false, { finish() }, {})
            }
        }

        val guardar = findViewById<Button>(R.id.guardar)
        guardar.text = "Guardar"
        guardar.setOnClickListener { view ->
            val id_tec = listTec!!.users?.get(spinnerT.selectedItemPosition)?.id_user
            val id_qui = listQ!!.quintas?.get(spinnerQ.selectedItemPosition)?.id_quinta
            val f = ConversorDate.convertToBD(fecha.text.toString())
            val des = desc.text.toString()
            var v = Visita(f, des, id_tec, id_qui)
            val dCreate= DialogHelper.espera(this@CrearVisita)
            dCreate.show()
            lifecycleScope.launch {
                v = VisitaConeccion.post(v)
                dCreate.dismiss()
                if (v.error != null) {
                    DialogHelper.dialogo(this@CrearVisita,"Error",v.error!!,
                    true,false,{},{})
                } else {
                    DialogHelper.dialogo(this@CrearVisita,
                    "Guardado exitoso"," Se guardo correctamente la nueva visita",
                    true,false,{finish()},{})

                }
            }

        }


        var year: Int
        var month: Int
        var day: Int
        LocalDate.now().let { now ->

            year = now.year
            month = now.monthValue
            day = now.dayOfMonth
        }
        fecha.setText(ConversorDate.formatDate(year, month - 1, day))
        fecha.setOnClickListener {
            showDatePicker(fecha)
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePicker(fecha: EditText) {
        val date = ConversorDate.getCurrentDate(fecha.text.toString())
        val hoy = ConversorDate.toLong(LocalDateTime.now())
        FechaDialog.newInstance(
            year = date.year,
            month = date.monthValue,
            day = date.dayOfMonth,
            minDate = hoy
        ) { _, year, month, day ->
            fecha.setText(ConversorDate.formatDate(year, month, day))
        }.show(supportFragmentManager, "date-picker")

    }


    private fun creacionSpinner(spinner: Spinner, list: Array<String>) {
        val mSortAdapter: ArrayAdapter<CharSequence> = ArrayAdapter(
            baseContext,
            android.R.layout.simple_spinner_item,
            list
        )
        mSortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = mSortAdapter
    }



}