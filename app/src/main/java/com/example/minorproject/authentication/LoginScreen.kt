package com.example.minorproject.authentication

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.minorproject.HomePage
import com.example.minorproject.R
import com.example.minorproject.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login_screen.*
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass.
 */
class LoginScreen : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    private lateinit var mAuth: FirebaseAuth

    private val mLoginViewModel by lazy {
        ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setObservers()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.not_a_member_signup_button -> navController!!.navigate(
                R.id.action_loginScreen_to_signUpScreen
            )

            R.id.login_button -> mLoginViewModel.onLoginClicked(
                emailEditTextView.text.toString(),
                passwordEditTextView.text.toString()
            ).observe(viewLifecycleOwner, Observer {
                if (it == true) {
                    newScreen()

                }
            })


        }

    }


    private fun setObservers() {

        mLoginViewModel.getErrMessage().observe(viewLifecycleOwner, Observer {
            // Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            emailEditTextView.setError(it)
            passwordEditTextView.setError(it)
        })


    }
    

    private fun newScreen() {
        val intent = Intent(
            this.context,
            HomePage::class.java
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity?.finish()
    }

}
