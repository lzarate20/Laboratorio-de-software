package com.sample.foo.labsof.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.Listados.ListUsers
import com.sample.foo.labsof.Listados.ListVisita
import com.sample.foo.labsof.R

class UserAdapter(private val listaUser: ListUsers) :
    RecyclerView.Adapter<UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserViewHolder(layoutInflater.inflate(R.layout.item_visita, parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = listaUser.users!!.get(position)
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return listaUser.size()

    }
}