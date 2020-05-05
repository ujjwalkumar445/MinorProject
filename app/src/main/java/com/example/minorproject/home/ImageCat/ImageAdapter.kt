package com.example.minorproject.home.ImageCat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.minorproject.Interface.OnImageClick
import com.example.minorproject.R
import com.example.minorproject.databinding.ImageListBinding
import com.example.minorproject.home.CatImageModel
import com.squareup.picasso.Picasso

class ImageAdapter(private var context : Context,private val onImageClick: OnImageClick) :
    RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {

    lateinit var SingleImage: ImageView
    private var catImagelist: List<CatImageModel>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ImageListBinding = DataBindingUtil.inflate(inflater,R.layout.image_list,parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return catImagelist?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.subCat = catImagelist?.get(position)
        holder.binding.executePendingBindings()

        var md : CatImageModel? = catImagelist?.get(position)

        Picasso.get().load(md?.imageUrl).into(holder.binding.SingleImage)
        holder.binding.ImageCardView.setOnClickListener{
            catImagelist?.get(position)?.let { it1 -> onImageClick.OnImgclick(it1) }
        }

    }

    fun notifyChange(catImagelist: List<CatImageModel>) {
        this.catImagelist = catImagelist
        notifyDataSetChanged()

    }

     class MyViewHolder(var binding : ImageListBinding) : RecyclerView.ViewHolder(binding.root) {


    }


}