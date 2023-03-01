package com.sample.foo.labsof.Adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sample.foo.labsof.DataClass.FamiliaProductora
import com.sample.foo.labsof.DataClass.Quinta
import com.sample.foo.labsof.databinding.ItemQuintaBinding

class QuintaViewHolder(view: View) : ViewHolder(view)  {
    val binding = ItemQuintaBinding.bind(view)

    fun render(quinta: Quinta, familia: FamiliaProductora, editOnClickListener:(Quinta) -> Unit,routeOnClickListener:(Quinta) -> Unit, deleteOnClickListener:(Quinta) -> Unit){
        binding.nombreQ.text = quinta.nombre.toString()
        binding.direccion.text = quinta.direccion.toString()
        binding.nombreF.text = familia.nombre.toString()
        binding.editarQuinta.setOnClickListener{editOnClickListener(quinta)}
        binding.rutaQuinta.setOnClickListener{routeOnClickListener(quinta)}
        binding.delete.setOnClickListener{
            deleteOnClickListener(quinta)
        }
    }


}