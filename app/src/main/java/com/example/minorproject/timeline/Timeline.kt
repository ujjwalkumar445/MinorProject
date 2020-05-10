package com.example.minorproject.timeline

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
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.minorproject.R
import com.example.minorproject.viewmodel.TimelineViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.fragment_timeline.*

/**
 * A simple [Fragment] subclass.
 */
class Timeline : Fragment() {

    private lateinit var navController: NavController
    private lateinit var database: FirebaseFirestore
    lateinit var mTimelineViewModel: TimelineViewModel
    private lateinit var timelinelist: LiveData<ArrayList<TimelineModal>>
    var mTimelineAdapter: TimelineAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        timelineUi()

        activity!!.menu.setOnItemSelectedListener {
            when (it) {
                R.id.home -> navController.navigate(R.id.action_timeline2_to_home2)
                R.id.profile -> navController.navigate(R.id.action_timeline2_to_userProfile)
            }
        }
    }

    private fun timelineUi() {

        database = FirebaseFirestore.getInstance()
        mTimelineAdapter = context?.let { TimelineAdapter() }


        timelineRecycler.layoutManager = LinearLayoutManager(this.context)
        timelineRecycler.setHasFixedSize(true)

        mTimelineViewModel = ViewModelProvider(requireActivity()).get(TimelineViewModel::class.java)


        timelinelist = mTimelineViewModel.GetTimelinedata()

        timelineRecycler.adapter = mTimelineAdapter


        mTimelineViewModel.mTimelineRecyclerData.observe(
            viewLifecycleOwner,
            Observer<List<TimelineModal>> { list ->
                list?.let {
                    mTimelineAdapter?.setData(it)
                }

            })


    }


}
