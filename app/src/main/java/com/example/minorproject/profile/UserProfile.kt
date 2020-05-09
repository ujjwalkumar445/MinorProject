package com.example.minorproject.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.minorproject.MainActivity

import com.example.minorproject.R
import com.example.minorproject.repo.UserRepo
import com.example.minorproject.viewmodel.UserViewModal
import com.google.firebase.auth.FirebaseAuth
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
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var PICK_IMAGE_CODE = 908
    private var filePath: Uri? = null
    lateinit var mUserViewModal: UserViewModal


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_user_profile, container, false)

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

        mUserViewModal = ViewModelProvider(activity!!).get(UserViewModal::class.java)
        mUserViewModal.onUserInfo().observe(viewLifecycleOwner, Observer {
            emailText.setText(it.email)
            usernameText.setText(it.name)
            Picasso.get().load(it.image).resize(250, 250).centerCrop().into(profile_image1)
        })

        logout.setOnClickListener {
            val currentUser = mAuth.currentUser
            if (currentUser != null) {
                val intent = Intent(view.context, MainActivity::class.java)
                val bundle = Bundle()
                bundle.putString("key", "LogOut")
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
        view?.let { uploadImage(it) }
    }

    private fun uploadImage(view: View) {
        mUserViewModal.onImageChanged(
            emailText.text.toString(),
            usernameText.text.toString(),
            view,
            filePath
        )

    }

}

