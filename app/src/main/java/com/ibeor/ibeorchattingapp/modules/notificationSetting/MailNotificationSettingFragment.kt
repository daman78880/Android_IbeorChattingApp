package com.ibeor.ibeorchattingapp.modules.notificationSetting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentMailNotificationSettingBinding


class MailNotificationSettingFragment : Fragment() {
    private var binding:FragmentMailNotificationSettingBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentMailNotificationSettingBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        clickListner()
    }
    private fun init(){
        binding!!.apply {
            val checkValue=arguments?.getInt("checkType")
            if(checkValue == 1) {
                toolbarMailNotificationSetting.imBackBtnFilterSetting.setOnClickListener { activity?.onBackPressed() }
                toolbarMailNotificationSetting.txtTitleFilterSetting.text = "Email Notifications"
                toolbarMailNotificationSetting.txtDoneFilterSetting.visibility = View.GONE
            }
           else{
                toolbarMailNotificationSetting.imBackBtnFilterSetting.setOnClickListener { activity?.onBackPressed() }
                toolbarMailNotificationSetting.txtTitleFilterSetting.text = "Push Notifications"
                toolbarMailNotificationSetting.txtDoneFilterSetting.visibility = View.GONE
            }
        }
    }
    private fun clickListner(){
        binding!!.apply {

        }

    }

}