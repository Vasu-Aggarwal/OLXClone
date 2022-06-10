package com.example.olx.ui.details.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.olx.R

class DetailImagesAdapter(var context: Context,
                          private val imageList: ArrayList<String>
                          , var onItemClickListener: onItemClick) : PagerAdapter() {

    private var inflater : LayoutInflater ?= null
    private var doNotifyDatasetChanged = false

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        if(doNotifyDatasetChanged){
            doNotifyDatasetChanged = false
            notifyDataSetChanged()
        }
        return imageList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = inflater?.inflate(R.layout.adapter_images_detail, container, false)
        val imageView = view?.findViewById<ImageView>(R.id.imageView)

        imageView?.setOnClickListener(View.OnClickListener {
            onItemClickListener.onClick(position)
        })

        Glide.with(context)
            .load(imageList.get(position))
            .placeholder(R.drawable.box)
            .into(imageView!!)

        container.addView(view, 0)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun saveState(): Parcelable? {
        return null
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    interface onItemClick{
        fun onClick(position: Int)
    }
}