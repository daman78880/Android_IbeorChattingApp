//package com.ibeor.ibeorchattingapp.modules.editProfile.GettingUserData
//
//import android.content.Context
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.CompoundButton
//import android.widget.RadioButton
//import androidx.appcompat.widget.AppCompatImageView
//import androidx.appcompat.widget.AppCompatTextView
//import androidx.recyclerview.widget.RecyclerView
//import com.ibeor.ibeorchattingapp.R
//import com.ibeor.ibeorchattingapp.modules.userData.userDataByRadioBox.GettingUserDataForRadio
//
//
//class AdapterForGettingChildernData(val context: Context,val list:ArrayList<GettingUserDataForRadio>):RecyclerView.Adapter<AdapterForGettingChildernData.MyViewHolder>(){
//    var pos:Int?=null
//    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
//        val txtTitle:AppCompatTextView=itemView.findViewById(R.id.txtTitleRadioChildren)
//        val imgEmoji:AppCompatImageView=itemView.findViewById(R.id.childrenEmoji)
//        val radioButton:RadioButton=itemView.findViewById(R.id.radioButtonChildren)
//        val view:View=itemView.findViewById(R.id.viewChildrenView)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val view=LayoutInflater.from(context).inflate(R.layout.getting_childer_data,parent,false)
//        return MyViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        Log.i("checkNow","list size is $list")
//        holder.txtTitle.text=list[position].title
//        if(list.size-1 == position){
//            holder.view.visibility=View.INVISIBLE
//        }
//        if(list[position].img != null) {
//            Log.i("abcdef","inside poos value is $pos")
//            holder.imgEmoji.setImageResource(list[position].img!!)
//                holder.imgEmoji.visibility = View.VISIBLE
//            }
//        if(pos != null){
////            list.map {
////                holder.radioButton.isChecked=false
////            }
////            Log.i("abcdefd","inside false method$pos")
//            if(pos != holder.adapterPosition){
//                Log.i("abcdefd","inside false method$pos and adapter position ${holder.adapterPosition}")
//                holder.radioButton.isChecked=false
//            }
//            else{
//                holder.radioButton.isChecked=true
//            }
//        }
//        holder.radioButton.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
//            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
//                if(p1){
//
//                    pos=holder.adapterPosition
//                    Log.i("abcdef","pos value is $pos")
//
//                    notifyDataSetChanged()
//                }
//            }
//        })
//    }
//    override fun getItemCount(): Int {
//        return list.size
//    }
// interface Click{
//    fun  onClick(position:Int)
// }
//
//}


package com.ibeor.ibeorchattingapp.modules.editProfile.GettingUserData

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.modules.userData.userDataByRadioBox.GettingUserDataForRadio


class AdapterForGettingChildernData(val context: Context, val list:ArrayList<GettingUserDataForRadio>,
                                    var value:String?, val clicks:Click):RecyclerView.Adapter<AdapterForGettingChildernData.MyViewHolder>(){
    var pos:Int?=null
    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val txtTitle:AppCompatTextView=itemView.findViewById(R.id.txtTitleRadioChildren)
        val imgEmoji:AppCompatImageView=itemView.findViewById(R.id.childrenEmoji)
        val radioButton:RadioButton=itemView.findViewById(R.id.radioButtonChildren)
        val view:View=itemView.findViewById(R.id.viewChildrenView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.getting_childer_data,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if(list[position].radioCheck == true){
            holder.radioButton.isChecked=true
        }
        else{
            holder.radioButton.isChecked=false
        }
        holder.txtTitle.text=list[position].title

        if(value !=null){
            Log.i("sajklfn","holder vlaue ${holder.txtTitle.text} and value is ${value}")
            if(holder.txtTitle.text.equals(value)) {
                holder.radioButton.isChecked = true
                value="asfdn"
            }
//            else{
//                holder.radioButton.isChecked=false
//            }
        }


        if(list.size-1 == position){
            holder.view.visibility=View.INVISIBLE
        }
        else{
            holder.view.visibility=View.VISIBLE
        }
        if(list[position].img != null) {
            holder.imgEmoji.setImageResource(list[position].img!!)
            holder.imgEmoji.visibility = View.VISIBLE
        }
        else{
            holder.imgEmoji.visibility = View.GONE
        }
        holder.radioButton.setOnClickListener {
            clicks.onClick(position,holder.txtTitle.text.toString())
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
    interface Click{
        fun  onClick(position:Int,name:String)
    }
}