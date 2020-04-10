package com.example.minorproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.ButtonBarLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation

/**
 * A simple [Fragment] subclass.
 */
class LoginScreen : Fragment(), View.OnClickListener{

    var navController : NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.login_button).setOnClickListener(this)
        view.findViewById<Button>(R.id.not_a_member_signup_button).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.login_button -> navController!!.navigate(R.id.action_loginScreen_to_homeFragment)
            R.id.not_a_member_signup_button -> navController!!.navigate(R.id.action_loginScreen_to_signUpScreen)
        }

    }

}
