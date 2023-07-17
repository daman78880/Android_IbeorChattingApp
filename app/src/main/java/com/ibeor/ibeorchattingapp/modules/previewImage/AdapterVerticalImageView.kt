package com.ibeor.ibeorchattingapp.modules.previewImage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.google.firebase.storage.internal.AdaptiveStreamBuffer
import com.ibeor.ibeorchattingapp.R
import java.util.*
import kotlin.collections.ArrayList

class AdapterVerticalImageView (val context: Context,val list:ArrayList<String>):RecyclerView.Adapter<AdapterVerticalImageView.MyViewHolder>(){
    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val img:AppCompatImageView=itemView.findViewById(R.id.imgViewPagwer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.image_viewpager_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context)
            .load(list[position])
            .into(holder.img)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}