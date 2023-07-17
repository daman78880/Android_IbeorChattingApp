package com.ibeor.ibeorchattingapp.modules.userInfo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.ibeor.ibeorchattingapp.R
import java.lang.invoke.ConstantCallSite
import java.util.*
import kotlin.collections.ArrayList

class ImageViewPager(val context: Context, val imageList: ArrayList<String>): PagerAdapter() {

    override fun getCount(): Int {
        return imageList.size
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View =
            mLayoutInflater.inflate(R.layout.image_viewpager_layout, container, false)
        val imageView: AppCompatImageView =
            itemView.findViewById<View>(R.id.imgViewPagwer) as AppCompatImageView
        Glide.with(context).load(imageList[position]).into(imageView)
//        imageView.setImageResource(imageList[position])
        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}