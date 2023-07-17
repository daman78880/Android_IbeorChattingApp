package com.ibeor.ibeorchattingapp.modules.registerMail

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentRegisterNameBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils

class RegisterNameFragment : Fragment() {
    private var fragmentRegisterNameFragment:FragmentRegisterNameBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentRegisterNameFragment=FragmentRegisterNameBinding.inflate(layoutInflater)
        return fragmentRegisterNameFragment!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListener()
    }
    private fun onClickListener(){
        fragmentRegisterNameFragment!!.apply {
            imBackBtnRegisterNameN.setOnClickListener {
                findNavController().popBackStack()
            }

            btnRegisterName.setOnClickListener {
                if(!TextUtils.isEmpty(etEnterName.text.toString())){

                    val data2=HashMap<String,Any>()
                    data2.put("user_name",etEnterName.text.toString())
                    HomeViewModel().uploadDataToFirebaseRegister2(requireContext(),data2).observe(requireActivity(),
                        androidx.lifecycle.Observer {
                            val flag=it as Boolean
                            if(flag){
                              MyUtils.putString(requireContext(),MyUtils.userNameKey,etEnterName.text.toString())
                                if(findNavController().currentDestination!!.id == R.id.registerNameFragment) {
                                    findNavController().navigate(R.id.action_registerNameFragment_to_genderFragment)
                                }
                            }
                        })
                }
                else{
                    etEnterName.error="Please enter your name"
                }
            }

            }
        }
}