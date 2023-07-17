package com.ibeor.ibeorchattingapp.modules.editProfile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.developers.imagezipper.ImageZipper
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentEditProfileBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.gettingUserDetail.AddImageAdapter
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import com.ibeor.ibeorchattingapp.modules.userData.json.json_heightItem
import com.ibeor.ibeorchattingapp.modules.userData.uploadToFirebaseDataBase.UserFirebaseAboutData
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.*


class EditProfileFragment : Fragment() {
    private var binding:FragmentEditProfileBinding?=null
    private var list:ArrayList<String>?=null
    var persongList:ArrayList<EditProfileUserDataGettingTwo>?=null
    private var aboutTxtLength:Int=421
    private var aboutList=ArrayList<EditProfileUserDataGettingOne>()
    private var flag=true

    private var userAboutUser:String?=null
    private var userCurrentWork:String?=null
    private var userSchool:String?=null
    private var userCityLive:String?=null
    private var userHometown:String?=null
    private var userGender:String?=null
    private var  db: FirebaseFirestore?=null
    private var uid:String?=null
    private var userBirthday:String?=null
    private var updateModel:UserFirebaseAboutData?=null

    private var heightList= java.util.ArrayList<json_heightItem>()
    private var heightListOnly= java.util.ArrayList<String>()

// for add image
    private var replacePosition:Int?=null
    private var clickPostion:Int?=null
    private var imageUriSecond:Uri?=null
    private var RESULT_LOAD_IMAGE=100
    private var PICTURE_RESULT=112
    private var galleryFile:File?=null
    private var listAddImage=ArrayList<String>()
    var addImageadapter : AddImageAdapter?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentEditProfileBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("asklfnsadf","inside create view opekn")
        MyUtils.showProgress(requireContext())
        init()
        clickListner()
    }
    private fun setDataOnView(){
        binding!!.apply {
            lifecycleScope.launchWhenResumed {
                if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
            genderRadioGroup.setOnCheckedChangeListener(object :RadioGroup.OnCheckedChangeListener{
                override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                    val btn:RadioButton=view!!.findViewById(p1)
                    userGender=btn.text.toString()
                    MyUtils.putString(requireContext(),MyUtils.userGenderKey,userGender.toString())
                }
            })}}
            lifecycleScope.launchWhenResumed {
                if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
            etUserAbout.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(p0!!.length >= 0){
                        val d=aboutTxtLength-p0.length
                        txtLengthRemainCharacterAbout.text=d.toString()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    userAboutUser=p0.toString()
                    MyUtils.putString(requireContext(),MyUtils.userAboutInfo,userAboutUser.toString())
//                    Log.i("before","after ff text change ${p0}")
                }
            })}}
            lifecycleScope.launchWhenResumed {
                if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
            etUserCurrentWork.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    userCurrentWork=p0.toString()
                    MyUtils.putString(requireContext(),MyUtils.userCurrentWork,userCurrentWork.toString())
//                    Log.i("before","after ff text change ${p0}")
                }
            })}}
            lifecycleScope.launchWhenResumed {
                if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
            etUserSchoolTemp.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    userSchool=p0.toString()
                    MyUtils.putString(requireContext(),MyUtils.userSchool,userSchool.toString())
//                    Log.i("before","after ff text change ${p0}")
                }
            })}}
            lifecycleScope.launchWhenResumed {
                if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
            etUserCityTemp.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    userCityLive=p0.toString()
                    MyUtils.putString(requireContext(),MyUtils.userCityLive,userCityLive.toString())
//                    Log.i("before","after ff text change ${p0}")
                }
            })}}
            lifecycleScope.launchWhenResumed {
                if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
            etUserHomeTemp.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    userHometown=p0.toString()
                    MyUtils.putString(requireContext(),MyUtils.userHomeTown,userHometown.toString())
//                    Log.i("before","after ff text change ${p0}")
                }
            })}}
            lifecycleScope.launchWhenResumed {
                if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
            userAboutUser=MyUtils.getString(requireContext(),MyUtils.userAboutInfo)
            if (userAboutUser!= null && userAboutUser !="" && userAboutUser != "null") {
                etUserAbout.setText(userAboutUser)
            }}}
            lifecycleScope.launchWhenResumed {
                if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
            userCurrentWork=MyUtils.getString(requireContext(),MyUtils.userCurrentWork)
            if (userCurrentWork!= null && userCurrentWork !=""&& userCurrentWork != "null") {
                etUserCurrentWork.setText(userCurrentWork)
            }}}
            lifecycleScope.launchWhenResumed {
                if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
            userSchool=MyUtils.getString(requireContext(),MyUtils.userSchool)
            if (userSchool!= null && userSchool !=""&& userSchool !="null") {
                etUserSchoolTemp.setText(userSchool)
            }}}
            lifecycleScope.launchWhenResumed {
                if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
            userCityLive=MyUtils.getString(requireContext(),MyUtils.userCityLive)
            if (userCityLive!= null && userCityLive !="" && userCityLive !="null") {
                etUserCityTemp.setText(userCityLive)
            }}}
            lifecycleScope.launchWhenResumed {
                if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
            userHometown=MyUtils.getString(requireContext(),MyUtils.userHomeTown)
            if (userHometown!= null && userHometown !=""&& userHometown !="null") {
                Log.i("aseghfioasfn","value is $userHometown")
                etUserHomeTemp.setText(userHometown)
            }}}
            lifecycleScope.launchWhenResumed {
                if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
            userGender=MyUtils.getString(requireContext(),MyUtils.userGenderKey)
            if (userGender!= null && userGender !="" && userGender !="null") {
                if(userGender=="Man") {
                    genderRadioGroup.check(R.id.menRadio)
                }
                else{
                    genderRadioGroup.check(R.id.womenRadio)
                }
            }}}

        }
    }
    private fun setInfoData(){
        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
            if (updateModel?.user_about_me != null && updateModel?.user_about_me != "" && updateModel?.user_about_me != "null") {
                MyUtils.putString(requireContext(),
                    MyUtils.userAboutInfo,
                    updateModel?.user_about_me!!)
            }
        }}
        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
            if (updateModel?.user_current_work != null && updateModel?.user_current_work != "" && updateModel?.user_current_work != "null") {
                MyUtils.putString(requireContext(),
                    MyUtils.userCurrentWork,
                    updateModel?.user_current_work!!)
            }
        }}
        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
            if(updateModel?.user_school != null  && updateModel?.user_school != ""  && updateModel?.user_school !="null"){
            MyUtils.putString(requireContext(),MyUtils.userSchool,updateModel?.user_school!!)
        }}}

        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
            if(updateModel?.user_city_live != null  && updateModel?.user_city_live != "" && updateModel?.user_city_live !="null"){
            MyUtils.putString(requireContext(),MyUtils.userCityLive,updateModel?.user_city_live!!)
        }}}

        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
            if(updateModel?.user_home_town != null  && updateModel?.user_home_town != "" && updateModel?.user_home_town !="null"){
            MyUtils.putString(requireContext(),MyUtils.userHomeTown,updateModel?.user_home_town!!)
        }}}

        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
            if(updateModel?.user_looking_for_person_type != null){
            MyUtils.putArray(requireContext(),MyUtils.userLookingForKey,updateModel?.user_looking_for_person_type!!)
        }}}

        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
