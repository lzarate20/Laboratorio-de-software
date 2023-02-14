package com.sample.foo.labsof.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.Listados.ListVisita
import com.sample.foo.labsof.R

class VisitaAdapter(private val listaVisita: List<ListVisita.Union>) :
    RecyclerView.Adapter<VisitaViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return VisitaViewHolder(layoutInflater.inflate(R.layout.item_visita, parent, false))
    }

    override fun onBindViewHolder(holder: VisitaViewHolder, position: Int) {
        val item = listaVisita.get(position)
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return listaVisita.size

    }


}