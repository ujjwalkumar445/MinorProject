package com.example.minorproject.home

import android.os.Bundle
import android.util.Log
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

import com.example.minorproject.R
import com.example.minorproject.utils.ARG_ID
import com.example.minorproject.viewmodel.HomeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.fragment_home2.*

/**
 * A simple [Fragment] subclass.
 */
class Home : Fragment(), View.OnClickListener, OnItemClick {

    private lateinit var navController: NavController
    private lateinit var catlist: LiveData<ArrayList<CatModel>>
    private lateinit var database: FirebaseFirestore
    lateinit var homeViewModel: HomeViewModel
    var CategoryAdapter: CategoryAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragmloaddataent
        return inflater.inflate(R.layout.fragment_home2, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<FloatingActionButton>(R.id.addCategory).setOnClickListener(this)
        attachAdapter()


        activity?.menu?.setOnItemSelectedListener {
            when (it) {
                R.id.profile -> navController.navigate(R.id.action_home2_to_userProfile)
                R.id.timeline -> navController.navigate(R.id.action_home2_to_timeline2)
            }
        }

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.addCategory -> navController.navigate(R.id.action_home2_to_adddetail)
        }
    }


    private fun attachAdapter() {

        database = FirebaseFirestore.getInstance()
        CategoryAdapter = context?.let { CategoryAdapter(it, this) }
        recycler.layoutManager = GridLayoutManager(context, 2)
        recycler.setHasFixedSize(true)

        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
//        recycler.adapter= null

        catlist = homeViewModel.Getdata()

        recycler.adapter = CategoryAdapter


        homeViewModel.mRecyclerData.observe(viewLifecycleOwner, Observer<List<CatModel>> { list ->
            list?.let {

                CategoryAdapter?.notifychange(it)
            }

        })


    }

    override fun OnClick(catModel: CatModel) {
        val bundle = Bundle()
        bundle.putString(ARG_ID, catModel.cat_id)
        Log.e("click", "Id" + catModel.cat_id)

        navController.navigate(R.id.action_home2_to_subCatImage, bundle)
    }


}
