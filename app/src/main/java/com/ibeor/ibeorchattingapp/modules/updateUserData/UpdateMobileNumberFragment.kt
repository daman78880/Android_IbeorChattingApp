package com.ibeor.ibeorchattingapp.modules.updateUserData

import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentGenderBinding
import com.ibeor.ibeorchattingapp.databinding.FragmentUpdateMobileNumberBinding
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils


class UpdateMobileNumberFragment : Fragment() {
    var binding:FragmentUpdateMobileNumberBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentUpdateMobileNumberBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        clicklistners()
    }
    private fun clicklistners(){
        binding!!.apply {
            toolbarUpdateMobileNumber.imBackBtnFilterSetting.setOnClickListener {
                activity?.onBackPressed()
            }
            btnUpdateNumber.setOnClickListener {
                if(!etMobileNumber.equals("") && etMobileNumber.text?.length == 10){
                    Toast.makeText(requireContext(), "Updated number Testing process", Toast.LENGTH_SHORT).show()
                    etMobileNumber.text?.clear()
                }
                else{
                    Toast.makeText(requireContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun init(){
        binding!!.apply {
//            toolbarUpdateMobileNumber.defaultToolbar.setBackgroundColor(requireActivity().getColor(R.color.backgroundLikeColorTwo))
            toolbarUpdateMobileNumber.txtDoneFilterSetting.visibility=View.GONE
            toolbarUpdateMobileNumber.txtTitleFilterSetting.text="Phone Number"
            etMobileNumber.filters=arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
                if (source.isNotEmpty()) {
                    if (!Character.isDigit(source[0])) return@InputFilter "" else {
                        if (dstart == 2) {
                            return@InputFilter "$source-"
                        } else if (dstart == 6) {
                            return@InputFilter "$source-"
                        } else if (dstart > 11) return@InputFilter ""
                    }
                }
                null
            })

            txtUserNumber.text=MyUtils.getString(requireContext(),MyUtils.userMobileNumberKey )
        }
    }
}