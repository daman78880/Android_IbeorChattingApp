package com.ibeor.ibeorchattingapp.modules.home.stackViewAdapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibeor.ibeorchattingapp.R
import kotlinx.coroutines.NonDisposableHandle.parent

class PreviewImageVerticalAdapter (val context: Context, val list:ArrayList<String>,var extend:Boolean):
    RecyclerView.Adapter<PreviewImageVerticalAdapter.MyViewHolderr>(){
    class MyViewHolderr(itemView: View): RecyclerView.ViewHolder(itemView){
        val img: AppCompatImageView =itemView.findViewById(R.id.imgViewPagwer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewImageVerticalAdapter.MyViewHolderr {
        if(extend) {
            val view=LayoutInflater.from(context).inflate(R.layout.fragment_user_info, parent, false)
            return MyViewHolderr(view)
        }
        else {
        Log.i("asjfhdnsa","postition is  ${viewType}")
            val view = LayoutInflater.from(context).inflate(R.layout.image_viewpager_layout, parent, false)
                return MyViewHolderr(view)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PreviewImageVerticalAdapter.MyViewHolderr, position: Int) {
        Glide.with(context)
            .load(list[position])
            .into(holder.img)
                Log.i("asfbasf","equal now get adapter ${holder.adapterPosition}")
//        if(holder.adapterPosition == list.size-1){
//            }


    }


}