package com.sample.foo.labsof

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.content.Intent




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
        val bolsones = view.findViewById<View>(R.id.bolsones) as Button

        //Asigna listener para poder abrir Activity.
        bolsones.setOnClickListener{ view: View ->
            val intent = Intent (activity , Bolsones::class.java)
            activity?.startActivity(intent)
        }
        val visitas : Button =view.findViewById<Button>(R.id.visitas)
        visitas.setOnClickListener{ view: View ->
            val intent = Intent (activity , Visitas::class.java)
            activity?.startActivity(intent)
        }
        val familia : Button =view.findViewById<Button>(R.id.familias)
        familia.setOnClickListener{ view: View ->
            val intent = Intent (activity , QuintasActivity::class.java)
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