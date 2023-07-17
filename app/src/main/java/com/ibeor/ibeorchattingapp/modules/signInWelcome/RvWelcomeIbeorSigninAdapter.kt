package com.ibeor.ibeorchattingapp.modules.signInWelcome

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.ibeor.ibeorchattingapp.R

class RvWelcomeIbeorSigninAdapter(val context: Context,val list:ArrayList<WelcomeIbeorDataModel>):RecyclerView.Adapter<RvWelcomeIbeorSigninAdapter.MyViewModel>() {
    inner class MyViewModel(itemView:View):RecyclerView.ViewHolder(itemView){
        val txtTitle:AppCompatTextView=itemView.findViewById(R.id.rvWelcomeIbeorTitleSigned)
        val txtSubTitle:AppCompatTextView=itemView.findViewById(R.id.rvWelcomeIbeorSubTitleSigned)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewModel {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.rv_layout_welcome_ibeor,parent,false)
        return MyViewModel(view)
    }

    override fun onBindViewHolder(holder: MyViewModel, position: Int) {
        holder.txtTitle.text=list[position].title
        holder.txtSubTitle.text=list[position].subTitle
    }
    override fun getItemCount(): Int {
        return list.size
    }

}