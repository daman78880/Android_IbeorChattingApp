package com.ibeor.ibeorchattingapp.modules.filterSetting

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentFilterSettingBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import com.ibeor.ibeorchattingapp.modules.userData.settingDataClass.SettingDataClassForFirebase


class FilterSettingFragment : Fragment() {
    var binder:FragmentFilterSettingBinding?=null
    var lookingGender:String?=null
    var looking_Age_Start:Long?=null
    var looking_Age_End:Long?=null
    var looking_Age_Flag:Boolean?=null
    var distance:Long?=null
    var distance_Flag:Boolean?=null
    var userList=SettingDataClassForFirebase()

   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
       binder=FragmentFilterSettingBinding.inflate(layoutInflater)
       return binder!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("asgasdf","data is feathc ")
//        init()
//        listener()
        lifecycleScope.launchWhenResumed {
            if (findNavController().currentDestination!!.id == R.id.filterSettingFragment) {
                MyUtils.showProgress(requireContext())
            }
        }
            HomeViewModel().fetchingSettingFilter(requireContext())
                .observe(requireActivity(), Observer {
                    val data = it as SettingDataClassForFirebase

                    Log.i("asgasdf", "data is feathc ${data}")
                    userList = data
                    lifecycleScope.launchWhenResumed {
                        init()
                        listener()
                    }
                })

    }
    private fun init(){
//        lookingGender=MyUtils.getString(requireContext(),MyUtils.userLookingForKey)
//        looking_Age_Start=MyUtils.getInt(requireContext(),MyUtils.userFilterStartAge)?.toLong()
//        looking_Age_End=MyUtils.getInt(requireContext(),MyUtils.userFilterEndAge)?.toLong()
//        looking_Age_Flag=MyUtils.getBoolean(requireContext(),MyUtils.userFilterAgeFlag)
//        distance=MyUtils.getInt(requireContext(),MyUtils.userFilterDistance)?.toLong()
//        distance_Flag=MyUtils.getBoolean(requireContext(),MyUtils.userFilterDistanceFlag)

        if (userList.looking_Gender != null){
        MyUtils.putString(requireContext(),MyUtils.userLookingForKey,userList.looking_Gender!!)
        lookingGender=MyUtils.getString(requireContext(),MyUtils.userLookingForKey)
        }
        if (userList.looking_Age_Start != null){
            val startAge=userList.looking_Age_Start
        MyUtils.putInt(requireContext(),MyUtils.userFilterStartAge,startAge!!.toInt())
        looking_Age_Start=MyUtils.getInt(requireContext(),MyUtils.userFilterStartAge)?.toLong()
        }
        if (userList.looking_Age_End != null){
            val endAge=userList.looking_Age_End
        MyUtils.putInt(requireContext(),MyUtils.userFilterEndAge,endAge!!.toInt())
        looking_Age_End=MyUtils.getInt(requireContext(),MyUtils.userFilterEndAge)?.toLong()
        }
        if (userList.looking_Distance != null){
        MyUtils.putInt(requireContext(),MyUtils.userLookingForKey,userList.looking_Distance!!.toInt())
        distance=MyUtils.getInt(requireContext(),MyUtils.userFilterDistance)?.toLong()
        }
        if(userList.looking_Distance_Flag != null){
            MyUtils.putBoolean(requireContext(),MyUtils.userFilterDistanceFlag, userList.looking_Distance_Flag!!)?:true
            distance_Flag=MyUtils.getBoolean(requireContext(),MyUtils.userFilterDistanceFlag)
        }
        if(userList.looking_Age_Flag != null){
            MyUtils.putBoolean(requireContext(),MyUtils.userFilterAgeFlag, userList.looking_Age_Flag!!)?:true
            looking_Age_Flag=MyUtils.getBoolean(requireContext(),MyUtils.userFilterAgeFlag)
        }


//        looking_Age_Start=userList.looking_Age_Start
//        looking_Age_End=userList.looking_Age_End
//        distance=userList.looking_Distance
//        looking_Age_Flag=userList.looking_Age_Flag
//        distance_Flag=userList.looking_Distance_Flag
        if(lookingGender != null){
            if(lookingGender.equals("Men") ){
                menselect()
            }
            else{
                womenSelect()
            }
        }

        binder!!.apply {
            if (userList.looking_Gender != null && userList.looking_Age_Flag != null && userList.looking_Age_Flag != null && userList.looking_Distance_Flag != null && userList.looking_Age_Start != null && userList.looking_Age_End != null && userList.looking_Distance != null) {
                ageRangeSlider.setValues(userList.looking_Age_Start?.toFloat(),
                    userList.looking_Age_End?.toFloat())
                distanceSlider.value = userList.looking_Distance!!.toFloat()

                if (userList.looking_Age_Flag == true) {
                    switchForAge.isChecked = true
                    switchForAge.trackDrawable.setColorFilter(ContextCompat.getColor(requireActivity(),
                        R.color.teal_200), PorterDuff.Mode.SRC_IN)
                }
                else {
                    switchForAge.isChecked = false
                    switchForAge.trackDrawable.setColorFilter(ContextCompat.getColor(requireActivity(),
                        R.color.white), PorterDuff.Mode.SRC_IN)
                }
                if (userList.looking_Distance_Flag == true) {
                    switchForDistance.isChecked = true
                    switchForDistance.trackDrawable.setColorFilter(ContextCompat.getColor(
                        requireActivity(),
                        R.color.teal_200), PorterDuff.Mode.SRC_IN)
                }
                else {
                    switchForDistance.isChecked = false
                    switchForDistance.trackDrawable.setColorFilter(ContextCompat.getColor(
                        requireActivity(),
                        R.color.white), PorterDuff.Mode.SRC_IN)
                }
                txtFilterAge.text = "${
                    ageRangeSlider.values[0].toInt().toString()
                }-${ageRangeSlider.values[1].toInt().toString()}"
                txtDistanceFilter.text = distanceSlider.value.toInt().toString() + "mi"
            }
        }
        MyUtils.stopProgress()
    }
    private fun  listener(){
        binder!!.apply {
            filterInculdeToolbar.imBackBtnFilterSetting.setOnClickListener {
                findNavController().popBackStack()
            }
            filterInculdeToolbar.txtDoneFilterSetting.setOnClickListener {
                val data=SettingDataClassForFirebase(lookingGender,looking_Age_Start,looking_Age_End,looking_Age_Flag,distance,distance_Flag)
                HomeViewModel().saveSettingData(requireContext(),data).observe(requireActivity(),
                    Observer {
                        val t = it as Boolean
                        if(t == true){
                            lifecycleScope.launchWhenResumed {
                                if(findNavController().currentDestination!!.id ==  R.id.filterSettingFragment) {
                                    findNavController().popBackStack()
                                }
                            }
                        }
                        else{
                            lifecycleScope.launchWhenResumed {
                                if(findNavController().currentDestination!!.id ==  R.id.filterSettingFragment) {
                                    findNavController().popBackStack()
                                    Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    })
            }
            btnMenFilter.setOnClickListener {
                menselect()

            }
            btnWomenFilter.setOnClickListener {
                womenSelect()
            }
            ageRangeSlider.addOnChangeListener(object : RangeSlider.OnChangeListener{
                override fun onValueChange(slider: RangeSlider, value: Float, fromUser: Boolean) {
                    val value= "${slider.values[0].toInt()}-${slider.values[1].toInt()}"
                    txtFilterAge.text=value
                    looking_Age_Start=slider.values[0].toLong()
                    looking_Age_End=slider.values[1].toLong()
                    MyUtils.putInt(requireContext(),MyUtils.userFilterStartAge,slider.values[0].toInt())
                    MyUtils.putInt(requireContext(),MyUtils.userFilterEndAge,slider.values[1].toInt())
                }
            })
            distanceSlider.addOnChangeListener(object : Slider.OnChangeListener{
                override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
                    distance=slider.value.toLong()
                    MyUtils.putInt(requireContext(),MyUtils.userFilterDistance,distance!!.toInt())
                    Log.i("askjfbdnsad","Save distance is $distance")
                    txtDistanceFilter.text="${slider.value.toInt().toString()}mi"
                }

            })
            switchForDistance.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                    if(p1){
                        switchForDistance.trackDrawable.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.teal_200), PorterDuff.Mode.SRC_IN)
                        distance_Flag=true
                        MyUtils.putBoolean(requireContext(),MyUtils.userFilterDistanceFlag,true)
                    }
                    else{
                        switchForDistance.trackDrawable.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.white), PorterDuff.Mode.SRC_IN)
                        distance_Flag=false
                        MyUtils.putBoolean(requireContext(),MyUtils.userFilterDistanceFlag,false)
                    }
                }

            })
            switchForAge.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                    if(p1){
                        switchForAge.trackDrawable.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.teal_200), PorterDuff.Mode.SRC_IN)
                        looking_Age_Flag=true
                        MyUtils.putBoolean(requireContext(),MyUtils.userFilterAgeFlag,true)

                    }
                    else{
                        switchForAge.trackDrawable.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.white), PorterDuff.Mode.SRC_IN)
                        looking_Age_Flag=false
                        MyUtils.putBoolean(requireContext(),MyUtils.userFilterAgeFlag,false)
                    }
                }

            })

        }
    }
    private fun menselect(){
        binder!!.apply {
            btnMenFilter.setBackgroundDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.fillter_btn_round))
            btnMenFilter.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            btnWomenFilter.setBackgroundDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.fillter_btn_round_two))
            btnWomenFilter.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            lookingGender="Men"
            MyUtils.putString(requireContext(),MyUtils.userLookingForKey,lookingGender!!)
            lookingGender=MyUtils.getString(requireContext(),MyUtils.userLookingForKey)
        }
    }
    private fun womenSelect(){
        binder!!.apply {
            btnWomenFilter.setBackgroundDrawable(ContextCompat.getDrawable(requireActivity(),
                R.drawable.filter_right_btn_unclick))
            btnWomenFilter.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            btnMenFilter.setBackgroundDrawable(ContextCompat.getDrawable(requireActivity(),
                R.drawable.fillter_left_btn_unclick))
            btnMenFilter.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
                lookingGender="Women"
            MyUtils.putString(requireContext(),MyUtils.userLookingForKey,lookingGender!!)
            lookingGender=MyUtils.getString(requireContext(),MyUtils.userLookingForKey)
        }
    }

    override fun onDestroy() {
        lifecycleScope.launchWhenResumed {
            if (findNavController().currentDestination!!.id == R.id.filterSettingFragment) {
                MyUtils.stopProgress()
            }
        }
        super.onDestroy()
    }

}