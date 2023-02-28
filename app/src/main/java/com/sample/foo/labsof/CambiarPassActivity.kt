package com.sample.foo.labsof

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.UserConeccion
import com.sample.foo.labsof.helpers.Session
import com.sample.foo.labsof.helpers.Verificacion
import kotlinx.coroutines.launch

class CambiarPassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_pass)
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
        val pass = findViewById<EditText>(R.id.pass)
        val new_pass = findViewById<EditText>(R.id.new_pass)
        val new_pass2 = findViewById<EditText>(R.id.new_pass2)

        val guardar = findViewById<Button>(R.id.guardar)
        guardar.setOnClickListener { view: View ->
            if (Verificacion.notVacio(pass) && Verificacion.notVacio(new_pass)
                && Verificacion.notVacio(new_pass2)
            ) {
                if (new_pass.text.toString() == new_pass2.text.toString()) {
                    val builder: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(view.context)
                    builder.setTitle("Enviando Información")
                    builder.setMessage("Su solicitud esta siendo procesada")
                    builder.setCancelable(false)
                    val bCreate = builder.create()
                    bCreate.show()
                    lifecycleScope.launch {

                        val user = UserConeccion.getSingle(session.getSession().id_user)

                        if (user.error != null) {

                            bCreate.dismiss()
                            var builder = android.app.AlertDialog.Builder(view.context)
                            builder.setTitle("Error")
                            builder.setMessage(user.error)
                            builder.setPositiveButton("Ok",
                                DialogInterface.OnClickListener { dialog, which ->
                                    dialog.dismiss()
                                })
                            builder.create().show()
                        } else {
                            if (pass.text.toString() != user.password) {

                                bCreate.dismiss()
                                var builder = android.app.AlertDialog.Builder(view.context)
                                builder.setTitle("Error")
                                builder.setMessage("Error al introducir la contraseña anterior")
                                builder.setPositiveButton("Ok",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        dialog.dismiss()
                                    })
                                builder.create().show()
                            } else {
                                val user = session.getSession()
                                user.password= new_pass.text.toString()
                                val new_user = UserConeccion.put(user)
                                bCreate.dismiss()
                                if(new_user.error== null) {
                                    var builder = android.app.AlertDialog.Builder(view.context)
                                    builder.setTitle("Guardado Exitoso")
                                    builder.setMessage("Se guardo exitosamente el usuario")
                                    builder.setPositiveButton("Ok",
                                        DialogInterface.OnClickListener { dialog, which ->
                                            finish()
                                        })
                                    builder.create().show()
                                }else{
                                    var builder = android.app.AlertDialog.Builder(view.context)
                                    builder.setTitle("Error")
                                    builder.setMessage(new_user.error)
                                    builder.setPositiveButton("Ok",
                                        DialogInterface.OnClickListener { dialog, which ->
                                            dialog.dismiss()
                                        })
                                    builder.create().show()
                                }
                            }
                        }

                    }
                } else {
                    var builder = android.app.AlertDialog.Builder(view.context)
                    builder.setTitle("Error")
                    builder.setMessage("Las contraseñas deben coincidir")
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
}