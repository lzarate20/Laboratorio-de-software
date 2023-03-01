package com.sample.foo.labsof

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.sample.foo.labsof.DataClass.Verdura
import com.sample.foo.labsof.helpers.Verificacion

class CrearVerduraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_verdura)
        val mes= arrayOf<String>("Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre")
        val nombre = findViewById<EditText>(R.id.nombre)
        val descripcion = findViewById<EditText>(R.id.descripcion)
        val cosecha = findViewById<Spinner>(R.id.cosecha)
        val siembra = findViewById<Spinner>(R.id.siembra)
        val image = findViewById<EditText>(R.id.image)
        creacionSpinner(cosecha,mes)
        creacionSpinner(siembra,mes)
        val guardar = findViewById<Button>(R.id.guardar)
        if (Verificacion.notVacio(nombre) && Verificacion.notVacio(descripcion)
            && Verificacion.notVacio(image)
        ) {
            guardar.setOnClickListener { view: View ->

            }
        }else{
            var builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Error")
            builder.setMessage("Debe introducir un email valido")
            builder.setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
            builder.create().show()
        }
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