//        if(MyUtils.getArray(requireContext(),MyUtils.userPet)!!.size <= 1) {
            if (updateModel?.user_pets != null) {
                Log.i("listValxvxue", " saffaspet ${MyUtils.getArray(requireContext(), MyUtils.userPet)}")
                MyUtils.putArray(requireContext(), MyUtils.userPet, updateModel?.user_pets!!)
            }}}
//        }

        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
//        if(MyUtils.getArray(requireContext(),MyUtils.userInterestsStatus)!!.size <= 1) {
            if (updateModel?.user_interests != null) {
                MyUtils.putArray(requireContext(),
                    MyUtils.userInterestsStatus,
                    updateModel?.user_interests!!)
            }}}
//        }

        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
            if(updateModel?.user_children != null  && updateModel?.user_children != ""){
            MyUtils.putString(requireContext(),MyUtils.userChildren,updateModel?.user_children!!)
        }}}

        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
            if(updateModel?.user_astrological_sign != null  && updateModel?.user_astrological_sign != ""){
            MyUtils.putString(requireContext(),MyUtils.userAstrologicalSign,updateModel?.user_astrological_sign!!)
        }}}

        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
            if(updateModel?.user_religion != null  && updateModel?.user_religion != ""){
            MyUtils.putString(requireContext(),MyUtils.userReligion,updateModel?.user_religion!!)
        }}}

        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
            if(updateModel?.user_education != null  && updateModel?.user_education != ""){
            MyUtils.putString(requireContext(),MyUtils.userEducation,updateModel?.user_education!!)
        }}}

        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
            Log.i("safjbsdgf","lisfsafdst is ${updateModel?.user_language}")
        if(updateModel?.user_language != null  && updateModel?.user_language!!.size >= 1){
            MyUtils.putArray(requireContext(),MyUtils.userLanguage,updateModel?.user_language!!)
        }}}

        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
            if(updateModel?.user_select_your_country != null ){
            MyUtils.putArray(requireContext(),MyUtils.userLanguage,updateModel?.user_select_your_country!!)
        }}}

        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
            if(updateModel?.user_height != null && updateModel?.user_height != "" ){
            MyUtils.putString(requireContext(),MyUtils.userHeight,updateModel?.user_height!!)
        }}}

        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
            if(updateModel?.user_body_type != null && updateModel?.user_body_type != "" ){
            MyUtils.putString(requireContext(),MyUtils.userBodyType,updateModel?.user_body_type!!)
        }}}

        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
            if(updateModel?.user_exercise != null && updateModel?.user_exercise != "" ){
            MyUtils.putString(requireContext(),MyUtils.userExerciseStatus,updateModel?.user_exercise!!)
        }}}

        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
            if(updateModel?.user_drink != null && updateModel?.user_drink != "" ){
            MyUtils.putString(requireContext(),MyUtils.userDrinkStatus,updateModel?.user_drink!!)
        }}}

        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
            if(updateModel?.user_smoker != null && updateModel?.user_smoker != "" ){
            MyUtils.putString(requireContext(),MyUtils.userSmokerStatus,updateModel?.user_smoker!!)
        }}}

        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.editProfileFragment){
            if(updateModel?.user_marijuana != null && updateModel?.user_marijuana != "" ){
            MyUtils.putString(requireContext(),MyUtils.userMarijuanaStatus,updateModel?.user_marijuana!!)
        }}}
    }
    private fun nextInit(){
        binding!!.txtLengthRemainCharacterAbout.text = aboutTxtLength.toString()
        binding!!.apply {
            lifecycleScope.launchWhenResumed {
                if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
                    userBirthday = MyUtils.getString(requireContext(), MyUtils.userFullAgeKey)
                    txtUserBirthday.text = userBirthday
                    toolbarEditProfile.imBackBtnFilterSetting.setOnClickListener { activity?.onBackPressed() }
                    toolbarEditProfile.txtTitleFilterSetting.text = "Edit Profile"
                }}
            setPhotoPick()
            lifecycleScope.launchWhenResumed {
                if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
                    rvGettingUserPersonalDetail.layoutManager =
                        LinearLayoutManager(requireContext())
                    val adapterr = AdapterForGettingAboutInfo(requireContext(),
                        aboutList,
                        object : AdapterForGettingAboutInfo.Clickss {
                            override fun onClick(position: Int, positio: Int) {
                                val v = "$position$positio"
                                if (v == "00") {
                                    val bundle = Bundle()
                                    bundle.putString("status", v)
                                    findNavController().navigate(R.id.action_editProfileFragment_to_lookingForUserDataByTickFragment,
                                        bundle)
                                } else if (v == "01") {
                                    val bundle = Bundle()
                                    bundle.putString("status", v)
                                    findNavController().navigate(R.id.action_editProfileFragment_to_lookingForUserDataByTickFragment,
                                        bundle)
                                } else if (v == "02") {
                                    val bundle = Bundle()
                                    bundle.putString("status", v)
                                    findNavController().navigate(R.id.action_editProfileFragment_to_childrenUserDataByRadioButtonFragment,
                                        bundle)
                                } else if (v == "03") {
                                    val bundle = Bundle()
                                    bundle.putString("status", v)
                                    findNavController().navigate(R.id.action_editProfileFragment_to_childrenUserDataByRadioButtonFragment,
                                        bundle)
                                } else if (v == "04") {
                                    val bundle = Bundle()
                                    bundle.putString("status", v)
                                    findNavController().navigate(R.id.action_editProfileFragment_to_childrenUserDataByRadioButtonFragment,
                                        bundle)
                                } else if (v == "05") {
                                    val bundle = Bundle()
                                    bundle.putString("status", v)
                                    findNavController().navigate(R.id.action_editProfileFragment_to_childrenUserDataByRadioButtonFragment,
                                        bundle)
                                } else if (v == "06") {
//                            val bundle=Bundle()
//                            bundle.putString("status",v)
                                    findNavController().navigate(R.id.action_editProfileFragment_to_selectLanguageFragment)
                                } else if (v == "07") {
                                    Log.i("sfasf", "inside Position $v")
                                    findNavController().navigate(R.id.action_editProfileFragment_to_selectCountryFragment)
                                } else if (v == "10") {
                                    heightList = getHeight()
                                    for (i in 0 until heightList.size) {
                                        heightListOnly.add(heightList[i].height)
                                    }
                                    bottomHeightDialog()
                                } else if (v == "11") {
                                    val bundle = Bundle()
                                    bundle.putString("status", v)
                                    findNavController().navigate(R.id.action_editProfileFragment_to_childrenUserDataByRadioButtonFragment,
                                        bundle)
                                } else if (v == "20") {
                                    val bundle = Bundle()
                                    bundle.putString("status", v)
                                    findNavController().navigate(R.id.action_editProfileFragment_to_childrenUserDataByRadioButtonFragment,
                                        bundle)
                                } else if (v == "21") {
                                    val bundle = Bundle()
                                    bundle.putString("status", v)
                                    findNavController().navigate(R.id.action_editProfileFragment_to_childrenUserDataByRadioButtonFragment,
                                        bundle)
                                } else if (v == "22") {
                                    val bundle = Bundle()
                                    bundle.putString("status", v)
                                    findNavController().navigate(R.id.action_editProfileFragment_to_childrenUserDataByRadioButtonFragment,
                                        bundle)
                                } else if (v == "23") {
                                    val bundle = Bundle()
                                    bundle.putString("status", v)
                                    findNavController().navigate(R.id.action_editProfileFragment_to_childrenUserDataByRadioButtonFragment,
                                        bundle)
                                } else if (v == "30") {
                                    val bundle = Bundle()
                                    bundle.putString("status", v)
                                    Log.i("sfasf", "inside Position $v")
                                    activity?.runOnUiThread {
                                        findNavController().navigate(R.id.action_editProfileFragment_to_lookingForUserDataByTickFragment,
                                            bundle)
                                    }
                                }
                            }
                        })

                    rvGettingUserPersonalDetail.adapter = adapterr
                }}
        }
    }
    private fun init() {
        Log.i("asklfnsadf","inside init")
        db= FirebaseFirestore.getInstance()
        uid=MyUtils.getString(requireContext(),MyUtils.userUidKey)
        lifecycleScope.launchWhenResumed {
            if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
                HomeViewModel().fetchFirebaseAboutData(requireContext(),
                    uid!!,
                    MyUtils.getString(requireContext(), MyUtils.userFullAgeKey)!!)
                    .observe(requireActivity(), Observer {
                        updateModel = it as UserFirebaseAboutData
                        inilizeList()
                        setInfoData()
                        setDataOnView()
                        nextInit()
                        MyUtils.stopProgress()
                    })
            }}
        lifecycleScope.launchWhenResumed {
            if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
        inilizeList()
        setInfoData()
        setDataOnView()
        nextInit()
        MyUtils.stopProgress()
    }}
    }
    private fun clickListner(){
        binding!!.apply {
            toolbarEditProfile.txtDoneFilterSetting.setOnClickListener {
                MyUtils.putArray(requireContext(),MyUtils.userArrayImageLinkKey,listAddImage)
                val list=MyUtils.getArray(requireContext(),MyUtils.userArrayImageLinkKey)
                val newList=ArrayList<String>()
                for(i in 0 until list?.size!!){
                if(!list[i].equals("")){
                    newList.add(list[i])
                }
                }

                HomeViewModel().updateAddImage(requireContext(),newList).observe(requireActivity(),
                    Observer {
                        val ch=it as Boolean
                        if(ch == true){
                            Log.i("safhsna","uploded image success ")
                        }
                    })
                val mapModel=HashMap<String,Any>()
                mapModel["user_birthday"] = userBirthday?:""
                mapModel["user_gender"] = userGender?:""
                mapModel["user_about_me"] = userAboutUser?:""
                mapModel["user_current_work"] = userCurrentWork?:""
                mapModel["user_school"] = userSchool?:""
                mapModel["user_city_live"] = userCityLive?:""
                mapModel["user_home_town"] = userHometown?:""
                mapModel["user_looking_for_person_type"]=MyUtils.getArray(requireContext(),MyUtils.userLookingForInPeople)?:ArrayList<String>()
                mapModel["user_pets"]=MyUtils.getArray(requireContext(),MyUtils.userPet)?:ArrayList<String>()
                mapModel["user_interests"]=MyUtils.getArray(requireContext(),MyUtils.userInterestsStatus)?:ArrayList<String>()
//                mapModel["userArrayImageLinkKey"]=MyUtils.getArray(requireContext(),MyUtils.userArrayImageLinkKey)?:ArrayList<String>()
                Log.i("listValxvxue","all value asafdasfdnasfdsafdv pet ${MyUtils.getArray(requireContext(),MyUtils.userPet)}")
                mapModel["user_children"]=MyUtils.getString(requireContext(),MyUtils.userChildren)?:"null"
                mapModel["user_astrological_sign"]=MyUtils.getString(requireContext(),MyUtils.userAstrologicalSign)?:"null"
                mapModel["user_religion"]=MyUtils.getString(requireContext(),MyUtils.userReligion)?:"null"
                mapModel["user_education"]=MyUtils.getString(requireContext(),MyUtils.userEducation)?:"null"
                mapModel["user_height"]=MyUtils.getString(requireContext(),MyUtils.userHeight)?:"null"
                mapModel["user_body_type"]=MyUtils.getString(requireContext(),MyUtils.userBodyType)?:"null"
                mapModel["user_exercise"]=MyUtils.getString(requireContext(),MyUtils.userExerciseStatus)?:"null"
                mapModel["user_drink"]=MyUtils.getString(requireContext(),MyUtils.userDrinkStatus)?:"null"
                mapModel["user_smoker"]=MyUtils.getString(requireContext(),MyUtils.userSmokerStatus)?:"null"
                mapModel["user_marijuana"]=MyUtils.getString(requireContext(),MyUtils.userMarijuanaStatus)?:"null"
                db?.collection("User")!!.document(uid.toString()).collection("UserInfo").document(uid.toString())
                    .set(mapModel).addOnSuccessListener {
                        Toast.makeText(requireContext(), "Data updated", Toast.LENGTH_SHORT).show()
                        activity?.onBackPressed()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "failed data update ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }

        txtPreviewImageEdityy.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_previewImageVerticalFragment)
        }
        }
    }
    private fun inilizeList() {
        if (flag) {
            flag=false
            list?.clear()
            list?.add("")
            list?.add("")
            list?.add("")
            list?.add("")
            list?.add("")
            list?.add("")
            persongList = ArrayList<EditProfileUserDataGettingTwo>()
            persongList!!.clear()
            persongList!!.add(EditProfileUserDataGettingTwo(R.drawable.search_advance,
                "Looking For",
                "Add to your profile..."))
            persongList!!.add(EditProfileUserDataGettingTwo(R.drawable.pet_img,
                "Pets",
                "Add to your profile..."))
            persongList!!.add(EditProfileUserDataGettingTwo(R.drawable.child,
                "Children",
                "Add to your profile..."))
            persongList!!.add(EditProfileUserDataGettingTwo(R.drawable.jadu,
                "Astrological Sign",
                "Add to your profile..."))
            persongList!!.add(EditProfileUserDataGettingTwo(R.drawable.temple,
                "Religion",
                "Add to your profile..."))
            persongList!!.add(EditProfileUserDataGettingTwo(R.drawable.education,
                "Education",
                "Add to your profile..."))
            persongList!!.add(EditProfileUserDataGettingTwo(R.drawable.language,
                "Languages",
                "Add to your profile..."))
            persongList!!.add(EditProfileUserDataGettingTwo(R.drawable.country,
                "Select your country",
                "Add to your profile..."))
            val appppearanceList = ArrayList<EditProfileUserDataGettingTwo>()
            appppearanceList.add(EditProfileUserDataGettingTwo(R.drawable.height,
                "Height",
                "Add to your profile..."))
            appppearanceList.add(EditProfileUserDataGettingTwo(R.drawable.multi_person,
                "Body Type",
                "Add to your profile..."))
            val habbitList = ArrayList<EditProfileUserDataGettingTwo>()
            habbitList.add(EditProfileUserDataGettingTwo(R.drawable.gym,
                "Exercise",
                "Add to your profile..."))
            habbitList.add(EditProfileUserDataGettingTwo(R.drawable.drink,
                "Drink",
                "Add to your profile..."))
            habbitList.add(EditProfileUserDataGettingTwo(R.drawable.cigrate,
                "Smoker",
                "Add to your profile..."))
            habbitList.add(EditProfileUserDataGettingTwo(R.drawable.neem_tree,
                "Marijuana",
                "Add to your profile..."))
            val interestList = ArrayList<EditProfileUserDataGettingTwo>()
            interestList.add(EditProfileUserDataGettingTwo(R.drawable.interest,
                "My Interests",
                "Add to your profile..."))
            aboutList.add(EditProfileUserDataGettingOne("PERSONAL", persongList))
            aboutList.add(EditProfileUserDataGettingOne("APPEARANCE", appppearanceList))
            aboutList.add(EditProfileUserDataGettingOne("HABITS", habbitList))
            aboutList.add(EditProfileUserDataGettingOne("INTEREST", interestList))
        }
    }
    fun bottomHeightDialog(){
        val dialog= BottomSheetDialog(requireContext(),R.style.BottomSheetDialogTheme)
        dialog.window?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        val view = layoutInflater.inflate(R.layout.height_layout, null)
        dialog.setContentView(view)
        val imgCancel:AppCompatImageView=view.findViewById(R.id.imgCancelDialog)
        val txtHeight: AppCompatTextView =view.findViewById(R.id.txtHeight)
        val txtCancel: AppCompatTextView =view.findViewById(R.id.txtCancelHeight)
        val txtDone: AppCompatTextView =view.findViewById(R.id.txtDoneHeight)
        val numberPicker: NumberPicker =view.findViewById(R.id.hightNumberPickerHeight)
        txtCancel.setOnClickListener {

            dialog.dismiss()
        }
        txtDone.setOnClickListener {
            dialog.dismiss()
        }
        imgCancel.setOnClickListener {
            dialog.dismiss()
        }
        val str = arrayOfNulls<String>(heightListOnly.size)
        for(i in 0 until heightListOnly.size){
            str[i]=heightListOnly[i]
        }
        numberPicker.maxValue=heightListOnly.size-1
        numberPicker.minValue=0
        txtHeight.text = heightListOnly[0]
        numberPicker.displayedValues=str
        numberPicker.wrapSelectorWheel=true
        numberPicker.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener{
            override fun onValueChange(p0: NumberPicker?, p1: Int, p2: Int) {
                txtHeight.text=heightListOnly[p2]
                Log.i("sdfbsajdf","Selected height is  ${heightListOnly[p2]}")
                MyUtils.putString(requireContext(),MyUtils.userHeight,heightListOnly[p2])
            }
        })
        dialog.show()
    }
    fun getHeight(): java.util.ArrayList<json_heightItem> {
        var languageList: java.util.ArrayList<json_heightItem>? = null
        val inputStream: InputStream = requireActivity().resources.openRawResource(R.raw.height)
        try {
            val reader: Reader = InputStreamReader(inputStream, "UTF-8")
            val gson = Gson()
            languageList = gson.fromJson(
                reader,
                object : TypeToken<java.util.ArrayList<json_heightItem>>() {}.type
            )
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return languageList!!
    }
private fun setPhotoPick(){
    binding!!.apply {
        lifecycleScope.launchWhenResumed {
            if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
        val d=ArrayList<String>()
        for(i in 0 until 6){
            d.add("")
        }}}
        lifecycleScope.launchWhenResumed {
            if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
        listAddImage=MyUtils.getArray(requireContext(),MyUtils.userArrayImageLinkKey)?: ArrayList<String>()
        Log.i("safjhbsadf","List size before ${listAddImage.size}")
        for(i in 0 until 5){
            val size=listAddImage.size
            Log.i("safjhbsadf","List size before ${listAddImage.size} and i ${i}")
            if(size-1 == i){
                listAddImage.add("")
            }
        }
        Log.i("safjhbsadf","List size ${listAddImage.size
        }")}}
        lifecycleScope.launchWhenResumed {
            if(findNavController().currentDestination!!.id == R.id.editProfileFragment) {
                addImageadapter = AddImageAdapter(requireContext(),
                    listAddImage,
                    1,
                    object : AddImageAdapter.Clicks {
                        override fun onAddImage(position: Int, viewId: Int) {
                            when (viewId) {
                                R.id.imgAddImgDD -> {
                                    showSheetDialog(requireContext(), position)
                                }
                                R.id.imgUserImgDD -> {
                                    replacePosition = position
                                    showSheetDialog(requireContext(), position)
                                }
                            }
                        }

                        override fun onDelteImage(position: Int, viewId: Int) {
                            when (viewId) {
                                R.id.imgDeleteImgDD -> {
                                    setDeleteDialog(position)
                                }
                            }
                        }
                    })
                addImageEdit.rvAddPhotoComon.adapter = addImageadapter
            }}

        }

}
    private fun showSheetDialog(context: Context, position: Int){
        val dialog=BottomSheetDialog(context,R.style.BottomSheetDialogTheme)
        dialog.window?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        val view = layoutInflater.inflate(R.layout.image_pic_dialog, null)
        dialog.setContentView(view)
        val galleryImage:AppCompatImageView=view.findViewById(R.id.dialogGalleryPick)
        val cameraImage:AppCompatImageView=view.findViewById(R.id.dialogCameraPick)
        galleryImage.setOnClickListener {
            val per= Manifest.permission.READ_EXTERNAL_STORAGE
            requestPermssionn(position,per,null,0)
            dialog.dismiss()
        }
        cameraImage.setOnClickListener {
            Log.i("checkNowq","ON camera click")
            val per= Manifest.permission.CAMERA
            val per2= Manifest.permission.WRITE_EXTERNAL_STORAGE
            val per3=ArrayList<String>()
            per3.add(per)
            per3.add(per2)
            requestPermssionn(position,null,per3,1)
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun requestPermssionn(position:Int,permission:String?,per:ArrayList<String>?,value:Int){
        if(permission!=null) {
            Dexter.withActivity(activity).withPermissions(permission)
        }
        else{
            Dexter.withActivity(activity).withPermissions(per)
        }
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0!!.areAllPermissionsGranted()) {
                        clickPostion=position
                        if(value == 0){
                            getImageFormGallery()
                        }
                        else if(value == 1){
                            getImageFromCameraSecondMethod()
                        }
                        else{
                            Log.i("checkNowq","else inside request permission")
                        }
                    }
                    if (p0.isAnyPermissionPermanentlyDenied) {
                        Log.i("checkNowq","inside denid")
                        if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED
                            ||
                            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            ||
                            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            showSettingsDialog()
                        }
                        else{
                            clickPostion=position
                            if(value == 0){
                                getImageFormGallery()
                            }
                            else{
                                getImageFromCameraSecondMethod()
                            }
                        }
                        if(value == 0 ){

                        }
                        else{

                        }
                    }
                }
                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?,
                ) {
                    p1!!.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(requireContext(),
                    "Error occurred! ",
                    Toast.LENGTH_SHORT).show();
            }.onSameThread().check()
    }
    private fun setDeleteDialog(a:Int){
        val dialog= Dialog(requireContext())
        dialog.setContentView(R.layout.delete_image_dialog)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val deleteTxt:AppCompatTextView=dialog.findViewById(R.id.delelteBtnDeleteImage)
        val cancelTxt:AppCompatTextView=dialog.findViewById(R.id.cancelBtnDeleteImage)
        deleteTxt.setOnClickListener {
            removeImage(a)
            dialog.dismiss()
        }
        cancelTxt.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun removeImage(position:Int){
        listAddImage.removeAt(position)
        listAddImage.add("")
        addImageadapter!!.updateList(listAddImage)
//        binding!!.rvAddPhoto.adapter=adapter
        binding!!.addImageEdit.rvAddPhotoComon.adapter=addImageadapter
    }
    fun getImageFormGallery(){
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, RESULT_LOAD_IMAGE)
    }
    private fun getImageFromCameraSecondMethod(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUriSecond = activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriSecond)
        startActivityForResult(intent, PICTURE_RESULT)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK &&  data != null ) {
            val selectedImage: Uri = data.data!!
            val path:String?=getFilePath(selectedImage)
            if(path!= null){
                Log.i("asfsadf","path not null ")
//            galleryFile=File(path.toString())
                galleryFile=abc(File(path.toString()))
                addDataToList(galleryFile!!.path)
            }
            else{
                Toast.makeText(requireContext(), "Uploading failed due to image path not found!!", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == PICTURE_RESULT && resultCode == Activity.RESULT_OK) {
            try {
                val thumbnail = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUriSecond)
//                imageUrl = getRealPathFromURI(imageUriSecond)
//                addDataToList(imageUrl!!)
                val f=getFilePath(imageUriSecond!!)
                val fi=abc(File(f.toString()))
//                imageUrl = getRealPathFromURI(imageUriSecond)
                addDataToList(fi.path)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Camera Permissions Required")
        builder.setMessage("You have forcefully denied some of the required permissions for this action. Please open setting, go to permissions and allow them.")
        builder.setPositiveButton("SETTINGS") { dialog: DialogInterface, which: Int ->
            dialog.cancel()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
            intent.data = uri
            startActivityForResult(intent, 101)
        }
        builder.setNegativeButton("Cancel"
        ) { dialog: DialogInterface, which: Int ->
            dialog.cancel()
        }
        builder.show()
    }
    fun getFilePath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri!!, projection, null, null, null)
        if (cursor != null) {
            val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } else
            return null
    }
    fun abc(file: File):File {
        val imageCompress= ImageZipper(requireContext())
            .setQuality(100)
            .setMaxWidth(700)
            .setMaxHeight(700)
            .compressToFile(file)
        return imageCompress
    }
    private fun addDataToList(path:String){

        if (replacePosition != null){

                HomeViewModel().saveImageToFireBase(requireContext(),"images",path).observe(requireActivity(),
                Observer {
                   val  newPath=it as String
                    listAddImage[replacePosition!!] = newPath
                    addImageadapter!!.updateList(listAddImage)
                    binding!!.addImageEdit.rvAddPhotoComon.adapter = addImageadapter
                    clickPostion = null
                    replacePosition=null
                })
        }
        else {
            for (i in 0.until(listAddImage.size)) {
                if (listAddImage[i] == "") {
                    HomeViewModel().saveImageToFireBase(requireContext(),"images",path).observe(requireActivity(),
                        Observer {
                            val  newPath=it as String
                            listAddImage[i] = newPath
                            addImageadapter!!.updateList(listAddImage)
                            binding!!.addImageEdit.rvAddPhotoComon.adapter = addImageadapter
                            clickPostion = null
//                            break
                        })
                    break
                }
            }
        }
    }

}
