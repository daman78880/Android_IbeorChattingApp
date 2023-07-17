package com.ibeor.ibeorchattingapp.modules.signIn.signOtp

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentSignOtpBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import com.ibeor.ibeorchattingapp.modules.userData.FeatchFireBaseRegisterData
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignOtpFragment : Fragment(), View.OnClickListener {
    private lateinit var fragmentSignOtpBinding:FragmentSignOtpBinding
    private lateinit var auth: FirebaseAuth
    private var callbackss: PhoneAuthProvider.OnVerificationStateChangedCallbacks?=null
    private var verificationId: String? = null
    private var resendToken : PhoneAuthProvider.ForceResendingToken?=null
    var cTimer: CountDownTimer?=null
    var storedVerificationId:String?=null
    var mobileNumber=""
    var uid:String?=null
    private var userUid:String?=null
    private var viewModel:HomeViewModel?=null
    private var userList:ArrayList<String>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
         fragmentSignOtpBinding=FragmentSignOtpBinding.inflate(layoutInflater)
        auth= FirebaseAuth.getInstance()
//        uid= auth.currentUser!!.uid
        viewModel= HomeViewModel()
        return fragmentSignOtpBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storedVerificationId= arguments?.getString("id")
        mobileNumber= arguments?.getString("mobileNumber").toString()
        // otp libaray link https://github.com/aabhasr1/OtpView
        setTimeSecond()
        fragmentSignOtpBinding.apply {
            tvResend.setOnClickListener {
                setTimeSecond()
                setVerificationCode()
            }

//            tvSignSubLineTime.text
            imBackBtnSignOtp.setOnClickListener(this@SignOtpFragment)
            btnOtpContinue.setOnClickListener {
            if(otpView2.otp?.length ==  6 && otpView2.otp != null){
//                otpView2.showSuccess()
//                otpView2.resetState()
//                cTimer?.cancel()
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(storedVerificationId.toString(), otpView2.otp!!)
                signInWithPhoneAuthCredential(credential)
            }
            else{
                Toast.makeText(requireContext(), "Please enter otp correct", Toast.LENGTH_SHORT).show()
                    otpView2.requestFocusOTP()
                    otpView2.showError()
            }
            }
        }
    }
    private fun setTimeSecond(){
        cTimer = object : CountDownTimer(45000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val timeS=(millisUntilFinished/1000).toString()
                var str:String?=null
              if(timeS.length == 1){
                  str="00:0${millisUntilFinished/1000}"
              }
                else{
                  str="00:${millisUntilFinished/1000}"
              }
                fragmentSignOtpBinding.tvSignSubLineTime.text=str
                fragmentSignOtpBinding.tvResend.isClickable=false
                fragmentSignOtpBinding.tvResend.setTextColor(requireContext().resources.getColor(R.color.blackNotClick))
            }
            override fun onFinish() {
                fragmentSignOtpBinding.tvSignSubLineTime.text="00:00"
                fragmentSignOtpBinding.tvResend.isClickable=true
                fragmentSignOtpBinding.tvResend.setTextColor(requireContext().resources.getColor(R.color.black, resources.newTheme()))
            }
        }
        cTimer!!.start()
    }
    override fun onDestroyView() {
        if(cTimer!=null){
            cTimer?.cancel()
        }
        super.onDestroyView()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        MyUtils.showProgress(requireContext())
        fragmentSignOtpBinding.btnOtpContinue.isClickable=false
        auth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val uid=auth.currentUser!!.uid
                    MyUtils.putString(requireContext(),MyUtils.userUidKey,uid)
                    cTimer?.cancel()
                    MyUtils.putString(requireContext(),MyUtils.userUidKey,uid)
                    navigateAuto()
//                    if(findNavController().currentDestination!!.id == R.id.signOtpFragment) {
//                        MyUtils.stopProgress()
//                        findNavController().navigate(R.id.action_signOtpFragment_to_signOtpFragment)
//                    }
                }
                else {
                    Toast.makeText(requireContext(),"Please enter valis Otp",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setVerificationCode(){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(mobileNumber)
            .setTimeout(40L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbackss!!)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.imBackBtnSignOtp->
            {
               findNavController().popBackStack()
            }
        }
    }
    private fun navigateAuto(){
        userList=ArrayList<String>()
        userUid=viewModel!!.getUserId()
//        MyUtils.showProgress(requireContext())
        Log.e("insidenavegateup","inside navigateup")
        HomeViewModel().getUserIdList(requireContext()).observe(requireActivity(), Observer {
            userList=it as ArrayList<String>
            Log.e("insidenavegateup","inside navigateup function")
            if(userList!!.contains(userUid)){
                Log.e("insidenavegateup","inside navigateup function contain")
                viewModel!!.getCheckStatusTwo(requireContext(), userUid!!).observe(requireActivity(),
                    Observer {
                        val data=it as FeatchFireBaseRegisterData
                        val checkStatus=data.user_completeStatus

                        if(checkStatus.equals("4")  ){
                            saveLocation(requireContext(), data.user_mobileNumber!!,data.user_mobileNumber_code!!,data.user_email!!,data.user_name!!,data.user_gender!!,data.user_looking_gender!!,data.user_age!!,data.user_full_Age!!,data.selected_first_root!!,data.selected_second_root!!,data.image_url_list!!,data.user_latitute!!,data.user_longitute!!)
                            if(findNavController().currentDestination!!.id == R.id.signOtpFragment) {
                                MyUtils.stopProgress()
                                findNavController().navigate(R.id.action_signOtpFragment_to_homeFragment2)
                            }
                        }
                        else if(checkStatus.equals("3")){
                            saveImage(requireContext(), data.user_mobileNumber!!,data.user_mobileNumber_code!!,data.user_email!!,data.user_name!!,data.user_gender!!,data.user_looking_gender!!,data.user_age!!,data.user_full_Age!!,data.selected_first_root!!,data.selected_second_root!!,data.image_url_list!!)
                            if(findNavController().currentDestination!!.id == R.id.signOtpFragment) {
                                MyUtils.stopProgress()
                                findNavController().navigate(R.id.action_signOtpFragment_to_gettingLocationFragment)
                            }
                        }
                        else if(checkStatus.equals("2")) {
                            saveDob(requireContext(), data.user_mobileNumber!!,data.user_mobileNumber_code!!,data.user_email!!,data.user_name!!,data.user_gender!!,data.user_looking_gender!!,data.user_age!!,data.user_full_Age!!)
                            if (findNavController().currentDestination!!.id == R.id.signOtpFragment) {
                                MyUtils.stopProgress()
                                findNavController().navigate(R.id.action_signOtpFragment_to_selectCountryFragment)
                            }
                        }
                        else if(checkStatus.equals("1") ) {
                            saveEmail(requireContext(), data.user_mobileNumber!!,data.user_mobileNumber_code!!,data.user_email!!)
                            if (findNavController().currentDestination!!.id == R.id.signOtpFragment) {
                                MyUtils.stopProgress()
                                findNavController().navigate(R.id.action_signOtpFragment_to_registerNameFragment)
                            }
                        }
                    })
            }
            else{
                if(findNavController().currentDestination!!.id == R.id.signOtpFragment) {
                    MyUtils.stopProgress()
                    findNavController().navigate(R.id.action_signOtpFragment_to_registerMailFragment)
                }
            }
        })
    }
    private fun saveEmail(context: Context, number:String, numCode:String, mail:String){
        MyUtils.putString(context,MyUtils.userEmailKey,mail)
        MyUtils.putString(context,MyUtils.userMobileNumberKey,number)
        MyUtils.putString(context,MyUtils.userMobileNumberCodeKey,numCode)
    }
    private fun saveDob(context: Context, number:String, numCode:String, mail:String, name:String,
                        gender:String, lookingGender:String, age:String, fullAge:String){
        saveEmail(context,number,numCode,mail)
        MyUtils.putString(context,MyUtils.userNameKey,name)
        MyUtils.putString(context,MyUtils.userGenderKey,gender)
        MyUtils.putString(context,MyUtils.userLookingForKey,lookingGender)
        MyUtils.putString(context,MyUtils.userAgeKey,age)
        MyUtils.putString(context,MyUtils.userFullAgeKey,fullAge)
    }
    private fun saveImage(context: Context, number:String, numCode:String, mail:String, name:String,
                          gender:String, lookingGender:String, age:String, fullAge:String, firstCountryRoot:String, secondCountryRoot:String, imageList:ArrayList<String>){
        saveDob(context,number,numCode,mail,name,gender,lookingGender,age,fullAge)
        MyUtils.putString(context,MyUtils.userSelectedCountryKeyOne,firstCountryRoot)
        MyUtils.putString(context,MyUtils.userSelectedCountryKeyTwo,secondCountryRoot)
        MyUtils.putArray(context,MyUtils.userArrayImageLinkKey,imageList)
    }

    private fun saveLocation(context: Context, number:String, numCode:String, mail:String, name:String,
                             gender:String, lookingGender:String, age:String, fullAge:String, firstCountryRoot:String, secondCountryRoot:String, imageList:ArrayList<String>, lat:String, longi:String)
    {
        saveImage(context, number, numCode, mail, name, gender, lookingGender, age, fullAge, firstCountryRoot, secondCountryRoot, imageList)
        MyUtils.putString(context,MyUtils.userLotiKey,lat)
        MyUtils.putString(context,MyUtils.userLongiKey,longi)
    }
}