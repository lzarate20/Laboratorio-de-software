package com.sample.foo.labsof

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.UserConeccion
import com.sample.foo.labsof.DataClass.User
import com.sample.foo.labsof.helpers.DialogHelper
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
                    val bCreate = DialogHelper.espera(this@CrearUserActivity)
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
                            DialogHelper.dialogo(
                                this@CrearUserActivity,
                                "Error",new_user.error!!,
                                true,false,{},{}
                            )
                        } else {
                            DialogHelper.dialogo(
                                this@CrearUserActivity,
                                "Guardado exitoso",
                                "Se guardo exitosamente el usuario",
                                true,false,{finish()},{}
                            )
                        }
                    }
                } else {
                    DialogHelper.dialogo(
                        this@CrearUserActivity,
                        "Error",
                        "Debe introducir un email valido.\n Por ejemplo: usuario@gmail.com",
                        true,false,{},{}
                    )
                }
            } else {
                DialogHelper.dialogo(
                    this@CrearUserActivity,
                    "Error",
                    "No puede haber campos vacios",
                    true,false,{},{}
                )
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
