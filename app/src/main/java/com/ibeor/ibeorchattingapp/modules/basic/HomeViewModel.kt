package com.ibeor.ibeorchattingapp.modules.basic

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.service.autofill.FieldClassification
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.defaultDecayAnimationSpec
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.*
import com.google.android.gms.location.ActivityTransition
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.modules.chatting.MatchUserExtraData
import com.ibeor.ibeorchattingapp.modules.chatting.UserLikedCheckDetail
import com.ibeor.ibeorchattingapp.modules.chatting.chatNow.ChatMatchDataExtraDataClass
import com.ibeor.ibeorchattingapp.modules.chatting.chatNow.ChatModel
import com.ibeor.ibeorchattingapp.modules.gettingUserDetail.countryItem
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import com.ibeor.ibeorchattingapp.modules.userData.FeatchFireBaseRegisterData
import com.ibeor.ibeorchattingapp.modules.userData.settingDataClass.SettingDataClassForFirebase
import com.ibeor.ibeorchattingapp.modules.userData.uploadToFirebaseDataBase.UserFirebaseAboutData
import java.io.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.log


class HomeViewModel():ViewModel() {
    var firebaseb = FirebaseFirestore.getInstance()
    var userResponseList:MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>();
    var errorData:MutableLiveData<String> = MutableLiveData<String>()
//     var cardListView = ArrayList<FeatchFireBaseRegisterData>()
    fun getUserId():String{
        val id=Firebase.auth.currentUser!!.uid
        return id
    }

    fun checkUserPresentOrNot(context: Context,userUid:String,list:ArrayList<FeatchFireBaseRegisterData>):LiveData<ArrayList<FeatchFireBaseRegisterData>>{
        val sendList= MutableLiveData<ArrayList<FeatchFireBaseRegisterData>>()
        val tempList=list
//        list.filter { featchFireBaseRegisterData ->
//            featchFireBaseRegisterData.user_uid!=userUid
//        }
        for(i in 0 until tempList.size){
            if(list[i].user_uid.equals(userUid)){
                tempList.removeAt(i)
                break
            }
        }
        sendList.value=tempList
        return sendList
    }

    fun getUserIdList( context:Context):LiveData<ArrayList<String>> {
        var idList:ArrayList<String>?=null
            firebaseb.collection("User").get().addOnSuccessListener {
                idList= ArrayList<String>()
                for(i in  0 until it.documents.size) {
                    idList?.add(it.documents[i].id)
                }
                userResponseList.value=idList!!
            }.addOnFailureListener {
                errorData.value="Failure"
                Toast.makeText(context, "Failed due to ${it.message}", Toast.LENGTH_SHORT).show()
            }
            return userResponseList
    }


    fun getCheckStatusTwo(context: Context,uid:String):LiveData<FeatchFireBaseRegisterData>{
        val check=MutableLiveData<FeatchFireBaseRegisterData>()
        val firebaseb = FirebaseFirestore.getInstance()
        firebaseb.collection("User").document(uid).get().addOnSuccessListener {
            val user = FeatchFireBaseRegisterData()
            user.user_uid = it.data?.get("user_uid").toString()?:""
            user.user_name = it.data?.get("user_name").toString()?:""
            user.user_email = it.data?.get("user_email").toString()?:""
            user.user_age = it.data?.get("user_age").toString()?:""
            user.user_full_Age = it.data?.get("user_full_Age").toString()?:""
            user.user_mobileNumber =
                it.data?.get("user_mobileNumber").toString()?:""
            user.user_mobileNumber_code =
                it.data?.get("user_mobileNumber_code").toString()?:""
            user.user_gender = it.data?.get("user_gender").toString()?:""
            user.user_looking_gender =
                it.data?.get("user_looking_gender").toString()?:""
            user.user_latitute = it.data?.get("user_latitute").toString()?:""
            user.user_completeStatus =
                it.data?.get("user_completeStatus").toString()
            user.user_longitute = it.data?.get("user_longitute").toString()?:""
            user.selected_first_root =
                it.data?.get("selected_first_root").toString()?:""
            user.selected_second_root =
                it.data?.get("selected_second_root").toString()?:""
            user.image_url_list =
                it.data?.get("image_url_list") as ArrayList<String>?
            check.value=user

        }.addOnFailureListener {
            Toast.makeText(context,"Failed due to ${it.message}", Toast.LENGTH_SHORT).show()
        }
        return check
    }

