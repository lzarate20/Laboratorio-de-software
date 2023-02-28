package com.sample.foo.labsof.Adapter

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils.getActivity
import com.sample.foo.labsof.*
import com.sample.foo.labsof.Coneccion.UserConeccion
import com.sample.foo.labsof.Coneccion.VisitaConeccion
import com.sample.foo.labsof.DataClass.User
import com.sample.foo.labsof.helpers.Session
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val nombre = itemView.findViewById<TextView>(R.id.nombre)

    val email = itemView.findViewById<TextView>(R.id.email)

    val rol = itemView.findViewById<TextView>(R.id.rol)
    val ver = itemView.findViewById<Button>(R.id.ver)
    val eliminar = itemView.findViewById<Button>(R.id.eliminar)
    @SuppressLint("RestrictedApi")
    fun render(userModel: User) {
        nombre.text = "${(userModel.nombre)} ${(userModel.apellido)}"
        email.text = "Email: ${(userModel.email)}"
        rol.text = "Rol: ${(userModel.rol())}"
        eliminar.visibility = View.VISIBLE
        ver.setOnClickListener { v ->
            val intent = Intent(v.context, VerUserActivity::class.java)
            val bun = Bundle()
            intent.putExtra("id", userModel.id_user)
            ContextCompat.startActivity(v.context, intent, bun)
        }

        var activity = getActivity(itemView.context)!!
        var Im= Session(activity).getSession()
        if(Im.id_user!= userModel.id_user) {
            eliminar.setOnClickListener { v ->
                val builder: android.app.AlertDialog.Builder =
                    android.app.AlertDialog.Builder(v.context)
                builder.setTitle("¿Eliminar?")
                builder.setMessage("¿Esta seguro que desea eliminar este usuario?")
                builder.setPositiveButton(
                    "Eliminar",
                    DialogInterface.OnClickListener { dialog, which ->
                        GlobalScope.launch {
                           val delete= userModel.id_user.let { UserConeccion.delete(it!!) }
                            if(delete== true){
                                val intent=
                                    if(userModel.isTecnico()){
                                        Intent(activity, ListarTecActivity::class.java)
                                    }else{
                                        Intent(activity, ListarAdminActivity::class.java)
                                    }
                                activity.finish()
                                activity.overridePendingTransition(0, 0)
                                activity.startActivity(intent)
                                activity.overridePendingTransition(0, 0);
                            }
                        }
                    })
                builder.setNegativeButton(
                    "Cancelar",
                    DialogInterface.OnClickListener { dialog, which ->

                    })
                builder.create().show()
            }
        }else{
            eliminar.visibility= View.GONE
        }

    }
}