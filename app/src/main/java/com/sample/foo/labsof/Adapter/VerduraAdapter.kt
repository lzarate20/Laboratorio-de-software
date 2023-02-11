package com.sample.foo.labsof.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.Verdura
import com.sample.foo.labsof.R
import com.sample.foo.labsof.databinding.ItemVerduraBinding

class VerduraAdapter(val listaVerdura:List<Verdura>,val listaSelected: List<Verdura>? = null): RecyclerView.Adapter<VerduraAdapter.VerduraViewHolder>() {


    private val verdurasMap = hashMapOf<Int,String>()

    fun getData(): HashMap<Int, String> {
        return verdurasMap
    }

    inner class VerduraViewHolder(private val binding: ItemVerduraBinding): RecyclerView.ViewHolder(binding.root)  {

        fun render(vegetal: Verdura,position: Int,isSelected:Boolean = false){
            binding.inputVegetal.text = vegetal.nombre.toString()
            binding.inputVegetal.isChecked = isSelected
            binding.inputVegetal.setBackgroundResource(
                if (binding.inputVegetal.isChecked)
                    android.R.color.holo_green_dark
                else
                    android.R.color.white
            )
            binding.inputVegetal.setOnClickListener {
                binding.inputVegetal.isChecked = !binding.inputVegetal.isChecked
                binding.inputVegetal.setBackgroundResource(
                    if (binding.inputVegetal.isChecked)
                        android.R.color.holo_green_dark
                    else
                        android.R.color.white
                )
                verdurasMap.put(position,it.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerduraViewHolder {
        val binding = ItemVerduraBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VerduraViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VerduraViewHolder, position: Int) {
        val item = listaVerdura[position]
        var isSelected = false

        if(listaSelected!=null) {
            isSelected = listaSelected!!.any{it.id_verdura == item.id_verdura}
        }
        holder.render(item,position,isSelected)
    }

    override fun getItemCount(): Int {
        return listaVerdura.size
    }
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

}