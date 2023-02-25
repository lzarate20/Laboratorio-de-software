package com.sample.foo.labsof.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.DataClass.FamiliaProductora
import com.sample.foo.labsof.DataClass.Quinta
import com.sample.foo.labsof.Listados.ListQuintas
import com.sample.foo.labsof.R

class QuintaAdapter(val listaQuinta: List<Quinta>, val listaFamilia: List<FamiliaProductora>, private val editOnClickListener:(Quinta) -> Unit, private val deleteOnClickListener:(Quinta) -> Unit): RecyclerView.Adapter<QuintaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuintaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return QuintaViewHolder(layoutInflater.inflate(R.layout.item_quinta,parent,false))
    }

    override fun onBindViewHolder(holder: QuintaViewHolder, position: Int) {
        val item = listaQuinta[position]
        val itemF = listaFamilia.find { it.id_fp == item?.fpId }
        holder.render(item!!,itemF!!, editOnClickListener,deleteOnClickListener)
    }

    override fun getItemCount(): Int {
        return listaQuinta.size
    }

}