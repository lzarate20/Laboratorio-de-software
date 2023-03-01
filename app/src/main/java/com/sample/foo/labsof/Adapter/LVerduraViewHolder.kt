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
import com.google.android.material.internal.ContextUtils
import com.sample.foo.labsof.*
import com.sample.foo.labsof.Coneccion.UserConeccion
import com.sample.foo.labsof.Coneccion.VerduraConeccion
import com.sample.foo.labsof.DataClass.User
import com.sample.foo.labsof.DataClass.VerduraFechaList
import com.sample.foo.labsof.helpers.Session
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LVerduraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val nombre = itemView.findViewById<TextView>(R.id.nombre)

    val cosecha = itemView.findViewById<TextView>(R.id.cosecha)

    val siembra = itemView.findViewById<TextView>(R.id.siembra)
    val actualizar = itemView.findViewById<Button>(R.id.actualizar)
    val eliminar = itemView.findViewById<Button>(R.id.eliminar)

    @SuppressLint("RestrictedApi")
    fun render(verdura: VerduraFechaList) {
        nombre.text = verdura.nombre
        cosecha.text = verdura.mes_cosecha()
        siembra.text = verdura.mes_siembra()

        actualizar.setOnClickListener { v ->
            val intent = Intent(v.context, VerUserActivity::class.java)
            val bun = Bundle()
            intent.putExtra("id", verdura.id_verdura)
            ContextCompat.startActivity(v.context, intent, bun)
        }
            eliminar.setOnClickListener { v ->
                val builder: android.app.AlertDialog.Builder =
                    android.app.AlertDialog.Builder(v.context)
                builder.setTitle("¿Eliminar?")
                builder.setMessage("¿Esta seguro que desea eliminar esta verdura?")
                builder.setPositiveButton(
                    "Eliminar",
                    DialogInterface.OnClickListener { dialog, which ->
                        GlobalScope.launch {
                            val builder: android.app.AlertDialog.Builder =
                                android.app.AlertDialog.Builder(itemView.context)
                            builder.setTitle("Enviando Información")
                            builder.setMessage("Su solicitud esta siendo procesada")
                            builder.setCancelable(false)
                            val bCreate = builder.create()
                            bCreate.show()
                            val delete = verdura.id_verdura.let { VerduraConeccion.delete(it!!) }
                            bCreate.dismiss()
                            if (delete == true) {

                                var activity = ContextUtils.getActivity(itemView.context)!!
                                val intent = Intent(activity, ListadoVerdurasActivity::class.java)

                                activity.finish()
                                activity.overridePendingTransition(0, 0)
                                activity.startActivity(intent)
                                activity.overridePendingTransition(0, 0);
                            }else{
                                var builder= android.app.AlertDialog.Builder(itemView.context)
                                builder.setTitle("Error")
                                builder.setMessage("No se pudo eliminar la verdura")
                                builder.setPositiveButton("Ok",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        dialog.dismiss()
                                    })
                                builder.create().show()
                            }
                        }
                    })
                builder.setNegativeButton(
                    "Cancelar",
                    DialogInterface.OnClickListener { dialog, which ->

                    })
                builder.create().show()
            }


    }
}