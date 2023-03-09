package com.sample.foo.labsof.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.Listados.ListUsers
import com.sample.foo.labsof.Listados.ListVisita
import com.sample.foo.labsof.R

class UserAdapter(private val listaUser: ListUsers,
                  private val registerForActivityResult: ActivityResultLauncher<Intent>,
                  private val ac: AppCompatActivity
) :
    RecyclerView.Adapter<UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserViewHolder(layoutInflater.inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = listaUser.users!!.get(position)
        holder.render(item,registerForActivityResult,ac)
    }

    override fun getItemCount(): Int {
        return listaUser.size()

    }
}