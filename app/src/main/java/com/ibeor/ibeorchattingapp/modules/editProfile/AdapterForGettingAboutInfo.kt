package com.ibeor.ibeorchattingapp.modules.editProfile

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibeor.ibeorchattingapp.R

class AdapterForGettingAboutInfo(val context: Context,val list:ArrayList<EditProfileUserDataGettingOne>,val clickk:Clickss):RecyclerView.Adapter<AdapterForGettingAboutInfo.MyViewHolder>(){
inner class MyViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView){
    val parentTitle:AppCompatTextView=itemView.findViewById(R.id.parent_item_title)
    val childRecyclerView:RecyclerView=itemView.findViewById(R.id.child_recyclerview)
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.rv_for_getting_user_data,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.parentTitle.text=list[position].parentTitle
        val layoutManager = LinearLayoutManager(holder.childRecyclerView.context, LinearLayoutManager.VERTICAL, false)
        holder.childRecyclerView.layoutManager = layoutManager
        layoutManager.initialPrefetchItemCount = list[position].userNestedArrayList?.size!!
        val childItemAdapter = AdapterForGettingAboutInfoChild(context,list[position].userNestedArrayList!!,object :AdapterForGettingAboutInfoChild.Clicks{
            override fun onCardClick(positio: Int) {
                Log.i("checkPosition","child postition ${positio}parent  position  ${holder.adapterPosition}")
                clickk.onClick(holder.adapterPosition,positio)
            }
        })
        holder.childRecyclerView.adapter = childItemAdapter
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface Clickss{
        fun onClick(position: Int,positio: Int)
    }
}