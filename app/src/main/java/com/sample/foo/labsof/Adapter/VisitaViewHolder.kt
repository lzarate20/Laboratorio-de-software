package com.sample.foo.labsof.Adapter

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sample.foo.labsof.Coneccion.ParcelaConeccion
import com.sample.foo.labsof.Coneccion.VisitaConeccion
import com.sample.foo.labsof.EditarVerduraActivity
import com.sample.foo.labsof.Listados.ListVisita
import com.sample.foo.labsof.R
import com.sample.foo.labsof.VerVisita
import com.sample.foo.labsof.helpers.DialogHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.launch

class VisitaViewHolder(itemView: View) : ViewHolder(itemView) {

    val fecha = itemView.findViewById<TextView>(R.id.fecha)

    val quinta = itemView.findViewById<TextView>(R.id.quinta)

    val tecnico = itemView.findViewById<TextView>(R.id.tecnico)
    val ver = itemView.findViewById<Button>(R.id.ver)
    val eliminar = itemView.findViewById<Button>(R.id.eliminar)
    fun render(
        visitaModel: ListVisita.Union,
        registerForActivityResult: ActivityResultLauncher<Intent>,
        ac: AppCompatActivity
    ) {
        fecha.text = "Fecha: ${(visitaModel.fecha)}"
        quinta.text = "Quinta: ${(visitaModel.nombre_q)}"
        tecnico.text = "Tecnico: ${(visitaModel.nombre)}"
        if (visitaModel.futura) {
            ver.text = "Actualizar"
            eliminar.visibility = View.VISIBLE
            ver.setOnClickListener{
                val intent = Intent(itemView.context, VerVisita::class.java)
                intent.putExtra("id", visitaModel.id)
                registerForActivityResult.launch(intent)
            }
        } else {
            ver.text = "Ver"
            ver.setOnClickListener { v ->
                val intent = Intent(v.context, VerVisita::class.java)
                val bun = Bundle()
                intent.putExtra("id", visitaModel.id)
                startActivity(v.context, intent, bun)
            }
        }
        eliminar.setOnClickListener { v ->
            DialogHelper.dialogo(ac,
            "¿Eliminar?",
                "¿Esta seguro que desea eliminar esta visita?",
            true,true,
                {
                    val dCreate= DialogHelper.espera(ac)
                    dCreate.show()
                    ac.lifecycleScope.launch {
                        val el=VisitaConeccion.delete(visitaModel.id)
                        dCreate.dismiss()
                        if(el==true){
                            DialogHelper.dialogo(ac,
                            "Eliminación exitosa",
                                "Se elimino con exito la parcela",
                                true,false,{
                                    val intent = Intent(v.context, VerVisita::class.java)
                                    ac.finish()
                                    ac.overridePendingTransition(0, 0)
                                    ac.startActivity(intent)
                                    ac.overridePendingTransition(0, 0)
                                },{}
                            )
                        }else{
                            DialogHelper.dialogo(ac,
                            "Error","No se pudo eliminar la vista",
                            true,false,{},{})
                        }
                    }
                },{})
        }

    }
}