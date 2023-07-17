package com.ibeor.ibeorchattingapp.modules.chatting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.core.GoalRow
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentLikeMeBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.userData.FeatchFireBaseRegisterData
import kotlinx.coroutines.flow.combine


class LikeMeFragment : Fragment() {
    private var data:ArrayList<UserLikedCheckDetail>?=null
    private var binding:FragmentLikeMeBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding=FragmentLikeMeBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        clickLisner()
    }
    private fun init(){
        binding!!.apply {
            data=arguments?.getParcelableArrayList("userList")
            Log.i("asfbasf","data is $data")
            if( data != null && data?.size!! > -1 ){
                noUserLayoutInsideWhoLikesMe.visibility=View.GONE
                userLayoutInsideWhoLikesMe.visibility=View.VISIBLE
                val adapterr=WhoLikeMeAdapter(requireContext(),data!!,object:WhoLikeMeAdapter.onClicked{
                    override fun onClikc(id: String) {

                        HomeViewModel().getCheckStatusTwo(requireContext(),id).observe(requireActivity(),
                            Observer {
                                val data = it as FeatchFireBaseRegisterData
                                if(data!= null ){
                                    val bundle = Bundle()
                                    bundle.putString("idKey", data.user_uid)
                                    bundle.putString("userFullAge", data.user_age)
                                    bundle.putString("userName", data.user_name)
                                    bundle.putStringArrayList("imageUrlList", data.image_url_list)
                                    bundle.putDouble("userLatFirst", data.user_latitute?.toDouble()!!)
                                    bundle.putDouble("userLongFirst", data.user_longitute?.toDouble()!!)
                                    bundle.putString("firstRoot", data.selected_first_root)
                                    bundle.putString("SecondRoot", data.selected_second_root)
                                findNavController().navigate(R.id.action_likeMeFragment_to_userInfoFragment,
                                 bundle)
                                }

                            })
                    }

                })
//                rvWhoLikesMe.layoutManager=GridLayoutManager.
                rvWhoLikesMe.adapter=adapterr
            }
            else{
                noUserLayoutInsideWhoLikesMe.visibility=View.VISIBLE
                userLayoutInsideWhoLikesMe.visibility=View.GONE
            }
        }
    }
    private fun clickLisner(){
        binding!!.apply {
            imBackBtnWhoLIkeMe.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }


}