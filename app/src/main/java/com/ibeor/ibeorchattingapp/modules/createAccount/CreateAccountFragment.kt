package com.ibeor.ibeorchattingapp.modules.createAccount

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentCreateAccountBinding


class CreateAccountFragment : Fragment() {
        private var fragmentCreateAccountFragment: FragmentCreateAccountBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding=FragmentCreateAccountBinding.bind(view)
        fragmentCreateAccountFragment=binding
        setSpanableString(requireContext())
        fragmentCreateAccountFragment!!.apply {
            TS.setOnClickListener {
                findNavController().navigate(R.id.action_createAccountFragment_to_troubleSigningWelcomeFragment)
            }
            btnSignInWithPhoneNumberS.setOnClickListener {
                findNavController().navigate(R.id.action_createAccountFragment_to_signInFragment)
            }

        }
    }
    override fun onDestroyView() {
        fragmentCreateAccountFragment=null
        super.onDestroyView()
    }
    private fun setSpanableString(context: Context){
        val str=requireContext().resources.getString(R.string.welcomeTerms)
        val ss = SpannableString(str)
        val span1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                Toast.makeText(requireContext(),"Click", Toast.LENGTH_SHORT).show()
            }
        }
        val span2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                Toast.makeText(requireContext(),"Click", Toast.LENGTH_SHORT).show()
            }
        }
        val span3: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                Toast.makeText(requireContext(),"Click", Toast.LENGTH_SHORT).show()
            }
        }
        ss.setSpan(span1, 55, 60, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span2, 100, 114, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span3, 119, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        fragmentCreateAccountFragment!!.txtTermsS.text=ss
        fragmentCreateAccountFragment!!.txtTermsS.movementMethod= LinkMovementMethod.getInstance()
    }

}