package com.sample.foo.labsof

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.Menu
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.sample.foo.labsof.helpers.Session


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "toolbar"


class ToolbarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString("toolbar")
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_toolbar, container, false)
        val f = activity as AppCompatActivity
        val FT: FragmentTransaction = f.supportFragmentManager.beginTransaction()
        f.setSupportActionBar(view.findViewById(R.id.my_toolbar))

        /*f.getSupportActionBar()?.setDisplayShowTitleEnabled(false)*/
        var toolbar: Fragment
        var bool: Boolean

        if (param1.equals("1")) {
            toolbar = ToolbarBasicoFragment()
            bool = false

        } else {
            toolbar = ToolbarMejoradoFragment()
            bool = true


        }

        f.getSupportActionBar()?.setDisplayHomeAsUpEnabled(bool);
        f.getSupportActionBar()?.setDisplayShowHomeEnabled(bool);
        FT.add(R.id.tool, toolbar)
        FT.commit()
        var activity = activity as Activity
        val session=Session(activity)

        var b= view.findViewById<ImageButton>(R.id.menu)
        if(session.haveSesion()){
            b.visibility=View.VISIBLE
            b.setOnClickListener {
                showPopup(it)
            }
        }


        return view
    }




    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val m = R.menu.menu_main
        inflater.inflate(m, menu)
    }
    fun showPopup(view: View) {
        val popup = PopupMenu(view.context, view)
        popup.inflate(R.menu.menu_tooltip)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.cSesion -> {

                }
                R.id.config -> {
                }
                R.id.pass -> {
                }
            }

            true
        })

        popup.show()
    }
}