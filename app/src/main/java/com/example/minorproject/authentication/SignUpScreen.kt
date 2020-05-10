package com.example.minorproject.authentication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.minorproject.MainActivity
import com.example.minorproject.R
import com.example.minorproject.viewmodel.signUpViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_sign_up_screen.*

/**
 * A simple [Fragment] subclass.
 */
class SignUpScreen : Fragment(), View.OnClickListener {

    lateinit var navController: NavController
    private val PICK_IMAGE_REQUEST = 1234
    private var filePath: Uri? = null
    private val mSignupViewModel by lazy {
        ViewModelProvider(requireActivity()).get(signUpViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.registerButton).setOnClickListener(this)
        view.findViewById<CircleImageView>(R.id.profile_image).setOnClickListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setSignUpObservers()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.registerButton -> mSignupViewModel.onSignUpClicked(
                name.text.toString(),
                email.text.toString(),
                password.text.toString(),
                filePath
            ).observe(viewLifecycleOwner, Observer {
                if (it == true) {
                    loginScreen()

                }
            })

            R.id.profile_image -> showFileChooser()
        }


    }

    private fun setSignUpObservers() {

        mSignupViewModel.getSignUpErrMessage().observe(viewLifecycleOwner, Observer {
            name.setError(it)
            email.setError(it)
            password.setError(it)
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.getData();
            profile_image.setImageURI(data.data)
        }

    }

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST)
    }

    private fun loginScreen() {
        val intent = Intent(
            this.context,
            MainActivity::class.java
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }


}
