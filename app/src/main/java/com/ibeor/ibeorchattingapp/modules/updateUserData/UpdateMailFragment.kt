package com.ibeor.ibeorchattingapp.modules.updateUserData

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentUpdateMailBinding
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils


class UpdateMailFragment : Fragment() {
    private var binding:FragmentUpdateMailBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentUpdateMailBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        clickListeners()
    }
    private fun init(){
        binding!!.apply {
            toolbarUpdateMail.imBackBtnFilterSetting.setOnClickListener { activity?.onBackPressed() }
            toolbarUpdateMail.txtDoneFilterSetting.visibility=View.GONE
            toolbarUpdateMail.txtTitleFilterSetting.text="Email"
            val mail=MyUtils.getString(requireContext(),MyUtils.userEmailKey)
            txtUserMail.text=mail
//            Toast.makeText(requireContext(), mail, Toast.LENGTH_SHORT).show()
        }
    }
    private fun clickListeners(){
        binding!!.apply {
            btnUpdateMail.setOnClickListener{
                if(!etEnterEmailR.equals("")){
                    etEnterEmailR.text?.clear()
                    Toast.makeText(requireContext(), "Update Testing process", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(requireContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}