package com.sample.foo.labsof

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.UserConeccion
import com.sample.foo.labsof.DataClass.User
import com.sample.foo.labsof.helpers.Session
import kotlinx.coroutines.launch

class VerUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_user)
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
        var builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(this)
        builder.setTitle("Enviando Información")
        builder.setMessage("Su solicitud esta siendo procesada")
        builder.setCancelable(false)
        var bCreate = builder.create()
        bCreate.show()
        val roles = arrayOf<String>("Administrador","Técnico")
        val nombre = findViewById<TextView>(R.id.nombre)
        val apellido = findViewById<TextView>(R.id.apellido)
        val email = findViewById<TextView>(R.id.email)
        val direc = findViewById<TextView>(R.id.direc)
        val user_name = findViewById<TextView>(R.id.user_name)
        val rol = findViewById<Spinner>(R.id.rol)
        creacionSpinner(rol,roles)

        var id: Int = this.intent.getIntExtra("id", 0)
        var user: User = User()
        lifecycleScope.launch{
            user = UserConeccion.getSingle(id)
            bCreate.dismiss()
            if(user.error != null){
                var builder= android.app.AlertDialog.Builder(this@VerUserActivity)
                builder.setTitle("Error")
                builder.setMessage(user.error)
                builder.setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                builder.create().show()
            }else{
                nombre.setText(user.nombre)
                apellido.setText(user.apellido)
                email.setText(user.email)
                direc.setText(user.direccion)
                user_name.setText(user.username)
                rol.setSelection(user.roles!!)
            }
        }
        val guardar = findViewById<Button>(R.id.guardar)
        guardar.setOnClickListener { view: View ->
            lifecycleScope.launch{
                user.roles= rol.selectedItemPosition
                bCreate.dismiss()
                var new_user = UserConeccion.put(user)
                if(new_user.error == null){
                    var builder = android.app.AlertDialog.Builder(view.context)
                    builder.setTitle("Guardado Exitoso")
                    builder.setMessage("Se guardaron exitosamente los cambios")
                    builder.setPositiveButton("Ok",
                        DialogInterface.OnClickListener { dialog, which ->
                            finish()
                            })
                    builder.create().show()
                }
                var builder= android.app.AlertDialog.Builder(view.context)
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