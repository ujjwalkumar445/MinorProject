package com.example.minorproject.timeline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.minorproject.R
import kotlinx.android.synthetic.main.activity_home_page.*

/**
 * A simple [Fragment] subclass.
 */
class Timeline : Fragment() {

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        activity!!.menu.setOnItemSelectedListener {
            when (it) {
                R.id.home -> navController.navigate(R.id.action_timeline2_to_home2)
                R.id.profile -> navController.navigate(R.id.action_timeline2_to_userProfile)
            }
        }
    }

}
