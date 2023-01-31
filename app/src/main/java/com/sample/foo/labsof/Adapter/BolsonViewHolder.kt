package com.sample.foo.labsof.Adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.R
import org.w3c.dom.Text

class BolsonViewHolder(view:View):ViewHolder(view) {
    val familiaProductora = view.findViewById<TextView>(R.id.familiaProductora)
    fun render(bolson: Bolson){
        familiaProductora.text = bolson.id_fp.toString()
    }
}