package com.example.minorproject.authentication

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
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
import com.example.minorproject.MainActivity
import com.example.minorproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_sign_up_screen.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass.
 */
class SignUpScreen : Fragment(), View.OnClickListener {

    lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var mcollection: CollectionReference

    lateinit var email1: String
    lateinit var password1: String
    lateinit var name1: String
    private lateinit var Storage: FirebaseStorage
    private lateinit var mStorageReference: StorageReference
    private val PICK_IMAGE_REQUEST = 1234
    private var filePath: Uri? = null
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        mcollection = db.collection("User")
        Storage = FirebaseStorage.getInstance()
        mStorageReference = Storage.reference

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

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.registerButton -> {
                if (TextUtils.isEmpty(name.text)) {
                    name.error = "UserName should not be empty"
                } else if (TextUtils.isEmpty(email.text)) {
                    email.error = "Email Address Should not be empty"
                } else if (!validEmail(email.text.toString())) {
                    email.error = "Invalid Email"
                } else if (TextUtils.isEmpty(password.text)) {
                    password.error = "Password field Empty"
                } else if (!(validPassword(password.text.toString())
                            && password.text!!.length >= 10)
                ) {
                    password.error = "Must have at least 10 digit password"
                } else {
                    email1 = email.text.toString().trim()
                    password1 = password.text.toString().trim()
                    name1 = name.text.toString().trim()
                    mAuth.createUserWithEmailAndPassword(email1, password1)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) { // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                Toast.makeText(
                                    activity,
                                    "Authentication success.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                //addData()
                                uploadfile()
                                // verifyEmail()
                                updateUI()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }

                }


            }
            R.id.profile_image -> ShowFileChooser()
        }


    }

    //to give the valid email pattern for eg : name@domain

    private fun validEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    //to give the restriction for the password with the help of regex

    private fun validPassword(password: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }

    private fun updateUI() {
        val intent = Intent(this.context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

//    private fun verifyEmail(){
//        val muser = mAuth.currentUser
//        muser!!.sendEmailVerification()
//            .addOnCompleteListener {task ->
//                if(task.isSuccessful){
//                    Toast.makeText(view?.context,"Verification email sent to" + muser.email,Toast.LENGTH_SHORT).show()
//                } else {
//                    Log.e(TAG,"Failed to send verification email.",task.exception)
//                    Toast.makeText(view?.context,"Failed to send verification email.",Toast.LENGTH_SHORT).show()
//                }
//            }
//    }

    private fun addData() {
        var user = hashMapOf(
            "email" to email1, "name" to name1,
            "password" to password1, "image" to url
        )
        db.collection("User").document(mAuth.currentUser!!.uid).set(user as Map<String, Any>)
            .addOnCompleteListener { documentReference ->
                Log.i("data added", "DocumentSnapshot added with ID")
            }
            .addOnFailureListener { documentRefrence ->
                Log.i("data not added", "Error adding document")
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.getData();
            profile_image.setImageURI(data.data)
        }

    }

    private fun ShowFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST)
    }

    private fun uploadfile() {

        if (filePath != null) {
            val imageRef = mStorageReference.child("images/" + mAuth.currentUser!!.uid)
            imageRef.putFile(filePath!!)
                .addOnSuccessListener {
                    Log.i("on success", "uploaded")
                    downloadUrl(imageRef)
                }
                .addOnFailureListener {
                    Log.i("on failure", "not uploaded")


                }
        }
    }


    private fun downloadUrl(imageRef: StorageReference) {
        imageRef.getDownloadUrl()
            .addOnSuccessListener {
                url = it.toString()
                Log.i(" image url", url)
                addData()

            }
    }

}
