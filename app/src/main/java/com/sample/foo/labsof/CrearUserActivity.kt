package com.sample.foo.labsof

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.UserConeccion
import com.sample.foo.labsof.DataClass.User
import com.sample.foo.labsof.helpers.Session
import com.sample.foo.labsof.helpers.Verificacion
import kotlinx.coroutines.launch

class CrearUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_user)
        val session = Session(this)
        if (!session.haveSesion()) {
            finish()
        }
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "2")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)
        FT.commit()
        val nombre = findViewById<EditText>(R.id.nombre)
        val apellido = findViewById<EditText>(R.id.apellido)
        val email = findViewById<EditText>(R.id.email)
        val direc = findViewById<EditText>(R.id.direc)
        val user_name = findViewById<EditText>(R.id.user_name)
        val pass = findViewById<EditText>(R.id.pass)
        val rol = findViewById<Spinner>(R.id.rol)
        val roles = arrayOf<String>("Administrador", "Técnico")
        creacionSpinner(rol, roles)

        val guardar = findViewById<Button>(R.id.guardar)
        guardar.setOnClickListener { view: View ->
            if (Verificacion.notVacio(nombre) && Verificacion.notVacio(apellido)
                && Verificacion.notVacio(email) && Verificacion.notVacio(direc)
                && Verificacion.notVacio(user_name) && Verificacion.notVacio(pass)
            ) {
                if (Verificacion.email(email)) {
                    val builder: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(view.context)
                    builder.setTitle("Enviando Información")
                    builder.setMessage("Su solicitud esta siendo procesada")
                    builder.setCancelable(false)
                    val bCreate = builder.create()
                    bCreate.show()
                    lifecycleScope.launch {
                        val user = User(
                            nombre.text.toString(),
                            apellido.text.toString(),
                            direc.text.toString(),
                            user_name.text.toString(),
                            pass.text.toString(),
                            email.text.toString(),
                            rol.selectedItemPosition
                        )
                        val new_user = UserConeccion.post(user)
                        bCreate.dismiss()
                        if (new_user.error != null) {
                            var builder = android.app.AlertDialog.Builder(view.context)
                            builder.setTitle("Error")
                            builder.setMessage(new_user.error)
                            builder.setPositiveButton("Ok",
                                DialogInterface.OnClickListener { dialog, which ->
                                    dialog.dismiss()
                                })
                            builder.create().show()
                        } else {
                            session.saveSession(new_user)
                            var builder = android.app.AlertDialog.Builder(view.context)
                            builder.setTitle("Guardado Exitoso")
                            builder.setMessage("Se guardo exitosamente el usuario")
                            builder.setPositiveButton("Ok",
                                DialogInterface.OnClickListener { dialog, which ->
                                   finish()
                                })
                            builder.create().show()
                        }
                    }
                } else {
                    var builder = android.app.AlertDialog.Builder(view.context)
                    builder.setTitle("Error")
                    builder.setMessage("Debe introducir un email valido")
                    builder.setPositiveButton("Ok",
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog.dismiss()
                        })
                    builder.create().show()
                }
            } else {
                var builder = android.app.AlertDialog.Builder(view.context)
                builder.setTitle("Error")
                builder.setMessage("No puede haber campos vacios")
                builder.setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                builder.create().show()
            }
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
