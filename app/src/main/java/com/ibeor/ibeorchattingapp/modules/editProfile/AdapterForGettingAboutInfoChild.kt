package com.ibeor.ibeorchattingapp.modules.editProfile

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.ibeor.ibeorchattingapp.R

class AdapterForGettingAboutInfoChild(val context: Context,val list:ArrayList<EditProfileUserDataGettingTwo>,val clickChild:Clicks):RecyclerView.Adapter<AdapterForGettingAboutInfoChild.MyViewHOlder>(){
    inner class MyViewHOlder(itemView:View):RecyclerView.ViewHolder(itemView){
        val imgLogo:AppCompatImageView=itemView.findViewById(R.id.imgLogoTemp)
        val title:AppCompatTextView=itemView.findViewById(R.id.txtTitleTemp)
        val card:MaterialCardView=itemView.findViewById(R.id.materialCard)
        val subTitle:AppCompatTextView=itemView.findViewById(R.id.txtUserLookingFor)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AdapterForGettingAboutInfoChild.MyViewHOlder {
        val view=LayoutInflater.from(context).inflate(R.layout.rv_for_getting_user_data_second,parent,false)
        return MyViewHOlder(view)
    }
    override fun onBindViewHolder(
        holder: AdapterForGettingAboutInfoChild.MyViewHOlder,
        position: Int,
    ) {
        holder.card.setOnClickListener {
            clickChild.onCardClick(position)
            Log.i("checkPosition","child position  $position")
//            Toast.makeText(context, "Click on position $position", Toast.LENGTH_SHORT).show()
        }
        holder.imgLogo.setImageResource(list[position].imgLogo!!)
        holder.title.text=list[position].txtTitleTemp
    }

    override fun getItemCount(): Int {
        return list.size
    }
interface Clicks{
    fun onCardClick(position: Int)
}
}