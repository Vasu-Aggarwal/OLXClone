package com.example.olx.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.olx.R
import com.example.olx.model.CategoriesModel
import de.hdodenhof.circleimageview.CircleImageView

class sellAdapter(var categoriesList: MutableList<CategoriesModel>, var itemClickListener: ItemClickListener)
    : RecyclerView.Adapter<sellAdapter.ViewHolder>() {

    private lateinit var context: Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle = itemView.findViewById<TextView>(R.id.ivTitle)
        val imageView = itemView.findViewById<ImageView>(R.id.ivIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.adapter_sell, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTitle.setText(categoriesList.get(position).key)
        Glide.with(context).load(categoriesList.get(position).image_bw).into(holder.imageView)

        holder.itemView.setOnClickListener{
            itemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    fun updateList(temp: MutableList<CategoriesModel>) {
        categoriesList = temp
        notifyDataSetChanged()
    }

    interface ItemClickListener{
        fun onItemClick(position: Int){

        }

    }


}