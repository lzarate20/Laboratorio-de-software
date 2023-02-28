package com.sample.foo.labsof.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.Coneccion.Coneccion
import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.Service.BolsonService
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.DataClass.Quinta
import com.sample.foo.labsof.R
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class BolsonAdapter(val listaBolsones:List<Bolson>, val listaQuintas:List<Quinta>, private val editOnClickListener:(Bolson) -> Unit, private val deleteOnClickListener:(Bolson) -> Unit): RecyclerView.Adapter<BolsonViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BolsonViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BolsonViewHolder(layoutInflater.inflate(R.layout.item_bolson,parent,false))
    }

    override fun onBindViewHolder(holder: BolsonViewHolder, position: Int) {
            val item = listaBolsones[position]
            val itemQ = listaQuintas.find { it.fpId == item.idFp }!!
            holder.render(item,itemQ, editOnClickListener,deleteOnClickListener)
    }

    override fun getItemCount(): Int {
        return listaBolsones.size
    }



}