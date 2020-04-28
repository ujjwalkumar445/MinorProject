package com.example.minorproject.profile

import android.content.Intent
import android.os.Bundle
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
    private lateinit var mname: TextView
    private lateinit var mimage: CircleImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mAuth = FirebaseAuth.getInstance()
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
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


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
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
    }
}
