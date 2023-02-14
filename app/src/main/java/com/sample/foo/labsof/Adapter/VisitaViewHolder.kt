package com.sample.foo.labsof.Adapter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sample.foo.labsof.Listados.ListVisita
import com.sample.foo.labsof.R
import com.sample.foo.labsof.VerVisita

class VisitaViewHolder(itemView: View) :ViewHolder(itemView) {

    val fecha=itemView.findViewById<TextView>(R.id.fecha)

    val quinta=itemView.findViewById<TextView>(R.id.quinta)

    val tecnico=itemView.findViewById<TextView>(R.id.tecnico)
    val ver= itemView.findViewById<Button>(R.id.ver)

    fun render(visitaModel: ListVisita.Union){
        fecha.text = "Fecha: ${(visitaModel.fecha)}"
        quinta.text = "Quinta: ${(visitaModel.nombre_q)}"
        tecnico.text= "Tecnico: ${(visitaModel.nombre)}"
        if(visitaModel.futura){
            ver.text= "Actualizar"
        }else{
            ver.text= "Ver"
        }
        ver.setOnClickListener { v->
            val intent = Intent(v.context, VerVisita::class.java)
            val bun = Bundle()
            intent.putExtra("id", visitaModel.id)
            startActivity(v.context,intent,bun)
        }

    }
}