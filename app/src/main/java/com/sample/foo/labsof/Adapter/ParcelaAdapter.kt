package com.sample.foo.labsof.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.DataClass.ParcelaVerdura
import com.sample.foo.labsof.DataClass.VerduraFechaList
import com.sample.foo.labsof.DataClass.VisitaFechaList
import com.sample.foo.labsof.R
import com.sample.foo.labsof.VerVisita

class ParcelaAdapter(
    private val listaParcela: List<ParcelaVerdura>?,
    private val v: VisitaFechaList,
    private val actualizar: Boolean,
    private val verduras: List<VerduraFechaList>?,
    private  val view: VerVisita
) :
    RecyclerView.Adapter<ParcelaViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParcelaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ParcelaViewHolder(layoutInflater.inflate(R.layout.item_parcela, parent, false))
    }

    override fun onBindViewHolder(holder: ParcelaViewHolder, position: Int) {
        val item = listaParcela?.get(position)
        holder.render(item!!,v, actualizar,verduras,view)
    }

    override fun getItemCount(): Int {
        return listaParcela?.size!!

    }


}