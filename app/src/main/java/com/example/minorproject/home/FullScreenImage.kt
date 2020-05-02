package com.example.minorproject.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_full_screen_image.*

import com.example.minorproject.R
import com.example.minorproject.utils.ARG_ID
import com.example.minorproject.utils.CAT_ID
import com.example.minorproject.utils.SUBCAT_ID
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home_page.*

/**
 * A simple [Fragment] subclass.
 */
class FullScreenImage : Fragment() {

    private lateinit var navController: NavController

    var db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_full_screen_image, container, false)

        val SubCat_ID = arguments?.getString(SUBCAT_ID)
        val Cat_ID = arguments?.getString(CAT_ID)
        Log.e("arg","value"+ SubCat_ID)
        Log.e("arg1","value"+ Cat_ID)


        db.collection("CategoryImage").document(Cat_ID!!).collection("Image").document(SubCat_ID!!)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                val imageUrl: String? = documentSnapshot?.getString("ImageUrl")
                Picasso.get().load(imageUrl)
                    .into(fullScreenImage)
            }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)


        activity?.menu?.setOnItemSelectedListener {
            when (it) {
                R.id.profile -> navController.navigate(R.id.action_fullScreenImage2_to_userProfile)
                R.id.timeline -> navController.navigate(R.id.action_fullScreenImage2_to_timeline2)
            }
        }

        val SubCat_ID = arguments?.getString(SUBCAT_ID)
        val Cat_ID = arguments?.getString(CAT_ID)
        Log.e("arg","value"+ SubCat_ID)
        Log.e("arg1","value"+ Cat_ID)

        deletebtn.setOnClickListener{

            db.collection("CategoryImage").document(Cat_ID!!)
                .collection("Image").document(SubCat_ID!!).delete().addOnCompleteListener {
                    db.collection("Timeline").document(SubCat_ID).delete().addOnCompleteListener {
                        val bundle = Bundle()
                        bundle.putString(ARG_ID,Cat_ID)
                        Log.e("arg1","value"+ Cat_ID)

                        navController.navigate(R.id.action_fullScreenImage2_to_subCatImage,bundle)

                    }
                }

        }

    }


}
