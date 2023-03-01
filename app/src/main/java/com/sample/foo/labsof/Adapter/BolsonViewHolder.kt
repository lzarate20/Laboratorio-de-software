package com.sample.foo.labsof.Adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.Quinta
import com.sample.foo.labsof.databinding.ItemBolsonBinding


class BolsonViewHolder(view:View):ViewHolder(view) {
    val binding = ItemBolsonBinding.bind(view)


    fun render(bolson: Bolson, quinta: Quinta, editOnClickListener:(Bolson) -> Unit, deleteOnClickListener:(Bolson) -> Unit){
        binding.familiaProductora.text = quinta.nombre.toString()
        binding.cantidad.text = bolson.cantidad.toString()
        binding.editarBolson.setOnClickListener{editOnClickListener(bolson)}
        binding.delete.setOnClickListener{
            deleteOnClickListener(bolson)
        }
    }
}