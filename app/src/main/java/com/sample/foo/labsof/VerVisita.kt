package com.sample.foo.labsof

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.foo.labsof.Adapter.ParcelaAdapter
import com.sample.foo.labsof.Coneccion.*
import com.sample.foo.labsof.DataClass.*
import com.sample.foo.labsof.Listados.ListQuintas
import com.sample.foo.labsof.Listados.ListUsers
import com.sample.foo.labsof.helpers.ConversorDate
import com.sample.foo.labsof.helpers.DialogHelper
import com.sample.foo.labsof.helpers.FechaDialog
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class VerVisita : AppCompatActivity() {
    var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_visita)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "2")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)
        FT.commit()
        id= this.intent.getIntExtra("id", 0)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerPacela)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val agregar = findViewById<Button>(R.id.agregarP)
        val guardar = findViewById<Button>(R.id.guardar)
        var visita: VisitaFechaList? = null
        var tecnicos: ListUsers? = null
        var quintas: ListQuintas?
        val editT = findViewById<Spinner>(R.id.editT)
        val editF = findViewById<EditText>(R.id.editF)
        val editQ = findViewById<Spinner>(R.id.editQ)
        val dCreate = DialogHelper.espera(this@VerVisita)
        dCreate.show()
        lifecycleScope.launch {
            visita = VisitaConeccion.getSingle(id)
            tecnicos = UserConeccion.get().getTecnicos()
            quintas = QuintaConeccion.get()
            dCreate.dismiss()
            if (visita?.error != null && tecnicos?.error != null && quintas?.error != null) {
                DialogHelper.dialogo(this@VerVisita, "Error",
                    "Los datos necesarios para crear una visita no se pudieron obtener",
                    true, false, { finish() }, {})
            }
            if (!visita!!.visitaPasada()&& !visita!!.esHoy()) {
                guardar.visibility = View.VISIBLE
                editF.setText(visita!!.fechaString())
                editF.setOnClickListener {
                    showDatePicker(editF)
                }
                var lista =
                    tecnicos?.users!!.map { x -> "${(x.nombre)} ${(x.apellido)}" }.toTypedArray()
                creacionSpinner(editT, lista)
                tecnicos?.getPos(visita!!.id_tecnico!!)?.let { editT.setSelection(it) }
                lista =
                    quintas?.quintas!!.map { x -> "${(x.nombre)}" }.toTypedArray()
                creacionSpinner(editQ, lista)
                quintas?.getPos(visita!!.id_quinta!!)?.let { editQ.setSelection(it) }
                editT.visibility = View.VISIBLE
                editF.visibility = View.VISIBLE
            } else {

                if (visita!!.esHoy()){
                    agregar.visibility = View.VISIBLE
                    guardar.visibility = View.GONE
                }
                val textT = findViewById<TextView>(R.id.textTx)
                val textF = findViewById<TextView>(R.id.textFx)
                textF.setText(visita!!.fechaString())
                textT.text =
                    "${(tecnicos?.getById(visita!!.id_tecnico!!)?.nombre)} ${
                        (tecnicos?.getById(
                            visita?.id_tecnico!!
                        )?.apellido)
                    }"
            }
            val textQ = findViewById<TextView>(R.id.textQx)
            textQ.text = quintas?.getById(visita!!.id_quinta!!)?.nombre
            val verduras = VerduraConeccion.get()
            recyclerView.adapter =
                ParcelaAdapter(visita!!.parcelas, visita!!.esHoy(), verduras, this@VerVisita)
            recyclerView.visibility = View.VISIBLE
        }


        guardar.setOnClickListener {
            DialogHelper.dialogo(this@VerVisita,
                "¿Guardar?", "¿Desea guardar los cambios?", true, false,
                {
                    val dialog = DialogHelper.espera(this@VerVisita)
                    dialog.show()
                    lifecycleScope.launch {
                        val vi = Visita(visita!!)
                        vi.id_tecnico = tecnicos?.users?.get(editT.selectedItemPosition)?.id_user
                        vi.fecha_visita = ConversorDate.convertToBD(editF.text.toString())
                        val v = VisitaConeccion.put(vi)
                        dialog.dismiss()
                        if (v.error != null) {
                            DialogHelper.dialogo(this@VerVisita, "Error",
                                v.error, true, false, {}, {})
                        } else {
                            DialogHelper.dialogo(this@VerVisita,
                                "Guardado exitoso",
                                "Se guardaron correctamente los cambios",
                                true,
                                false,
                                {
                                    val intent = Intent()
                                    setResult(Activity.RESULT_OK, intent)
                                    finish()
                                },
                                {})
                        }
                    }
                }, {})
        }
        agregar.setOnClickListener {
            val dialog = DialogHelper.espera(this@VerVisita)
            dialog.show()
            var verduras: List<VerduraFechaList>?=null
            lifecycleScope.launch {
                verduras = VerduraConeccion.get()
                dialog.dismiss()
                if(verduras == null){
                    DialogHelper.dialogo(this@VerVisita,
                    "Error","No se pudieron obtener las verduras para la parcela",
                    true,false,{},{})
                }else{
                    val builder: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(this@VerVisita)
                    builder.setTitle("¿Guardar?")
                    val linearLayout = LinearLayout(this@VerVisita)
                    linearLayout.orientation = LinearLayout.VERTICAL
                    val l: MutableList<LinearLayout> = mutableListOf()
                    for (i: Int in 0..3) {
                        l.add(LinearLayout(this@VerVisita))
                        l[i].orientation = LinearLayout.HORIZONTAL
                    }
                    val surcos = EditText(this@VerVisita)
                    surcos.inputType = InputType.TYPE_CLASS_NUMBER
                    surcos.width = 200
                    var text = TextView(this@VerVisita)
                    text.text = "Cantidad de surcos: "
                    l[0].addView(text)
                    l[0].addView(surcos)
                    text = TextView(this@VerVisita)
                    text.text = "Cosechado: "
                    val cosechado = CheckBox(this@VerVisita)
                    l[1].addView(text)
                    l[1].addView(cosechado)
                    text = TextView(this@VerVisita)
                    text.text = "Cubierto: "
                    val cubierto = CheckBox(this@VerVisita)
                    l[2].addView(text)
                    l[2].addView(cubierto)
                    text = TextView(this@VerVisita)
                    text.text = "Verdura: "
                    val verdura = Spinner(this@VerVisita)
                    val lista =
                        verduras?.map { x -> "${(x.nombre)} " }?.toTypedArray()
                    lista?.let { creacionSpinner(verdura, it) }
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
                                val cantidad_surcos: Int = surcos.text.toString().toInt()
                                val p = Parcela(
                                    id_visita = id,
                                    cantidad_surcos = cantidad_surcos,
                                    cubierta = cubierto.isChecked,
                                    cosecha = cosechado.isChecked ,
                                    id_verdura = verduras?.get(verdura.selectedItemPosition)?.id_verdura,
                                    0
                                )
                                val dialog = DialogHelper.espera(this@VerVisita)
                                dialog.show()
                                lifecycleScope.launch {
                                    val par = ParcelaConeccion.post(p)
                                    dialog.dismiss()
                                    if (par != null) {
                                        val intent = Intent(this@VerVisita, VerVisita::class.java)
                                        intent.putExtra("id", id)
                                        finish()
                                        overridePendingTransition(0, 0)
                                        startActivity(intent)
                                        overridePendingTransition(0, 0)
                                    } else {
                                        DialogHelper.dialogo(this@VerVisita,"Error",
                                            "No se pudo guardar la parcela, vuelva a intentarlo.",true,false,{},{})
                                    }
                                }

                            } else {
                                DialogHelper.dialogo(this@VerVisita,"Error",
                                    "La cantidad de surcos no puede ser menor a 1",true,false,{},{})
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
    }

    private fun creacionSpinner(spinner: Spinner, list: Array<String>) {
        val mSortAdapter: ArrayAdapter<CharSequence> = ArrayAdapter(
            baseContext,
            android.R.layout.simple_spinner_item,
            list
        )
        mSortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = mSortAdapter
    }

    private fun showDatePicker(fecha: EditText) {
        val date = ConversorDate.getCurrentDate(fecha.text.toString())
        val hoy = ConversorDate.toLong(LocalDateTime.now())
        FechaDialog.newInstance(
            year = date.year,
            month = date.monthValue,
            day = date.dayOfMonth,
            minDate = hoy
        ) { _, year, month, day ->
            fecha.setText(ConversorDate.formatDate(year, month, day))
        }.show(supportFragmentManager, "date-picker")

    }


}