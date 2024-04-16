package com.example.namumovil.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.namumovil.R

class CarouselAdapter(private val context: Context, private var arrayList: ArrayList<String>) : RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.list_item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.carrousel_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(arrayList[position]).into(holder.imageView)
        holder.itemView.setOnClickListener { onItemClickListener?.onClick(holder.imageView, arrayList[position]) }
    }

    override fun getItemCount() = arrayList.size

    interface OnItemClickListener {
        fun onClick(imageView: ImageView, path: String)
    }
}
