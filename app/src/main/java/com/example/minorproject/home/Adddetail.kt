package com.example.minorproject.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.minorproject.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_adddetail.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class Adddetail : Fragment(), View.OnClickListener {

    lateinit var mAuth: FirebaseAuth
    var navController: NavController? = null
    lateinit var database: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    private var filepath: Uri? = null
    private var url: String? = null
    lateinit var mcollection: CollectionReference
    private lateinit var mStorageReference: StorageReference
    private val PICK_CAMERA_REQUEST = 1234
    private val PICK_IMAGE_REQUEST = 5678
    lateinit var ImageTitle: String
    lateinit var userId: String
    lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        mcollection = database.collection("Category")
        storage = FirebaseStorage.getInstance()
        mStorageReference = storage.reference
        userId = mAuth.currentUser!!.uid


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_adddetail, container, false)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<ImageView>(R.id.image).setOnClickListener(this)
        view.findViewById<Button>(R.id.savebtn).setOnClickListener(this)

        if (url != null) {
            Picasso.get().load(url).resize(250, 250).centerCrop().into(image)


        } else {
            Log.i("", "")
        }
    }


    private fun selectImage() {
        val option = arrayOf("Gallery", "Cancel")
        val builder = MaterialAlertDialogBuilder(context)
        with(builder) {
            setItems(option) { dialog, which ->
//                if (option[which].equals("Camera")) {
//                    dialog.dismiss()
//                    val CameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                    startActivityForResult(CameraIntent, PICK_CAMERA_REQUEST)
//                } else if (option[which].equals("Gallery")) {
                if (option[which].equals("Gallery")) {
                    dialog.dismiss()
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(
                        Intent.createChooser(intent, "SELECT PICTURE"),
                        PICK_IMAGE_REQUEST
                    )

                } else if (option[which].equals("Cancel")) {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
//            PICK_CAMERA_REQUEST -> if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
//                filepath = data.getData()
//                image.setImageURI(data.data)
//            }

            PICK_IMAGE_REQUEST -> if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                filepath = data.getData()
                image.setImageURI(data.data)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.image -> selectImage()
            R.id.savebtn -> {
                UploadImage()
                ImageTitle = titleText.text.toString()
                navController!!.navigate(R.id.action_adddetail_to_home2)

            }
        }
    }

    private fun UploadImage() {
        if (filepath != null) {
            val pathRef = mStorageReference.child("CImages/" + UUID.randomUUID().toString())
            pathRef.putFile(filepath!!)
                .addOnSuccessListener {
                    Log.i("on success", "uploaded")
                    dwnldUrl(pathRef)

                }
                .addOnFailureListener {
                    Log.i("on failure", "not uploaded")

                }

        }
    }

    private fun dwnldUrl(pathRef: StorageReference) {
        pathRef.downloadUrl
            .addOnSuccessListener {
                url = it.toString()
                UploadData()
            }
    }

    private fun UploadData() {
        val image = hashMapOf("ImageUrl" to url, "ImageTitle" to ImageTitle, "User Id" to userId)
        database.collection("Category")
            .document().set(image as Map<*, *>)
            .addOnCompleteListener {
                Log.i("data added", "DocumentSnapshot added with ID")
            }
            .addOnFailureListener {
                Log.i("data not added", "Error adding document")
            }
    }

}
