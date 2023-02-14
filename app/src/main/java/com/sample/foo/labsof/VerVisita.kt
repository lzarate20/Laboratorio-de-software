package com.sample.foo.labsof

import android.content.DialogInterface
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
import com.sample.foo.labsof.helpers.FechaDialog
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class VerVisita : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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
        var id: Int = this.intent.getIntExtra("id", 0)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerPacela)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val agregar = findViewById<Button>(R.id.agregarP)
        val guardar = findViewById<Button>(R.id.guardar)
        var visita: VisitaFechaList? = null
        var tecnicos: ListUsers? = null
        var quintas: ListQuintas? = null
        val editT = findViewById<Spinner>(R.id.editT)
        val editF = findViewById<EditText>(R.id.editF)
        val editQ = findViewById<Spinner>(R.id.editQ)
        lifecycleScope.launch {
            visita = VisitaConeccion.getSingle(id)
            tecnicos = UserConeccion.get().getTecnicos()
            quintas = QuintaConeccion.get()
            if (!visita!!.visitaPasada()) {
                guardar.visibility = View.VISIBLE
            }
            if (visita!!.esHoy()) {
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
                agregar.visibility = View.VISIBLE
            } else {
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
            var verduras = VerduraConeccion.get()
            recyclerView.adapter = ParcelaAdapter(visita!!.parcelas, visita!!.esHoy(),verduras,this@VerVisita)
            recyclerView.visibility = View.VISIBLE

            val es = findViewById<TextView>(R.id.esperando)
            es.visibility = View.GONE
        }


        guardar.setOnClickListener {
            val builder: android.app.AlertDialog.Builder =
                android.app.AlertDialog.Builder(this)
            builder.setTitle("¿Guardar?")
            builder.setMessage("¿Desea guardar los cambios?")
            builder.setPositiveButton("Si",
                DialogInterface.OnClickListener { dialog, which ->
                    lifecycleScope.launch {
                        var vi = Visita()
                        vi?.id_visita = visita?.id_visita
                        vi?.id_tecnico = tecnicos?.users?.get(editT.selectedItemPosition)?.id_user
                        vi?.id_quinta = visita?.id_quinta
                        vi?.fecha_visita = ConversorDate.convertToBD(editF.text.toString())
                        val v = VisitaConeccion.put(vi)
                        val builder: android.app.AlertDialog.Builder =
                            android.app.AlertDialog.Builder(this@VerVisita)
                        if (v.error != null) {
                            builder.setTitle("Error")
                            builder.setMessage(v.error)
                            builder.setPositiveButton("Ok",
                                DialogInterface.OnClickListener { dialog, which ->
                                    dialog.dismiss()
                                })

                        } else {
                            builder.setTitle("Guardado Exitoso")
                            builder.setMessage("Se guardaron correctamente los cambios")
                            builder.setPositiveButton("Listo",
                                DialogInterface.OnClickListener { dialog, which ->
                                    finish()

                                })
                        }

                        builder.create()?.show()
                    }
                })
            builder.setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
            builder.create()?.show()
        }
        agregar.setOnClickListener {
            val builder: android.app.AlertDialog.Builder =
                android.app.AlertDialog.Builder(this)
            lifecycleScope.launch {
                val verduras = VerduraConeccion.get()

                builder.setTitle("¿Guardar?")
                var linearLayout = LinearLayout(this@VerVisita)
                linearLayout.orientation = LinearLayout.VERTICAL
                var l: MutableList<LinearLayout> = mutableListOf()
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
                var lista =
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
                            var cantidad_surcos: Int = surcos.text.toString().toInt()
                            var p = Parcela(
                                id_visita = visita?.id_visita,
                                cantidad_surcos = cantidad_surcos,
                                cubierta = cubierto.isChecked == true,
                                cosecha = cosechado.isChecked == true,
                                id_verdura = verduras?.get(verdura.selectedItemPosition)?.id_verdura
                            )
                            println(cosechado.isChecked == true)
                            lifecycleScope.launch {
                                val par = ParcelaConeccion.post(p)
                                if (par != null) {
                                    println("no null")
                                    var mParcelas = mutableListOf<ParcelaVerdura>()
                                    visita?.parcelas?.let { it1 ->
                                        mParcelas.addAll(it1)
                                    }
                                    mParcelas.add(par)
                                    visita?.parcelas = mParcelas
                                    recyclerView.adapter =
                                        ParcelaAdapter(visita!!.parcelas, visita!!.esHoy(), verduras,this@VerVisita)
                                } else {
                                }
                            }


                        } else {
                            val builder: android.app.AlertDialog.Builder =
                                android.app.AlertDialog.Builder(this@VerVisita)
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

    private fun creacionSpinner(spinner: Spinner, list: Array<String>) {
        val mSortAdapter: ArrayAdapter<CharSequence> = ArrayAdapter(
            baseContext,
            android.R.layout.simple_spinner_item,
            list
        )
        mSortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = mSortAdapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
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