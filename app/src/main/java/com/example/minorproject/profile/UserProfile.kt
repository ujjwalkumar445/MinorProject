package com.example.minorproject.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.minorproject.MainActivity

import com.example.minorproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.fragment_user_profile.*

/**
 * A simple [Fragment] subclass.
 */
class UserProfile : Fragment() {

    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private lateinit var memail: TextView
    private lateinit var mStorageReference: StorageReference
    private lateinit var mname: TextView
    private lateinit var mimage: CircleImageView
    private lateinit var Storage: FirebaseStorage
    lateinit var email1: String
    lateinit var name1: String
    private var PICK_IMAGE_CODE = 908
    private var filePath: Uri? = null
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var newimageurld:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Storage = FirebaseStorage.getInstance()
        mStorageReference = Storage.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
       var view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        getregisterdetail(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        activity!!.menu.setOnItemSelectedListener {
            when (it) {
                R.id.home -> navController.navigate(R.id.action_userProfile_to_home2)
                R.id.timeline -> navController.navigate(R.id.action_userProfile_to_timeline2)
            }
        }

        logout.setOnClickListener{
            val currentUser = mAuth.currentUser
            if(currentUser != null){
                val intent = Intent(view.context, MainActivity::class.java)
                val bundle = Bundle()
                bundle.putString("key","LogOut")
                intent.putExtras(bundle)
                mAuth.signOut()
                startActivity(intent)
                activity?.finish()
            }
        }
        profile_image1.setOnClickListener {
            SelectImage()
        }
    }

    private fun getregisterdetail(view: View){

        mAuth = FirebaseAuth.getInstance()

        val documentReference: DocumentReference =
            db.collection("User").document(mAuth.currentUser!!.uid)
        documentReference.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

            val email: String? = documentSnapshot?.getString("email")
            val name: String? = documentSnapshot?.getString("name")
            val imageurl: String? = documentSnapshot?.getString("image")

            memail = view?.findViewById<View>(R.id.emailText) as TextView
            memail.setText(email).toString()
            mname = view?.findViewById<View>(R.id.usernameText) as TextView
            mname.setText(name).toString()

            mimage = view?.findViewById<View>(R.id.profile_image1) as CircleImageView
            Picasso.get().load(imageurl).resize(250, 250).centerCrop().into(mimage)

        }

    }

    private fun SelectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, PICK_IMAGE_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.getData();
            profile_image1.setImageURI(data.data)
        }
        uploadfile()
    }

    private fun uploadfile() {

        if (filePath != null) {
            val imageRef = mStorageReference.child("images/" + mAuth.currentUser!!.uid)
            imageRef.putFile(filePath!!)
                .addOnSuccessListener {
                    Log.e("Success", "Save to Storage")
                    downloadUrl(imageRef)
                }
                .addOnFailureListener {
                    Log.e("Failure", "Not Save to Storage")


                }
        }
    }

    private fun downloadUrl(imageRef: StorageReference) {
        imageRef.getDownloadUrl()
            .addOnSuccessListener {
                newimageurld = it.toString()
                Log.e(" image url", newimageurld)
                addData()

            }
    }

    private fun addData() {
        email1 = emailText.text.toString().trim()
        name1 = usernameText.text.toString().trim()
        val user = hashMapOf(
            "email" to email1, "name" to name1,"image" to newimageurld
        )
        db.collection("User").document(mAuth.currentUser!!.uid).set(user as Map<String, Any>)
            .addOnCompleteListener { documentReference ->
                Log.e("Data Added", "Added to collection with the id")
                getchangeImage()
            }
            .addOnFailureListener { documentRefrence ->
                Log.e("Failure", "Data not added to collection")
            }
    }

    private fun getchangeImage(){

        mAuth = FirebaseAuth.getInstance()

        val documentReference: DocumentReference =
            db.collection("User").document(mAuth.currentUser!!.uid)
        documentReference.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

            memail.text.toString()
            mname.text.toString()

            val imageurl: String? = documentSnapshot?.getString("image")

            mimage = view?.findViewById<View>(R.id.profile_image1) as CircleImageView
            Picasso.get().load(imageurl).resize(250, 250).centerCrop().into(mimage)

        }

    }

}

