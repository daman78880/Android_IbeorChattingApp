package com.ibeor.ibeorchattingapp.modules.gettingUserDetail

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Contacts
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentSelectCountryBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.UnsupportedEncodingException


class SelectCountryFragment : Fragment() {
    private var binding:FragmentSelectCountryBinding?=null
    private var flagNext=false
    private var countryList:ArrayList<countryItem>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding=FragmentSelectCountryBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val f=MyUtils.getString(requireContext(),MyUtils.userSelectedCountryKeyOne)
        val t=MyUtils.getString(requireContext(),MyUtils.userSelectedCountryKeyTwo)
        if(f != null && f!!.isNotEmpty() && !f.equals("null")  ){
            binding!!.tvCountrySelected.text="${f}"
        }
        if(t != null && t!!.isNotEmpty() && !t.equals("null") ){
            binding!!.tvCountrySelected.text="${f},${t}"
        }
        if(t== null && f == null){
            binding!!.tvCountrySelected.text="Select Country"
        }
        countryList=getCountry()
        Log.e("asjdfb","country is ${countryList}")
        clickListner()
    }



    private fun clickListner(){
        binding!!.apply {
            imBackBtnSelectCountry.setOnClickListener {
                findNavController().popBackStack()
            }
            tvCountrySelected.setOnClickListener {
                showSheetDialog(requireContext())
//                countryDialog(requireContext())
            }
            btnCountrySelected.setOnClickListener {
                val r=MyUtils.getString(requireContext(),MyUtils.userStageGoing)
                if(r.equals("4")){
                    val data2=HashMap<String,Any>()
                    val firstCountry=MyUtils.getString(requireContext(),MyUtils.userSelectedCountryKeyOne)
                    val secondCountry=MyUtils.getString(requireContext(),MyUtils.userSelectedCountryKeyTwo)
                    data2.put("selected_first_root",firstCountry!!)
                    data2.put("selected_second_root",secondCountry!!)
                    HomeViewModel().uploadDataToFirebaseRegister2(requireContext(),data2).observe(requireActivity(),
                        androidx.lifecycle.Observer {
                            val flag=it as Boolean
                            if(flag){
                               findNavController().popBackStack()
                            }
                        })
                }
                else{
                    val data2=HashMap<String,Any>()
                    val firstCountry=MyUtils.getString(requireContext(),MyUtils.userSelectedCountryKeyOne)
                    val secondCountry=MyUtils.getString(requireContext(),MyUtils.userSelectedCountryKeyTwo)
                    data2.put("user_completeStatus","2")
                    data2.put("selected_first_root",firstCountry!!)
                    data2.put("selected_second_root",secondCountry!!)
                    HomeViewModel().uploadDataToFirebaseRegister2(requireContext(),data2).observe(requireActivity(),
                        androidx.lifecycle.Observer {
                            val flag=it as Boolean
                            if(flag){
                                if(findNavController().currentDestination!!.id == R.id.selectCountryFragment) {
                                    findNavController().navigate(R.id.action_selectCountryFragment_to_addImageFragment)
                                }
                            }
                        })

                }

            }
        }
    }
    private fun showSheetDialog(context: Context){
        val dialog= BottomSheetDialog(context,R.style.BottomSheetDialogTheme)
        var returnList=ArrayList<String>()
        dialog.window?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        val view = layoutInflater.inflate(R.layout.rv_select_country_layout, null)
        dialog.setContentView(view)
        val doneBtn : AppCompatTextView =view.findViewById(R.id.btnSelectedCountry)
        val etSearchCountry:AppCompatEditText=view.findViewById(R.id.etSearchCountry)
        val rv:RecyclerView=view.findViewById(R.id.rvCountrySelect)
            rv.layoutManager=LinearLayoutManager(context)
        val adapterr=CountryAdatper(requireContext(),countryList!!,object:CountryAdatper.clickedd{
            override fun onItemChange(data: ArrayList<String>) {
                returnList=data
            }
        })
        rv.adapter=adapterr
        doneBtn.setOnClickListener {

          if(returnList.size == 2){
              MyUtils.putString(requireContext(),MyUtils.userSelectedCountryKeyOne,returnList[0])
              MyUtils.putString(requireContext(),MyUtils.userSelectedCountryKeyTwo,returnList[1])
              val f=MyUtils.getString(requireContext(),MyUtils.userSelectedCountryKeyOne)
              val t=MyUtils.getString(requireContext(),MyUtils.userSelectedCountryKeyTwo)
              if(f!!.isNotEmpty() && !f.equals("null")){
                  binding!!.tvCountrySelected.text="${f}"
              }
              if(t!!.isNotEmpty() && !t.equals("null")){
                  binding!!.tvCountrySelected.text="${f},${t}"
              }
              dialog.dismiss()
          }
            else{
                if (returnList.size>1) {
                    MyUtils.putString(requireContext(),
                        MyUtils.userSelectedCountryKeyOne,
                        returnList[0])
                    val f = MyUtils.getString(requireContext(), MyUtils.userSelectedCountryKeyOne)
                    val t = MyUtils.getString(requireContext(), MyUtils.userSelectedCountryKeyTwo)
                    if (f!!.isNotEmpty() && !f.equals("null")) {
                        binding!!.tvCountrySelected.text = "${f}"
                    }
                    if (t!!.isNotEmpty() && !t.equals("null")) {
                        binding!!.tvCountrySelected.text = "${f},${t}"
                    }
                    dialog.dismiss()
                }
              else{
                    MyUtils.putString(requireContext(),MyUtils.userSelectedCountryKeyOne,"")
                    MyUtils.putString(requireContext(),MyUtils.userSelectedCountryKeyTwo,"")
                    binding!!.tvCountrySelected.text=""
                    dialog.dismiss()
                }
          }
        }
        dialog.show()
    }
     fun getCountry(): ArrayList<countryItem> {
        var countryList: ArrayList<countryItem>? = null
        val inputStream: InputStream = requireActivity().resources.openRawResource(R.raw.countries)
        try {
            val reader: Reader = InputStreamReader(inputStream, "UTF-8")
            val gson = Gson()
            countryList = gson.fromJson(
                reader,
                object : TypeToken<ArrayList<countryItem>>() {}.type
            )
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return countryList!!
    }

}