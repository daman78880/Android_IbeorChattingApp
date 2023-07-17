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
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel

class MatchUserAdatper(val context: Context,val list:ArrayList<MatchUserExtraData>,val clicks:Clisks):RecyclerView.Adapter<MatchUserAdatper.MyViewHolder>(){
    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val img:AppCompatImageView=itemView.findViewById(R.id.imgLikedPerson)
        val imgDot:AppCompatImageView=itemView.findViewById(R.id.imgDotHorizontal)
        val name:AppCompatTextView=itemView.findViewById(R.id.txtLikedPeople)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MatchUserAdatper.MyViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.liked_people_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchUserAdatper.MyViewHolder, position: Int) {
        Log.i("asjfdsajfb","list is ${list[position].user_Image}")
        holder.img.clipToOutline=true
        Glide.with(context).load(list[position].user_Image).into(holder.img)
        holder.name.text=list[position].user_name
//        Log.i("askfbsjanfb ","user status outside is ${list[position].user_msg_status}")
        if (list[position].login_user_view.equals("1")){
//            Log.i("askfbsjanfb ","user status inside is ${list[position].user_msg_status}")
            holder.imgDot.visibility=View.GONE
        }
        holder.img.setOnClickListener {
            clicks.onImageClick(position,holder.img.id)
        }
        holder.name.setOnClickListener {
            clicks.onTextClick(position,holder.name.id)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface Clisks{
        fun onImageClick(position:Int,id:Int)
        fun onTextClick(position:Int,id:Int)
    }

}