package com.ibeor.ibeorchattingapp.modules.chatting

import android.annotation.SuppressLint
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MatchUserVerticalAdatper(val context: Context,val list:ArrayList<MatchUserExtraData>,val click:CClicks):RecyclerView.Adapter<MatchUserVerticalAdatper.MyViewHolder>() {
    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val imgg:AppCompatImageView=itemView.findViewById(R.id.imgMatchPersonList)
        val namee:AppCompatTextView=itemView.findViewById(R.id.txtLikedPeople)
        val receviedMsg:AppCompatTextView=itemView.findViewById(R.id.txtUserSendList)
        val timeAgoMsgSend:AppCompatTextView=itemView.findViewById(R.id.txtTimeSendAgoList)
        val unReadMsgCount:AppCompatTextView=itemView.findViewById(R.id.txtMsgCountSendList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.chat_member_list,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.i("asdjfhaskjfdsdf","Executing ${list.get(position).user_Typing_Status} and ${list.get(position).login_Typing_Status}")
        Log.i("asjfbssgdsfdjf","usermsg ${list[position].msg}")

        if (list[position].user_Typing_Status.equals("true")){
            Log.i("asdjfhaskjfdsdf","inside condition running")
//            holder.timeAgoMsgSend.visibility=View.GONE
            holder.receviedMsg.visibility=View.VISIBLE
            holder.receviedMsg.text="Typing..."
            holder.receviedMsg.setTextColor(context.getColor(R.color.teal_200))
        }
        else{
            if (list[position].lastType.toInt() == 1 || list[position].lastType.toInt() == 2 || list[position].lastType.toInt() == 3 || list[position].lastType.toInt() == 4 ||list[position].lastType.toInt() == 5){
                holder.receviedMsg.text=list[position].msg
                val time=getTime(list[position].time)
                Log.i("asoMsgSend.visibility=View.VISIBLE\n" +
                        "            holder.timeAgjfbsjf","usertime ${list[position].time} and converted value is ${time}")
                holder.timeAgoMsgSend.text=time
                holder.timeAgoMsgSend.visibility=View.VISIBLE
                holder.timeAgoMsgSend.setTextColor(context.getColor(R.color.black))
                holder.receviedMsg.setTextColor(context.getColor(R.color.black))
            }
            else{
                holder.timeAgoMsgSend.visibility=View.GONE
                holder.receviedMsg.text=""
                holder.timeAgoMsgSend.text=""
            }
        }
        Log.i("asjfndjasbfd","count ${list[position].user_unseenMsg_coung}")
        if(list[position].user_unseenMsg_coung > 0){
            holder.unReadMsgCount.visibility=View.VISIBLE
            holder.unReadMsgCount.text=list[position].user_unseenMsg_coung.toString()
        }
        else{
            holder.unReadMsgCount.visibility=View.GONE
        }
        holder.imgg.clipToOutline=true
        Glide.with(context).load(list[position].user_Image).into(holder.imgg)
        holder.namee.text=list[position].user_name

        holder.itemView.setOnClickListener {
            click.onViewClick(position)
        }
    }
    @SuppressLint("SimpleDateFormat")
    private fun getTime(data:Long?): String {
        val gg= data?.toLong()
        val simpleDataFormate= SimpleDateFormat("hh:mm")
        val date= Date(gg!!)
        val time=simpleDataFormate.format(date)
        return time
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface CClicks{
        fun onViewClick(position: Int)
    }
}