    fun fetchFirebaseData( context:Context,testingList:ArrayList<String>):LiveData<ArrayList<FeatchFireBaseRegisterData>> {
        val userResponseList:MutableLiveData<ArrayList<FeatchFireBaseRegisterData>> = MutableLiveData<ArrayList<FeatchFireBaseRegisterData>>()
        val lisst=ArrayList<FeatchFireBaseRegisterData>()
        var indeValue=0
//        val testingList=ArrayList<String>()
        firebaseb.collection("User").get().addOnSuccessListener {
            if (!it.isEmpty) {
                Log.i("asjfhdkjsabfd","Not empty data")
                val len=it.documents.size
                for (i in 0 until it.documents.size) {
//                    Log.i("skajfjkasbfd", "inside loop is ${testingList[i]}")
                    val user = FeatchFireBaseRegisterData()
                    user.user_uid = it.documents[i].data?.get("user_uid").toString()
                    user.user_name = it.documents[i].data?.get("user_name").toString()
                    user.user_email = it.documents[i].data?.get("user_email").toString()
                    user.user_age = it.documents[i].data?.get("user_age").toString()
                    user.user_full_Age = it.documents[i].data?.get("user_full_Age").toString()
                    user.user_mobileNumber =
                        it.documents[i].data?.get("user_mobileNumber").toString()
                    user.user_mobileNumber_code =
                        it.documents[i].data?.get("user_mobileNumber_code").toString()
                    user.user_gender = it.documents[i].data?.get("user_gender").toString()
                    user.user_looking_gender =
                        it.documents[i].data?.get("user_looking_gender").toString()
                    user.user_latitute = it.documents[i].data?.get("user_latitute").toString()
                    user.user_completeStatus =
                        it.documents[i].data?.get("user_completeStatus") as String?
                    user.user_longitute = it.documents[i].data?.get("user_longitute").toString()
                    user.selected_first_root =
                        it.documents[i].data?.get("selected_first_root").toString()
                    user.selected_second_root =
                        it.documents[i].data?.get("selected_second_root").toString()
                    user.image_url_list =
                        it.documents[i].data?.get("image_url_list") as ArrayList<String>?
                    Log.i("stt", "3")
                    if (!testingList.contains(user.user_uid!!) && user.user_completeStatus.equals("4") ) {
                        Log.i("owenerDataAded","not like data present me")
                        if (user.user_uid != MyUtils.getString(context, MyUtils.userUidKey)) {
                            val ownerLookingGender = MyUtils.getString(context, MyUtils.userLookingForKey)
                            var filterAgeBoolean = MyUtils.getBoolean(context, MyUtils.userFilterAgeFlag)
                            var filterLocationBoolean =
                                MyUtils.getBoolean(context, MyUtils.userFilterDistanceFlag)
                            if (filterAgeBoolean == true && filterLocationBoolean == true) {
                                Log.i("asfbsaf", "dual filter true condition")
                                var genderValue = false
                                var awayValue = false
                                if (ownerLookingGender == user.user_gender) {
                                    Log.i("asfbsaf", "inside gender equal")
                                    val age = user.user_age?.toInt()
                                    val startAge = MyUtils.getInt(context, MyUtils.userFilterStartAge) ?: 18
                                    val endAge = MyUtils.getInt(context, MyUtils.userFilterEndAge) ?: 37
                                    if (startAge <= age!! && endAge >= age) {
                                        Log.i("asfbsaf", "inside age equla")
                                        genderValue = true
                                    }
                                    val filterDistance =
                                        MyUtils.getInt(context, MyUtils.userFilterDistance)

                                        val wL=MyUtils.getLongAsDouble(context, MyUtils.userLotiKey).toString()

                                    val ownerLat =wL.toDouble()?:30.7131424
                                        val wLo=MyUtils.getLongAsDouble(context, MyUtils.userLongiKey).toString()
                                    val ownerLong=wLo.toDouble()?:76.7095579
                                    val userLat: Double = user.user_latitute?.toDouble()!!
                                    val userLong: Double = user.user_longitute?.toDouble()!!
                                    val fullDistance = MyUtils.getAwayLocation(
                                        ownerLat as Double,
                                        ownerLong as Double,
                                        userLat,
                                        userLong
                                    )
                                    if (filterDistance != null) {
                                        if (filterDistance >= fullDistance) {
                                            Log.i("asfbsaf", "distance equal ")
                                            awayValue = true
                                        }
                                    }
                                }
                                if (genderValue == true && awayValue == true) {
                                    indeValue+=1
                                    lisst.add(user)
                                    if (indeValue == len - 1) {
                                        userResponseList.value = lisst
                                    }
                                    Log.i("owenerDataAded","filter data added")
                                    Log.i("asfbsaf", "addin data list ${user}")
                                }
                            } else if (filterAgeBoolean == true && filterLocationBoolean == false) {
                                if (ownerLookingGender == user.user_gender) {
                                    val age = user.user_age?.toInt()
                                    val startAge =
                                        MyUtils.getInt(context, MyUtils.userFilterStartAge) ?: 18
                                    val endAge =
                                        MyUtils.getInt(context, MyUtils.userFilterEndAge) ?: 37
                                    if (startAge <= age!! && endAge >= age) {
                                        Log.i("owenerDataAded","filter data added location toogle off")
                                        indeValue+=1
                                        lisst.add(user)
                                        if (indeValue == len - 1) {
                                            userResponseList.value = lisst
                                        }
                                    }
                                }
                            } else if (filterAgeBoolean == false && filterLocationBoolean == true) {
                                val filterDistance =
                                    MyUtils.getInt(context, MyUtils.userFilterDistance)
                                val ownerLat: Double =
                                    MyUtils.getString(context, MyUtils.userLotiKey)?.toDouble()!!
                                val ownerLong: Double =
                                    MyUtils.getString(context, MyUtils.userLongiKey)?.toDouble()!!
                                val userLat: Double = user.user_latitute?.toDouble()!!
                                val userLong: Double = user.user_longitute?.toDouble()!!
                                val fullDistance =
                                    MyUtils.getAwayLocation(ownerLat, ownerLong, userLat, userLong)
                                if (filterDistance != null) {
                                    if (filterDistance >= fullDistance) {
                                        Log.i("owenerDataAded","filter data added age toogle off")
                                        indeValue+=1
                                        lisst.add(user)
                                        if (indeValue == len - 1) {
                                            userResponseList.value = lisst
                                        }
                                    }
                                }
                            } else {
                                Log.i("owenerDataAded","filter data added location toogle off else")
                                indeValue+=1
                                lisst.add(user)
                                if (indeValue == len - 1) {
                                    userResponseList.value = lisst
                                }
                            }
                        }
                        else {
                            indeValue+=1
                            Log.i("owenerDataAded","direct Owner added successfuly i${i} and listsize ${len}")
                            lisst.add(user)
                            if (indeValue == len - 1) {
                                userResponseList.value = lisst
                            }
                            if (i == len - 1) {
                                userResponseList.value = lisst
                            }
                        }
                        if (i == len - 1) {
                            userResponseList.value = lisst
                        }
                        if (indeValue == len - 1) {
                            userResponseList.value = lisst
                        }

                    }
                    else{
                        indeValue+=1
                        if (indeValue == len - 1) {
                            userResponseList.value = lisst
                        }
                    }
//                    if (indeValue == len - 1) {
//                        userResponseList.value = lisst
//                    }
                }
//                    userResponseList.value = lisst
                }
            else{
                Log.i("asjfhdkjsabfd","empty data return ")
                userResponseList.value=lisst
            }
            }
        .addOnFailureListener {
            Toast.makeText(context, "Process Failed due to ${it.message}", Toast.LENGTH_SHORT).show()
        }
        return userResponseList
    }

