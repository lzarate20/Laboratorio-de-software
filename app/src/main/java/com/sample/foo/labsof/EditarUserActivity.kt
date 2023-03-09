package com.sample.foo.labsof

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.UserConeccion
import com.sample.foo.labsof.DataClass.User
import com.sample.foo.labsof.helpers.DialogHelper
import com.sample.foo.labsof.helpers.Session
import com.sample.foo.labsof.helpers.Verificacion
import kotlinx.coroutines.launch

class EditarUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_user)
        val session = Session(this)
        val userSession = session.getSession()
        if (!session.haveSesion() && userSession.isTecnico()) {
            finish()
        }
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "2")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)
        FT.commit()

        val bCreate = DialogHelper.espera(this@EditarUserActivity)
        bCreate.show()
        val roles = arrayOf<String>("Administrador", "Técnico")
        val nombre = findViewById<EditText>(R.id.nombre)
        val apellido = findViewById<EditText>(R.id.apellido)
        val email = findViewById<EditText>(R.id.email)
        val direc = findViewById<EditText>(R.id.direc)
        val pass = findViewById<EditText>(R.id.pass)
        val user_name = findViewById<EditText>(R.id.user_name)
        val rol = findViewById<Spinner>(R.id.rol)

        val guardar = findViewById<Button>(R.id.guardar)
        creacionSpinner(rol, roles)

        val id: Int = this.intent.getIntExtra("id", 0)
        var user = User()

        lifecycleScope.launch {
            user = UserConeccion.getSingle(id)
            bCreate.dismiss()
            if (user.error != null) {
                DialogHelper.dialogo(
                    this@EditarUserActivity,
                    "Error",
                    user.error!!,
                    true,
                    false,
                    {},
                    {})

            } else {
                nombre.setText(user.nombre)
                apellido.setText(user.apellido)
                email.setText(user.email)
                direc.setText(user.direccion)
                user_name.setText(user.username)
                pass.setText(user.password)
                rol.setSelection(user.roles!!)
            }
        }
        guardar.setOnClickListener { view: View ->
            if (Verificacion.notVacio(nombre) && Verificacion.notVacio(apellido)
                && Verificacion.notVacio(email) && Verificacion.notVacio(direc)
                && Verificacion.notVacio(user_name) && Verificacion.notVacio(pass)
            ) {
                if (Verificacion.email(email)) {

                    val bCreate = DialogHelper.espera(this@EditarUserActivity)
                    bCreate.show()
                    lifecycleScope.launch {
                        val user = User(
                            nombre.text.toString(),
                            apellido.text.toString(),
                            direc.text.toString(),
                            user_name.text.toString(),
                            pass.text.toString(),
                            email.text.toString(),
                            rol.selectedItemPosition,
                            user.id_user
                        )
                        val new_user = UserConeccion.put(user)
                        bCreate.dismiss()
                        if (new_user.error != null) {
                            DialogHelper.dialogo(
                                this@EditarUserActivity,
                                "Error",
                                user.error!!,
                                true,
                                false,
                                {},
                                {})
                        } else {
                            DialogHelper.dialogo(
                                this@EditarUserActivity,
                                "Guardado Exitoso",
                                "Se guardaron exitosamente los cambios",
                                true,
                                false,
                                {
                                    val intent = Intent()
                                    setResult(Activity.RESULT_OK, intent)
                                    finish()
                                },
                                {})
                        }
                    }
                } else {
                    DialogHelper.dialogo(
                        this@EditarUserActivity,
                        "Error",
                        "Debe introducir un email valido",
                        true,
                        false,
                        {},
                        {})

                }
            } else {
                DialogHelper.dialogo(
                    this@EditarUserActivity,
                    "Error",
                    "No puede haber campos vacios",
                    true,
                    false,
                    {},
                    {})
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