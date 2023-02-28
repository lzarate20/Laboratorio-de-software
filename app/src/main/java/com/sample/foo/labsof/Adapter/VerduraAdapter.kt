package com.sample.foo.labsof.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.Verdura
import com.sample.foo.labsof.DataClass.VerduraFechaList
import com.sample.foo.labsof.R
import com.sample.foo.labsof.databinding.ItemVerduraBinding

class VerduraAdapter(var listaVerdura:List<VerduraFechaList>,var listaSelected: List<VerduraFechaList>? = null): RecyclerView.Adapter<VerduraAdapter.VerduraViewHolder>() {


    private val verdurasMap = hashMapOf<Int,String>()

    fun set(listaV: List<VerduraFechaList>) {
        this.listaVerdura = listaV
        this.notifyDataSetChanged()
    }
    fun getData(): HashMap<Int, String> {
        return verdurasMap
    }

    inner class VerduraViewHolder(private val binding: ItemVerduraBinding): RecyclerView.ViewHolder(binding.root)  {

        fun render(vegetal: VerduraFechaList,position: Int,isSelected:Boolean = false){
            binding.inputVegetal.text = vegetal.nombre.toString()
            binding.inputVegetal.isChecked = isSelected
            if (binding.inputVegetal.isChecked) {
                verdurasMap.put(position, vegetal.nombre.toString())
            }
            else{
                verdurasMap.remove(position)
            }
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
                if (binding.inputVegetal.isChecked) {
                    verdurasMap.put(position, it.toString())
                }
                else{
                    verdurasMap.remove(position)
                }
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