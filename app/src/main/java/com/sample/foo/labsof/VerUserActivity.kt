package com.sample.foo.labsof

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.sample.foo.labsof.helpers.DialogHelper
import com.sample.foo.labsof.helpers.Session


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

        val bCreate = DialogHelper.espera(this@VerUserActivity)
        val nombre = findViewById<TextView>(R.id.nombre)
        val apellido = findViewById<TextView>(R.id.apellido)
        val email = findViewById<TextView>(R.id.email)
        val direc = findViewById<TextView>(R.id.direc)
        val user_name = findViewById<TextView>(R.id.user_name)
        val rolT = findViewById<TextView>(R.id.rolT)


        rolT.text = userSession.rol()
        nombre.setText(userSession.nombre)
        apellido.setText(userSession.apellido)
        email.setText(userSession.email)
        direc.setText(userSession.direccion)
        user_name.setText(userSession.username)
        bCreate.dismiss()

    }
}