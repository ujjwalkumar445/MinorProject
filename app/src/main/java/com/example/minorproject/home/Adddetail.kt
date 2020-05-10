package com.example.minorproject.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.minorproject.R
import com.example.minorproject.viewmodel.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.fragment_adddetail.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class Adddetail : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    private var filepath: Uri? = null
    private var url: String? = null
    private val PICK_IMAGE_REQUEST = 5678
    lateinit var rootView: View

    private val mAddCatViewModel by lazy {
        ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
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

        activity?.menu?.setOnItemSelectedListener {
            when (it) {
                R.id.profile -> navController?.navigate(R.id.action_adddetail_to_userProfile)
                R.id.timeline -> navController?.navigate(R.id.action_adddetail_to_timeline2)
            }
        }

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
                //UploadImage()
                onAddCatClicked()
                // ImageTitle = titleText.text.toString()
                navController!!.navigate(R.id.action_adddetail_to_home2)

            }
        }
    }
    

    private fun onAddCatClicked() {
        mAddCatViewModel.onAddCatClicked(
            filepath, titleText.text.toString()
        )

    }

}
