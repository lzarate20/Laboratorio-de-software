package com.sample.foo.labsof

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
import com.sample.foo.labsof.helpers.DialogHelper
import com.sample.foo.labsof.helpers.Session
import com.sample.foo.labsof.helpers.Verificacion
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
                    val bCreate = DialogHelper.espera(this@EditarPerfilActivity)
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
                            DialogHelper.dialogo(this@EditarPerfilActivity,
                            "Error", new_user.error!!,true,false,{},{})
                        } else {
                            session.saveSession(new_user)
                            DialogHelper.dialogo(this@EditarPerfilActivity,
                            "Guardado exitoso",
                            "Se guardaron exitosamente los cambios",
                            true,false,{finish()},{})
                        }
                    }
                }else {
                    DialogHelper.dialogo(
                        this@EditarPerfilActivity,
                        "Error",
                        "Debe introducir un email valido.\n Por ejemplo: usuario@gmail.com",
                        true,false,{},{}
                    )
                }
            } else {
                DialogHelper.dialogo(
                    this@EditarPerfilActivity,
                    "Error",
                    "No puede haber campos vacios",
                    true,false,{},{}
                )
            }
        }

    }


}
