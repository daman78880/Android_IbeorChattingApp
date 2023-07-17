package com.ibeor.ibeorchattingapp.modules.gettingUserDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentLookingBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils

class LookingFragment : Fragment() {
    private var binding:FragmentLookingBinding?=null
    private var selectedSeekingFor:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding=FragmentLookingBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        onClickListner()
    }
    private fun onClickListner(){
        binding!!.apply {
            imBackBtnSeeking.setOnClickListener {
                findNavController().popBackStack()
            }
            tvMaleSelectS.setOnClickListener {
                setMan()
            }
            tvWomanSelectS.setOnClickListener {
                setWomen()
            }
            btnSeekingContinue.setOnClickListener {
                if(selectedSeekingFor !=null){
                    val data2=HashMap<String,Any>()
                    data2.put("user_looking_gender",selectedSeekingFor!!)
                    HomeViewModel().uploadDataToFirebaseRegister2(requireContext(),data2).observe(requireActivity(),
                        androidx.lifecycle.Observer {
                            val flag=it as Boolean
                            if(flag){
                                if(findNavController().currentDestination!!.id == R.id.lookingFragment) {
                                    MyUtils.putString(requireContext(), MyUtils.userLookingForKey, selectedSeekingFor!!)
                                    findNavController().navigate(R.id.action_lookingFragment_to_DOBFragment)
                                }
                            }
                        })
                }
            }
        }
    }
    private fun setMan(){
        binding!!.apply {
            tvMaleSelectS.setBackgroundDrawable(ContextCompat.getDrawable(requireActivity(),
                R.drawable.choise_select_drawable))
            tvMaleSelectS.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            tvWomanSelectS.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvWomanSelectS.setBackgroundDrawable(ContextCompat.getDrawable(requireActivity(),
                R.drawable.choice_select_one))
            selectedSeekingFor = "Men"
        }
    }
    private fun setWomen(){
        binding!!.apply {
            tvWomanSelectS.setBackgroundDrawable(ContextCompat.getDrawable(requireActivity(),
                R.drawable.choise_select_drawable))
            tvWomanSelectS.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            tvMaleSelectS.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvMaleSelectS.setBackgroundDrawable(ContextCompat.getDrawable(requireActivity(),
                R.drawable.choice_select_one))
            selectedSeekingFor = "Women"
        }
    }
    private fun init(){
        binding!!.apply {
            val gen=MyUtils.getString(requireContext(),MyUtils.userGenderKey)
            if(gen == "Men"){
                setWomen()
            }
            else{
                setMan()
            }
        }
    }

}