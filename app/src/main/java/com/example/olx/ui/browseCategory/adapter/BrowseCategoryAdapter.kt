package com.example.olx.ui.browseCategory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.olx.R
import com.example.olx.model.CategoriesModel
import com.example.olx.model.DataItemModel
import com.example.olx.ui.browseCategory.BrowseCategoryFragment
import com.example.olx.ui.home.adapter.CategoriesAdapter
import com.example.olx.ui.myAds.MyAdsFragment
import com.google.android.gms.common.internal.Constants
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat

class BrowseCategoryAdapter(var dataItemModel: MutableList<DataItemModel>, var mClickListener: ItemClickListener)
    : RecyclerView.Adapter<BrowseCategoryAdapter.ViewHolder>() {

    private lateinit var context: Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewPrice = itemView.findViewById<TextView>(R.id.tvPrice)
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        val textViewBrand = itemView.findViewById<TextView>(R.id.tvBrand)
        val textViewAddress = itemView.findViewById<TextView>(R.id.tvAddress)
        val textViewDate = itemView.findViewById<TextView>(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.adapter_my_ads, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewPrice.setText(com.example.olx.utilities.Constants.CURRENCY_SYMBOL + dataItemModel.get(position).price)
        holder.textViewBrand.setText(dataItemModel.get(position).brand)
        holder.textViewAddress.setText(dataItemModel.get(position).address)
//      Glide.with(context).load(dataItemModel.get(position).images.get(0)).into(holder.imageView)

//        val sdf = SimpleDateFormat("dd/mm/yyyy")
//        val formattedDate = sdf.format(dataItemModel[position].createdDate?.time)
//        holder.textViewDate.setText(formattedDate)
        holder.itemView.setOnClickListener{
            mClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return dataItemModel.size
    }

    fun updateList(temp: MutableList<DataItemModel>) {
        dataItemModel = temp
        notifyDataSetChanged()
    }

    interface ItemClickListener{
        fun onItemClick(position: Int)
    }
}