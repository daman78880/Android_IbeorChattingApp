package com.ibeor.ibeorchattingapp.modules.signIn

import android.app.Dialog
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentSignInBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import java.util.concurrent.TimeUnit


class SignInFragment : Fragment() {
    private var binding:FragmentSignInBinding?=null
    private var mAuth: FirebaseAuth? = null
    private var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks?=null
    private var verificationId: String? = null
    private var resendToken :PhoneAuthProvider.ForceResendingToken?=null
    var mobileNumber=""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding=FragmentSignInBinding.inflate(layoutInflater)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun filterEdittext(){
        binding!!.etSignEnterdMobileNumber.filters=arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            if (source.isNotEmpty()) {
                if (!Character.isDigit(source[0])) return@InputFilter "" else {
                    if (dstart == 0) {
                        return@InputFilter "$source-"
                    }
                    else if (dstart == 6) {
                        return@InputFilter "$source-"
                    }
                    else if (dstart > 11) return@InputFilter ""
                }
            }
            null
        })
    }
    private fun init(){

        mAuth = FirebaseAuth.getInstance()
        Log.i("asfdnkasnf","user uid is ${mAuth}")
        filterEdittext()
        callBacks()
//        findNavController().navigate(R.id.action_signInFragment_to_signOtpFragment)
        binding!!.apply {
            backBtnSign.setOnClickListener {
                findNavController().popBackStack()
            }
            val countryCode:String?=countryCodePicker.selectedCountryCode?:null
            signInContinueBtn.setOnClickListener {
                Log.i("testing","CLick")
                if(etSignEnterdMobileNumber.text.toString().count() == 12){
                    val num=etSignEnterdMobileNumber.text.toString()

                    for(i in 0.until(num.length)){
                        if(num[i] != '-'){
                            mobileNumber += num[i]
                        }
                    }
                    if(mobileNumber.length == 10){
                        Log.i("testing","inside CLick 10")
                        MyUtils.putString(requireContext(),MyUtils.userMobileNumberCodeKey,countryCode.toString())
                        MyUtils.putString(requireContext(),MyUtils.userMobileNumberKey,mobileNumber)
                        val num2= "+$countryCode$mobileNumber"
                        MyUtils.showProgress(requireContext())
                        Log.i("asfdbasjfbd","Enter number is ->${num2}")
                        setVerificationCode(num2)
                    }
                }
                else{
                    val dialog=Dialog(requireContext())
                    dialog.setContentView(R.layout.invalid_number_layout)
                    dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    val btn=dialog.findViewById<AppCompatButton>(R.id.okheBtnDialog)
                    btn.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()
                }
            }
        }

    }
    private fun navigateToOtpFragment(bundle: Bundle?) {
        MyUtils.stopProgress()
        if(bundle != null){
            findNavController().navigate(R.id.action_signInFragment_to_signOtpFragment,bundle)
        }
        else {
            findNavController().navigate(R.id.action_signInFragment_to_signOtpFragment)
        }
    }
    private fun setVerificationCode(number:String){
        Log.i("testing","inside Start verification")
        val options = PhoneAuthOptions.newBuilder(mAuth!!)
            .setPhoneNumber(number)
            .setTimeout(50L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks!!)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun callBacks(){
         callbacks=(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Log.i("testingNow","With Verification Complete")
                navigateToOtpFragment(null)
            }
            override fun onVerificationFailed(p0: FirebaseException) {
                Log.i("testingNow","Level 2 failed ${p0.message}")
            }

             override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                 Log.i("testingNow","Level 2 oncodeSend")
                 verificationId = p0
                 resendToken=p1
                 val bundle=Bundle()
                 bundle.putString("id",verificationId!!)
                 bundle.putString("mobileNumber",mobileNumber)
                 navigateToOtpFragment(bundle)
             }
        })
    }
}