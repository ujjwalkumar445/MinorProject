package com.example.minorproject.home.ImageCat

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
import com.example.minorproject.utils.ARG_ID
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_add_image.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AddImage : Fragment(), View.OnClickListener {


    lateinit var mAuth: FirebaseAuth
    var navController: NavController? = null
    lateinit var database: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    private var filepath: Uri? = null
    private var url: String? = null
    lateinit var mcollection: CollectionReference
    private lateinit var mStorageReference: StorageReference
    private val PICK_CAMERA_REQUEST = 789
    private val PICK_IMAGE_REQUEST = 345


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        mcollection = database.collection("CategoryImage")
        storage = FirebaseStorage.getInstance()
        mStorageReference = storage.reference


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<ImageView>(R.id.addCatImage).setOnClickListener(this)
        view.findViewById<Button>(R.id.addImagebtn).setOnClickListener(this)

        if (url != null) {
            Picasso.get().load(url).resize(250, 250).centerCrop().into(addCatImage)


        } else {
            Log.i("", "")
        }
    }


    private fun Dialogbox() {
        val option = arrayOf("Camera", "Gallery", "Cancel")
        val builder = MaterialAlertDialogBuilder(context)
        with(builder) {
            setItems(option) { dialog, which ->
                if (option[which].equals("Camera")) {
                    dialog.dismiss()
                    val CameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(CameraIntent, PICK_CAMERA_REQUEST)
                } else if (option[which].equals("Gallery")) {
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
            PICK_CAMERA_REQUEST -> if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                filepath = data.getData()
                addCatImage.setImageURI(data.data)
            }

            PICK_IMAGE_REQUEST -> if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                filepath = data.getData()
                addCatImage.setImageURI(data.data)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.addCatImage -> Dialogbox()
            R.id.addImagebtn -> {
                UploadCatImage()

            }
        }
    }


    private fun UploadCatImage() {
        if (filepath != null) {
            val Ref = mStorageReference.child("CImages/" + UUID.randomUUID().toString())
            Ref.putFile(filepath!!)
                .addOnSuccessListener {
                    Log.i("on success", "uploaded")
                    downloadUrl(Ref)

                }
                .addOnFailureListener {
                    Log.i("on failure", "not uploaded")

                }

        }
    }

    private fun downloadUrl(Ref: StorageReference) {
        Ref.downloadUrl
            .addOnSuccessListener {
                url = it.toString()
                UploadImage()
            }
    }

    private fun UploadImage() {
        val args: String? = arguments?.getString(ARG_ID)
        val image = hashMapOf("ImageUrl" to url)
        database.collection("CategoryImage")
            .document(args!!).collection("Image").document().set(image as Map<*, *>)
            .addOnCompleteListener {
                navController!!.navigate(R.id.action_addImage_to_subCatImage2)
                Log.i("data added", "DocumentSnapshot added with ID")
            }
            .addOnFailureListener {
                Log.i("data not added", "Error adding document")
            }
    }

}
