package com.ibeor.ibeorchattingapp.modules.editProfile.GettingUserData

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentChildrenUserDataByRadioButtonBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import com.ibeor.ibeorchattingapp.modules.userData.userDataByRadioBox.GettingUserDataForRadio
import kotlinx.coroutines.flow.combine
import java.util.Observer


class ChildrenUserDataByRadioButtonFragment : Fragment() {
    private var binding:FragmentChildrenUserDataByRadioButtonBinding?=null
    private var type:String?=null
    private var name:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentChildrenUserDataByRadioButtonBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        clickListner()

    }
    private fun clickListner(){
        binding!!.apply {

        }
    }
    private fun init(){
        binding!!.apply {
            toolbarGettingchildernUserData.imBackBtnFilterSetting.setOnClickListener { activity?.onBackPressed() }
            toolbarGettingchildernUserData.txtDoneFilterSetting.setOnClickListener {
                if(type=="02"){
                    MyUtils.putString(requireContext(),MyUtils.userChildren,name!!)
                }
                else if(type=="03"){
                    MyUtils.putString(requireContext(),MyUtils.userAstrologicalSign,name!!)
                }
                else if(type=="04"){
                    MyUtils.putString(requireContext(),MyUtils.userReligion,name!!)
                }
                else if(type=="05"){
                    MyUtils.putString(requireContext(),MyUtils.userEducation,name!!)
                }
                else if(type=="10"){
                    MyUtils.putString(requireContext(),MyUtils.userHeight,name!!)
                }
                else if(type=="11"){
                    MyUtils.putString(requireContext(),MyUtils.userBodyType,name!!)
                }
                else if(type=="20"){
                    MyUtils.putString(requireContext(),MyUtils.userExerciseStatus,name!!)
                }
                else if(type=="21"){
                    MyUtils.putString(requireContext(),MyUtils.userDrinkStatus,name!!)
                }
                else if(type=="22"){
                    MyUtils.putString(requireContext(),MyUtils.userSmokerStatus,name!!)
                }
                else if(type=="23"){
                    MyUtils.putString(requireContext(),MyUtils.userMarijuanaStatus,name!!)
                }
//
                HomeViewModel().saveInfoData(requireContext()).observe(requireActivity(), androidx.lifecycle.Observer {
                    val gg=it as Boolean
                    if(gg){
                           backFunction()
                    }
                    else{
                        backFunction()
                    }
                })
                backFunction()
//               findNavController().navigateUp()
//                findNavController().popBackStack()
            }
             type= (arguments?.get("status")) as String?
            if(type == "02"){
                toolbarGettingchildernUserData.txtTitleFilterSetting.text="Children"
                val value=MyUtils.getString(requireContext(),MyUtils.userChildren)
                val list=inilizezChildrenList()
                setAdatper(list,value)
            }
            else if(type == "03"){
                toolbarGettingchildernUserData.txtTitleFilterSetting.text="Astrological Sign"
                val value=MyUtils.getString(requireContext(),MyUtils.userAstrologicalSign)
                val list=inilizezAstrologicalSignList()
                setAdatper(list,value)
            }
            else if(type=="04"){
                toolbarGettingchildernUserData.txtTitleFilterSetting.text="Religion"
                val value=MyUtils.getString(requireContext(),MyUtils.userReligion)
                val list=inilizezReligionSignList()
                setAdatper(list,value)
            }
            else if(type=="05") {
                val value=MyUtils.getString(requireContext(),MyUtils.userEducation)
                toolbarGettingchildernUserData.txtTitleFilterSetting.text = "Education"
                val list = inilizezEducationSignList()
                setAdatper(list,value)
            }

            else if(type=="11"){
                val value=MyUtils.getString(requireContext(),MyUtils.userBodyType)
                toolbarGettingchildernUserData.txtTitleFilterSetting.text="Body Type"
                val list=inilizezBodyTypeList()
                setAdatper(list,value)
            }
            else if(type=="20"){
                val value=MyUtils.getString(requireContext(),MyUtils.userExerciseStatus)
                toolbarGettingchildernUserData.txtTitleFilterSetting.text="Exercise"
                val list=inilizezExerciseList()
                setAdatper(list,value)
            }
            else if(type=="21"){
                val value=MyUtils.getString(requireContext(),MyUtils.userDrinkStatus)
                toolbarGettingchildernUserData.txtTitleFilterSetting.text="Drink"
                val list=inilizezDrinkList()
                setAdatper(list,value)
            }
            else if(type=="22"){
                val value=MyUtils.getString(requireContext(),MyUtils.userSmokerStatus)
                toolbarGettingchildernUserData.txtTitleFilterSetting.text="Smoker"
                val list=inilizezDrinkList()
                setAdatper(list,value)
            }
            else if(type=="23"){
                val value=MyUtils.getString(requireContext(),MyUtils.userMarijuanaStatus)
                toolbarGettingchildernUserData.txtTitleFilterSetting.text="Marijuana"
                val list=inilizezDrinkList()
                setAdatper(list,value)
            }
        }
    }
    private fun backFunction(){
        lifecycleScope.launchWhenResumed {
        if(findNavController().currentDestination!!.id == R.id.childrenUserDataByRadioButtonFragment) {
//            findNavController().navigateUp()
            findNavController().popBackStack()
        }}
    }
    private fun setAdatper(list:ArrayList<GettingUserDataForRadio>,value:String?){
        binding!!.apply {
            rvGettingChilderData.layoutManager= LinearLayoutManager(requireContext())
            val adapterr=AdapterForGettingChildernData(requireContext(),list,value,object :AdapterForGettingChildernData.Click{
                override fun onClick(position: Int, namee: String) {
                    for(i in 0.until(list.size)){
                        if(position != i){
                            list[i].radioCheck=false
                        }
                        else {
                            list[position].radioCheck=true
                        }
                    }
                    rvGettingChilderData.adapter!!.notifyDataSetChanged()
                    name=namee
//                    Toast.makeText(requireContext(), "Selected value is -> $name", Toast.LENGTH_SHORT).show()
                }
            })
            rvGettingChilderData.adapter=adapterr
        }}
    private fun inilizezDrinkList():ArrayList<GettingUserDataForRadio>{
        val list=ArrayList<GettingUserDataForRadio>()
        list.add(GettingUserDataForRadio(null,"Never"))
        list.add(GettingUserDataForRadio(null,"Sometimes"))
        list.add(GettingUserDataForRadio(null,"Socially"))
        list.add(GettingUserDataForRadio(null,"Often"))
        return list
    }
    private fun inilizezSmokerList():ArrayList<GettingUserDataForRadio>{
        val list=ArrayList<GettingUserDataForRadio>()
        list.add(GettingUserDataForRadio(null,"Never"))
        list.add(GettingUserDataForRadio(null,"Sometimes"))
        list.add(GettingUserDataForRadio(null,"Socially"))
        list.add(GettingUserDataForRadio(null,"Often"))
        return list
    }
    private fun inilizezEducationSignList():ArrayList<GettingUserDataForRadio>{
        val list=ArrayList<GettingUserDataForRadio>()
        list.add(GettingUserDataForRadio(null,"High school"))
        list.add(GettingUserDataForRadio(null,"Some college"))
        list.add(GettingUserDataForRadio(null,"Associate degree"))
        list.add(GettingUserDataForRadio(null,"Bachelor’s degree"))
        list.add(GettingUserDataForRadio(null,"Graduate degree"))
        list.add(GettingUserDataForRadio(null,"PhD/Post Doctoral"))
        return list
    }
    private fun inilizezExerciseList():ArrayList<GettingUserDataForRadio>{
        val list=ArrayList<GettingUserDataForRadio>()
        list.add(GettingUserDataForRadio(null,"Never"))
        list.add(GettingUserDataForRadio(null,"Sometimes"))
        list.add(GettingUserDataForRadio(null,"Often"))
        return list
    }
    private fun inilizezBodyTypeList():ArrayList<GettingUserDataForRadio>{
        val list=ArrayList<GettingUserDataForRadio>()
        list.add(GettingUserDataForRadio(null,"Average"))
        list.add(GettingUserDataForRadio(null,"Fit"))
        list.add(GettingUserDataForRadio(null,"Curvy"))
        list.add(GettingUserDataForRadio(null,"Slim"))
        list.add(GettingUserDataForRadio(null,"Chubby"))
        return list
    }
    private fun inilizezReligionSignList(): ArrayList<GettingUserDataForRadio> {
        val list=ArrayList<GettingUserDataForRadio>()
        list.add(GettingUserDataForRadio(null,"None"))
        list.add(GettingUserDataForRadio(null,"Atheist"))
        list.add(GettingUserDataForRadio(null,"Buddhist/Taoist"))
        list.add(GettingUserDataForRadio(null,"Christian/Catholic"))
        list.add(GettingUserDataForRadio(null,"Christian/LDS"))
        list.add(GettingUserDataForRadio(null,"Christian/Protestant"))
        list.add(GettingUserDataForRadio(null,"Christian/Other"))
        list.add(GettingUserDataForRadio(null,"Hindu"))
        list.add(GettingUserDataForRadio(null,"Jewish"))
        list.add(GettingUserDataForRadio(null,"Muslim/Islam"))
        list.add(GettingUserDataForRadio(null,"Spritual but not religious"))
        list.add(GettingUserDataForRadio(null,"Other"))
        return list
    }
    private fun inilizezAstrologicalSignList():ArrayList<GettingUserDataForRadio>{
        val list=ArrayList<GettingUserDataForRadio>()
        list.add(GettingUserDataForRadio(R.drawable.aquarius_sign,"Aquarius"))
        list.add(GettingUserDataForRadio(R.drawable.aries_sign,"Aries"))
        list.add(GettingUserDataForRadio(R.drawable.cancer_sign,"Cancer"))
        list.add(GettingUserDataForRadio(R.drawable.capricorn_sign,"Capricorn"))
        list.add(GettingUserDataForRadio(R.drawable.gemini_sign,"Gemini"))
        list.add(GettingUserDataForRadio(R.drawable.leo_sign,"Leo"))
        list.add(GettingUserDataForRadio(R.drawable.a_sign,"Libra"))
        list.add(GettingUserDataForRadio(R.drawable.pisces_sign,"Pisces"))
        list.add(GettingUserDataForRadio(R.drawable.sagittarius_sign,"Sagittarius"))
        list.add(GettingUserDataForRadio(R.drawable.scorpio_sign,"Scorpio"))
        list.add(GettingUserDataForRadio(R.drawable.taurus_sign,"Taurus"))
        list.add(GettingUserDataForRadio(R.drawable.virgo_sign,"Virgo"))
        return list
    }
    private fun inilizezChildrenList():ArrayList<GettingUserDataForRadio>{
    val list=ArrayList<GettingUserDataForRadio>()
    list.add(GettingUserDataForRadio(null,"Don’t want"))
    list.add(GettingUserDataForRadio(null,"Want Someday"))
    list.add(GettingUserDataForRadio(null,"Have & want more"))
    list.add(GettingUserDataForRadio(null,"Have & don’t want more"))
    return list
}
}