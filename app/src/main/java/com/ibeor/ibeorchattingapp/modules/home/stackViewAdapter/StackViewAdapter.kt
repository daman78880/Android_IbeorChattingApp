
package com.ibeor.ibeorchattingapp.modules.home.stackViewAdapter

import android.content.Context
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.modules.gettingUserDetail.countryItem
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import com.ibeor.ibeorchattingapp.modules.userData.FeatchFireBaseRegisterData
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.UnsupportedEncodingException

class StackViewAdapter(val context:Context,private var userList:ArrayList<FeatchFireBaseRegisterData>, var click:Clickss):RecyclerView.Adapter<StackViewAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
//        val imgUser:AppCompatImageView=itemView.findViewById(R.id.userImageCard)
        val imgUser:ViewPager2=itemView.findViewById(R.id.userImageCard)
        val txtName:AppCompatTextView=itemView.findViewById(R.id.userNameCard)
        val txtAwayLocation:AppCompatTextView=itemView.findViewById(R.id.txtLocationCard)
        val imgFirstFlag:AppCompatImageView=itemView.findViewById(R.id.UserFirdFlagCard)
        val imgSecondFlag:AppCompatImageView=itemView.findViewById(R.id.UserSecondFlagCard)
        val flagLayout:LinearLayout=itemView.findViewById(R.id.flagLayoutHome)
        val infoUser:AppCompatImageView=itemView.findViewById(R.id.imgInfoCard)
        val indicator: WormDotsIndicator =itemView.findViewById(R.id.indicator)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =LayoutInflater.from(context).inflate(R.layout.user_view_layout,parent,false)
        return MyViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtName.text=userList[position].user_name
//        val distance: Double = userList[position].user_latitute!!.toDouble().distanceTo(endPoint).toDouble()
//        binding!!.txtLocationAwayStatusUserInfo.text= String.format("%.1f miles aways", distance/1000f)
//        Log.i("asakjflnas","lat current ${MyUtils.getString(context,MyUtils.userLotiKey)} and long ${MyUtils.getString(context,MyUtils.userLongiKey)}")
//        Log.i("asakjflnas","adapter lat ${userList[position].user_latitute} and long ${userList[position].user_longitute}")
//        val dis=distance(MyUtils.getString(context,MyUtils.userLotiKey)!!.toDouble(),MyUtils.getString(context,MyUtils.userLongiKey)!!.toDouble(),userList[position].user_latitute!!.toDouble(),userList[position].user_longitute!!.toDouble())
////        holder.txtAwayLocation.text="${userList[position].user_latitute}+${userList[position].user_longitute}"
//        val startPoint = Location("locationA")
//        startPoint.setLatitude(MyUtils.getString(context,MyUtils.userLotiKey)!!.toDouble())
//        startPoint.setLongitude(MyUtils.getString(context,MyUtils.userLongiKey)!!.toDouble())
//        val endPoint = Location("locationB")
//        endPoint.setLatitude(userList[position].user_latitute!!.toDouble())
//        endPoint.setLongitude(userList[position].user_longitute!!.toDouble())
//        val distance: Double = startPoint.distanceTo(endPoint).toDouble()

        val lat=MyUtils.getLongAsDouble(context,MyUtils.userLotiKey)
        val long=MyUtils.getLongAsDouble(context,MyUtils.userLongiKey)
        val userLat=userList[position].user_latitute!!.toDouble()
        val userLong=userList[position].user_longitute!!.toDouble()
        val flag_one=userList[position].selected_first_root
        val flag_Two=userList[position].selected_second_root
        val tempList=getCountry(context)
        if(flag_one.equals("") && flag_Two.equals("") || flag_one.equals("null") && flag_Two.equals("null")){
            holder.flagLayout.visibility=View.GONE
        }
        if(!flag_one.equals("") && !flag_one.equals("null")){
            Log.i("sfasdf","inside conditino first ${flag_one}")
        for (i in 0 until tempList.size){
            if(tempList[i].name == flag_one){
                Log.i("sfasdf","inside insed loop conditino two")
                val flag = context.resources.getIdentifier(tempList[i].flag.lowercase(), "drawable", context.packageName)
                Log.i("sfasdf","flag name drawable ${flag}")
                holder.imgFirstFlag.setImageResource(flag)
            }
        }
        }
        if(!flag_Two.equals("") && !flag_Two.equals("null")){
            Log.i("sfasdf","inside conditino  flagtwo ${flag_Two}")
            for (i in 0 until tempList.size){
                if(tempList[i].name == flag_Two){
                    Log.i("sfasdf","inside insed loop conditino two")
                    val flag = context.resources.getIdentifier(tempList[i].flag.lowercase(), "drawable", context.packageName)
//                    flag.setImageResource(flag)
                    Log.i("sfasdf","flag name drawable two  ${flag}")

                    holder.imgSecondFlag.setImageResource(flag)
                }
            }
        }
       val distance=MyUtils.getAwayLocation(lat,long,userLat,userLong)
//        holder.txtAwayLocation.text= String.format("%.1f miles aways", distance)
        holder.txtAwayLocation.text= String.format("${distance.toInt()} miles aways")
//        Glide.with(holder.imgUser).load(userList[position].image_url_list?.get(0)).into(holder.imgUser)
        val adapterr=PreviewImageVerticalAdapter(context, userList[position].image_url_list!!,false)
            holder.imgUser.adapter=adapterr
            holder.indicator.attachTo(holder.imgUser)
        holder.infoUser.setOnClickListener {
            click.onInfoClick(holder.infoUser.id,position)
        }



//        holder.imgUser.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                val layoutManager: LinearLayoutManager =
//                    LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
//                val totalItemCount: Int = layoutManager.getItemCount()
//                val lastVisible: Int = layoutManager.findLastVisibleItemPosition()
//                val endHasBeenReached = lastVisible + 5 >= totalItemCount
//                if (totalItemCount > 0 && endHasBeenReached) {
//                    //you have reached to the bottom of your recycler view
//                }
//            }
//        })
    }



    override fun getItemCount(): Int {
        return userList.size
    }
    interface Clickss{
        fun onInfoClick(id:Int,position: Int)
    }
    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
    fun getCountry(context: Context): ArrayList<countryItem> {
        var countryList: ArrayList<countryItem>? = null
        val inputStream: InputStream = context.resources.openRawResource(R.raw.countries)
        try {
            val reader: Reader = InputStreamReader(inputStream, "UTF-8")
            val gson = Gson()
            countryList = gson.fromJson(
                reader,
                object : TypeToken<ArrayList<countryItem>>() {}.type
            )
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return countryList!!
    }


}