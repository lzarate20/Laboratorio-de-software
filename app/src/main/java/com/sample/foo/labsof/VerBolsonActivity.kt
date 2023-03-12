package com.sample.foo.labsof

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Adapter.VerduraAdapter
import com.sample.foo.labsof.Coneccion.BolsonConeccion
import com.sample.foo.labsof.Coneccion.FamiliaProductoraConeccion
import com.sample.foo.labsof.Coneccion.QuintaConeccion
import com.sample.foo.labsof.DataClass.Bolson
import com.sample.foo.labsof.DataClass.FamiliaProductora
import com.sample.foo.labsof.DataClass.VerduraFechaList
import com.sample.foo.labsof.databinding.ActivityVerBolsonBinding
import com.sample.foo.labsof.helpers.DialogHelper
import kotlinx.coroutines.launch


class VerBolsonActivity: AppCompatActivity() {

    lateinit var binding: ActivityVerBolsonBinding
    lateinit var  adapter: VerduraAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerBolsonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val FT: FragmentTransaction = supportFragmentManager.beginTransaction()
        val toolbar: Fragment = ToolbarFragment()
        val bun = Bundle()
        bun.putString("toolbar", "2")
        toolbar.setArguments(bun)
        FT.add(R.id.toolbar, toolbar)
        FT.commit()
        val bolson_id:Int = intent.getIntExtra("bolson",-1)
        val dCreate = DialogHelper.espera(this@VerBolsonActivity)
        dCreate.show()
        lifecycleScope.launch{
            var bolson = BolsonConeccion.getBolson(bolson_id)
            var quinta = QuintaConeccion.get().quintas!!.first{ it.fpId == bolson!!.idFp }
            dCreate.dismiss()
            var verduras = bolson!!.verduras!!.map { it.nombre }.toMutableList()
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this@VerBolsonActivity,R.layout.item_ver_verdura,R.id.nombre,verduras)
            binding.verduras.adapter = adapter
            binding.verduras.divider = null
            binding.cantidad.text = bolson.cantidad.toString()
            binding.quinta.text = quinta.nombre.toString()
        }


    }

}