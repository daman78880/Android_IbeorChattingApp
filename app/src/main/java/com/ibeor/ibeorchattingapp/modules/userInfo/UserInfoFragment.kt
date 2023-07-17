package com.ibeor.ibeorchattingapp.modules.userInfo

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentUserInfoBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.chatting.chatNow.ChatMatchDataExtraDataClass
import com.ibeor.ibeorchattingapp.modules.gettingUserDetail.countryItem
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import com.ibeor.ibeorchattingapp.modules.userData.uploadToFirebaseDataBase.UserFirebaseAboutData
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.UnsupportedEncodingException


class UserInfoFragment : Fragment() {
    private var binding:FragmentUserInfoBinding?=null
    private var listInterst=ArrayList<String>()
    var image=ArrayList<String>()
    var fullAge:String?=null
    var firstContry:String?=null
    var secondCountry:String?=null
    var lat:Double?=null
    var long:Double?=null
    var awayLocation:String?=null
    var userUid:String?=null
    var ownerUid:String?=null
    val viewModel=HomeViewModel()
    var userName:String?=null
    var updateModel=UserFirebaseAboutData()
    var userLat:Double?=null
    var userLong:Double?=null
    var chatId:Double?=null
    var iimage=ArrayList<String>()
    private lateinit var homeViewModel:HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding= FragmentUserInfoBinding.inflate(layoutInflater)
        homeViewModel= HomeViewModel()
        return binding!!.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            MyUtils.showProgress(requireContext())
        ownerUid=MyUtils.getString(requireContext(),MyUtils.userUidKey)
            userUid = arguments?.getString("idKey")
            fullAge = arguments?.getString("userFullAge")
            userName = arguments?.getString("userName")
            userLat = arguments?.getDouble("userLatFirst")
            userLong = arguments?.getDouble("userLongFirst")
            firstContry = arguments?.getString("firstRoot")
            secondCountry = arguments?.getString("SecondRoot")
            iimage= arguments?.getStringArrayList("imageUrlList") as ArrayList<String>
        Log.i("imagess","images link are ${iimage}")
            lat = MyUtils.getLongAsDouble(requireContext(), MyUtils.userLotiKey)
            long = MyUtils.getLongAsDouble(requireContext(), MyUtils.userLongiKey)
            viewModel.fetchFirebaseAboutData(requireContext(), userUid!!, fullAge!!)
                .observe(requireActivity(),
                    Observer {
                        updateModel = it as UserFirebaseAboutData
                        Log.i("sjajfbjsabf","full user data is ${updateModel}")
                        MyUtils.stopProgress()
                        init()
                        iniliseList()
                        clickListner()
                    })

    }
    private fun clickListner(){
        binding!!.apply {
            backBtnUserInfo.setOnClickListener {
                lifecycleScope.launchWhenResumed {
                    activity?.onBackPressed()
                }
            }
            userInfoLikeBtn.setOnClickListener {
                Log.i("sfbsajdfb","clicked")
                MyUtils.showProgress(requireContext())
                likeButton(userUid!!,userName!!,fullAge!!,iimage[0])
            }
            userInfoDislikeBtn.setOnClickListener {
                MyUtils.showProgress(requireContext())
                Log.i("sfbsajdfb","clicked")
                disLike(userUid!!,userName!!,fullAge!!,iimage[0])
            }

        }
    }
    private fun disLike(uid:String,name:String,age:String,imageUrl:String) {
        lifecycleScope.launchWhenResumed {
            if(findNavController().currentDestination!!.id == R.id.userInfoFragment) {
                homeViewModel.addDataToLikeDataTwo(requireContext(),uid,name
                    ,age,
                    imageUrl,"2").observe(requireActivity(),
                    Observer {
                        val flag=it as Boolean
                        if(flag== true){
                            MyUtils.stopProgress()
                            findNavController().popBackStack()
                        }
                    })
            }}
    }
    private fun likeButton(uid:String,name:String,age:String,imageUrl:String){
        Log.i("sajdfnjksabdf","Likebutton click")

//        lifecycleScope.launchWhenResumed {
//            if(findNavController().currentDestination!!.id == R.id.userInfoFragment) {
//                homeViewModel.checkingUserAlreadyLikeYou(requireContext(),uid).observe(requireActivity(),
//                    Observer {
//                        val llist = it as String
//                        if ( llist != null && !llist.equals("null") ) {
//                            Log.i("sajdfnjksabdf","user alery like you")
//                            lifecycleScope.launchWhenResumed {
//                                if (findNavController().currentDestination!!.id == R.id.userInfoFragment) {
//                                 val chatId=MyUtils.getChatId(requireContext(),userUid!!)
//                                    saveMemberData(ownerUid!!,userUid!!,chatId)
//                                    val userOneMap=HashMap<String,String>()
//                                    val userTwoMap=HashMap<String,String>()
//                                    val img=MyUtils.getArray(requireContext(), MyUtils.userArrayImageLinkKey)
//                                    Log.i("userimagekasfbdnksa","user image link is ${img!!.get(0)}")
//                                    userOneMap["name"] = MyUtils.getString(requireContext(),MyUtils.userNameKey)!!
//                                    userOneMap["photo"] = img[0]
//                                    userOneMap["userView"] ="0"
//                                    userOneMap["chatId"] =chatId
//                                    userTwoMap["name"]=name
//                                    userTwoMap["photo"]=imageUrl
//                                    userTwoMap["userView"]= "0"
//                                    userTwoMap["chatId"]= chatId
//                                    saveUserData(chatId,userOneMap,ownerUid!!)
//                                    saveUserData(chatId,userTwoMap,userUid!!)
//                                    homeViewModel.addDataToLikeDataMatchTwo(requireContext(), uid, name, age, imageUrl).observe(requireActivity(),
//                                            Observer {
//                                                val flag = it as Boolean
//                                                if (flag == true) {
//                                                    Log.i("userimagekasfbdnksa","add data to like data")
//                                                    lifecycleScope.launchWhenResumed {
//                                                        if (findNavController().currentDestination!!.id == R.id.userInfoFragment) {
//                                                            homeViewModel.changUserStatus(requireContext(), uid).observe(
//                                                                    requireActivity(),
//                                                                    Observer {
//                                                                        MyUtils.stopProgress()
//                                                                        findNavController().popBackStack() })
//                                                        }
//                                                    }
//
//                                                }
//                                            })
//                                }
//                            }
////                                        }}
//                        } else {
//                            lifecycleScope.launchWhenResumed {
//                                if (findNavController().currentDestination!!.id == R.id.userInfoFragment) {
//                                    val chatId=MyUtils.getChatId(requireContext(),userUid!!)
//                                    saveMemberData(ownerUid!!,userUid!!,chatId)
//                                    val userOneMap=HashMap<String,String>()
//                                    val userTwoMap=HashMap<String,String>()
//                                    val img=MyUtils.getArray(requireContext(), MyUtils.userArrayImageLinkKey)
//                                    Log.i("userimagekasfbdnksa","user image link is ${img!!.get(0)}")
//                                    userOneMap["name"] = MyUtils.getString(requireContext(),MyUtils.userNameKey)!!
//                                    userOneMap["photo"] = img[0]
//                                    userOneMap["userView"] ="0"
//                                    userOneMap["chatId"] =chatId
//                                    userTwoMap["name"]=name
//                                    userTwoMap["photo"]=imageUrl
//                                    userTwoMap["userView"]= "0"
//                                    userTwoMap["chatId"]= chatId
//                                    saveUserData(chatId,userOneMap,ownerUid!!)
//                                    saveUserData(chatId,userTwoMap,userUid!!)
//                                    homeViewModel.addDataToLikeDataTwo(requireContext(), uid, name, age, imageUrl,
//                                        "1").observe(requireActivity(),
//                                        Observer {
//                                            val flag = it as Boolean
//                                            if (flag == true) {
//                                                MyUtils.stopProgress()
//                                                lifecycleScope.launchWhenResumed {
//                                                if(findNavController().currentDestination!!.id == R.id.userInfoFragment) {
//                                                    findNavController().popBackStack()
//                                                }}
//                                            }
//
//                                        })
//                                }
//                            }
//                        }
//                    })
//            }}

        lifecycleScope.launchWhenResumed {
            if(findNavController().currentDestination!!.id == R.id.userInfoFragment) {
                homeViewModel.checkingUserAlreadyLikeYou(requireContext(), userUid!!).observe(requireActivity(),
                    Observer {
                        val llist = it as String
                        if (!llist.equals("null") && llist != null) {
                            var chatId=""
                            if(ownerUid!! < userUid!!){
                                chatId="${ownerUid}_${uid}"
                            }else{
                                chatId="${uid}_${ownerUid}"
                            }
                            val userOneMap=HashMap<String,Any>()
                            val userTwoMap=HashMap<String,Any>()
                            val img=MyUtils.getArray(requireContext(), MyUtils.userArrayImageLinkKey)
                            Log.i("userimagekasfbdnksa","user image link is ${img!!.get(0)}")
                            userOneMap["name"] = MyUtils.getString(requireContext(),MyUtils.userNameKey)!!
                            userOneMap["photo"] = img[0]
                            userOneMap["userView"] ="0"
                            userOneMap["typingStatus"] ="false"

                            userOneMap["last_msg"] =""
                            userOneMap["time"] =0L
                            userOneMap["unseen_msg_count"] =0L

                            userOneMap["chatId"] =chatId
                            userTwoMap["name"]=name
                            userTwoMap["photo"]= imageUrl
                            userTwoMap["userView"]= "0"
                            userTwoMap["typingStatus"] ="false"
                            userTwoMap["chatId"]= chatId

                            userTwoMap["last_msg"]= ""
                            userTwoMap["unseen_msg_count"]= 0L
                            userTwoMap["time"]= 0L
                            Log.i("asjkdfnsjkadfbn","going to save member")
//                                            CoroutineScope(Dispatchers.Main).run {
//                                                saveMemberData(userUid!!, cardList[posInside - 1].user_uid!!, chatId)
//                                            }
//                                            Log.i("asjkdfnsjkadfbn","going to saveUserData member")
//                                            CoroutineScope(Dispatchers.Main).run {
//                                            }
//                                            saveUserData(chatId,userOneMap,userUid!!)
//                                            Log.i("asjkdfnsjkadfbn","going for other one saveUserData member")
//                                            CoroutineScope(Dispatchers.Main).run {
//                                                saveUserData(chatId,userTwoMap,cardList[posInside-1].user_uid!!)}
                            val arrayTesting=ArrayList<ChatMatchDataExtraDataClass>()
                            arrayTesting.add(ChatMatchDataExtraDataClass(chatId,userOneMap,ownerUid!!))
                            arrayTesting.add(ChatMatchDataExtraDataClass(chatId,userTwoMap,uid))
                            lifecycleScope.launchWhenResumed {
                                if (findNavController().currentDestination!!.id == R.id.userInfoFragment) {
                                    homeViewModel.arrayAddUserChatExtraFeature(arrayTesting).observe(requireActivity(),
                                        Observer {
                                            val check = it as Boolean
                                            if(check){
//                                                                CoroutineScope(Dispatchers.Main).run {
                                                lifecycleScope.launchWhenResumed {
                                                    if(findNavController().currentDestination!!.id == R.id.userInfoFragment) {
                                                        saveMemberData(ownerUid!!, uid, chatId)
                                                    }}

                                                Log.i("asdfbnasbdf","Data added not turn to change like status")
                                                lifecycleScope.launchWhenResumed {
                                                    if (findNavController().currentDestination!!.id == R.id.userInfoFragment) {
                                                        homeViewModel.addDataToLikeDataMatchTwo(
                                                            requireContext(),
                                                            uid,
                                                            name,
                                                            age,
                                                            imageUrl)
                                                            .observe(requireActivity(),
                                                                Observer {
                                                                    val flag = it as Boolean
                                                                    if (flag == true) {

                                                                        lifecycleScope.launchWhenResumed {
                                                                            if (findNavController().currentDestination!!.id == R.id.userInfoFragment) {
                                                                                homeViewModel.changUserStatus(
                                                                                    requireContext(),
                                                                                    uid)
                                                                                    .observe(
                                                                                        requireActivity(),
                                                                                        Observer {
                                                                                            val flag = it as Boolean
                                            if (flag == true) {
                                                MyUtils.stopProgress()
                                                lifecycleScope.launchWhenResumed {
                                                if(findNavController().currentDestination!!.id == R.id.userInfoFragment) {
                                                    findNavController().popBackStack()
                                                }}
                                            }
                                                                                            Log.i("checkNowSataus",
                                                                                                "update status of ${uid}+${MyUtils.getString(
                                                                                                    requireContext(), MyUtils.userUidKey)} bro")
                                                                                        })
                                                                            }
                                                                        }
                                                                        Log.i("dataAddded",
                                                                            "Like data added successfully inside fragment")
                                                                    }
                                                                })
                                                    }
                                                }
                                            }else{

                                                MyUtils.stopProgress()
                                                lifecycleScope.launchWhenResumed {
                                                if(findNavController().currentDestination!!.id == R.id.userInfoFragment) {
                                                    findNavController().navigateUp()
                                                }}
                                            }
                                        })
                                }
                            }

                        }
                        else {
                            Log.i("lkjsafnjksnf", "else part of liked user")
                            lifecycleScope.launchWhenResumed {
                                if (findNavController().currentDestination!!.id == R.id.userInfoFragment) {
                                    homeViewModel.addDataToLikeDataTwo(
                                        requireContext(),
                                        uid,
                                        name,
                                        age,
                                        imageUrl,
                                        "1").observe(requireActivity(),
                                        Observer {
                                            val flag = it as Boolean
                                            if (flag == true) {
                                                val flag = it as Boolean
                                            if (flag == true) {
                                                MyUtils.stopProgress()
                                                lifecycleScope.launchWhenResumed {
                                                if(findNavController().currentDestination!!.id == R.id.userInfoFragment) {
                                                    findNavController().popBackStack()
                                                }}
                                            }
                                                Log.i("dataAddded",
                                                    "Like data added successfully inside fragment")
                                            }
                                        })
                                }
                            }
                        }
                    })
            }}
    }
    private fun init(){
        binding!!.apply {
            userNameUserInfo.text="${userName}' Profile"
            txtUserNameUserInfo.text="${userName},"
            txtUserAgeUserInfo.text=fullAge
            Log.i("sjajfbjsabf","user fulll age si ${fullAge}")
            txtFirstCountryUserInfo.text=firstContry
            txtSecondCountryUserInfo.text=secondCountry
            txtUserAgeUserInfo.text=updateModel.user_birthday

            if(!updateModel.user_about_me.equals("") && updateModel.user_about_me != null && !updateModel.user_about_me.equals("null")){
                aboutLayout.visibility=View.VISIBLE
                txtABoutMe.text=updateModel.user_about_me
            }
            if(!updateModel.user_current_work.equals("") && updateModel.user_current_work != null && !updateModel.user_current_work.equals("null")){
                currenWorkLayout.visibility=View.VISIBLE
                txtCurrentWork.text=updateModel.user_current_work
            }
            if(!updateModel.user_school.equals("") && updateModel.user_school != null && !updateModel.user_school.equals("null")){
                SchoolLayout.visibility=View.VISIBLE
                txtSchool.text=updateModel.user_school
            }

            if(!updateModel.user_city_live.equals("") && updateModel.user_city_live != null && !updateModel.user_city_live.equals("null")){
                cityLayout.visibility=View.VISIBLE
                txtCity.text=updateModel.user_city_live
            }

            if(!updateModel.user_home_town.equals("") && updateModel.user_home_town != null && !updateModel.user_home_town.equals("null")){
                HomeLayout.visibility=View.VISIBLE
                txtHome.text=updateModel.user_home_town
            }
            if(updateModel.user_looking_for_person_type != null && updateModel.user_looking_for_person_type?.size!! >= 0  ){
                for(i in 0 until updateModel.user_looking_for_person_type?.size!!){
                    listInterst.add(updateModel.user_looking_for_person_type!![i])
                }
            }
            if(updateModel.user_pets != null && updateModel.user_pets?.size!! >= 0){
                for(i in 0 until updateModel.user_pets?.size!!){
                    listInterst.add(updateModel.user_pets!![i])
                }
            }


            if(updateModel.user_language != null && updateModel.user_language?.size!! >= 0){
                for(i in 0 until updateModel.user_language?.size!!){
                    listInterst.add(updateModel.user_language!![i])
                }
            }
            if(updateModel.user_interests != null && updateModel.user_interests?.size!! >= 0){
                for(i in 0 until updateModel.user_interests?.size!!){
                    listInterst.add(updateModel.user_interests!![i])
                }
            }
            if(updateModel.user_children != null && updateModel.user_children?.equals("")!! && updateModel.user_children?.equals("null")!! ){
                    listInterst.add(updateModel.user_children.toString())
            }
            if(updateModel.user_astrological_sign != null && !updateModel.user_astrological_sign?.equals("")!!&& !updateModel.user_astrological_sign?.equals("null")!!  ){
                    listInterst.add(updateModel.user_astrological_sign.toString())
            }
            if( updateModel.user_religion != null && !updateModel.user_religion?.equals("")!!  && !updateModel.user_religion?.equals("null")!!  ){
                    listInterst.add(updateModel.user_religion.toString())
            }
            if( updateModel.user_education != null && !updateModel.user_education?.equals("")!! && !updateModel.user_education?.equals("null")!!  ){
                    listInterst.add(updateModel.user_education.toString())
            }

            if( updateModel.user_select_your_country != null && !updateModel.user_select_your_country?.equals("")!! && !updateModel.user_select_your_country?.equals("null")!!  ){
                    listInterst.add(updateModel.user_select_your_country.toString())
            }
            if(updateModel.user_height != null && !updateModel.user_height?.equals("")!!  && !updateModel.user_height?.equals("null")!! ){
                    listInterst.add(updateModel.user_height.toString())
            }
            if(  updateModel.user_body_type != null && !updateModel.user_body_type?.equals("")!!  && !updateModel.user_body_type?.equals("null")!! ){
                    listInterst.add(updateModel.user_body_type.toString())
            }
            if( updateModel.user_exercise != null && !updateModel.user_exercise?.equals("")!! && !updateModel.user_exercise?.equals("null")!!  ){
                    listInterst.add(updateModel.user_exercise.toString())
            }
            if( updateModel.user_drink != null && !updateModel.user_drink?.equals("")!!  && !updateModel.user_drink?.equals("null")!!){
                    listInterst.add(updateModel.user_drink.toString())
            }
            if( updateModel.user_smoker != null && !updateModel.user_smoker?.equals("")!!  && !updateModel.user_smoker?.equals("null")!!){
                    listInterst.add(updateModel.user_smoker.toString())
            }
            if( updateModel.user_marijuana != null && !updateModel.user_marijuana?.equals("")!!  && !updateModel.user_marijuana?.equals("null")!!){
                    listInterst.add(updateModel.user_marijuana.toString())
            }
//            if(listInterst.size > 0){
//                chipLayout.visibility=View.VISIBLE
//            }
            Log.i("asfdbasjdf","list interst ${listInterst}")
            lifecycleScope.launchWhenResumed {
                for (i in 0.until(listInterst.size)) {
                    Log.i("asfdbasjdf","list interst ${listInterst[i]}")
                if(listInterst[i].equals("Friends")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.friend_emoji)!!)
                }
                   else if(listInterst[i].equals("FWB")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.naughty_emoji)!!)
                }
                else if(listInterst[i].equals("Something Casual")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.kiss_emoji)!!)
                }
                else if(listInterst[i].equals("Exclusive Dating")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.trofy_emoji)!!)
                }
                else  if(listInterst[i].equals("Long Term Relationship")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.heart_emoji)!!)
                }
                else if(listInterst[i].equals("Wedding Band")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.ring_emoji)!!)
                }
                else if(listInterst[i].equals("Cat(s)")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.cat_emoji)!!)
                }
                else  if(listInterst[i].equals("Dog(s)")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.dog_emoji)!!)
                }
                else  if(listInterst[i].equals("Other")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.pet_img)!!)
                }
                else if(listInterst[i].equals("Bowling")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.booling_img)!!)
                }
                else  if(listInterst[i].equals("Danceing")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.dance_img)!!)
                }
                else if(listInterst[i].equals("Reading")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.reading_img)!!)
                }
             else  if(listInterst[i].equals("Make")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.makeup_img)!!)
                }
                    else  if(listInterst[i].equals("Music")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.music_img)!!)
                }
                else if(listInterst[i].equals("Karaoke")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.karaoke_img)!!)
                }
                else if(listInterst[i].equals("Traveling")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.traveling_img)!!)
                }
                else   if(listInterst[i].equals("Film & TV")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.moviethreater_img)!!)
                }
                else  if(listInterst[i].equals("Concerts")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.concert_img)!!)
                }
                else   if(listInterst[i].equals("Cooking")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.kitchen_img)!!)
                }
                else   if(listInterst[i].equals("Virgo")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.virgo_sign)!!)
                }
                else  if(listInterst[i].equals("Scorpio")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.scorpio_sign)!!)
                }
                else   if(listInterst[i].equals("Taurus")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.taurus_sign)!!)
                }
                else if(listInterst[i].equals("Pisces")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.pisces_sign)!!)
                }
                else  if(listInterst[i].equals("Sagittarius")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.sagittarius_sign)!!)
                }
                else  if(listInterst[i].equals("Leo")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.leo_sign)!!)
                }
                else  if(listInterst[i].equals("Libra")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.a_sign)!!)
                }
                else  if(listInterst[i].equals("Capricorn")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.capricorn_sign)!!)
                }
                else   if(listInterst[i].equals("Gemini")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.gemini_sign)!!)
                }
                else  if(listInterst[i].equals("Aquarius")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.aquarius_sign)!!)
                }
                else if(listInterst[i].equals("Aries")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.aries_sign)!!)
                }
                else   if(listInterst[i].equals("Cancer")){
                    addChipToGroup(requireContext(), listInterst[i], requireContext().getDrawable(R.drawable.cancer_sign)!!)
                }
                    else{
                        addChipToGroup(requireContext(), listInterst[i],null)
                    }
            }
                chipLayout.visibility=View.VISIBLE
        }

        val tempList=getCountry(requireContext())

        if(firstContry.equals("") && secondCountry.equals("") || firstContry.equals("null") && secondCountry.equals("null")){
//            holder.flagLayout.visibility=View.GONE
            txtFirstCountryUserInfo.visibility=View.GONE
            imgFirstFlagUsrInfo.visibility=View.GONE
            txtConcateTwoCountryFlags.visibility=View.GONE
            txtSecondCountryUserInfo.visibility=View.GONE
            imgSecondFlagUsrInfo.visibility=View.GONE
        }
        if(!firstContry.equals("") && !firstContry.equals("null")){
            for (i in 0 until tempList.size){
                if(tempList[i].name.equals( firstContry)){
                    val flag = requireContext()!!.resources.getIdentifier(tempList[i].flag.lowercase(), "drawable", requireContext().packageName)
                    imgFirstFlagUsrInfo.setImageResource(flag)
                }
            }
        }
        if(!secondCountry.equals("") && !secondCountry.equals("null")){
            for (i in 0 until tempList.size){
                if(tempList[i].name.equals( secondCountry)){
                    val flag = requireContext()!!.resources.getIdentifier(tempList[i].flag.lowercase(), "drawable", requireContext().packageName)
                    imgSecondFlagUsrInfo.setImageResource(flag)
                }
            }
        }
    }}

        fun addChipToGroup(context: Context, person: String,img:Drawable?) {
            val chip = Chip(context)
            chip.text = person
            if(img != null) {
                chip.chipIcon =img
            }
            chip.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
            chip.isCheckable = false
            binding!!.chipGroup.addView(chip as View)
            chip.chipStrokeColor=ColorStateList.valueOf(ContextCompat.getColor(context, R.color.normaltxtColor))
            chip.chipStrokeWidth=2.0f
            chip.setOnCloseIconClickListener { binding!!.chipGroup.removeView(chip as View) }
        }
    fun iniliseList(){

        for(i in 0.until(iimage.size)){
            val str= iimage[i]
                image.add(str)
        }
        Log.i("askfnbkasf","chip list is ${listInterst}")
        lifecycleScope.launchWhenResumed {
            val adapterr = ImageViewPager(requireContext(), image)
            binding!!.imgUserImageUserInfo.adapter = adapterr
            binding!!.dotsIndicator.attachTo(binding!!.imgUserImageUserInfo)

        }
        val distance =MyUtils.getAwayLocation(lat!!,long!!,userLat!!,userLong!!)
        binding!!.txtLocationAwayStatusUserInfo.text= "${distance.toInt()} miiles aways"
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
    fun saveUserData(chatId:String,userOneMap:HashMap<String,String>,userUid:String)
    {
        viewModel.addUserChatExtraFeature(requireContext(),chatId,userOneMap,userUid).observe(requireActivity(),
            Observer {
                val data=it as Boolean
                if(data){
                    Log.i("sjfhsakjfdn","data added successfull")}
                else{
                    Log.i("sjfhsakjfdn","data not added")
                }
            })
    }
    private fun saveMemberData(userUid: String, userUid1: String,chatId:String) {
        var userList=ArrayList<String>()
        userList.add(userUid)
        userList.add(userUid1)
        viewModel.addUserDataList(chatId,userList)
    }
}