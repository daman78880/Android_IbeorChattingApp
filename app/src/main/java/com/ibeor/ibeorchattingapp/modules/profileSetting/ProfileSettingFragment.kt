package com.ibeor.ibeorchattingapp.modules.profileSetting

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentProfileSettingBinding
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import kotlinx.coroutines.flow.combine


class ProfileSettingFragment : Fragment() {
    private var binding:FragmentProfileSettingBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentProfileSettingBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        onClickListeners()
    }
    private fun init(){
        binding!!.apply {
            toolbarProfileSetting.txtTitleFilterSetting.text="Settings"
            toolbarProfileSetting.txtDoneFilterSetting.setTextColor(Color.parseColor("#eb5f7f"))
            txtMobileNumberSetting.text=MyUtils.getString(requireContext(),MyUtils.userMobileNumberKey)
            txtEmailSetting.text=MyUtils.getString(requireContext(),MyUtils.userEmailKey)
        }
    }
    private fun onClickListeners(){
        binding!!.apply {
            phoneNumberLayoutSetting.setOnClickListener {
                findNavController().navigate(R.id.action_profileSettingFragment_to_updateMobileNumberFragment)
            }
            loginLayoutSetting.setOnClickListener {
                findNavController().navigate(R.id.action_profileSettingFragment_to_loginMethodFragment)
            }
            mailLayoutSetting.setOnClickListener {
                findNavController().navigate(R.id.action_profileSettingFragment_to_updateMailFragment)
            }
            mailNotificationLayout.setOnClickListener {
                val bundle=Bundle()
                bundle.putInt("checkType",1)
                findNavController().navigate(R.id.action_profileSettingFragment_to_mailNotificationSettingFragment,bundle)
            }
            btnLogoutProfileSetting.setOnClickListener {
//                var b=MyUtils.getBoolean(requireContext(),MyUtils.userLoginKey)
//                MyUtils.setAllThingString(requireContext())
//                val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("userData", Context.MODE_PRIVATE)
//                sharedPreferences.edit().clear()
//                MyUtils.putBoolean(requireContext(),MyUtils.userLoginKey,false)
                MyUtils.clearPreference(requireActivity())
                findNavController().navigate(R.id.action_profileSettingFragment_to_welcomeFragment)
            }
            puchNotificationLayout.setOnClickListener {
                val bundle=Bundle()
                bundle.putInt("checkType",2)
                findNavController().navigate(R.id.action_profileSettingFragment_to_mailNotificationSettingFragment,bundle)
            }
            toolbarProfileSetting.imBackBtnFilterSetting.setOnClickListener {
                activity?.onBackPressed()
            }

            switchForAds.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                    if(p1){
                        switchForAds.trackDrawable.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.teal_200), PorterDuff.Mode.SRC_IN)
                    }
                    else{
                        switchForAds.trackDrawable.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.white), PorterDuff.Mode.SRC_IN)
                    }
                }

            })
        }
    }



}