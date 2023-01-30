package com.sample.foo.labsof

import android.os.Bundle
import android.view.*
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction


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
        val f = getActivity() as AppCompatActivity
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
        FT.add(R.id.tool,toolbar)
        FT.commit()

        return view
    }

    

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

}