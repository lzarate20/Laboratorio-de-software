package com.sample.foo.labsof

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.UserConeccion
import com.sample.foo.labsof.helpers.DialogHelper
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
                    val bCreate = DialogHelper.espera(this@CambiarPassActivity)
                    bCreate.show()
                    lifecycleScope.launch {
                        val user = UserConeccion.getSingle(session.getSession().id_user)
                        bCreate.dismiss()
                        if (user.error != null) {
                            DialogHelper.dialogo(
                                this@CambiarPassActivity,
                                "Error", user.error!!,
                                true, false, {}, {}
                            )
                        } else {
                            if (pass.text.toString() != user.password) {
                                DialogHelper.dialogo(
                                    this@CambiarPassActivity,
                                    "Error", "Error al introducir la contraseña anterior",
                                    true,false,{},{}
                                )
                            } else {
                                val user = session.getSession()
                                user.password= new_pass.text.toString()
                                val new_user = UserConeccion.put(user)
                                bCreate.dismiss()
                                if(new_user.error== null) {
                                    DialogHelper.dialogo(
                                        this@CambiarPassActivity,
                                        "Guardado Exitoso",
                                        "Se cambio exitosamente la contraseña",
                                        true,false,{finish()},{}
                                    )
                                }else{
                                    DialogHelper.dialogo(
                                        this@CambiarPassActivity,
                                        "Error", new_user.error!!,
                                        true,false,{},{}
                                    )
                                }
                            }
                        }

                    }
                } else {
                    DialogHelper.dialogo(
                        this@CambiarPassActivity,
                        "Error",
                        "Las contraseñas deben coincidir",
                        true,false,{},{}
                    )
                }
            } else {
                DialogHelper.dialogo(
                    this@CambiarPassActivity,
                    "Error",
                    "No puede haber campos vacios",
                    true,false,{},{}
                )
            }
        }
    }
}