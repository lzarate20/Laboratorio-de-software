package com.sample.foo.labsof.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.ParcelaVerdura
import com.sample.foo.labsof.DataClass.Verdura
import com.sample.foo.labsof.DataClass.VerduraFechaList
import com.sample.foo.labsof.R
import com.sample.foo.labsof.databinding.ItemVerduraBinding


class VerduraAdapter(var listaVerdura:MutableList<ParcelaVerdura>,var listaSelected: MutableList<VerduraFechaList>? = null): RecyclerView.Adapter<VerduraAdapter.VerduraViewHolder>() {


    private val verdurasMap = hashMapOf<Int,String>()

    fun getData(): HashMap<Int, String> {
        return verdurasMap
    }

    inner class VerduraViewHolder(private val binding: ItemVerduraBinding): RecyclerView.ViewHolder(binding.root)  {

        fun render(parcela: ParcelaVerdura,position: Int,isSelected:Boolean = false){
            binding.inputVegetal.text = parcela.verdura!!.nombre.toString()
            binding.inputVegetal.isChecked = isSelected
            binding.inputVegetal.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked && !parcela.cosecha!!){
                Toast.makeText(buttonView.context,"${parcela.verdura!!.nombre.toString()} no se encontraba cosechada en la ultima visita",Toast.LENGTH_SHORT).show()
                }
                if (isChecked) {
                    verdurasMap.put(parcela.verdura!!.id_verdura!!, buttonView.text.toString())
                }
                else{
                    verdurasMap.remove(parcela.verdura!!.id_verdura!!)
                }
            }
            if (binding.inputVegetal.isChecked) {
                verdurasMap.put(parcela.verdura!!.id_verdura!!, parcela.verdura!!.nombre.toString())
            }
            else{
                verdurasMap.remove(parcela.verdura!!.id_verdura!!)
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
            isSelected = listaSelected!!.any{ it.id_verdura == item.verdura!!.id_verdura}
            if (!verdurasMap.isEmpty() && !isSelected){
                isSelected = verdurasMap.containsValue(item.verdura?.nombre)
            }
        }
        else if (!verdurasMap.isEmpty() && !isSelected){
            isSelected = verdurasMap.containsValue(item.verdura?.nombre)
        }
        holder.render(item,position,isSelected)
    }

    override fun getItemCount(): Int {
        return listaVerdura.size
    }
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()


}