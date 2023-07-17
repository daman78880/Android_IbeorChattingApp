package com.ibeor.ibeorchattingapp.modules.loginMethod

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentLoginMethodBinding
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils


class LoginMethodFragment : Fragment() {
    private var binding:FragmentLoginMethodBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentLoginMethodBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        clickListeners()

    }
    private fun init(){
        binding!!.apply {
        toolbarLoginMethods.imBackBtnFilterSetting.setOnClickListener { activity?.onBackPressed() }
        toolbarLoginMethods.txtDoneFilterSetting.visibility=View.GONE
        toolbarLoginMethods.txtTitleFilterSetting.text="Login Methods"
            val num=MyUtils.getString(requireContext(),MyUtils.userMobileNumberKey)
            var lastNum:String=""
            if(num?.length == 10) {
                for (i in 0.until(num!!.length)) {
                    if(i >=6){
                        lastNum+=num[i]
                    }
                }
            }
            txtHalfNumber.text=lastNum
        }
    }
    private fun clickListeners(){
        binding!!.apply {
            faceBookSwitch.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                    if(p1){
                        Toast.makeText(requireContext(), "On", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(), "off", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            googleSwitch.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                    if(p1){
                        Toast.makeText(requireContext(), "On", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(), "off", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        }
    }

}