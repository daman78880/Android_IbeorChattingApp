package com.ibeor.ibeorchattingapp.modules.welcome

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.LocalTextInputService
import androidx.core.text.set
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentWelcomeBinding
import com.ibeor.ibeorchattingapp.modules.basic.HomeViewModel
import com.ibeor.ibeorchattingapp.modules.home.HomeFragment
import com.ibeor.ibeorchattingapp.modules.myUtils.MyUtils
import javax.security.auth.login.LoginException


class WelcomeFragment : Fragment(){


    private  var fragmentWelcomeBinding: FragmentWelcomeBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding=FragmentWelcomeBinding.bind(view)
        fragmentWelcomeBinding=binding

       var b=MyUtils.getBoolean(requireContext(),MyUtils.userLoginKey)
        Log.i("going","b value is ->$b")
        if(b != null) {
            if (b == true) {
                findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment2)
            }
        }
        setSpanableString(requireContext())
        fragmentWelcomeBinding!!.apply {
            btnCreateAccount.setOnClickListener{
                findNavController().navigate(R.id.action_welcomeFragment_to_createAccountFragment)
            }
            btnSignIn.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeFragment_to_signInFragment)
            }

            T.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeFragment_to_troubleSigningWelcomeFragment)
            }
        }
    }

    override fun onDestroy() {
        fragmentWelcomeBinding=null
        super.onDestroy()
    }
    private fun setSpanableString(context: Context){
        val str=requireContext().resources.getString(R.string.welcomeTerms)
        val ss = SpannableString(str)
        val span1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                // do some thing
                Log.i("testingQ","click")
                Toast.makeText(requireContext(),"Click",Toast.LENGTH_SHORT).show()
            }
        }
        val span2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                // do some thing
                Log.i("testingQ","click")
                Toast.makeText(requireContext(),"Click",Toast.LENGTH_SHORT).show()
            }
        }
        val span3: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                // do some thing
                Log.i("testingQ","click")
                Toast.makeText(requireContext(),"Click",Toast.LENGTH_SHORT).show()
            }
        }
        ss.setSpan(span1, 55, 60, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span2, 100, 114, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span3, 119, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        fragmentWelcomeBinding!!.txtTerms.text=ss
        fragmentWelcomeBinding!!.txtTerms.movementMethod=LinkMovementMethod.getInstance()
    }


}
