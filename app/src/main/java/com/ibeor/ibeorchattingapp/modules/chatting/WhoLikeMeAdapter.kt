package com.ibeor.ibeorchattingapp.modules.chatting

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibeor.ibeorchattingapp.R

class WhoLikeMeAdapter(val context: Context,val list:ArrayList<UserLikedCheckDetail>,val click:onClicked):RecyclerView.Adapter<WhoLikeMeAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val img:AppCompatImageView=itemView.findViewById(R.id.imgUserLikeYou)
        val nameAge:AppCompatTextView=itemView.findViewById(R.id.txtUserLikesYou)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.who_likes_me_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.i("salkfjsdaf","img ${list[position].liked_user_img}")
        Glide.with(context).load(list[position].liked_user_img).into(holder.img)
        holder.nameAge.text="${list[position].liked_user_name},${list[position].liked_user_age}"
        holder.img.setOnClickListener {
            click.onClikc(list[position].liked_id!!)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface onClicked{
        fun onClikc(id:String)
    }
}