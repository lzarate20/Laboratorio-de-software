package com.sample.foo.labsof

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.content.Intent
import com.sample.foo.labsof.helpers.Session


class Menu : Fragment() {

    companion object {
        fun newInstance() = Menu()
    }

    private lateinit var viewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        val bolsones = view.findViewById<Button>(R.id.bolsones)
        val visitas: Button = view.findViewById<Button>(R.id.visitas)
        val verduras: Button = view.findViewById(R.id.verduras)
        val user: Button = view.findViewById(R.id.users)
        if (activity?.let { Session(it).getSession().isTecnico() } == true) {
            user.visibility = View.GONE
        }
        //Asigna listener para poder abrir Activity.
        bolsones.setOnClickListener { view: View ->
            val intent = Intent(activity, Bolsones::class.java)
            activity?.startActivity(intent)
        }

        visitas.setOnClickListener { view: View ->
            val intent = Intent(activity, Visitas::class.java)
            activity?.startActivity(intent)
        }

        user.setOnClickListener { view: View ->
            val intent = Intent(activity, UserActivity::class.java)
            activity?.startActivity(intent)
        }
        verduras.setOnClickListener { view: View ->
            val intent = Intent(activity, VerduraActivity::class.java)
            activity?.startActivity(intent)
        }
        val quintas : Button =view.findViewById<Button>(R.id.familias)
        quintas.setOnClickListener{ view: View ->
            val intent = Intent (activity , QuintasActivity::class.java)
            activity?.startActivity(intent)
        }
        val rondas : Button =view.findViewById<Button>(R.id.rondas)
        rondas.setOnClickListener{ view: View ->
            val intent = Intent (activity , RondaActivity::class.java)
            activity?.startActivity(intent)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)
        // TODO: Use the ViewModel


    }


}