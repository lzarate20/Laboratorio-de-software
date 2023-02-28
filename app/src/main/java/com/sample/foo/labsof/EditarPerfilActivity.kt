package com.sample.foo.labsof

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.UserConeccion
import com.sample.foo.labsof.DataClass.User
import com.sample.foo.labsof.helpers.Session
import com.sample.foo.labsof.helpers.Verificacion
import com.sample.foo.labsof.helpers.Verificacion.Companion.notVacio
import kotlinx.coroutines.launch

class EditarPerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)
        val session = Session(this)
        val userSession = session.getSession()
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
        val rol = findViewById<TextView>(R.id.rol)
        nombre.setText(userSession.nombre)
        apellido.setText(userSession.apellido)
        email.setText(userSession.email)
        direc.setText(userSession.direccion)
        user_name.setText(userSession.username)
        rol.text = userSession.rol()

        val guardar = findViewById<Button>(R.id.guardar)
        guardar.setOnClickListener { view: View ->
            if (Verificacion.notVacio(nombre) && Verificacion.notVacio(apellido)
                && Verificacion.notVacio(email) && Verificacion.notVacio(direc)
                && Verificacion.notVacio(user_name)
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
                            null,
                            email.text.toString(),
                            userSession.roles,
                            userSession.id_user
                        )
                        val new_user = UserConeccion.put(user)
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
                            builder.setMessage("Se guardaron exitosamente los cambios")
                            builder.setPositiveButton("Ok",
                                DialogInterface.OnClickListener { dialog, which ->
                                    dialog.dismiss()
                                    val intent= Intent(view.context,MainActivity::class.java)
                                    finish()
                                    overridePendingTransition(0, 0)
                                    startActivity(intent)
                                    overridePendingTransition(0, 0);
                                })
                            builder.create().show()
                        }
                    }
                }else {
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


}
