package com.ibeor.ibeorchattingapp.modules.editProfile.GettingUserData

import android.os.Bundle
import android.os.Handler
import android.util.ArraySet
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentLookingForUserDataByTickBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import com.ibeor.ibeorchattingapp.modules.userData.userDataByCheckBox.GettingUserDataForCheckBox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LookingForUserDataByTickFragment : Fragment() {
    private var binding:FragmentLookingForUserDataByTickBinding?=null
    private var userSelectOptionList=ArrayList<String>()
    private var userSelectOPtionListThree=ArrayList<GettingCheckDataList>()
    private var type:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentLookingForUserDataByTickBinding.inflate(layoutInflater)
        init()
        onClickListner()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      binding!!.circleProgressBarT.visibility=View.VISIBLE
    }

    private fun onClickListner(){
        binding!!.apply {
        toolbarEditProfileLookingFor.imBackBtnFilterSetting.setOnClickListener { activity?.onBackPressed() }
            toolbarEditProfileLookingFor.txtDoneFilterSetting.setOnClickListener {
                Log.i("listValue","selected list is $userSelectOptionList")
                var newlist=ArrayList<String>()
                    for (i in 0 until userSelectOPtionListThree.size) {
                        if (userSelectOPtionListThree[i].flag == true) {
                            newlist.add(userSelectOPtionListThree[i].title.toString())
                        }
                    }
                Log.i("listValxvxue","list data is ${newlist}")
                if(type=="00") {
                    Log.i("listValxvxue","inside type 00 list data is ${newlist}")
                    MyUtils.putArray(requireContext(),MyUtils.userLookingForInPeople,newlist)
                }
                else if(type=="01"){
                    Log.i("listValxvxue","inside type 01 list data is ${newlist}")
                    MyUtils.putArray(requireContext(),MyUtils.userPet,newlist)
                    Log.i("listValxvxue"," pet ${MyUtils.getArray(requireContext(),MyUtils.userPet)}")

                }
                else if(type== "30"){
                    Log.i("listValxvxue","inside type 30 list data is ${newlist}")
                    MyUtils.putArray(requireContext(),MyUtils.userInterestsStatus,newlist)
                }
                HomeViewModel().saveInfoData(requireContext()).observe(requireActivity(), Observer {
                    val gg=it as Boolean
                    if(gg){
//                        Toast.makeText(requireContext(), "sucess", Toast.LENGTH_SHORT).show()
                        lifecycleScope.launchWhenResumed {
                            activity?.onBackPressed()
                        }
                    }
                    else{
//                        Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
                        lifecycleScope.launchWhenResumed {
                        activity?.onBackPressed()
                        }
                    }
                })
            }
    }}
    private fun init(){binding!!.apply {
         type = arguments?.get("status") as String?
         val userUid=MyUtils.userUidKey
        GlobalScope.launch(Dispatchers.Main) {
        if (type == "00") {
            val data=MyUtils.getArray(requireContext(),MyUtils.userLookingForKey)
            Log.i("askfnkasnf","looking selected data is ${data}")
            val list = inilizezLookingList()
            setAdapter(list,data)
            txtMsgtoogleLayoutForShowFeatureOnProfile.visibility=View.VISIBLE
            toogleLayoutForShowFeatureOnProfile.visibility=View.VISIBLE
            txtMainTitleCheckBox.text = "SELECT ONE OR MORE"
            txtMainTitleCheckBox.visibility=View.VISIBLE
            toolbarEditProfileLookingFor.txtTitleFilterSetting.text = "Looking For"
            circleProgressBarT.visibility=View.GONE


        } else if (type == "01") {
            val list = inilizezPetsList()
            val data=MyUtils.getArray(requireContext(),MyUtils.userPet)
            setAdapter(list,data)
            Log.i("listValxvxue","gettin data ${data}")
            txtMsgtoogleLayoutForShowFeatureOnProfile.visibility=View.GONE
            toogleLayoutForShowFeatureOnProfile.visibility=View.GONE
            txtMainTitleCheckBox.text = "SELECT ONE OR MORE"
            txtMainTitleCheckBox.visibility=View.VISIBLE
            toogleLayoutForShowFeatureOnProfile.visibility = View.GONE
            txtMsgtoogleLayoutForShowFeatureOnProfile.visibility = View.GONE
            toolbarEditProfileLookingFor.txtTitleFilterSetting.text = "Pets"
            circleProgressBarT.visibility=View.GONE
        } else if (type == "30") {
            val list = inilizezInterestList()
            val data=MyUtils.getArray(requireContext(),MyUtils.userInterestsStatus)
            setAdapter(list,data)
            Handler().postDelayed(Runnable{

                setAdapter(list,data)
                txtMsgtoogleLayoutForShowFeatureOnProfile.visibility=View.GONE
                toogleLayoutForShowFeatureOnProfile.visibility=View.GONE
                txtMainTitleCheckBox.text = "Select up to 5 interest for profile"
                txtMainTitleCheckBox.visibility=View.VISIBLE
                toogleLayoutForShowFeatureOnProfile.visibility = View.GONE
                txtMsgtoogleLayoutForShowFeatureOnProfile.visibility = View.GONE
                toolbarEditProfileLookingFor.txtTitleFilterSetting.text = "Interests"
            circleProgressBarT.visibility=View.GONE
            },500)
        }
    }
        }

}
    private fun setAdapter(list:ArrayList<GettingUserDataForCheckBox>,data:ArrayList<String>?){
//    private fun setAdapter(list:ArrayList<GettingUserDataForCheckBox>, uid:String,value: String){
        binding!!.apply {
            rvGetUserDataByCheckBok.layoutManager = LinearLayoutManager(requireContext())
            val adapterr = AdapterForGettingUserDataByCheckBox(requireContext(), list,data,object :AdapterForGettingUserDataByCheckBox.Click{
                override fun onClick(valueList: ArrayList<GettingCheckDataList>) {
                    Log.i("listslftk","list is ${valueList}")
                    userSelectOPtionListThree=valueList
                }
            })
            rvGetUserDataByCheckBok.adapter = adapterr
        }
    }

    private fun inilizezInterestList(): ArrayList<GettingUserDataForCheckBox> {
        val list=ArrayList<GettingUserDataForCheckBox>()
        list.add(GettingUserDataForCheckBox(R.drawable.concert_img,"Concerts"))
        list.add(GettingUserDataForCheckBox(R.drawable.kitchen_img,"Cooking"))
        list.add(GettingUserDataForCheckBox(R.drawable.traveling_img,"Traveling"))
        list.add(GettingUserDataForCheckBox(R.drawable.moviethreater_img,"Film & TV"))
        list.add(GettingUserDataForCheckBox(R.drawable.music_img,"Music"))
        list.add(GettingUserDataForCheckBox(R.drawable.karaoke_img,"Karaoke"))
        list.add(GettingUserDataForCheckBox(R.drawable.reading_img,"Reading"))
        list.add(GettingUserDataForCheckBox(R.drawable.makeup_img,"Make-up"))
        list.add(GettingUserDataForCheckBox(R.drawable.dance_img,"Danceing"))
        list.add(GettingUserDataForCheckBox(R.drawable.booling_img,"Bowling"))
        return list
    }
    private fun inilizezPetsList():ArrayList<GettingUserDataForCheckBox>{
        val list=ArrayList<GettingUserDataForCheckBox>()
        list.add(GettingUserDataForCheckBox(R.drawable.cat_emoji,"Cat(s)"))
        list.add(GettingUserDataForCheckBox(R.drawable.dog_emoji,"Dog(s)"))
        list.add(GettingUserDataForCheckBox(R.drawable.pet_img,"Other"))
        return list
    }
    private fun inilizezLookingList():ArrayList<GettingUserDataForCheckBox>{
        val list=ArrayList<GettingUserDataForCheckBox>()
        list.add(GettingUserDataForCheckBox(R.drawable.friend_emoji,"Friends"))
        list.add(GettingUserDataForCheckBox(R.drawable.naughty_emoji,"FWB"))
        list.add(GettingUserDataForCheckBox(R.drawable.kiss_emoji,"Something Casual"))
        list.add(GettingUserDataForCheckBox(R.drawable.trofy_emoji,"Exclusive Dating"))
        list.add(GettingUserDataForCheckBox(R.drawable.heart_emoji,"Long Term Relationship"))
        list.add(GettingUserDataForCheckBox(R.drawable.ring_emoji,"Wedding Band"))
        return list
    }
}