package com.sample.foo.labsof.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.internal.ContextUtils.getActivity
import com.sample.foo.labsof.*
import com.sample.foo.labsof.Coneccion.VerduraConeccion
import com.sample.foo.labsof.DataClass.VerduraFechaList
import com.sample.foo.labsof.helpers.DialogHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LVerduraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val nombre = itemView.findViewById<TextView>(R.id.nombre)

    val cosecha = itemView.findViewById<TextView>(R.id.cosecha)

    val siembra = itemView.findViewById<TextView>(R.id.siembra)
    val actualizar = itemView.findViewById<Button>(R.id.actualizar)
    val eliminar = itemView.findViewById<Button>(R.id.eliminar)
    val image = itemView.findViewById<ImageView>(R.id.image)

    @SuppressLint("RestrictedApi")
    fun render(verdura: VerduraFechaList, registerForActivityResult: ActivityResultLauncher<Intent>,ac:AppCompatActivity) {
        nombre.text = verdura.nombre
        cosecha.text = verdura.mes_cosecha()
        siembra.text = verdura.mes_siembra()


        Glide.with(itemView.context).load(verdura.archImg).error(R.drawable.icon_image).into(image)


        actualizar.setOnClickListener { v ->
            val intent = Intent(itemView.context, EditarVerduraActivity::class.java)
            intent.putExtra("id", verdura.id_verdura)
            registerForActivityResult.launch(intent)
        }
        eliminar.setOnClickListener { v ->
            DialogHelper.dialogo(v.context,
                "¿Eliminar?",
                "¿Esta seguro que desea eliminar esta verdura?",
                true,
                true,
                {

                    val activity = getActivity(itemView.context)!!
                    ac.lifecycleScope.launch {
                        val bCreate = DialogHelper.espera(itemView.context)
                        bCreate.show()
                        val delete = verdura.id_verdura.let { VerduraConeccion.delete(it!!) }
                        bCreate.dismiss()
                        if (delete == true) {
                            val intent = Intent(activity, ListadoVerdurasActivity::class.java)
                            activity.finish()
                            activity.overridePendingTransition(0, 0)
                            activity.startActivity(intent)
                            activity.overridePendingTransition(0, 0)
                        } else {
                            DialogHelper.dialogo(itemView.context,
                                "Error", "No se pudo eliminar la verdura", true, false, {}, {})
                        }
                    }
                },
                {})
        }

    }

}