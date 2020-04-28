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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.minorproject.HomePage
import com.example.minorproject.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login_screen.*
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass.
 */
class LoginScreen : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    private lateinit var mAuth: FirebaseAuth

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

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.not_a_member_signup_button -> navController!!.navigate(
                R.id.action_loginScreen_to_signUpScreen
            )

            R.id.login_button -> {
                if (TextUtils.isEmpty(emailEditTextView.text)) {
                    emailEditTextView.error = "Please Enter the Email first"
                } else if (!validEmail(emailEditTextView.text.toString())) {
                    emailEditTextView.error = "Invalid Email. Please try again"
                } else if (TextUtils.isEmpty(passwordEditTextView.text)) {
                    passwordEditTextView.error = "please Enter the Password first"
                } else if (!(validPassword(passwordEditTextView.text.toString())
                            && passwordEditTextView.text!!.length >= 10)
                ) {
                    passwordEditTextView.error = "Must have at least 10 Characters"
                } else {
                    mAuth.signInWithEmailAndPassword(
                        emailEditTextView.text.toString(),
                        passwordEditTextView.text.toString()
                    )
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "SignInWithEmail : Success")
                                updateUi()
                            } else {
                                Log.w(TAG, "UserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    activity, "Log In failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }

    }

    private fun validEmail(email: String): Boolean {
        val Pattern: Pattern = Patterns.EMAIL_ADDRESS
        return Pattern.matcher(email).matches()
    }

    private fun validPassword(password: String?): Boolean {
        val Pattern: Pattern =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")
        return Pattern.matcher(password).matches()
    }

    private fun updateUi() {
        val intent = Intent(this.context, HomePage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}
