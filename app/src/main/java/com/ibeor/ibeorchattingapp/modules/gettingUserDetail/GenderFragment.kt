package com.ibeor.ibeorchattingapp.modules.gettingUserDetail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentGenderBinding
import com.ibeor.ibeorchattingapp.databinding.FragmentHomeBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import com.ibeor.ibeorchattingapp.modules.myUtils.setGender


class GenderFragment : Fragment() {
    private var binding:FragmentGenderBinding?=null
    private var selectedGender:String?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGenderBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListner()
    }
    private fun onClickListner(){
        binding!!.apply {
            imBackBtnGender.setOnClickListener {
                findNavController().popBackStack()
            }
            tvMaleSelect.setOnClickListener {
                setMan()
            }
            tvWomanSelect.setOnClickListener {
                setWomen()
            }
            btnGenderContinue.setOnClickListener {
                if(selectedGender !=null){
                    USER_GENDER=selectedGender
                    val data2=HashMap<String,Any>()
                    data2.put("user_gender",selectedGender!!)
                    HomeViewModel().uploadDataToFirebaseRegister2(requireContext(),data2).observe(requireActivity(),
                        androidx.lifecycle.Observer {
                            val flag=it as Boolean
                            if(flag== true){
                                MyUtils.putString(requireContext(),MyUtils.userGenderKey, USER_GENDER!!)
                                lifecycleScope.launchWhenResumed {
                                    if (findNavController().currentDestination!!.id == R.id.genderFragment) {
                                        findNavController().navigate(R.id.action_genderFragment_to_lookingFragment)
                                    }
                                }
                            }
                        })
//                    findNavController().navigate(R.id.action_genderFragment_to_lookingFragment)
                }
            }
        }
    }
    private fun setMan(){
        binding!!.apply {
            tvMaleSelect.setBackgroundDrawable(ContextCompat.getDrawable(requireActivity(),
                R.drawable.choise_select_drawable))
            tvMaleSelect.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            tvWomanSelect.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvWomanSelect.setBackgroundDrawable(ContextCompat.getDrawable(requireActivity(),
                R.drawable.choice_select_one))
            selectedGender = "Men"
        }
    }
    private fun setWomen(){
        binding!!.apply {
            tvWomanSelect.setBackgroundDrawable(ContextCompat.getDrawable(requireActivity(),
                R.drawable.choise_select_drawable))
            tvWomanSelect.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            tvMaleSelect.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            tvMaleSelect.setBackgroundDrawable(ContextCompat.getDrawable(requireActivity(),
                R.drawable.choice_select_one))
            selectedGender = "Women"
        }
    }
    companion object{
    var USER_GENDER:String?=null
    }

}