package com.sample.foo.labsof.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.Verdura
import com.sample.foo.labsof.R
import com.sample.foo.labsof.databinding.ItemVerduraBinding

class VerduraAdapter(val listaVerdura:List<Verdura>): RecyclerView.Adapter<VerduraAdapter.VerduraViewHolder>() {


    private val verdurasCountMap = hashMapOf<Int,String>()

    fun getData(): HashMap<Int, String> {
        return verdurasCountMap
    }
    inner class VerduraViewHolder(private val binding: ItemVerduraBinding): RecyclerView.ViewHolder(binding.root)  {
        val verdura_nombre = binding.nombreVegetal


        fun render(vegetal: Verdura,position: Int){
            verdura_nombre.text = vegetal.nombre.toString()
            val input = binding.inputVegetal.doAfterTextChanged {
                verdurasCountMap[position] = it.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerduraViewHolder {
        val binding = ItemVerduraBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VerduraViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VerduraViewHolder, position: Int) {
        val item = listaVerdura[position]
        holder.render(item,position)
    }

    override fun getItemCount(): Int {
        return listaVerdura.size
    }
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

}