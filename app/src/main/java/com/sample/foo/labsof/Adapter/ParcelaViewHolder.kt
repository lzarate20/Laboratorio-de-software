package com.sample.foo.labsof.Adapter

import android.content.DialogInterface
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.Coneccion.ParcelaConeccion
import com.sample.foo.labsof.DataClass.Parcela
import com.sample.foo.labsof.DataClass.ParcelaVerdura
import com.sample.foo.labsof.DataClass.VerduraFechaList
import com.sample.foo.labsof.R
import com.sample.foo.labsof.VerVisita
import kotlinx.coroutines.launch


class ParcelaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val image=itemView.findViewById<ImageView>(R.id.image)

    val verdura=itemView.findViewById<TextView>(R.id.verdura)

    val cosecha=itemView.findViewById<CheckBox>(R.id.cosecha)
    val cubierta= itemView.findViewById<CheckBox>(R.id.cubierta)

    val ver= itemView.findViewById<Button>(R.id.ver)
    val eliminar= itemView.findViewById<Button>(R.id.eliminar)

    fun render( parcela:ParcelaVerdura, actualizar:Boolean, verduras: List<VerduraFechaList>?, view:VerVisita){

        cosecha.isChecked= parcela.cosecha == true
        cubierta.isChecked= parcela.cubierta == true
        verdura.text= parcela.verdura?.nombre
        if (actualizar){
            ver.text="Actualizar"
            eliminar.visibility=View.VISIBLE
        }else{
            ver.visibility=View.GONE
        }
        eliminar.setOnClickListener{v->
            val builder: android.app.AlertDialog.Builder =
                android.app.AlertDialog.Builder(v.context)
            builder.setTitle("¿Eliminar?")
            builder.setMessage("¿Esta seguro que desea eliminar esta parcela?")
            builder.setPositiveButton(
                "Eliminar",
                DialogInterface.OnClickListener { dialog, which ->
                        view.lifecycleScope.launch {
                            parcela.id_parcela?.let { ParcelaConeccion.delete(it) }
                        }
                })
            builder.setNegativeButton(
                "Cancelar",
                DialogInterface.OnClickListener { dialog, which ->

                })
            builder.create().show()
        }
        ver.setOnClickListener{v->
            val builder: android.app.AlertDialog.Builder =
                android.app.AlertDialog.Builder(v.context)
            builder.setTitle("¿Guardar?")
            var linearLayout = LinearLayout(v.context)
            linearLayout.orientation = LinearLayout.VERTICAL
            var l: MutableList<LinearLayout> = mutableListOf()
            for (i: Int in 0..3) {
                l.add(LinearLayout(v.context))
                l[i].orientation = LinearLayout.HORIZONTAL
            }
            val surcos = EditText(v.context)
            surcos.inputType = InputType.TYPE_CLASS_NUMBER
            surcos.width = 200
            parcela?.cantidad_surcos?.let { surcos.setText(it.toString()) }
            var text = TextView(v.context)
            text.text = "Cantidad de surcos: "
            l[0].addView(text)
            l[0].addView(surcos)
            text = TextView(v.context)
            text.text = "Cosechado: "
            val cosechado = CheckBox(v.context)
            cosechado.isChecked = parcela.cosecha == true
            l[1].addView(text)
            l[1].addView(cosechado)
            text = TextView(v.context)
            text.text = "Cubierto: "
            val cubierto = CheckBox(v.context)
            cubierto.isChecked = parcela.cosecha == true
            l[2].addView(text)
            l[2].addView(cubierto)
            text = TextView(v.context)
            text.text = "Verdura: "
            val verdura = Spinner(v.context)
            var lista:Array<String> = verduras?.map { x -> "${(x.nombre)} " }?.toTypedArray()!!
            val mSortAdapter: ArrayAdapter<CharSequence> = ArrayAdapter(
                v.context,
                android.R.layout.simple_spinner_item,
                lista
            )
            mSortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            verdura.adapter = mSortAdapter
            verduras?.let { verdura.setSelection(it.indexOfFirst { u -> u.id_verdura == parcela.verdura?.id_verdura }) }
            l[3].addView(text)
            l[3].addView(verdura)
            for (i: Int in 0..3) {
                linearLayout.addView(l[i])
            }
            builder.setView(linearLayout)
            builder.setPositiveButton(
                "Guardar",
                DialogInterface.OnClickListener { dialog, which ->
                    if (surcos.text.toString() != "" && surcos.text.toString().toInt() >= 1) {
                        var p = ParcelaVerdura(
                           id_visita= parcela.id_visita,
                            cantidad_surcos = surcos.text.toString().toInt(),
                            cubierta = cubierto.isChecked==true,
                            cosecha = cosechado.isChecked==true,
                            verdura = verduras?.get(verdura.selectedItemPosition),
                            id_parcela=parcela.id_parcela
                        )
                        view.lifecycleScope.launch {
                           var par= ParcelaConeccion.put(Parcela(p))
                            if(par!= null){
                                val builder: android.app.AlertDialog.Builder =
                                    android.app.AlertDialog.Builder(v.context)
                                builder.setTitle("Actualizada con exito")
                                builder.setMessage("La parcela se actualizo con exito")
                                builder.setPositiveButton("Ok",
                                    DialogInterface.OnClickListener { dialog, which ->
                                    })
                                builder.create().show()
                            }else{
                                val builder: android.app.AlertDialog.Builder =
                                    android.app.AlertDialog.Builder(v.context)
                                builder.setTitle("Error")
                                builder.setMessage("No se pudo actualizar la parcela")
                                builder.setPositiveButton("Ok",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        dialog.dismiss()
                                    })
                                builder.create().show()
                            }
                        }
                    } else {
                        val builder: android.app.AlertDialog.Builder =
                            android.app.AlertDialog.Builder(v.context)
                        builder.setTitle("Error")
                        builder.setMessage("La cantidad de surcos no puede ser menor a 1")
                        builder.setPositiveButton("Ok",
                            DialogInterface.OnClickListener { dialog, which ->
                                dialog.dismiss()
                            })
                        builder.create().show()

                    }
                })
            builder.setNegativeButton(
                "Cancelar",
                DialogInterface.OnClickListener { dialog, which ->

                })
            builder.create()?.show()
        }



    }
}