package com.sample.foo.labsof

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
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
import com.sample.foo.labsof.helpers.ConversorDate
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

        val fecha: EditText = findViewById(R.id.fecha)
        val desc: EditText = findViewById(R.id.descripcion)
        lifecycleScope.launch {
            val listTec = UserConeccion.get().getTecnicos()

            val spinnerQ = findViewById<Spinner>(R.id.spinnerQ)
            val spinnerT = findViewById<Spinner>(R.id.spinnerT)
            if (listTec.error == null) {
                val lista =
                    listTec.users!!.map { x -> "${(x.nombre)} ${(x.apellido)}" }.toTypedArray()

                creacionSpinner(spinnerT,lista)
            } else {
                val error= findViewById<TextView>(R.id.errrorT)
                mostrarError(error, listTec.error!!)
            }
            val listQ = QuintaConeccion.get()
            if (listQ.error == null) {
                val lista =
                    listQ.quintas!!.map { x -> "${(x.nombre)}" }.toTypedArray()

                creacionSpinner(spinnerQ,lista)
            } else {
                val error= findViewById<TextView>(R.id.errrorQ)
                mostrarError(error, listQ.error!!)
            }


            if (listQ.error == null && listTec.error == null) {
                val guardar = findViewById<Button>(R.id.guardar)
                guardar.text = "Guardar"
                guardar.setOnClickListener { view->
                    val id_tec= listTec.users?.get(spinnerT.selectedItemPosition)?.id_user
                    val id_qui= listQ.quintas?.get(spinnerQ.selectedItemPosition)?.id_quinta
                    var f= ConversorDate.convertToBD(fecha.text.toString())
                    val des= desc.text.toString()
                    var v= Visita(f, des,id_tec,id_qui)
                    lifecycleScope.launch {
                        v=VisitaConeccion.post(v)
                        if(v.error!=null){
                            mostrarError(findViewById(R.id.errrorG), v.error!!)
                        }else {
                            val builder: android.app.AlertDialog.Builder =
                                android.app.AlertDialog.Builder(view.context)
                            builder.setTitle("Guardado Exitoso")
                            builder.setMessage("Se Guardo exitosamente la visita")
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
            year=date.year,
            month = date.monthValue,
            day=date.dayOfMonth,
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
    private fun mostrarError(error:TextView, text: String){
        error.text = text
        error.visibility=View.VISIBLE
    }


}