package com.sample.foo.labsof.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.sample.foo.labsof.DataClass.Ronda
import com.sample.foo.labsof.databinding.ItemRondaBinding


class RondaAdapter(val listaRonda:List<Ronda>,val viewOnClickListener:(Ronda) -> Unit,val deleteOnClickListener:(Ronda) -> Unit):RecyclerView.Adapter<RondaAdapter.RondaViewHolder>() {


    inner class RondaViewHolder(private val binding: ItemRondaBinding): RecyclerView.ViewHolder(binding.root)  {
        fun render(ronda: Ronda, viewOnClickListener:(Ronda) -> Unit, deleteOnClickListener:(Ronda) -> Unit){
            if(Ronda.Compare.isAfterToday(ronda)){
                binding.rondaFinalizada.visibility = View.GONE
                binding.ver.visibility = View.GONE
                binding.edit.setOnClickListener { viewOnClickListener(ronda) }
                binding.delete.setOnClickListener{deleteOnClickListener(ronda)}

            }else{
                binding.delete.visibility = View.GONE
                binding.edit.visibility = View.GONE
                binding.ver.setOnClickListener { viewOnClickListener(ronda) }
            }
            binding.fechaInicio.text = String.format("%s/%s/%s",ronda.fecha_inicio[2],ronda.fecha_inicio[1],ronda.fecha_inicio[0])
            binding.fechaFin.text = String.format("%s/%s/%s",ronda.fecha_fin[2],ronda.fecha_fin[1],ronda.fecha_fin[0])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RondaViewHolder {
        val binding = ItemRondaBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RondaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RondaViewHolder, position: Int) {
        val item = listaRonda[position]
        holder.render(item,viewOnClickListener, deleteOnClickListener)
    }

    override fun getItemCount(): Int {
        return listaRonda.size
    }

}