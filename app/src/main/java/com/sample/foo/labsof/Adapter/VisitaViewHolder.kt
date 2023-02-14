package com.sample.foo.labsof.Adapter

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sample.foo.labsof.Coneccion.ParcelaConeccion
import com.sample.foo.labsof.Coneccion.VisitaConeccion
import com.sample.foo.labsof.Listados.ListVisita
import com.sample.foo.labsof.R
import com.sample.foo.labsof.VerVisita
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.launch

class VisitaViewHolder(itemView: View) :ViewHolder(itemView) {

    val fecha=itemView.findViewById<TextView>(R.id.fecha)

    val quinta=itemView.findViewById<TextView>(R.id.quinta)

    val tecnico=itemView.findViewById<TextView>(R.id.tecnico)
    val ver= itemView.findViewById<Button>(R.id.ver)
    val eliminar=itemView.findViewById<Button>(R.id.eliminar)
    fun render(visitaModel: ListVisita.Union){
        fecha.text = "Fecha: ${(visitaModel.fecha)}"
        quinta.text = "Quinta: ${(visitaModel.nombre_q)}"
        tecnico.text= "Tecnico: ${(visitaModel.nombre)}"
        if(visitaModel.futura){
            ver.text= "Actualizar"
            eliminar.visibility= View.VISIBLE
        }else{
            ver.text= "Ver"
        }
        ver.setOnClickListener { v->
            val intent = Intent(v.context, VerVisita::class.java)
            val bun = Bundle()
            intent.putExtra("id", visitaModel.id)
            startActivity(v.context,intent,bun)
        }
        eliminar.setOnClickListener{v->
            val builder: android.app.AlertDialog.Builder =
                android.app.AlertDialog.Builder(v.context)
            builder.setTitle("¿Eliminar?")
            builder.setMessage("¿Esta seguro que desea eliminar esta visita?")
            builder.setPositiveButton(
                "Eliminar",
                DialogInterface.OnClickListener { dialog, which ->
                    GlobalScope.launch {
                       visitaModel.id?.let { VisitaConeccion.delete(it) }

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