    fun gettingMatchUserFormChat(context:Context,allUserList:ArrayList<String>):LiveData<ArrayList<MatchUserExtraData>>{

//        ddUserChatExtraFeature(context: Context,chatId:String,data:HashMap<String,String>,userId:String):LiveData<Boolean>{
//            val responseData=MutableLiveData<Boolean>()
//            var userHashMap=HashMap<String,java.util.HashMap<String,String>>()
//            userHashMap.put(userId,data)
//            firebaseb.collection("Chat").document(chatId).set(userHashMap, SetOptions.merge()).addOnSuccessListener {

            val responseData=MutableLiveData<ArrayList<MatchUserExtraData>>()
        var index=0
        val uid=MyUtils.getString(context,MyUtils.userUidKey)
        var tempResponse=ArrayList<MatchUserExtraData>()
        Log.i("sfdasfdasfd","Function start herev${allUserList}")
        for(i in 0 until allUserList.size){
            firebaseb.collection("Chat").document(allUserList[i]).get().addOnSuccessListener {
                if(it.exists()){
                    var d=it.data?.get(uid)
                    Log.i("sfdasfdasfd","Exist data ${d}")
                }
                else{
                    var d=it.id
                    Log.i("sfdasfdasfd","not exist data ${d.get(i)}")
                }
            }
        }




        return responseData
    }
    fun gettingMatchChatUserFromChat(context:Context,data:ArrayList<String>):LiveData<ArrayList<MatchUserExtraData>>{
        val responseData=MutableLiveData<ArrayList<MatchUserExtraData>>()
        val chatIdArray=ArrayList<String>()
        var loginUserUid=MyUtils.getString(context,MyUtils.userUidKey)
        Log.i("aslkdfjlsdgfasdf","running functoin")
        for(i in 0 until data.size){
            var chatid=""
            if (data[i] > loginUserUid!!){
                chatid="${loginUserUid}_${data[i]}"
                chatIdArray.add(chatid)
                Log.i("sajfhdbsaf","user greater id ${chatid}")
            }
            else{
                chatid="${data[i]}_${loginUserUid}"
                chatIdArray.add(chatid)
                Log.i("sajfhdbsaf","owner uid greater id ${chatid}")
            }
            Log.i("aslkdfjlsdgfasdf","chat id list size id ${chatIdArray.size}")

            for(i in 0 until chatIdArray.size) {
                firebaseb.collection("Chat").document(chatIdArray[i]).collection("Message").get().addOnSuccessListener {
                    val data=
                    if(it.isEmpty){
                        val d=it.documents
//                        if(d.size > 0){
//
//                        }
                        Log.i("aslkdfjlsdgfasdf","data d is ${d}")
                    }else{
                        val d=it.documents
//                        val dd=it.
                        Log.i("aslkdfjlsdgfasdf","data else d is ${d}")
                    }
                }.addOnFailureListener {

                }
            }
        }
        return responseData
    }

    fun fetchFirebaseAboutData(context: Context,uidKey:String,age:String):LiveData<UserFirebaseAboutData>{
        val userList=MutableLiveData<UserFirebaseAboutData>()
        firebaseb.collection("User").get().addOnSuccessListener {
            if (!it.isEmpty) {
                    firebaseb.collection("User").document(uidKey).collection("UserInfo").document(uidKey)
                        .get()
                        .addOnSuccessListener {
                            val user=UserFirebaseAboutData()
                            if(it.exists()){
                                Log.i("sttsf","2")
                                user.user_birthday=age
                                user.user_gender=it.data?.get("user_gender").toString()
                                user.user_about_me=it.data?.get("user_about_me").toString()
                                user.user_current_work=it.data?.get("user_current_work").toString()
                                user.user_school=it.data?.get("user_school").toString()
                                user.user_city_live=it.data?.get("user_city_live").toString()
                                user.user_home_town=it.data?.get("user_home_town").toString()
                                user.user_looking_for_person_type=it.data?.get("user_looking_for_person_type")as ArrayList<String>?
                                user.user_pets=it.data?.get("user_pets")as ArrayList<String>?
                                user.user_children=it.data?.get("user_children").toString()
                                user.user_astrological_sign=it.data?.get("user_astrological_sign").toString()
                                user.user_religion=it.data?.get("user_religion").toString()
                                user.user_education=it.data?.get("user_education").toString()
                                user.user_language=it.data?.get("user_language") as ArrayList<String>?
                                Log.i("safjbsdgf","inside featch data is ${ user.user_language} ")
                                user.user_select_your_country=it.data?.get("user_select_your_country")as ArrayList<String>?
                                user.user_height=it.data?.get("user_height").toString()
                                user.user_body_type=it.data?.get("user_body_type").toString()
                                user.user_exercise=it.data?.get("user_exercise").toString()
                                user.user_drink=it.data?.get("user_drink").toString()
                                user.user_smoker=it.data?.get("user_smoker").toString()
                                user.user_marijuana=it.data?.get("user_marijuana").toString()
                                user.user_interests=it.data?.get("user_interests") as ArrayList<String>?
                                userList.value=user
                            }
                            else{user.user_birthday=age
                                userList.value=user
                                Log.i("asjdfasjkfnd","not exist")
                            }
                }.addOnFailureListener {
                            Log.i("asjifhasjfb","error message  ${it.message}")
                            Toast.makeText(context, "Featch data failed due to ${it.message}", Toast.LENGTH_SHORT).show()
                        }
            }
        }.addOnFailureListener {
            Log.i("asjifhasjfb","error message  ${it.message}")
            Toast.makeText(context, "Process Failed due to ${it.message}", Toast.LENGTH_SHORT).show()
        }
        return userList
    }

