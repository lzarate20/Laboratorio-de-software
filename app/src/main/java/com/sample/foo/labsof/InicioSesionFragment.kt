package com.sample.foo.labsof

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.sample.foo.labsof.Coneccion.UserConeccion
import com.sample.foo.labsof.DataClass.User
import com.sample.foo.labsof.helpers.DialogHelper
import com.sample.foo.labsof.helpers.Session
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InicioSesionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InicioSesionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_inicio_sesion, container, false)
        val username = view.findViewById<EditText>(R.id.username)
        val pass = view.findViewById<EditText>(R.id.pass)
        val iniciar = view.findViewById<Button>(R.id.iniciar)
        iniciar.setOnClickListener { view: View ->
            val bCreate = DialogHelper.espera(view.context)
            bCreate.show()
            lifecycleScope.launch{
                val user = UserConeccion.auth(User(username.text.toString(), pass.text.toString()))
                bCreate.dismiss()
                if(user.error == null){
                    val activity = activity as Activity
                    Session(activity).saveSession(user)
                    val intent= Intent(view.context,MainActivity::class.java)
                    activity.finish()
                    activity.overridePendingTransition(0, 0)
                    activity.startActivity(intent)
                    activity.overridePendingTransition(0, 0)
                }else{
                    DialogHelper.dialogo(view.context,
                    "Error",user.error!!,
                    true,false,{},{})
                }
            }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InicioSesion.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InicioSesionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}