package com.example.minorproject.home.ImageCat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.minorproject.R
import com.squareup.picasso.Picasso

class ImageAdapter(private var context: Context) :
    RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {

    lateinit var SingleImage: ImageView
    private var catImagelist: List<CatImageModel>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.image_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return catImagelist?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.Image()

    }

    fun setData(catImagelist: List<CatImageModel>) {
        this.catImagelist = catImagelist
        notifyDataSetChanged()

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun Image() {

            SingleImage = itemView.findViewById(R.id.SingleImage)

            val catImageModel = catImagelist?.get(adapterPosition)

            Picasso.get().load(catImageModel?.imageUrl).resize(250, 250).centerCrop()
                .into(SingleImage)
        }

    }


}