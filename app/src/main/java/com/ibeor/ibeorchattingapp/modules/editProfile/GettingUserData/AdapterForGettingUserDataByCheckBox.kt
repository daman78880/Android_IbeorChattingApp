package com.ibeor.ibeorchattingapp.modules.editProfile.GettingUserData

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.modules.userData.userDataByCheckBox.GettingUserDataForCheckBox
class AdapterForGettingUserDataByCheckBox(val context: Context,val list:ArrayList<GettingUserDataForCheckBox>,val data:ArrayList<String>?,val clicks:Click):RecyclerView.Adapter<AdapterForGettingUserDataByCheckBox.MyViewHolder>(){
    val  list2=ArrayList<String>()
    var gy:Int?=null
    val l2=HashMap<String,Boolean>()
    val l3=ArrayList<GettingCheckDataList>()
     class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val logo:AppCompatImageView=itemView.findViewById(R.id.imgEmoji)
        val Title:AppCompatTextView=itemView.findViewById(R.id.txtEmojiTitle)
        val CheckBox:CheckBox=itemView.findViewById(R.id.checkBox)
         val view:View=itemView.findViewById(R.id.lineViewCheckBox)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AdapterForGettingUserDataByCheckBox.MyViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.user_data_looking_for,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AdapterForGettingUserDataByCheckBox.MyViewHolder,
        position: Int,
    ) {

        l3.add(GettingCheckDataList(list[holder.adapterPosition].title,false))
//        l2[list[position].title.toString()] = false
        if(list.size-1 == holder.adapterPosition){
            holder.view.visibility=View.INVISIBLE
        }
        else{
            holder.view.visibility=View.VISIBLE
        }
        holder.CheckBox.buttonTintList=ColorStateList.valueOf(ContextCompat.getColor(context, R.color.greyLitee))
        holder.logo.setImageResource(list[holder.adapterPosition].img!!)
        holder.Title.text=list[holder.adapterPosition].title

            if (data != null) {
                for (i in 0 until data.size) {
                    if (holder.Title.text == data[i]) {
                        holder.CheckBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gulabiColor))
                        holder.CheckBox.isChecked = true
                        l3[holder.adapterPosition].flag=true
                    }
                }
            }

        holder.CheckBox.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if(p1){
                    Log.i("asfaksbfd","auto click ")
                    val chh= l3[holder.adapterPosition].flag
                    if(chh == true) {
                        l3[holder.adapterPosition].flag= false
                    }
                    else{
                        l3[holder.adapterPosition].flag= true
                    }
                    holder.CheckBox.buttonTintList= ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gulabiColor))
//                    for(i in 0 until l3.size){
//                        for(j in 0 until data?.size!!){
//                            if(l3[i].title == data[j]){
//                                l3[i].flag=true
//                            }
//                        }
//                    }
//                    Log.i("lasnfdmasnf","l3 $l3")
                    clicks.onClick(l3)
                }
                else{
                    Log.i("lasnfdmasnf"," unclick l3 $l3")

                    val chh= l3[holder.adapterPosition].flag
                    if(chh == true) {
                        l3[holder.adapterPosition].flag= false
                    }
                    else{
                        l3[holder.adapterPosition].flag= true
                    }
                    holder.CheckBox.buttonTintList=ColorStateList.valueOf(ContextCompat.getColor(context, R.color.greyLitee))

//                    for(i in 0 until l3.size){
//                        for(j in 0 until data?.size!!){
//                            if(l3[i].title == data[j]){
//                                l3[i].flag=true
//                            }
//                        }
//                    }
//                    Log.i("lasnfdmasnf","l3 $l3")

                    Log.i("aksfnkasntf","l3 value is ->>>>>>>>>>>${l3}")
                    clicks.onClick(l3)
                }
            }
        })

        Log.i("aksfnkasnf","l3 value is added ${l3}")

    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface Click{
       fun onClick(valueList:ArrayList<GettingCheckDataList>)
    }

}