    fun uploadDataToFirebaseRegister2(context: Context,dataClass:HashMap<String,Any>):LiveData<Boolean>{
        val value=MutableLiveData<Boolean>()
        val userId=getUserId()
        firebaseb.collection("User").document(userId).set(dataClass, SetOptions.merge()).addOnCompleteListener {
            if( it.isSuccessful){
                value.value=true
//                Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            value.value=false
            Toast.makeText(context, "Uploading Failed due to ${it.message}", Toast.LENGTH_SHORT).show()
        }
        return value
    }

    fun saveInfoData(context: Context):LiveData<Boolean>{
        var fg=MutableLiveData<Boolean>()
        fg.value=false
        var  db: FirebaseFirestore?=null
        db= FirebaseFirestore.getInstance()
        val uidd= MyUtils.getString(context, MyUtils.userUidKey)
        val mapModel=HashMap<String,Any>()
        mapModel["user_birthday"] = MyUtils.getString(context, MyUtils.userFullAgeKey).toString()
        mapModel["user_gender"] = MyUtils.getString(context, MyUtils.userGenderKey).toString()
        mapModel["user_about_me"] = MyUtils.getString(context, MyUtils.userAboutInfo).toString()
        mapModel["user_current_work"] = MyUtils.getString(context, MyUtils.userCurrentWork).toString()
        mapModel["user_school"] = MyUtils.getString(context, MyUtils.userSchool).toString()
        mapModel["user_city_live"] = MyUtils.getString(context, MyUtils.userCityLive).toString()
        mapModel["user_home_town"] = MyUtils.getString(context, MyUtils.userHomeTown).toString()
        mapModel["user_looking_for_person_type"]=
        MyUtils.getArray(context, MyUtils.userLookingForInPeople)?:ArrayList<String>()
        mapModel["user_pets"]= MyUtils.getArray(context, MyUtils.userPet)?:ArrayList<String>()
        mapModel["user_interests"]= MyUtils.getArray(context, MyUtils.userInterestsStatus)?:ArrayList<String>()
        mapModel["user_children"]= MyUtils.getString(context, MyUtils.userChildren)?:"null"
        mapModel["user_astrological_sign"]=
        MyUtils.getString(context, MyUtils.userAstrologicalSign)?:"null"
        mapModel["user_religion"]= MyUtils.getString(context, MyUtils.userReligion)?:"null"
        mapModel["user_education"]= MyUtils.getString(context, MyUtils.userEducation)?:"null"
        mapModel["user_language"]= MyUtils.getArray(context, MyUtils.userLanguage)?:ArrayList<String>()
        mapModel["user_height"]= MyUtils.getString(context, MyUtils.userHeight)?:"null"
        mapModel["user_body_type"]= MyUtils.getString(context, MyUtils.userBodyType)?:"null"
        mapModel["user_exercise"]= MyUtils.getString(context, MyUtils.userExerciseStatus)?:"null"
        mapModel["user_drink"]= MyUtils.getString(context, MyUtils.userDrinkStatus)?:"null"
        mapModel["user_smoker"]= MyUtils.getString(context, MyUtils.userSmokerStatus)?:"null"
        mapModel["user_marijuana"]= MyUtils.getString(context, MyUtils.userMarijuanaStatus)?:"null"
        db.collection("User").document(uidd!!).collection("UserInfo").document(uidd)
//                   .update(mapModel).addOnSuccessListener {
            .set(mapModel , SetOptions.merge()).addOnSuccessListener {
//                Toast.makeText(context, "Data updated", Toast.LENGTH_SHORT).show()
                fg.value= true
            }
            .addOnFailureListener {
                Toast.makeText(context, "failed data update ${it.message}", Toast.LENGTH_SHORT).show()
            }
        return fg
    }
    fun saveSettingData(context: Context, data:SettingDataClassForFirebase?):LiveData<Boolean>{
        var fg=MutableLiveData<Boolean>()
//        fg.value=false
        var  db: FirebaseFirestore?=null
        db= FirebaseFirestore.getInstance()
        val uidd= MyUtils.getString(context, MyUtils.userUidKey)
        if(data == null) {
            val mapModel = HashMap<String, Any>()
            mapModel["looking_Gender"] =
                MyUtils.getString(context, MyUtils.userLookingForKey).toString()
            mapModel["looking_Age_Start"] = 18
            mapModel["looking_Age_End"] = 50
            mapModel["looking_Age_Flag"] = true
            mapModel["looking_Distance"] = 100
            mapModel["looking_Distance_Flag"] = true
            db.collection("User").document(uidd!!).collection("UserFilterSetting").document(uidd)
                .set(mapModel, SetOptions.merge()).addOnSuccessListener {
//                    Toast.makeText(context, "Data updated", Toast.LENGTH_SHORT).show()
                    fg.value = true
                }
                .addOnFailureListener {
                    Toast.makeText(context, "failed data update ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }
        else{
            val mapModel = HashMap<String, Any>()
            mapModel["looking_Gender"] =data.looking_Gender.toString()
            db.collection("User").document(uidd!!).update("user_looking_gender",data.looking_Gender)
            mapModel["looking_Gender"] =data.looking_Gender.toString()
            mapModel["looking_Age_Start"] = data.looking_Age_Start.toString()
            mapModel["looking_Age_End"] = data.looking_Age_End .toString()
            mapModel["looking_Age_Flag"] = data.looking_Age_Flag as Boolean ?:true
            mapModel["looking_Distance"] = data.looking_Distance .toString()
            mapModel["looking_Distance_Flag"] = data.looking_Distance_Flag as Boolean ?:true
            db.collection("User").document(uidd!!).collection("UserFilterSetting").document(uidd)
                .set(mapModel).addOnSuccessListener {
                    Toast.makeText(context, "Data updated", Toast.LENGTH_SHORT).show()
                    Log.i("agsasfd","passed")
                    fg.value = true
                }
                .addOnFailureListener {
                    Log.i("agsasfd","fialed ${it.message}")
                    Toast.makeText(context, "failed data update ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }
        return fg
    }
    fun fetchingSettingFilter(context: Context):LiveData<SettingDataClassForFirebase>{
        val data=MutableLiveData<SettingDataClassForFirebase>()
        val uidKey=MyUtils.getString(context,MyUtils.userUidKey)
        Log.i("asgasdf","inside function")
        firebaseb.collection("User").get().addOnSuccessListener {
            if (!it.isEmpty) {
                Log.i("asgasdf","inside function not empty and key is ${uidKey}")
                firebaseb.collection("User").document(uidKey!!).collection("UserFilterSetting").document(uidKey)
                    .get().addOnSuccessListener {
                        val user=SettingDataClassForFirebase()
                        if(it.exists()){
                            Log.i("asgasdf","inside function sucess")
                                val start=it.data?.get("looking_Age_Start").toString()
                            user.looking_Age_Start=start.toLong()?:18
//                            user.looking_Age_Start=it.data?.get("looking_Age_Start") as Int ?: 18f
                            val end=it.data?.get("looking_Age_End").toString()
                            user.looking_Age_End=end.toLong()?:50
                            user.looking_Age_Flag=it.data?.get("looking_Age_Flag") as Boolean ?: true
                            val dis=it.data?.get("looking_Distance").toString()
                            user.looking_Distance=dis.toLong()?:100
                            user.looking_Distance_Flag=it.data?.get("looking_Distance_Flag") as Boolean ?: true
                            user.looking_Gender=it.data?.get("looking_Gender") as String
                            data.value=user
                        }
                    }.addOnFailureListener {
                        Log.i("asgasdf","inside failed")
                        Toast.makeText(context, "Featch data failed due to ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Process Failed due to ${it.message}", Toast.LENGTH_SHORT).show()
        }
        return data
    }
    fun saveImageToFireBase(context:Context,filePath:String,path:String):LiveData<String>{
         var storageReference: StorageReference=FirebaseStorage.getInstance().reference
        val linkList=MutableLiveData<String>()
            val progressdialog = ProgressDialog(context)
            progressdialog.setTitle("Uploading..")
            progressdialog.show()
            val uidd=Firebase.auth.uid
            val name=MyUtils.getString(context,MyUtils.userNameKey)?:uidd
            storageReference.child(filePath).child(uidd.toString()).child(name+"_"+ UUID.randomUUID().toString()).putFile(
                Uri.fromFile( File(path)))
                .addOnSuccessListener {
                    val result = it.metadata!!.reference!!.downloadUrl
                    result.addOnSuccessListener {
                        val uploadedUrlp = it.toString()
                        linkList.value=uploadedUrlp
                        progressdialog.dismiss()
                            }
                        }
                .addOnFailureListener {
                    progressdialog.dismiss()
                    Log.i("testingNow","error ${it.message}")
                    Toast.makeText(context, "Failed ${it.message}", Toast.LENGTH_SHORT).show()
                }.addOnProgressListener { snapshot ->
                    val process = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount)
                    progressdialog.setMessage("Uploaded ${process.toInt()}% ")
                }
        return linkList
    }
    fun saveAudioToFireBase(context:Context,secondPath:Uri):LiveData<String>{
         var storageReference: StorageReference=FirebaseStorage.getInstance().reference
        val linkList=MutableLiveData<String>()
            val uidd=Firebase.auth.uid
            val name=MyUtils.getString(context,MyUtils.userNameKey)?:uidd
            storageReference.child("audioRecoding").child(uidd.toString()).child(name+"_"+ UUID.randomUUID().toString()).putFile(
                secondPath)
                .addOnSuccessListener {
                    val result = it.metadata!!.reference!!.downloadUrl
                    result.addOnSuccessListener {
                        val uploadedUrlp = it.toString()
                        linkList.value=uploadedUrlp

                            }
                        }
                .addOnFailureListener {
                    Log.i("testingNow","error ${it.message}")
                    Toast.makeText(context, "Failed ${it.message}", Toast.LENGTH_SHORT).show()
                }
        return linkList
    }
    fun updateAddImage(context: Context, list:ArrayList<String>):LiveData<Boolean>{
        val data=MutableLiveData<Boolean>()
        val id=getUserId()
        data.value=false
        firebaseb.collection("User").document(id).update("image_url_list", list).addOnSuccessListener {
        data.value=true
        }.addOnFailureListener {
            data.value=false
            Toast.makeText(context, "Failed due to ${it.message}", Toast.LENGTH_SHORT).show()
            }
        return data
    }
    fun checkingUserAlreadyLikeYou(context: Context,userUid:String):LiveData<String> {
        val data = MutableLiveData<String>()
        val uid = MyUtils.getString(context, MyUtils.userUidKey)
                firebaseb.collection("Like").document(userUid).collection(userUid).document(uid!!).get()?.addOnSuccessListener {
                    if (it.exists()){
                        val status=it.data?.get("status")?:"null"
                            val user_uid=it.data?.get("user_uid")?:"null"
                                val user_by_uid=it.data?.get("user_by_uid")?:"null"
                        Log.i("askfnbnksafn","status ${status} and user_uid ${user_uid} and userbyuid ${user_by_uid}")
                        if(status.equals("1") && user_uid.equals(uid) && user_by_uid.equals(userUid)){
                            Log.i("askfnbnksafn","true inside")
                            data.value=userUid
                        }
                    }else{
                        Log.i("askfnbnksafn","else inside")
                        data.value = "null"
                    }

                }
            return data
    }
    fun changUserStatus(context: Context, userUid:String):LiveData<Boolean>{
        val data=MutableLiveData<Boolean>()
        val uid=MyUtils.getString(context,MyUtils.userUidKey)
        firebaseb.collection("Like").document(userUid).collection(userUid).document(uid!!).update("status","3").addOnSuccessListener {
            data.value=true
        }.addOnFailureListener {
            data.value=false
        }
        return data
    }
    fun addDataToLikeDataMatchTwo(context:Context,userUid: String,userName:String,userAge:String,image:String):MutableLiveData<Boolean>{
        var data=MutableLiveData<Boolean>()
        data.value=false
        val currentUid:String=MyUtils.getString(context,MyUtils.userUidKey)!!
        val currentAge:String=MyUtils.getString(context,MyUtils.userAgeKey)!!
        val currentName:String=MyUtils.getString(context,MyUtils.userNameKey)!!
        val currentImage:String=MyUtils.getArray(context,MyUtils.userArrayImageLinkKey)?.get(0).toString()
        var sendData=HashMap<String,String>()
        sendData.put("status","3")
        sendData.put("user_uid",userUid)
        sendData.put("user_name",userName)
        sendData.put("user_age",userAge)
        sendData.put("user_img",image)
        sendData.put("user_by_uid",currentUid)
        sendData.put("user_by_name",currentName)
        sendData.put("user_by_age",currentAge)
        sendData.put("user_by_img",currentImage)
        sendData.put("user_msg_status","0")
        val sendData2=HashMap<String,String>()
        sendData2.put("user_key",currentUid)
        firebaseb.collection("Like").document(currentUid).set(sendData2).addOnSuccessListener {
            Log.i("dataAddded","Like data added successfully inside viewModel")
        }
        firebaseb.collection("Like").document(currentUid).collection(currentUid).document(userUid).set(sendData).addOnSuccessListener {
            Log.i("dataAddded","Like data added successfully inside viewModel")
            data.value=true
        }
        return data
    }
    fun addDataToLikeDataTwo(context:Context,userUid: String,userName:String,userAge:String,image:String,stauts:String):MutableLiveData<Boolean>{
        var data=MutableLiveData<Boolean>()
        data.value=false
        val currentUid:String=MyUtils.getString(context,MyUtils.userUidKey)!!
        val currentAge:String=MyUtils.getString(context,MyUtils.userAgeKey)!!
        val currentName:String=MyUtils.getString(context,MyUtils.userNameKey)!!
        val currentImage:String=MyUtils.getArray(context,MyUtils.userArrayImageLinkKey)?.get(0).toString()
        var sendData=HashMap<String,String>()
        sendData.put("status", stauts)
        sendData.put("user_uid",userUid)
        sendData.put("user_name",userName)
        sendData.put("user_age",userAge)
        sendData.put("user_img",image)
        sendData.put("user_by_uid",currentUid)
        sendData.put("user_by_name",currentName)
        sendData.put("user_by_age",currentAge)
        sendData.put("user_by_img",currentImage)
        sendData.put("user_msg_status","0")
//        sendData.put("user_msg_status","1")
        val sendData2=HashMap<String,String>()
        sendData2.put("user_key",currentUid)
        val sendData3=HashMap<String,String>()
        sendData3.put("user_key",userUid)
        firebaseb.collection("Like").document(currentUid).set(sendData2).addOnSuccessListener {
            Log.i("dataAddded","Like data added successfully inside viewModel")
            data.value=true
        }
        firebaseb.collection("Like").document(currentUid).collection(currentUid).document(userUid).set(sendData).addOnSuccessListener {
            Log.i("dataAddded","Like data added successfully inside viewModel")
            data.value=true
        }
        return data
    }
    fun allLikedUsers(context:Context):LiveData<ArrayList<String>>{
        val data=MutableLiveData<ArrayList<String>>()
        var uid=MyUtils.getString(context,MyUtils.userUidKey)
        val allUserList=ArrayList<String>()
//        firebaseb.collection("Likee").get().addOnSuccessListener {
        firebaseb.collection("Like").get().addOnSuccessListener {
                if(!it.isEmpty){
                    val len=it.documents.size
                    for(i in 0 until len){
                        Log.i("asfknasf","liked user id is ${it.documents[i].id}")
                        if(it.documents[i].id != uid){
                            allUserList.add(it.documents[i].id)}
                        if(i==len-1)
                        {
                            data.value=allUserList
                        }
                    }

                }
            else{
                data.value=allUserList
                }
            }
        return data
        }
    fun checkWhoMatchMeStatus(context:Context,listt:ArrayList<String>):LiveData<ArrayList<UserLikedCheckDetail>>{
        val data=MutableLiveData<ArrayList<UserLikedCheckDetail>>()
        val uid=MyUtils.getString(context,MyUtils.userUidKey)
        var arrayListData=ArrayList<UserLikedCheckDetail>()
        for (i in 0 until listt.size ) {
                firebaseb.collection("Like").document(uid!!).collection(uid).document(listt[i]).addSnapshotListener { value, error ->
                    if(value?.exists()!!){
                        Log.i("asjkfxvxzvdbagsf","inside value not empty")
                            var userById=value.data?.get("user_uid").toString()
                            var status=value.data?.get("user_msg_status").toString()
                            arrayListData.add(UserLikedCheckDetail(userById,null,null,null,status))
                    }
                    else{
                        Log.i("asjkfxvxzvdbagsf","else part")
                    }

                    if(i == listt.size-1 ){
                        data.value=arrayListData
                    }
                }

        }
        return data
    }
    fun checkWhoMatch(context:Context,listt:ArrayList<String>):LiveData<ArrayList<UserLikedCheckDetail>>{
        val data=MutableLiveData<ArrayList<UserLikedCheckDetail>>()
        val uid=MyUtils.getString(context,MyUtils.userUidKey)
        var checkValue=0
        var arrayListData=ArrayList<UserLikedCheckDetail>()
        Log.i("repeat","inside checl match 3.5 paramenter list ${listt}")
        for (i in 0 until listt.size ) {
            if (!listt[i].equals(uid)) {
                Log.i("repeat","loop is ${i}")
                firebaseb.collection("Like").document(listt[i]).collection(listt[i]).document(uid!!).get()
                    .addOnSuccessListener {
                        Log.i("repeat","inside succesfull testing match")
                        if(it.exists()){

                            Log.i("repeat","inside exist")
                            val status=it.data?.get("status")
                            if(status?.equals("3")!!){
                                checkValue+=1
                                arrayListData.add(UserLikedCheckDetail(it.data?.get("user_by_uid").toString(),it.data?.get("user_by_name").toString(),it.data?.get("user_by_age").toString(),it.data?.get("user_by_img").toString(),it.data?.get("user_msg_status").toString()))
//                                if(listt[i] ){
//                                if(i == listt.size-1 ){
//                                    Log.i("repeat","returning")
//                                    data.value=arrayListData
//                                }
                                if(checkValue == listt.size){
                                    data.value=arrayListData
                                }
                            }
                            else{
                                checkValue+=1
                                if(checkValue == listt.size){
                                    data.value=arrayListData
                                }
//                                if(i == listt.size-1 ){
//                                    Log.i("repeat","returning else")
//                                    data.value=arrayListData
//                                }
                            }

                        }
                        else{
                            checkValue+=1
                            if(checkValue == listt.size){
                                data.value=arrayListData
                            }
                        }
                }
            }
            else{
                checkValue+=1
                if(checkValue == listt.size){
                    data.value=arrayListData
                }
//                if(i == listt.size-1 ){
//                    Log.i("repeat","return not exist person")
//                    data.value=arrayListData
//                }
            }
        }
        return data
    }
    fun checkWhoLikedMe(context: Context,listt:ArrayList<String>):LiveData<ArrayList<UserLikedCheckDetail>> {
        val data = MutableLiveData<ArrayList<UserLikedCheckDetail>>()
        val uid = MyUtils.getString(context, MyUtils.userUidKey)
        var checkValue = 0
        Log.i("uid0", "current user uid is ->${uid}")
        var arrayListData = ArrayList<UserLikedCheckDetail>() // 6
        Log.i("asfsbsdfbbasf", "loop run time ${listt.size}")
        if(listt.size > 0){
        for (i in 0 until listt.size) {
            Log.i("asfsbsdfbbasf", "inside run time  pos ${i}${listt[i]}")
            firebaseb.collection("Like").document(listt[i]).collection(listt[i]).document(uid!!)
                .get().addOnSuccessListener {
                if (it.exists()) {
                    val status = it.data?.get("status")
                    Log.i("asfjlkjasgklasnfd", "status is loop${i} ${status}")
                    if (status?.equals("1")!!) {
                        Log.i("sdjalfnlkdsn", "adding value here")
                        Log.i("snfsmnfsd", "model adding persons fragment")
                        arrayListData.add(UserLikedCheckDetail(it.data?.get("user_by_uid")
                            .toString(),
                            it.data?.get("user_by_name").toString(),
                            it.data?.get("user_by_age").toString(),
                            it.data?.get("user_by_img").toString()))
                        checkValue += 1
                        if (checkValue == listt.size) {
                            Log.i("sdjalfnlkdsn", "return adding value here")
                            data.value = arrayListData
                        }
                    } else {
                        checkValue += 1
//                            if(i == listt.size-1 ){
                        if (checkValue == listt.size) {
                            Log.i("sdjalfnlkdsn", "return not like value here")
                            data.value = arrayListData
                        }
                    }
                } else {
                    Log.i("sdjalfnlkdsn", "return not exist value here ${checkValue}")
//                        if(i == listt.size-1 ){
//                            data.value=arrayListData
//                        }
                    checkValue += 1
                    if (checkValue == listt.size) {
                        data.value = arrayListData
                    }
                }

            }

        }
    }else
    {
        data.value=arrayListData
    }
        return data

    }
    fun getClickStatus(context: Context,userUid: String,chatId:String):LiveData<String> {
        val responseData=MutableLiveData<String>()
        val uid=MyUtils.getString(context,MyUtils.userUidKey)
        firebaseb.collection("Chat").document(chatId).get().addOnSuccessListener {
            if(it.exists()){
                val data=it.data!!.get(userUid) as HashMap<String,String>
                Log.i("sabfsakfb","data is ${data}")
                val userView=data.get("userView").toString()
                responseData.value=userView
            }
            else{
                responseData.value="-1"
            }
        }
        return responseData
    }

     fun changeChatShowMsgValue(context: Context,userUid: String,value:String,chatId:String):LiveData<Boolean>{
        val data=MutableLiveData<Boolean>()
         Log.i("sabfsakfb","inside change outside just uid chatid ${chatId} and come id ${userUid}")
         firebaseb.collection("Chat").document(chatId).update(mapOf("$userUid.userView" to value)).addOnSuccessListener {
             data.value=true
         }.addOnFailureListener {
             data.value=false
         }
        return data
    }

//    fun changeChatTyingStatus(userUid:String,value:Boolean,chatId: String){
//        firebaseb.collection("Chat").document(chatId).update(mapOf("$userUid.typingStaus" to value)).addOnSuccessListener {
//
//        }.addOnFailureListener {
//            Toast.makeText(requiredContext, "Updating typing status failed due to ${it.message}", Toast.LENGTH_SHORT).show()
//        }
//    }
    fun saveChat(viewType:Int,dataLisyt:ChatModel):LiveData<Boolean>{
        val data=MutableLiveData<Boolean>()
        firebaseb.collection("Chat").document(dataLisyt.userId!!).collection("Message").document().set(dataLisyt).addOnSuccessListener {
        Log.i("data","Send")
            data.value=true
        }
            .addOnFailureListener {
            Log.i("data","Failed")
            data.value=false
        }
        return data
    }

    fun getAllLikeAndDisLikeUsers(context: Context):LiveData<ArrayList<String>>{
        val data=MutableLiveData<ArrayList<String>>()
        var uid=MyUtils.getString(context,MyUtils.userUidKey)
        val allUserList=ArrayList<String>()
        firebaseb.collection("Like").document(uid!!).collection(uid).get().addOnSuccessListener {
            if (!it.isEmpty){
                val len=it.documents.size
                for(i in 0 until len){
                    val id=it.documents[i].id
                    allUserList.add(id)
                    if(i == len-1){
                        data.value=allUserList
                    }
                }
            }
            else{
                data.value=allUserList
            }
        }
        return data
    }

    fun checkUserChatPresentOrNot(context:Context,chatId:String):LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()
        firebaseb.collection("Chat").document(chatId).collection("Message").get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    data.value = false
                }
                else {
                    data.value = true
                }
            }.addOnFailureListener {
                data.value=false
            }
        return data
    }
    fun addUserChatExtraFeature(context: Context,chatId:String,data:HashMap<String,String>,userId:String):LiveData<Boolean>{
        val responseData=MutableLiveData<Boolean>()
        var userHashMap=HashMap<String,java.util.HashMap<String,String>>()
        userHashMap.put(userId,data)
        Log.i("asjkdfnsjkadfbn","just just outside saveUserData member")
        firebaseb.collection("Chat").document(chatId).set(userHashMap, SetOptions.merge()).addOnSuccessListener {
            responseData.value=true
            Log.i("asjkdfnsjkadfbn","saved outside saveUserData member")
        }.addOnFailureListener {
            responseData.value=false
        }
        return responseData
    }

    fun arrayAddUserChatExtraFeature(data:ArrayList<ChatMatchDataExtraDataClass>):LiveData<Boolean>{
        val responseData=MutableLiveData<Boolean>()
        val len=data.size
        var index=0
        Log.i("asfnbkasfd","array user chat extra")
        for(i in 0 until len) {
            var userHashMap=HashMap<String,java.util.HashMap<String,Any>>()
            userHashMap.put(data[i].userUid!!,data[i].userOneMap!!)
            firebaseb.collection("Chat").document(data[i].chatId!!).set(userHashMap, SetOptions.merge())
                .addOnSuccessListener {
                    index+=1
                    if(index == len){
                    responseData.value = true
                    }
                    Log.i("asjkdfnsjkadfbn", "saved outside saveUserData member")
                }.addOnFailureListener {
                responseData.value = false
            }
        }
        return responseData
    }


    fun addUserDataList(chatId:String , userList:ArrayList<String>):LiveData<Boolean>{
        val responseData=MutableLiveData<Boolean>()
        var userHashMap=HashMap<String,Any>()
        userHashMap.put("memberData",userList)
        firebaseb.collection("Chat").document(chatId).set(userHashMap, SetOptions.merge()).addOnSuccessListener {
            responseData.value=true
            Log.i("asjkdfnsjkadfbn"," save member")
        }.addOnFailureListener {
           // responseData.value=false
        }
        return responseData
    }

    fun getDataMatchDataList(loginUserUid: String):LiveData<ArrayList<MatchUserExtraData>>
    {
        val responseData=MutableLiveData<ArrayList<MatchUserExtraData>>()
        var index=0
        val tempResponse=ArrayList<MatchUserExtraData>()
        firebaseb.collection("Chat").whereArrayContains("memberData",loginUserUid).get().addOnSuccessListener {
            var data=it as QuerySnapshot
            val len=it.documents.size
            Log.i("sfdjansdf","User match lenghth is ${len}")
            for(i in 0 until len){
                val dataList=MatchUserExtraData()
                if(!data.isEmpty) {
                    var otherUserId = ""
                    val nett = data.documents[i]
                    Log.i("aslfdnsjkanf", "all data ${nett}")
                    val net = data.documents.get(i).id
                    Log.i("aslfdnsjkanfd", "all data ${net}")
                    val usersList =
                        data.documents.get(i).data!!.get("memberData") as ArrayList<String>
                    Log.i("askdfknsanfd", "list ${usersList}")
                    if(usersList[0].equals(loginUserUid))
                    {
                        otherUserId=usersList[1].toString()
                        val userDetail=data.documents.get(i).data!!.get(otherUserId) as HashMap<String,String>
                        dataList.user_uid=otherUserId
                        dataList.user_name=userDetail.get("name")
                        dataList.user_Image=userDetail.get("photo")
                        dataList.user_view=userDetail.get("userView")
                        dataList.chatId=userDetail.get("chatId")
                        val loginUserDetail=data.documents.get(i).data!!.get(usersList[0]) as HashMap<String,String>
                        dataList.login_uid= loginUserUid
                        dataList.login_name=loginUserDetail.get("name")
                        dataList.login_image=loginUserDetail.get("photo")
                        dataList.login_user_view=loginUserDetail.get("userView")
                        dataList.chatId=loginUserDetail.get("chatId")
                        Log.e("dgmszgsgs===","data ${dataList}")
                        tempResponse.add(dataList)
                        index+=1
//                        if(index == data.size()){
//                            Log.i("sfdjansdf","return n mathc uid ${tempResponse}")
//                            responseData.value=tempResponse
//                        }
                        firebaseb.collection("Chat").document(dataList.chatId!!).collection("Message").get().addOnSuccessListener {
                            if(it.isEmpty) {
                                val size=it.documents.size
                                if(size > 0){
                                    dataList.messagePresent=true
                                    tempResponse.add(dataList)
                                    if(index == data.size()){
                                        Log.i("sfdjansdf","return not mathc uid ${tempResponse}")
                                        responseData.value=tempResponse
                                    }
                                }
                            }else{
                                if(index == data.size()){
                                    Log.i("sfdjansdf","return not mathc uid ${tempResponse}")
                                    responseData.value=tempResponse
                                }
                            }
                        }.addOnFailureListener {

                        }
                    }
                    else
                    {
                        otherUserId=usersList[0]
                        Log.i("asbfsjabf","userUid ${otherUserId} and login uid ${usersList[1]}")
                        val userDetail=data.documents.get(i).data!!.get(otherUserId) as HashMap<String,String>
                        dataList.user_uid=otherUserId
                        dataList.user_name=userDetail.get("name")
                        dataList.user_Image=userDetail.get("photo")
                        dataList.user_view=userDetail.get("userView")
                        dataList.chatId=userDetail.get("chatId")
                        val loginUserDetail=data.documents.get(i).data!!.get(usersList[1]) as HashMap<String,String>
                        dataList.login_uid= loginUserUid
                        dataList.login_name=loginUserDetail.get("name")
                        dataList.login_image=loginUserDetail.get("photo")
                        dataList.login_user_view=loginUserDetail.get("userView")
                        dataList.chatId=loginUserDetail.get("chatId")
                        Log.e("dgmszgsgs===","data ${dataList}")
                        index+=1
                        tempResponse.add(dataList)
//                        if(index == data.size()){
//                            Log.i("sfdjansdf","return not mathc uid ${tempResponse}")
//                            responseData.value=tempResponse
//                        }
                        firebaseb.collection("Chat").document(dataList.chatId!!).collection("Message").get().addOnSuccessListener {
                            if(it.isEmpty) {
                                val size=it.documents.size
                                if(size > 0){
                                    dataList.messagePresent=true
                                    tempResponse.add(dataList)
                                    if(index == data.size()){
                                        Log.i("sfdjansdf","return not mathc uid ${tempResponse}")
                                        responseData.value=tempResponse
                                    }
                                }
                            }else{
                                if(index == data.size()){
                                    Log.i("sfdjansdf","return not mathc uid ${tempResponse}")
                                    responseData.value=tempResponse
                                }
                            }
                        }.addOnFailureListener {

                        }
                    }
//dfhb

                    Log.i("jasdbfhbsdaf","Chat id ->${dataList.chatId!!}")
//                    firebaseb.collection("Chat").document(dataList.chatId!!).collection("Message").get().addOnSuccessListener {
//                            if(it.isEmpty) {
//                                val size=it.documents.size
//                                if(size > 0){
//                                    dataList.messagePresent=true
//                                    if(index == data.size()){
//                                        Log.i("sfdjansdf","return not mathc uid ${tempResponse}")
//                                        responseData.value=tempResponse
//                                    }
//                                }
//                            }else{
//                                if(index == data.size()){
//                                    Log.i("sfdjansdf","return not mathc uid ${tempResponse}")
//                                    responseData.value=tempResponse
//                                }
//                            }
//                    }.addOnFailureListener {
//
//                    }
                        //                val yy=data.documents.get(0)
                        //                Log.i("asfdnskanfd","value si ${yy}")

                        //                firebaseb.collection("Chat").document(net).collection("Message")
                        //
                        ////                firebaseb.collection("Chat").document(net).collection("Message").addSnapshotListener { value, error ->
                        ////                    if (value?.isEmpty!!){
                        ////                        Log.i("ashdbfjasbdf","data not exist  is ${value}")
                        ////                    }
                        ////                    else{
                        ////                        Log.e("ashdbfjasbdf","data exist ${dataList}")
                        ////                    }
                        ////                }
                        //                    if(index == data.size()){
                        //                        responseData.value=tempResponse
                        //                    }
                    }

                else{
                    index+=1
                    if(index == data.size()){
                        Log.i("sfdjansdf","return not exist  ${tempResponse}")
                        responseData.value=tempResponse
                    }
                }
            }
            }

        return responseData
    }
}