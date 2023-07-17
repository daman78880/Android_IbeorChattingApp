package com.ibeor.ibeorchattingapp.modules.myUtils

import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.ArraySet
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.ui.graphics.GraphicsLayerScope
import com.bumptech.glide.Glide
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.modules.userData.FeatchFireBaseRegisterData
import com.skyfishjy.library.RippleBackground
import java.lang.reflect.Type


object  MyUtils {
    var dialog:Dialog?=null
    var a:Int=1
    var sharedPref:SharedPreferences?=null

    var checkStatus:Boolean=false
    const val userMobileNumberKey="MOBILE_NUMBER"
    const val userMobileNumberCodeKey="MOBILE_NUMBER_CODE"
    const val userEmailKey="USER_MAIl"
    const val userNameKey="USER_NAME"
    const val userGenderKey="USER_GENDER"
    const val userLookingForKey="USER_Looking_GENDER"
    const val userFullAgeKey="USER_FULL_AGE"
    const val userAgeKey="USER_AGE"
    const val userLongiKey="USER_LOCATION_LONGI"
    const val userLotiKey="USER_LOCATION_LATI"
    const val userSelectedCountryKeyOne="USER_SelectedCoutryOne"
    const val userSelectedCountryKeyTwo="USER_SelectedCoutryTwo"
    const val userUidKey="USER_UID"
    const val userLoginKey="USER_LOGIN"
    const val userArrayImageLinkKey="USER_IMAGE_Link_LIST"
    const val userArrayImagePathKey="USER_IMAGE_Path_LIST"
    const val userAboutInfo="USER_INFO"
    const val userCurrentWork="USER_CURRENT_WORK"
    const val userSchool="USER_SCHOOL"
    const val userCityLive="USER_CITY_LIVE"
    const val userHomeTown="USER_HOME_TOWN"
    const val userLookingForInPeople="USER_LOOKING_FOR_PEOPLE_TYPE"
    const val userPet="USER_PETS"
    const val userChildren="USER_CHILDREN"
    const val userAstrologicalSign="USER_ASTROLOGICAL_SIGN"
    const val userReligion="USER_RELIGION"
    const val userEducation="USER_EDUCATION"
    const val userLanguage="USER_LANGUAGEE"
    const val userHeight="USER_HEIGHT"
    const val userBodyType="USER_BODY_TYPE"
    const val userExerciseStatus="USER_EXERCISE"
    const val userDrinkStatus="USER_DRINK_STATUS"
    const val userSmokerStatus="USER_SMOKER_STATUS"
    const val userMarijuanaStatus="USER_MARIJUANA_STATUS"
    const val userInterestsStatus="USER_INTEREST"
    const val userStageGoing="USER_PROGRESS_STATUS"
    const val userSaveCardList="USER_CARD_LIST"
    const val userFilterStartAge="USER_FILTER_START_AGE"
    const val userFilterEndAge="USER_FILTER_END_AGE"
    const val userFilterDistance="USER_FILTER_DISTANCE"
    const val userFilterDistanceFlag="USER_FILTER_DISTANCE_FLAG"
    const val userFilterAgeFlag="USER_FILTER_AGE_FLAG"
    const val userFirestRunFlag="USER_FIRST_RUN_FLAG"
//    const val userRefreshList="USER_REFRESH_LIST"
//    const val userDisappeardCard="USER_DISAPPEARD_UID"




