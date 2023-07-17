package com.ibeor.ibeorchattingapp.modules.userProfile

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.core.GoalRow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentHomeBinding
import com.ibeor.ibeorchattingapp.databinding.FragmentProfileBinding
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils


class ProfileFragment : Fragment() {
        private var binding:FragmentProfileBinding?=null
    private var progValue=1
    var progressStatus:String?=null
    var uri:ArrayList<String>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
       binding=FragmentProfileBinding.inflate(layoutInflater)
        uri=ArrayList()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        clickListeners()
    }
private fun clickListeners(){
    binding!!.apply {
        profileToolbar.txtDoneFilterSetting.visibility=View.GONE
        profileToolbar.txtTitleFilterSetting.text="My Profile"
        profileToolbar.imBackBtnFilterSetting.setOnClickListener {
            activity?.onBackPressed()
        }
        imSettingProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_profileSettingFragment)
        }
        imEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
        imUserImageUpdate.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
    }
}

//    override fun onStart() {
//        super.onStart()
//        lifecycleScope.launchWhenResumed {
//            if(findNavController().currentDestination!!.id == R.id.profileFragment) {
//                progressStatus=MyUtils.getString(requireContext(),MyUtils.userStageGoing)
//                uri=MyUtils.getArray(requireContext(),MyUtils.userArrayImageLinkKey)
//                if(progressStatus.equals("4")){
//
//                    startProgress(80)
//                }
//                else if(progressStatus.equals("3")){
//                    startProgress(60)
//                }
//                else if(progressStatus.equals("2")){
//                    startProgress(40)
//                }
//                else if(progressStatus.equals("1")){
//                    startProgress(20)
//                }
//                else{
//                    startProgress(10)
//                }
//                Glide.with(requireContext()).load(uri?.get(0)).into(binding!!.imUserImageUpdate)
//            }}}

//    override fun onResume() {
//        super.onResume()
//        lifecycleScope.launchWhenResumed {
//            if(findNavController().currentDestination!!.id == R.id.profileFragment) {
//        progressStatus=MyUtils.getString(requireContext(),MyUtils.userStageGoing)
//        uri=MyUtils.getArray(requireContext(),MyUtils.userArrayImageLinkKey)
//        if(progressStatus.equals("4")){
//            startProgress(80)
//        }
//        else if(progressStatus.equals("3")){
//            startProgress(60)
//        }
//        else if(progressStatus.equals("2")){
//            startProgress(40)
//        }
//        else if(progressStatus.equals("1")){
//            startProgress(20)
//        }
//        else{
//            startProgress(10)
//        }
//        Glide.with(requireContext()).load(uri?.get(0)).into(binding!!.imUserImageUpdate)
//    }}}
    private fun init(){
        binding!!.apply {

//             progressStatus=MyUtils.getString(requireContext(),MyUtils.userStageGoing)
//             uri=MyUtils.getArray(requireContext(),MyUtils.userArrayImageLinkKey)
//            Log.i("asdfasf","uri is ${uri?.get(0)}")
//            Glide.with(requireContext()).load(uri?.get(0)).into(imUserImageUpdate)
//            if(progressStatus.equals("4")){
//                startProgress(80)
//            }
//            else if(progressStatus.equals("3")){
//                startProgress(60)
//            }
//            else if(progressStatus.equals("2")){
//                    startProgress(40)
//            }
//            else if(progressStatus.equals("1")){
//                    startProgress(20)
//            }
//            else{
//                    startProgress(10)
//            }

            lifecycleScope.launchWhenResumed {
                if(findNavController().currentDestination!!.id == R.id.profileFragment) {
                    imUserImageUpdate.clipToOutline=true
                    progressStatus=MyUtils.getString(requireContext(),MyUtils.userStageGoing)
                    Log.i("usersjkafdns","userprogres status is  ${progressStatus}")
                    uri=MyUtils.getArray(requireContext(),MyUtils.userArrayImageLinkKey)
                    if(progressStatus.equals("4")){

                        startProgress(80)
                    }
                    else if(progressStatus.equals("3")){
                        startProgress(60)
                    }
                    else if(progressStatus.equals("2")){
                        startProgress(40)
                    }
                    else if(progressStatus.equals("1")){
                        startProgress(20)
                    }
                    else{
                        startProgress(10)
                    }
                    Glide.with(requireContext()).load(uri?.get(0)).into(binding!!.imUserImageUpdate)
                }}
            val userName=MyUtils.getString(requireContext(),MyUtils.userNameKey)
            txtUserNameProfile.text=userName
        }
    }
    private fun startProgress(value:Int){
        binding!!.apply {
            Handler().postDelayed(object :Runnable{
                override fun run() {
                    if(progValue<=value){
                        imUserProgressBarUpdate.progress=progValue
                        txtProfilePercentageUpdate.text="$progValue %"
                        progValue++
                        startProgress(value)
                    }
                }
            },50)
        }
    }

}