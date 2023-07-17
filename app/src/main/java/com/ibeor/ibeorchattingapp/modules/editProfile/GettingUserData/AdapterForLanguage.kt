//package com.ibeor.ibeorchattingapp.modules.editProfile.GettingUserData
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.res.ColorStateList
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.CheckBox
//import android.widget.CompoundButton
//import android.widget.Toast
//import androidx.appcompat.widget.AppCompatTextView
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.RecyclerView
//import com.ibeor.ibeorchattingapp.R
//import com.ibeor.ibeorchattingapp.modules.userData.updateLanguageData.LanguageData
//
//class AdapterForLanguage(val context: Context, val list:ArrayList<LanguageData>, val click: Clicks):RecyclerView.Adapter<AdapterForLanguage.MyViewHolder>() {
//    var gg=false
//    var selectedList=ArrayList<LanguageData>()
//    var sizee=1
//    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
//        val checkBox:CheckBox=itemView.findViewById(R.id.checkBoxLanguage)
//        val txtTitle:AppCompatTextView=itemView.findViewById(R.id.txtTilteLanguage)
//        val view:View=itemView.findViewById(R.id.viewLanguage)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val view=LayoutInflater.from(parent.context).inflate(R.layout.language_layout_checkbox,parent,false)
//        return MyViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
//        holder.txtTitle.text=list[position].title
//        holder.checkBox.isChecked=list[position].check?:false
//        if (gg){
//            for(i in 0 until list.size){
//                if(position == 0){
//                    list[position].check=true
//                }
//                else{
//                    list[position].check=false
//                }
//            }
//        }
//        if(list[position].check== true){
//            holder.checkBox.isChecked=true
//            holder.checkBox.buttonTintList= ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gulabiColor))
//        }
//        else{
//            holder.checkBox.isChecked=false
//            holder.checkBox.buttonTintList= ColorStateList.valueOf(ContextCompat.getColor(context, R.color.greyLitee))
//        }
//
////        if (gg==true){
////            for(i in 0 until list.size){
////                if(position == 0){
////                    list[position].check=true
////                }
////                else{
////                    list[position].check=false
////                }
////            }
////            notifyDataSetChanged()
////        }
////        else{
////            Log.i("kasfnk","notthing doing here")
////        }
//
//        holder.checkBox.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
//            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
//                if(p1) {
//                    if (position == 0) {
//                        gg = true
//                        notifyDataSetChanged()
////                            click.onViewClick(position, holder.txtTitle.text.toString(), list)
//                    }
//                    else {
//
//                        if (checkMax(sizee)) {
//                            sizee += 1
//                            holder.checkBox.buttonTintList =
//                                ColorStateList.valueOf(ContextCompat.getColor(context,
//                                    R.color.gulabiColor))
//                            if (position == 0) {
//                                gg = true
//                                notifyDataSetChanged()
////                            click.onViewClick(position, holder.txtTitle.text.toString(), list)
//                            } else {
//                                list[position].check = true
//                                click.onViewClick(position, holder.txtTitle.text.toString(), list)
//                            }
//                        } else {
//                            holder.checkBox.isChecked = false
//                            list[position].check = false
//                            holder.checkBox.buttonTintList =
//                                ColorStateList.valueOf(ContextCompat.getColor(context,
//                                    R.color.greyLitee))
//                            click.onViewClick(position, holder.txtTitle.text.toString(), list)
//                            Toast.makeText(context,
//                                "You can select only 3 language only.",
//                                Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//                else{
//                    if (position  == 0) {
//                        holder.checkBox.buttonTintList= ColorStateList.valueOf(ContextCompat.getColor(context, R.color.greyLitee))
//                        gg = false
//                    }
//                    else{
//                        holder.checkBox.buttonTintList= ColorStateList.valueOf(ContextCompat.getColor(context, R.color.greyLitee))
//                        sizee--
//                        list[position].check=false
////                        notifyDataSetChanged()
//                        click.onViewClick(position,holder.txtTitle.text.toString(),list)
//                    }
//                }
//            }
//        }
//    )
//}
//    override fun getItemCount(): Int {
//      return list.size
//    }
//    private fun checkMax(sizee:Int):Boolean{
//        Log.i("sakfjnsa","inside check size ${sizee}")
//        if(sizee <= 3 ){
//            Log.i("sakfjnsa","inside check true")
//            return true
//        }
//        else{
//            return false
//        }
//    }
//    interface Clicks{
//        fun onViewClick(position: Int,title:String,list:ArrayList<LanguageData>)
//    }
//}
//



