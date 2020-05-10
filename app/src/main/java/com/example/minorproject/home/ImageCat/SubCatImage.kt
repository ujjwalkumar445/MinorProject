package com.example.minorproject.home.ImageCat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.minorproject.Interface.OnImageClick

import com.example.minorproject.R
import com.example.minorproject.home.CatImageModel
import com.example.minorproject.utils.ARG_ID
import com.example.minorproject.utils.CAT_ID
import com.example.minorproject.utils.SUBCAT_ID
import com.example.minorproject.viewmodel.SubCatViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.fragment_sub_cat_image.*

/**
 * A simple [Fragment] subclass.
 */
class SubCatImage : Fragment(), View.OnClickListener, OnImageClick {

    private lateinit var navController: NavController
    private lateinit var database: FirebaseFirestore
    lateinit var subCatViewModel: SubCatViewModel
    private lateinit var catSublist: LiveData<ArrayList<CatImageModel>>
    var imageAdapter: ImageAdapter? = null

    var subCatId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subCatId = arguments?.getString(ARG_ID, "") ?: ""

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub_cat_image, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<FloatingActionButton>(R.id.addCategoryImage).setOnClickListener(this)
        basic()

        activity?.menu?.setOnItemSelectedListener {
            when (it) {
                R.id.profile -> navController.navigate(R.id.action_subCatImage_to_userProfile)
                R.id.timeline -> navController.navigate(R.id.action_subCatImage_to_timeline2)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.addCategoryImage -> navController.navigate(
                R.id.action_subCatImage_to_addImage,
                arguments
            )
        }
    }


    private fun basic() {

        database = FirebaseFirestore.getInstance()

        imageAdapter = context?.let { ImageAdapter(it, this) }


        recyclerImage.layoutManager = GridLayoutManager(this.context, 3)
        recyclerImage.setHasFixedSize(true)

        subCatViewModel = ViewModelProvider(requireActivity()).get(SubCatViewModel::class.java)


        catSublist = subCatViewModel.Getdata(subCatId)

        recyclerImage.adapter = imageAdapter


        subCatViewModel.mSubRecyclerData.observe(
            viewLifecycleOwner,
            Observer<List<CatImageModel>> { list ->
                list?.let {
                    imageAdapter?.notifyChange(it)
                }

            })


    }

    override fun OnImgclick(subCatModal: CatImageModel) {
        val bundle = Bundle()

        bundle.putString(SUBCAT_ID, subCatModal.sub_cat_id.toString())
        bundle.putString(CAT_ID, subCatModal.cat_id.toString())

        navController.navigate(R.id.action_subCatImage_to_fullScreenImage2, bundle)
    }
}


