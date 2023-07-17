package com.ibeor.ibeorchattingapp.modules.gettingUserDetail

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentDOBBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.util.*
import kotlin.collections.HashMap


class DOBFragment : Fragment() {
    private var binding:FragmentDOBBinding?=null
    private var day:Int?=null
    private var month:Int?=null
    private var year:Int?=null
    private var ag:Int?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding=FragmentDOBBinding.inflate(layoutInflater)
        return binding!!.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListner()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAge(dayy: Int, month: Int, yearr: Int): Int {
        val year= LocalDate.now().year
        val day= LocalDate.now().dayOfMonth
        var age: Int = year - yearr
        if(day >= dayy){
            age
        }
        else{
            age-=1
        }
        age-=1
        return age+1
    }
    fun getAgee(dayy: Int, month: Int, yearr: Int): Int {
        val cal = GregorianCalendar()
        val y: Int
        val m: Int
        val d: Int
        var a: Int
        y = cal.get(Calendar.YEAR)
        m = cal.get(Calendar.MONTH)
        d = cal.get(Calendar.DAY_OF_MONTH)
        cal.set(year!!, month, dayy)
        a = y - cal.get(Calendar.YEAR)
        if (m < cal.get(Calendar.MONTH)
            || m == cal.get(Calendar.MONTH) && d < cal
                .get(Calendar.DAY_OF_MONTH)
        ) {
            --a
        }
        require(a >= 0) { "Age < 0" }
        return a
    }
    private fun showUnderAgeDialog(){
        val dialog= android.app.Dialog(requireContext())
        dialog.setContentView(R.layout.dob_under_18_dialog)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val okheBtn:AppCompatTextView=dialog.findViewById(R.id.btnOkUnder18)
        okheBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun showDialog(context: Context){
        val dialog= android.app.Dialog(context)
        dialog.setContentView(R.layout.dob_dialog)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        val editBtn:AppCompatButton=dialog.findViewById(R.id.dobDialogEditBtb)
        val confirmBtn:AppCompatButton=dialog.findViewById(R.id.dobDialogConfirmBtn)
        // 20
        val userAge:AppCompatTextView=dialog.findViewById(R.id.dobDialogAge)
        //Born Jan 1, 1991
        val userDob:AppCompatTextView=dialog.findViewById(R.id.dobDialogDob)
        if(userAge != null){
            // change here
            userAge.text="${ag} years old"
        }
        if( (day != null ) && (year != null) && (month != null) ){
            val mon= DateFormatSymbols().getMonths()[month!!-1];
            userDob.text="Born $mon $day, $year"
        }
        confirmBtn.setOnClickListener {
            val data2=HashMap<String,Any>()
            data2.put("user_completeStatus","2")
            data2.put("user_age",ag.toString())
            data2.put("user_full_Age",userDob.text.toString())
            lifecycleScope.launchWhenResumed {  if(findNavController().currentDestination!!.id == R.id.DOBFragment) {
                HomeViewModel().uploadDataToFirebaseRegister2(requireContext(), data2)
                    .observe(requireActivity(),
                        androidx.lifecycle.Observer {
                            val flag = it as Boolean
                            if (flag) {
                                if (findNavController().currentDestination!!.id == R.id.DOBFragment) {
                                    MyUtils.putString(requireContext(),
                                        MyUtils.userFullAgeKey,
                                        userDob.text.toString())
                                    MyUtils.putString(requireContext(),
                                        MyUtils.userAgeKey,
                                        ag.toString())
                                    findNavController().navigate(R.id.action_DOBFragment_to_selectCountryFragment)
                                }
                            }
                        })
            }}
//            findNavController().navigate(R.id.action_DOBFragment_to_selectCountryFragment)
            dialog.dismiss()
        }
        editBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun clickListner(){
        binding!!.apply {
            imBackBtnDob.setOnClickListener {
                activity?.onBackPressed()
            }
            tvSelectDob.setOnClickListener {
                datePicker.visibility=View.VISIBLE
            }
            tvSetSelectedDate.setOnClickListener {
                datePicker.visibility=View.GONE
                day=datePicker.dayOfMonth
                month=datePicker.month+1
                year=datePicker.year
//                ag=getAge(day!!,month!!,year!!)
                ag=getAgee(day!!,month!!,year!!)
                ag!!.plus(1)
                if(ag!! > 17) {
                    tvSelectDob.text="$month-$day-$year"
                    tvAge.text="Age : $ag"
                    btnDobContinue.isClickable=true
                }
                else{
                    btnDobContinue.isClickable=false
                    showUnderAgeDialog()
                }
            }

            tvCancelSelectedDate.setOnClickListener {
                datePicker.visibility=View.GONE
            }

            btnDobContinue.setOnClickListener {
                if(ag != null){
                    if(ag!! > 17) {
                        showDialog(requireContext())
                    }
                    else{
                        Toast.makeText(requireContext(), "Under 18 not allowed", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    showUnderAgeDialog()
                }
            }
        }
    }

}