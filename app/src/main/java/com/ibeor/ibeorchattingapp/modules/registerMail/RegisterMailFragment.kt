package com.ibeor.ibeorchattingapp.modules.registerMail

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentRegisterMailBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import com.ibeor.ibeorchattingapp.modules.userData.FeatchFireBaseRegisterData
import java.util.regex.Matcher
import java.util.regex.Pattern


class RegisterMailFragment : Fragment() {
    private var fragmentRegisterMailFragment:FragmentRegisterMailBinding?=null
    private var f:Boolean=false
    private var viewModel:HomeViewModel?=null
//    private var userList=ArrayList<String>()
    private var userUid:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentRegisterMailFragment=FragmentRegisterMailBinding.inflate(layoutInflater)
        return fragmentRegisterMailFragment!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=HomeViewModel()
        clickListener()
    }

    private fun clickListener(){
        fragmentRegisterMailFragment!!.apply {
            userUid=viewModel!!.getUserId()
            MyUtils.showProgress(requireContext())
//            HomeViewModel().getUserIdList(requireContext()).observe(requireActivity(), Observer {
//                userList=it as ArrayList<String>
//                if(userList.contains(userUid)){
//                    viewModel!!.getCheckStatusTwo(requireContext(), userUid).observe(requireActivity(),
//                        Observer {
//                            val data=it as FeatchFireBaseRegisterData
//                            val checkStatus=data.user_completeStatus
//
//                            if(checkStatus.equals("4")  ){
//                                saveLocation(requireContext(), data.user_mobileNumber!!,data.user_mobileNumber_code!!,data.user_email!!,data.user_name!!,data.user_gender!!,data.user_looking_gender!!,data.user_age!!,data.user_full_Age!!,data.selected_first_root!!,data.selected_second_root!!,data.image_url_list!!,data.user_latitute!!,data.user_longitute!!)
//
//                                if(findNavController().currentDestination!!.id == R.id.registerMailFragment) {
//                                    MyUtils.stopProgress()
//                                    findNavController().navigate(R.id.action_registerMailFragment_to_homeFragment22)
//                                }
//                            }
//                            else if(checkStatus.equals("3")){
//                                saveImage(requireContext(), data.user_mobileNumber!!,data.user_mobileNumber_code!!,data.user_email!!,data.user_name!!,data.user_gender!!,data.user_looking_gender!!,data.user_age!!,data.user_full_Age!!,data.selected_first_root!!,data.selected_second_root!!,data.image_url_list!!)
//                                if(findNavController().currentDestination!!.id == R.id.registerMailFragment) {
//                                    MyUtils.stopProgress()
//                                    findNavController().navigate(R.id.action_registerMailFragment_to_gettingLocationFragment2)
//                                }
//                            }
//                            else if(checkStatus.equals("2")) {
//                                saveDob(requireContext(), data.user_mobileNumber!!,data.user_mobileNumber_code!!,data.user_email!!,data.user_name!!,data.user_gender!!,data.user_looking_gender!!,data.user_age!!,data.user_full_Age!!)
//                                if (findNavController().currentDestination!!.id == R.id.registerMailFragment) {
//                                    MyUtils.stopProgress()
//                                    findNavController().navigate(R.id.action_registerMailFragment_to_selectCountryFragment)
//                                }
//                            }
//                          else if(checkStatus.equals("1") ) {
//                                saveEmail(requireContext(), data.user_mobileNumber!!,data.user_mobileNumber_code!!,data.user_email!!)
//                                if (findNavController().currentDestination!!.id == R.id.registerMailFragment) {
//                                    MyUtils.stopProgress()
//                                    findNavController().navigate(R.id.action_registerMailFragment_to_registerNameFragment)
//                                }
//                            }
//                        })
//                }
//                else{
//                    MyUtils.stopProgress()
//                }
//            })
            imBackBtnRegisterMailR.setOnClickListener {
                findNavController().popBackStack()
            }
            signInContinueBtn.setOnClickListener {
                if (!TextUtils.isEmpty(etEnterEmailR.text.toString())){
                    f=emailValidator(etEnterEmailR.text.toString())
                    if(f){
                        val uid=viewModel!!.getUserId()
                        val data2=HashMap<String,Any>()
                        data2.put("user_email",etEnterEmailR.text.toString())
                        data2.put("user_mobileNumber",MyUtils.getString(requireContext(),MyUtils.userMobileNumberKey).toString())
                        data2.put("user_mobileNumber_code",MyUtils.getString(requireContext(),MyUtils.userMobileNumberCodeKey).toString())
                        data2.put("user_uid",uid)
                        data2.put("user_completeStatus","1")
                        viewModel!!.uploadDataToFirebaseRegister2(requireContext(),data2).observe(requireActivity(),
                            androidx.lifecycle.Observer {
                                val flag=it as Boolean
                                if(flag== true){
                                    MyUtils.stopProgress()
//                                    etEnterEmailR.text!!.clear()
                                    MyUtils.putString(requireContext(),MyUtils.userUidKey,uid)
                                    MyUtils.putString(requireContext(),MyUtils.userEmailKey,etEnterEmailR.text.toString())
                                    if(findNavController().currentDestination!!.id == R.id.registerMailFragment) {
                                        findNavController().navigate(R.id.action_registerMailFragment_to_registerNameFragment)
                                    }
                                }
                            })
                    }
                    else{
                        etEnterEmailR.text!!.clear()
                        etEnterEmailR.error="Please enter valid email address"
                    }
                }
                else{
                    etEnterEmailR.error="Please enter Email"
                }
            }
        }
    }
    fun emailValidator(email: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }
    private fun saveEmail(context: Context,number:String,numCode:String,mail:String){
        MyUtils.putString(context,MyUtils.userEmailKey,mail)
        MyUtils.putString(context,MyUtils.userMobileNumberKey,number)
        MyUtils.putString(context,MyUtils.userMobileNumberCodeKey,numCode)
    }
    private fun saveDob(context:Context,number:String,numCode:String,mail:String,name:String,
                        gender:String,lookingGender:String,age:String,fullAge:String){
        saveEmail(context,number,numCode,mail)
        MyUtils.putString(context,MyUtils.userNameKey,name)
        MyUtils.putString(context,MyUtils.userGenderKey,gender)
        MyUtils.putString(context,MyUtils.userLookingForKey,lookingGender)
        MyUtils.putString(context,MyUtils.userAgeKey,age)
        MyUtils.putString(context,MyUtils.userFullAgeKey,fullAge)
    }
    private fun saveImage(context:Context,number:String,numCode:String,mail:String,name:String,
                          gender:String,lookingGender:String,age:String,fullAge:String,firstCountryRoot:String,secondCountryRoot:String,imageList:ArrayList<String>){
        saveDob(context,number,numCode,mail,name,gender,lookingGender,age,fullAge)
        MyUtils.putString(context,MyUtils.userSelectedCountryKeyOne,firstCountryRoot)
        MyUtils.putString(context,MyUtils.userSelectedCountryKeyTwo,secondCountryRoot)
        MyUtils.putArray(context,MyUtils.userArrayImageLinkKey,imageList)
    }

    private fun saveLocation(context:Context,number:String,numCode:String,mail:String,name:String,
                             gender:String,lookingGender:String,age:String,fullAge:String,firstCountryRoot:String,secondCountryRoot:String,imageList:ArrayList<String>,lat:String,longi:String)
    {
        saveImage(context, number, numCode, mail, name, gender, lookingGender, age, fullAge, firstCountryRoot, secondCountryRoot, imageList)
        MyUtils.putString(context,MyUtils.userLotiKey,lat)
        MyUtils.putString(context,MyUtils.userLongiKey,longi)
    }

}