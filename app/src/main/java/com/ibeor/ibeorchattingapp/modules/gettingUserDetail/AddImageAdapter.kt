package com.ibeor.ibeorchattingapp.modules.gettingUserDetail

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibeor.ibeorchattingapp.R
import java.io.File

class AddImageAdapter(val context:Context,var list:ArrayList<String>,var checkFileUri:Int,val clicks:Clicks):RecyclerView.Adapter<AddImageAdapter.MyViewHolder>() {
inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val addImg:AppCompatImageView=itemView.findViewById(R.id.imgAddImgDD)
    val userImg:AppCompatImageView=itemView.findViewById(R.id.imgUserImgDD)
    val deleteImg:AppCompatImageView=itemView.findViewById(R.id.imgDeleteImgDD)
}
    fun updateList(list:ArrayList<String>){
        this.list=list
        Log.e("asfsadf","inside update list ${list}")
    }

    @SuppressLint("LogNotTimber")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.i("asfsadf","list is ${list}")
//        if(!list.get(position).equals("")){
//            Log.e("sjsadgfdfgffb","clickable true")
//            holder.addImg.isClickable=true
//        }
//        else{
//            holder.addImg.isClickable=false
//        }
        holder.addImg.setOnClickListener {
            clicks.onAddImage(position,holder.addImg.id)
        }
        holder.deleteImg.setOnClickListener {
            clicks.onDelteImage(position,holder.deleteImg.id)
        }
        if(!list.get(position).equals("")) {
            holder.userImg.setOnClickListener {
                clicks.onAddImage(position, holder.userImg.id)
            }
        }
        if(checkFileUri == 1) {
            if (!list[position].equals("")) {
                Log.i("testingNowww", "adapter uri ${list[position]}")
//          holder.userImg.setImageURI(Uri.parse(list[position]))
                Glide.with(context).load(list[position]).into(holder.userImg);
            }
        }
        else{
            if (!list[position].equals("")) {
                Log.i("testingNowww", "adapter uri ${list[position]}")
          holder.userImg.setImageURI(Uri.parse(list[position]))
            }

        }

        if(list[position] != ""){
            var p=position+1
            holder.addImg.visibility=View.GONE
            if(p <= list.size-1) {
                if (list[p] != "") {
                    holder.deleteImg.visibility = View.VISIBLE
                }
            }
            if (position > 0){
                holder.deleteImg.visibility=View.VISIBLE
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }
    interface Clicks{
        fun onAddImage(position:Int,viewId:Int)
        fun onDelteImage(position:Int,viewId:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.rv_add_photo_layout,parent,false)
        return MyViewHolder(view)
    }

}