    fun showProgress(context: Context){
         dialog= Dialog(context)
        dialog!!.setContentView(R.layout.dialog_layout)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.window!!.setLayout(    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setCancelable(false)
//        var image= dialog!!.findViewById<ImageView>(R.id.img)
        var tvCancel= dialog!!.findViewById<TextView>(R.id.tvCancelBar)
//        val clk_rotate = AnimationUtils.loadAnimation(
//            context,
//            R.anim.clockwise
//        )
        tvCancel.setOnClickListener {
            stopProgress()
        }
//        image.startAnimation(clk_rotate)
        dialog!!.setCancelable(false)
        dialog!!.setCanceledOnTouchOutside(false)
        if (!dialog!!.isShowing) {
            dialog!!.show()
        }
    }
    fun stopProgress() {
        if(dialog != null){
            dialog!!.cancel()}
    }
     fun showBoostDialog(context: Context){
        val boostDialog=Dialog(context)
        boostDialog.setContentView(R.layout.alert_booster_timer)
        boostDialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        boostDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val boostBtn: AppCompatButton =boostDialog.findViewById(R.id.btnRippleGetMoreBoosts)
        val boostOkBtn: AppCompatButton =boostDialog.findViewById(R.id.btnRippleOkBoost)
        val boostRipple: RippleBackground =boostDialog.findViewById(R.id.mainRipple)
        val boostImage: AppCompatImageView =boostDialog.findViewById(R.id.imgTestingRipple)
         val url=MyUtils.getArray(context,MyUtils.userArrayImageLinkKey)
         Glide.with(context).load(url!!.get(0)).into(boostImage)
         val time:AppCompatTextView=boostDialog.findViewById(R.id.txtRippleTimeBoost)
         var cTimer:CountDownTimer?=null
          fun setTimeSecond(){
             cTimer = object : CountDownTimer(45000, 1000) {
                 override fun onTick(millisUntilFinished: Long) {
                     Log.i("testingNoww","onTick ${millisUntilFinished/1000}")
                     val timeS=(millisUntilFinished/1000).toString()
                     var str:String?=null
                     if(timeS.length == 1){
                         str="00:0${millisUntilFinished/1000}"
                     }
                     else {
                         str = "00:${millisUntilFinished / 1000}"
                     }
                     time.text=str
                    }
                 override fun onFinish() {
                    time.text="00:00:00"
                     dialog?.dismiss()
                 }
             }
             cTimer!!.start()
         }
         setTimeSecond()
         boostRipple.startRippleAnimation()
        boostBtn.setOnClickListener {
            setTimeSecond()
            boostRipple.startRippleAnimation()
        }
        boostOkBtn.setOnClickListener {
            boostDialog.dismiss()
        }
        boostDialog.show()
    }
    fun setTerms(context:Context):SpannableStringBuilder{
        val builder = SpannableStringBuilder()
        val gg="By tapping create account or Sign in, you agree to our\nTerms. Learn how we Process your data on our\n <u>Privacy Policy</u> and <u>Cookies Policy</u>"
        val s="By tapping create account or Sign in, you agree to our\nTerms."
        val spannableString = SpannableString(s)
        val underlineSpan = UnderlineSpan()
        val foregroundColorSpanTeal = ForegroundColorSpan(context.resources.getColor(R.color.teal_200))
        spannableString.setSpan(foregroundColorSpanTeal, 55, 60, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(underlineSpan, 55, 60, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.append(spannableString)

        val ss=" Learn how we Process your data on our\n"
        val spannableString2 = SpannableString(s)
        val underlineSpan1 = UnderlineSpan()
        val foregroundColorSpanTeal1 = ForegroundColorSpan(context.resources.getColor(R.color.teal_200))
        spannableString2.setSpan(foregroundColorSpanTeal1, 101, 115, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString2.setSpan(underlineSpan1, 101, 115, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.append(spannableString2)


        val sss=" <u>Privacy Policy</u> and <u>Cookies Policy</u>"
        val spannableString3 = SpannableString(s)
        val underlineSpan2 = UnderlineSpan()
        val foregroundColorSpanTeal2 = ForegroundColorSpan(context.resources.getColor(R.color.teal_200))
        spannableString2.setSpan(foregroundColorSpanTeal2, 101, 115, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString2.setSpan(underlineSpan2, 101, 115, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.append(spannableString3)
//        val d=builder.toString()
     return builder
    }

     fun putString(context: Context,key:String,value:String){
        val sharedPref = context.getSharedPreferences("userData", MODE_PRIVATE)
        val editor=sharedPref.edit()
        editor.putString(key,value)
        editor.apply()
}

    fun getString(context: Context,key: String):String?{
        val sharedPref = context.getSharedPreferences("userData", MODE_PRIVATE)
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("userData", MODE_PRIVATE)
            val value = sharedPreferences.getString(key,null)
            return value

        }

     fun putInt(context: Context, key:String, value: Int){
        val sharedPref = context.getSharedPreferences("userData", MODE_PRIVATE)
        val editor=sharedPref.edit()
        editor.putInt(key,value)
        editor.apply()
}

    fun getInt(context: Context,key: String):Int?{
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("userData", MODE_PRIVATE)
            val value = sharedPreferences.getInt(key,0)
            return value

    }
    fun putLongAsDouble(context: Context,key:String,value:Double){
        val sharedPref = context.getSharedPreferences("userData", MODE_PRIVATE)
        val editor=sharedPref.edit()
        editor.putLong(key, value.toBits())
        editor.apply()
}

    fun getLongAsDouble(context: Context,key: String):Double{
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("userData", MODE_PRIVATE)
             val value=  Double.fromBits(sharedPreferences.getLong(key, 1))
            return value
    }

    fun putBoolean(context: Context,key:String,value:Boolean){
        val sharedPref = context.getSharedPreferences("userData", MODE_PRIVATE)
        val editor=sharedPref.edit()
        editor.putBoolean(key,value)
        Log.i("going","inside save value is ->$value")
        editor.apply()
    }
    fun getBoolean(context: Context,key: String):Boolean?{
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("userData", MODE_PRIVATE)
            val value = sharedPreferences.getBoolean(key, false)
            return value
        

    }
    fun putArray(context: Context,key:String,value:ArrayList<String>){
        val sharedPref = context.getSharedPreferences("userData", MODE_PRIVATE)
        val editor=sharedPref.edit()
        val gson = Gson()
        val json=gson.toJson(value)
        editor.putString(key,json)
        Log.i("going","Inside save Array value is ->$value")
        editor.apply()
    }
    fun getArray(context: Context, key: String): ArrayList<String>? {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("userData", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(key, "")
        val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return gson.fromJson(json, type)
    }
    fun clearPreference(context: Context){
        val pref = context.getSharedPreferences("userData", MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = pref.edit()
        prefsEditor.clear()
        prefsEditor.apply()
    }

    fun getCardUserList(context:Context): ArrayList<FeatchFireBaseRegisterData>? {
        val sharedPreferences= context.getSharedPreferences("userData", MODE_PRIVATE)
        val value = sharedPreferences.getString(userSaveCardList,"")
        val myType = object : TypeToken<ArrayList<FeatchFireBaseRegisterData>>() {}.type
        val logs = Gson().fromJson<ArrayList<FeatchFireBaseRegisterData>>(value, myType)
        return logs
    }
     fun getAwayLocation(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {

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

     fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

     fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
//    fun getRefreshStatus(context:Context):Boolean{
//        val sharedPreferences= context.getSharedPreferences("userData", MODE_PRIVATE)
//        val value = sharedPreferences.getBoolean(userRefreshList,true)
//        return value
//    }
//    fun saveRefreshStatus(context: Context,value:Boolean){
//        val sharedPreferences= context.getSharedPreferences("userData", MODE_PRIVATE)
//        val edit=sharedPreferences.edit()
//        edit.putBoolean(userRefreshList,value)
//        edit.commit()
//    }

    fun getChatId(context:Context,userUid:String):String{
        var chatid=""
        val loginuserUid=MyUtils.getString(context,MyUtils.userUidKey)
        if (userUid > loginuserUid!!){
            chatid="${loginuserUid}_${userUid}"
            return chatid
        }
        else{
            chatid="$userUid}_${loginuserUid}"
            return chatid
        }
    }

}