package com.ibeor.ibeorchattingapp.modules.editProfile.GettingUserData

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.text.toLowerCase
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.internal.LazilyParsedNumber
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentSelectLanguageBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import com.ibeor.ibeorchattingapp.modules.userData.json.json_languageItem
import com.ibeor.ibeorchattingapp.modules.userData.updateLanguageData.LanguageData
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.UnsupportedEncodingException
import java.util.*
import kotlin.collections.ArrayList


class SelectLanguageFragment : Fragment() {
private var binding:FragmentSelectLanguageBinding?=null
    private var languageList=ArrayList<LanguageData>()
    private var selectedLanugage=ArrayList<LanguageData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentSelectLanguageBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    private fun init(){
        binding!!.apply {
            val list = getLanguages()
            for (i in 0 until list.size){
                languageList.add(LanguageData(list[i].name,false))
            }
            btnLanguage.setOnClickListener {
                var data=ArrayList<String>()
            for (i in 0 until selectedLanugage.size){
                if (selectedLanugage[i].check == true){
                    data.add(selectedLanugage[i].title.toString())
                }
            }
                if(selectedLanugage.size >1){
                    MyUtils.putArray(requireContext(),MyUtils.userLanguage,data)
                    HomeViewModel().saveInfoData(requireContext()).observe(requireActivity(),
                        androidx.lifecycle.Observer {
                            val f=it as Boolean
                            if(f){
                                lifecycleScope.launchWhenResumed {
                                    Log.i("updasteStatus","Data updated")
                                    findNavController().popBackStack()
                                }
                            }
                            else{
                                findNavController().popBackStack()

                            }
                        })
                }
                else{
                    Toast.makeText(requireContext(), "Please select any Language.", Toast.LENGTH_SHORT).show()
                }
            }
            etSearchLanguage.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                    if(p0?.length!! >= 0) {
//                        filter(p0.toString())
//                    }
                }
                override fun afterTextChanged(p0: Editable?) {
                }

            })
            rvLanguage.layoutManager=LinearLayoutManager(requireContext())
            val anotherList=MyUtils.getArray(requireContext(),MyUtils.userLanguage)?:ArrayList<String>()

            setAdapter(languageList,anotherList)
        }
    }

     fun getLanguages(): ArrayList<json_languageItem> {
        var languageList: ArrayList<json_languageItem>? = null
        val inputStream: InputStream = requireActivity().resources.openRawResource(R.raw.languages)
        try {
            val reader: Reader = InputStreamReader(inputStream, "UTF-8")
            val gson = Gson()
            languageList = gson.fromJson(
                reader,
                object : TypeToken<ArrayList<json_languageItem>>() {}.type
            )
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return languageList!!
    }

    fun setAdapter(list:ArrayList<LanguageData>,data:ArrayList<String>){
        binding!!.apply {
            val adapter =
                AdapterForLanguage(requireContext(), list,data, object :AdapterForLanguage.Clicks{
                    override fun onViewClick(
                        position: Int,
                        title: String,
                        list: ArrayList<LanguageData>,
                    ) {
                       selectedLanugage=list
                    }
                })
            rvLanguage.adapter = adapter
        }
    }
}