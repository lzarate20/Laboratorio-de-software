package com.sample.foo.labsof.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.Listados.ListUsers
import com.sample.foo.labsof.Listados.ListVerduras
import com.sample.foo.labsof.R

class LVerduraAdapter(private val listaVerdura: ListVerduras) :
    RecyclerView.Adapter<LVerduraViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LVerduraViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return LVerduraViewHolder(layoutInflater.inflate(R.layout.item_lverdura, parent, false))
    }

    override fun onBindViewHolder(holder: LVerduraViewHolder, position: Int) {
        val item = listaVerdura.verduras!!.get(position)
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return listaVerdura.size()

    }
}