package com.sample.foo.labsof.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.Listados.ListVisita
import com.sample.foo.labsof.R

class VisitaAdapter(private val listaVisita: List<ListVisita.Union>,
                    private val registerForActivityResult: ActivityResultLauncher<Intent>,
                    private val ac: AppCompatActivity
) :
    RecyclerView.Adapter<VisitaViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return VisitaViewHolder(layoutInflater.inflate(R.layout.item_visita, parent, false))
    }

    override fun onBindViewHolder(holder: VisitaViewHolder, position: Int) {
        val item = listaVisita.get(position)
        holder.render(item,registerForActivityResult,ac)
    }

    override fun getItemCount(): Int {
        return listaVisita.size

    }


}