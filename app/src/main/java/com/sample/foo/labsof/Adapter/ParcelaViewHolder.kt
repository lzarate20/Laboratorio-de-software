package com.sample.foo.labsof.Adapter

import android.content.DialogInterface
import android.content.Intent
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sample.foo.labsof.Coneccion.ParcelaConeccion
import com.sample.foo.labsof.DataClass.Parcela
import com.sample.foo.labsof.DataClass.ParcelaVerdura
import com.sample.foo.labsof.DataClass.VerduraFechaList
import com.sample.foo.labsof.ListadoVerdurasActivity
import com.sample.foo.labsof.R
import com.sample.foo.labsof.VerVisita
import com.sample.foo.labsof.helpers.DialogHelper
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
        Glide.with(itemView.context).load(parcela.verdura?.archImg).error(R.drawable.icon_image).into(image)
        if (actualizar){
            ver.text="Actualizar"
            eliminar.visibility=View.VISIBLE
        }else{
            ver.visibility=View.GONE
        }
        eliminar.setOnClickListener{v->
            DialogHelper.dialogo(v.context,
            "¿Eliminar?","¿Esta seguro que desea eliminar esta parcela?",
            true,true,
                {
                    val bCreate= DialogHelper.espera(v.context)
                    bCreate.show()
                    view.lifecycleScope.launch {
                        val eli=ParcelaConeccion.delete(parcela.id_parcela!!)
                        bCreate.dismiss()
                        if (eli==true){
                            DialogHelper.dialogo(v.context,"Eliminación exitosa",
                            "Se elimino con exito la parcela",
                            true,false,
                                {
                                    val intent = Intent(v.context, VerVisita::class.java)
                                    view.finish()
                                    view.overridePendingTransition(0, 0)
                                    view.startActivity(intent)
                                    view.overridePendingTransition(0, 0)},
                                {}
                            )
                        }else{
                            DialogHelper.dialogo(v.context,
                            "Error","No se pudo eliminar la parcela",
                            true,false,{},{})
                        }
                }
                },{}
            )
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
                        val dCreate= DialogHelper.espera(view)
                        dCreate.show()
                        view.lifecycleScope.launch {
                           var par= ParcelaConeccion.put(Parcela(p))
                            dCreate.dismiss()
                            if(par!= null){
                                DialogHelper.dialogo(view,
                                "Actualizacion exitosa",
                                    "La parcela se actualizo con exito",
                                true,false,
                                    {
                                        val intent = Intent(v.context, VerVisita::class.java)
                                        view.finish()
                                        view.overridePendingTransition(0, 0)
                                        view.startActivity(intent)
                                        view.overridePendingTransition(0, 0)
                                    },{})
                            }else{
                                DialogHelper.dialogo(view,"Error",
                                    "No se pudo actualizar la parcela",true,
                                false,{},{})
                            }
                        }
                    } else {
                        DialogHelper.dialogo(view,
                        "Error","La cantidad de surcos no puede ser menor a 1",
                        true,false,{},{})
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