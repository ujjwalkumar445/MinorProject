package com.example.minorproject.timeline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.minorproject.R
import com.example.minorproject.databinding.TimelineItemBinding
import com.squareup.picasso.Picasso

class TimelineAdapter() :
    RecyclerView.Adapter<TimelineAdapter.MyViewHolder>() {

    private var timelinelist: List<TimelineModal>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: TimelineItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.timeline_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return timelinelist?.size ?: 0

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.timeline = timelinelist?.get(position)
        holder.binding.executePendingBindings()

        val ab: TimelineModal? = timelinelist?.get(position)
        holder.binding.timelinetext.text = (ab?.date!!).toString()
        Picasso.get().load(ab.imageUrl).resize(250, 250).centerCrop().into(holder.binding.timeImage)


    }

    fun setData(timelinelist: List<TimelineModal>) {
        this.timelinelist = timelinelist
        notifyDataSetChanged()

    }

    inner class MyViewHolder(var binding: TimelineItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}