package com.sample.foo.labsof.Adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.R
import com.sample.foo.labsof.databinding.ItemBolsonBinding
import org.w3c.dom.Text

class BolsonViewHolder(view:View):ViewHolder(view) {
    val binding = ItemBolsonBinding.bind(view)
    fun render(bolson: Bolson){
        binding.familiaProductora.text = bolson.id_bolson.toString()
    }
}