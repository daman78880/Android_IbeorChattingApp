package com.ibeor.ibeorchattingapp.modules.chatting.chatNow

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.view.marginLeft
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.giphy.sdk.core.GPHCore
import com.giphy.sdk.core.models.enums.RenditionType
import com.giphy.sdk.ui.Giphy
import com.giphy.sdk.ui.views.GPHMediaView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChattingAdatperr(val context: Context,val list:ArrayList<ChatModel>,val click:Clicked):RecyclerView.Adapter<ChattingAdatperr.MyViewHolder>(){
    val checkPosition=ArrayList<Int>()
    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val msg:AppCompatTextView=itemView.findViewById(R.id.msgChatting)
        val msgTime:AppCompatTextView=itemView.findViewById(R.id.txtMsgTimeChatting)
        val msgTick:AppCompatImageView=itemView.findViewById(R.id.imgMsgTick)


        val audioLayout:ConstraintLayout=itemView.findViewById(R.id.audioLayout)
        val imgPlayPause: ImageView = itemView.findViewById(R.id.audioPlayerPlayStop)
        val seekBar: SeekBar = itemView.findViewById(R.id.audioPlayerSeekbar)
        val audioTrackTiming: TextView = itemView.findViewById(R.id.audioPlayTimingTrack)
        val audioPlayerFullTiming: TextView = itemView.findViewById(R.id.audioPlayerFullTiming)
        val audioDemoImg: ImageView = itemView.findViewById(R.id.audioPlayerImg)
        val audioFullTiming: TextView = itemView.findViewById(R.id.recodingFullTiming)

        val audioTick: AppCompatImageView = itemView.findViewById(R.id.imgAudioTick)

        var currentaudioPosstion:Int?=null
        var mediaPlayer: MediaPlayer? = null
        var flag = true

        // for image view
        val imgImage:AppCompatImageView=itemView.findViewById(R.id.imgPickImagee)
        val imgLayout:ConstraintLayout=itemView.findViewById(R.id.imageLayout)
        val imgTime:AppCompatTextView=itemView.findViewById(R.id.imageShowTime)
        val imgImageTick:AppCompatImageView=itemView.findViewById(R.id.imgImageTick)

        // for vedio
        val showVedio: StyledPlayerView = itemView.findViewById(R.id.vvVidioShow)
        val showImgTime: AppCompatTextView = itemView.findViewById(R.id.txtImgTimingShow)
        val imgVedioTick:AppCompatImageView=itemView.findViewById(R.id.imgVedioTick)

        // for gif
        val gif: GPHMediaView=itemView.findViewById(R.id.gifsGridView)
        val gifTime: AppCompatTextView=itemView.findViewById(R.id.txtGifTimingShow)
        val imgGifTick:AppCompatImageView=itemView.findViewById(R.id.imgGifTick)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view:View?=null
        if(viewType == 1){
            // right Side
            view=LayoutInflater.from(context).inflate(R.layout.right_chat_layout,parent,false)
        }
        else{
            view=LayoutInflater.from(context).inflate(R.layout.left_chat_layout,parent,false)
        }
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Giphy.configure(context, "FKfvf2pRp0pcvQf9mX8lDFMhpyVERc42")
        if(list[position].viewType == 1) {
            holder.msg.visibility = View.VISIBLE
            holder.msgTime.visibility = View.VISIBLE
            holder.msg.text = list[position].message
            if (!checkPosition.contains(position)){
                holder.msgTick.visibility = View.VISIBLE
            if (list[position].readStatus) {
                holder.msgTick.setBackgroundResource(R.drawable.read_msg_tick)
            } else {
                holder.msgTick.setBackgroundResource(R.drawable.send_msg_tick)
            }
        }
            else{
                holder.msgTick.visibility = View.GONE
            }
            val time = getTime(list[position].time)
            holder.msgTime.text=time

        }
        else if(list[position].viewType == 2){
            holder.currentaudioPosstion=0
            holder.audioLayout.visibility=View.VISIBLE
            holder.audioFullTiming.visibility=View.VISIBLE
            holder.audioPlayerFullTiming.visibility=View.VISIBLE
            holder.audioPlayerFullTiming.visibility=View.VISIBLE
            if (!checkPosition.contains(position)) {
                holder.audioTick.visibility = View.VISIBLE
                if (list[position].readStatus) {
                    holder.audioTick.setBackgroundResource(R.drawable.read_msg_tick)
                } else {
                    holder.audioTick.setBackgroundResource(R.drawable.send_msg_tick)
                }
            }
            else{
                holder.audioTick.visibility = View.GONE
            }
            val updateSongTime = object : Runnable {
                override fun run() {
                    holder.currentaudioPosstion =  holder.mediaPlayer?.currentPosition
                    holder.seekBar.progress =  holder.currentaudioPosstion!!
                    val track=getTotalTime( holder.currentaudioPosstion!!)
                    holder.audioTrackTiming.text=track
                    Handler().postDelayed(this, 500)
                }
            }
            val time = getTime(list[position].time)
            holder.audioPlayerFullTiming.text = time
            holder.seekBar.progress = 0
            holder.imgPlayPause.setOnClickListener {
                if (holder.flag) {
                    holder.imgPlayPause.setImageResource(R.drawable.ic_baseline_stop_24)
                    holder.flag = false
                    try {
                        Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show()
                        Log.i("asfdmnsm,afn","lis ${list.get(position).audio_Url}")
                        holder.mediaPlayer = MediaPlayer()
                        holder.mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
                        holder.mediaPlayer?.setDataSource(list[position].audio_Url)
                        holder.mediaPlayer?.prepareAsync()
                        holder.mediaPlayer?.setOnPreparedListener {
                            holder.seekBar.max=holder.mediaPlayer!!.duration
                            val totalTime=getTotalTime(holder.mediaPlayer!!.duration)
                            holder.audioFullTiming.text=totalTime
                            Toast.makeText(context, "Playing", Toast.LENGTH_SHORT).show()
                            holder.mediaPlayer?.seekTo(holder.currentaudioPosstion!!)
                            it.start()
                            updateSongTime.run()
                        }
                        holder.mediaPlayer!!.setOnCompletionListener {
                            finishAudio(holder.mediaPlayer!!, holder.imgPlayPause, holder.seekBar)
                            holder.currentaudioPosstion=0
                            holder.mediaPlayer?.seekTo(0)
                            holder.flag=true
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("qwert", "error -> ${e.printStackTrace()}")
                    }
                } else {
                    holder.imgPlayPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    holder.flag = true
                    if (holder.mediaPlayer != null && holder.mediaPlayer?.isPlaying!!) {
                        try {
                            Toast.makeText(context,"Stop", Toast.LENGTH_SHORT).show()
                            holder.mediaPlayer!!.stop()

                        }
                        catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            holder.seekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    if(p2){
                        val track=getTotalTime(p1)
                        holder.currentaudioPosstion=p1
                        holder.audioTrackTiming.text=track
                        holder.mediaPlayer?.seekTo(p1)
                        holder.mediaPlayer?.start()
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }

            })

        }
        else if( list[position].viewType == 3){
            holder.imgLayout.visibility=View.VISIBLE
            holder.imgTime.visibility=View.VISIBLE
            holder.imgImage.clipToOutline=true
            if (!checkPosition.contains(position)) {
                holder.imgImageTick.visibility = View.VISIBLE
                if (list[position].readStatus) {
                    holder.imgImageTick.setBackgroundResource(R.drawable.read_msg_tick)
                } else {
                    holder.imgImageTick.setBackgroundResource(R.drawable.send_msg_tick)
                }
            }else{
                holder.imgImageTick.visibility = View.GONE
            }
            Glide.with(context).load(list[position].imageUrl).into(holder.imgImage)
            val time = getTime(list[position].time)
            holder.imgTime.text=time
            holder.imgImage.setOnClickListener {
                click.onClick(3,list[position].imageUrl!!)
            }
        }
        else if(list[position].viewType == 4){
            holder.showVedio.visibility=View.VISIBLE
            holder.showVedio.clipToOutline=true
            holder.showImgTime.visibility=View.VISIBLE
            if (!checkPosition.contains(position)) {
                holder.imgVedioTick.visibility = View.VISIBLE
                if (list[position].readStatus) {
                    holder.imgVedioTick.setBackgroundResource(R.drawable.read_msg_tick)
                } else {
                    holder.imgVedioTick.setBackgroundResource(R.drawable.send_msg_tick)
                }
            }
            else{
                holder.imgVedioTick.visibility = View.GONE
            }
            val player = ExoPlayer.Builder(context).build()
            holder.showVedio.player = player
            val mediaItemm = MediaItem.fromUri(list[position].vedioUrl!!)
            Log.i("abcdefgh", "uri vedio is ->${list[position].vedioUrl!!}")
            player.setMediaItem(mediaItemm)
            player.prepare()
            var f = true
            val time = getTime(list[position].time!!)
            holder.showImgTime.text = time
            holder.showVedio.setOnClickListener {
                player.playWhenReady = true
            }
            holder.showVedio.setOnLongClickListener {
                if(player.isPlaying){
                    player.stop()
                }
                click.onClick(4,list[position].vedioUrl!!)
                true
            }
        }
        else if(list[position].viewType == 5){
            holder.gifTime.visibility=View.VISIBLE
            holder.gif.visibility=View.VISIBLE
            if (!checkPosition.contains(position)) {
                holder.imgGifTick.visibility = View.VISIBLE
                if (list[position].readStatus) {
                    holder.imgGifTick.setBackgroundResource(R.drawable.read_msg_tick)
                } else {
                    holder.imgGifTick.setBackgroundResource(R.drawable.send_msg_tick)
                }
            }
            else{
                holder.imgGifTick.visibility = View.GONE
            }
            val time = getTime(list[position].time!!)
            holder.gifTime.text = time
            GPHCore.gifById(list[position].gif_sticker_emoji!!) { result, e ->
                holder.gif.setMedia(result?.data, RenditionType.original)
                e?.let {
                    //your code here
                }
            }
            holder.gif.setOnClickListener {
                click.onClick(5,list[position].gif_sticker_emoji!!)

            }
        }

        else{
            Toast.makeText(context, "Something error", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getTotalTime(time:Int):String{
        val minutes = (time % (1000 * 60 * 60)) as Int / (1000 * 60)
        val seconds = (time % (1000 * 60 * 60) % (1000 * 60) / 1000)
        val timing="${minutes} : $seconds"
        return timing
    }
    private fun finishAudio(mediaPlayer:MediaPlayer,imgPlayPause:ImageView,seekbar:SeekBar){
        imgPlayPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        mediaPlayer.seekTo(0)
        seekbar.progress=0

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

    override fun getItemViewType(position: Int): Int {
        var messageFrom=1
        val getLoginUid = MyUtils.getString(context,MyUtils.userUidKey)
        if(list[position].userIdUser == getLoginUid)
        {
            messageFrom=1
        }
        else{
            checkPosition.add(position)
            click.onChangeReadStatus(list[position].userId.toString(),list[position].documentId)
            messageFrom=0
        }
        return messageFrom
    }
    interface Clicked{
        fun onClick(type:Int,data:String)
        fun onChangeReadStatus(chatId:String,id:String)
    }


}