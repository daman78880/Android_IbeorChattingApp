package com.ibeor.ibeorchattingapp.modules.gettingUserDetail

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
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ibeor.ibeorchattingapp.R

class CountryAdatper(val context: Context,val list:ArrayList<countryItem>,val click:clickedd):RecyclerView.Adapter<CountryAdatper.MyViewHolder>() {
    var selectCount:Int=0
    var selectedCountryList=ArrayList<String>()
    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val flag:AppCompatImageView=itemView.findViewById(R.id.imgCountryFlag)
        val flagName:AppCompatTextView=itemView.findViewById(R.id.txtCountryFlag)
        val flagCheckBox:CheckBox=itemView.findViewById(R.id.checkBoxCountryFlag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.country_select_layout_for_rv,parent,false)
        return MyViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.flagName.text = list[position].name
        val flag = context.resources.getIdentifier(list[position].flag.lowercase(), "drawable", context.packageName)
        holder.flag.setImageResource(flag)
        if (holder.flagCheckBox.isChecked) {
            holder.flagCheckBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gulabiColor))
        }
        else {
            holder.flagCheckBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.greyLitee))
        }
        holder.flagCheckBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {

                if (selectCount < 2) {
                    if (holder.flagCheckBox.isChecked) {
                        selectCount += 1
                        selectedCountryList.add(list[holder.adapterPosition].name)
                        holder.flagCheckBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gulabiColor))
                    Log.e("asdfjhsabfd", "on click less then two ${selectCount}")
                    }
                    else {
                        selectCount -= 1
                        selectedCountryList.remove(list[holder.adapterPosition].name)
                        Log.e("asdfjhsabfd", "on click greater than two ${selectCount}")
                        holder.flagCheckBox.buttonTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(context,
                                R.color.greyLitee))
                    }
                }
                else {
                    if(!holder.flagCheckBox.isChecked) {
                        selectCount-=1
                        selectedCountryList.remove(list[holder.adapterPosition].name)
                        holder.flagCheckBox.isChecked = false
                        holder.flagCheckBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.greyLitee))
                        Log.e("asdfjhsabfd", "user select only two country try again. ${selectCount}")
                    }
                    else{
                        holder.flagCheckBox.isChecked = false
                        holder.flagCheckBox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.greyLitee))
                    }
                }
                click.onItemChange(selectedCountryList)
                Log.e("asdfjhsabfd", "on click ${selectedCountryList}")
            }
        })
    }
    override fun getItemCount(): Int {
        return list.size
    }
    interface clickedd{
        fun onItemChange(data:ArrayList<String>)
    }
}