package com.ibeor.ibeorchattingapp.modules.previewImage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.giphy.sdk.ui.Giphy
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentPreviewImageVerticalBinding
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import kotlinx.coroutines.flow.combine

class PreviewImageVerticalFragment : Fragment() {
    private var binding:FragmentPreviewImageVerticalBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding=FragmentPreviewImageVerticalBinding.inflate(layoutInflater)
        Giphy.configure(requireContext(), "FKfvf2pRp0pcvQf9mX8lDFMhpyVERc42")
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        onClickListner()
    }
    private fun init(){
        binding!!.apply {
            txtUserNamePreviewImage.text=MyUtils.getString(requireContext(),MyUtils.userNameKey)
            txtUserAgePreviewImage.text=",${MyUtils.getString(requireContext(),MyUtils.userAgeKey)}"
            previewImageVerticalToolBar.txtTitleFilterSetting.text="Preview"
            previewImageVerticalToolBar.txtDoneFilterSetting.visibility=View.GONE
        }
    }
    private fun onClickListner(){
        binding!!.apply {
            val list=MyUtils.getArray(requireContext(),MyUtils.userArrayImageLinkKey)
            previewImageVerticalToolBar.imBackBtnFilterSetting.setOnClickListener {
                findNavController().popBackStack()
            }
            imgInfoPreviewImage.setOnClickListener {
                val bundle=Bundle()
                val id=MyUtils.getString(requireContext(),MyUtils.userUidKey)
                Log.i("asfdn","id is ${id}")
                bundle.putString("idKey",id)
                bundle.putString("userFullAge",MyUtils.getString(requireContext(),MyUtils.userFullAgeKey))
                bundle.putString("userName",MyUtils.getString(requireContext(),MyUtils.userNameKey))
                bundle.putString("firstRoot",MyUtils.getString(requireContext(),MyUtils.userSelectedCountryKeyOne))
                bundle.putString("SecondRoot",MyUtils.getString(requireContext(),MyUtils.userSelectedCountryKeyTwo))
                bundle.putStringArrayList("imageUrlList",list)
                findNavController().navigate(R.id.action_previewImageVerticalFragment_to_userInfoFragment,bundle)
            }
            val addatper=AdapterVerticalImageView(requireContext(),list!!)
            viewPagerVerticalView.adapter=addatper
            dotsIndicatorVertical.attachTo(binding!!.viewPagerVerticalView)
        }
    }

}