package com.ibeor.ibeorchattingapp.modules.editProfile.GettingUserData

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.modules.userData.updateLanguageData.LanguageData

class AdapterForLanguage(val context: Context, val list:ArrayList<LanguageData>, val data:ArrayList<String>,val click: Clicks):RecyclerView.Adapter<AdapterForLanguage.MyViewHolder>() {
    var gg=1
    var checkValue=1
    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val checkBox:CheckBox=itemView.findViewById(R.id.checkBoxLanguage)
        val txtTitle:AppCompatTextView=itemView.findViewById(R.id.txtTilteLanguage)
        val view:View=itemView.findViewById(R.id.viewLanguage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.language_layout_checkbox,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.txtTitle.text=list[position].title
        holder.checkBox.isChecked=list[position].check?:false

       if(checkValue<=data.size) {
           if (data.size >= 1) {
               for (i in 0 until data.size) {
                   if (holder.txtTitle.text.equals(data[i])) {
                       list[position].check = true
                       checkValue += 1
                   }
               }
           }
       }
        else{
            data.clear()
       }
        if(list[position].check== true){
            holder.checkBox.buttonTintList= ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gulabiColor))
            holder.checkBox.isChecked=true
        }
        else{
            holder.checkBox.isChecked=false
            list[position].check=false
            holder.checkBox.buttonTintList= ColorStateList.valueOf(ContextCompat.getColor(context, R.color.greyLitee))
        }
        Log.i("safjbsdgf","list is ${data}")


        holder.checkBox.setOnClickListener {
            if (list[position].title!!.lowercase().equals("none")){
                Log.i("asfdasdf","inside sfa")
                if(!list[position].check!!){
                    Log.i("asfdasdf","inside two")
                    list.forEachIndexed { index, languageData ->
                        list[index].check=list[index].title?.lowercase().equals("none")
                    }
                    click.onViewClick(position,holder.txtTitle.text.toString(),list)
                    notifyDataSetChanged()
                }
                else{
                    Log.i("asfdasdf","inside else")
                    list[position].check=false
                    holder.checkBox.buttonTintList= ColorStateList.valueOf(ContextCompat.getColor(context, R.color.greyLitee))
                    click.onViewClick(position,holder.txtTitle.text.toString(),list)
                }
            }
            else{
                Log.i("asfdasdf","else else inside sfa")
                for(i in 0 until list.size){
                    if(list[i].title?.lowercase().equals("none")){
                        list[i].check=false
                        notifyDataSetChanged()
                        break
                    }
                }
                var clickValue=1
                for(i in 0 until list.size){
                    if(list[i].check==true) {
                        clickValue+=1
                    }
                }

                if(checkMax(clickValue)){
                    if (list[position].check==false) {
                        holder.checkBox.buttonTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(context,
                                R.color.gulabiColor))
                        list[position].check = true
                        holder.checkBox.isChecked = true
                        click.onViewClick(position,holder.txtTitle.text.toString(),list)
                    }
                    else{
                        holder.checkBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.greyLitee))
                        list[position].check = false
                        holder.checkBox.isChecked = false
                        click.onViewClick(position,holder.txtTitle.text.toString(),list)
                    }
                }
                else{
                    if(list[position].check==true){
                        holder.checkBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.greyLitee))
                        list[position].check = false
                        holder.checkBox.isChecked = false
                        click.onViewClick(position,holder.txtTitle.text.toString(),list)

                    }
                    else {
                        Toast.makeText(context, "You can select only three language", Toast.LENGTH_SHORT).show()
                        click.onViewClick(position,holder.txtTitle.text.toString(),list)
                    }
                }
            }
        }


    }
    override fun getItemCount(): Int {
        return list.size
    }
    private fun checkMax(sizee:Int):Boolean{
        Log.i("sakfjnsa","inside check size ${sizee}")
        if(sizee <= 3 ){
            Log.i("sakfjnsa","inside check true")
            return true
        }
        else{
            return false
        }
    }
    interface Clicks{
        fun onViewClick(position: Int,title:String,list:ArrayList<LanguageData>)
    }

}

