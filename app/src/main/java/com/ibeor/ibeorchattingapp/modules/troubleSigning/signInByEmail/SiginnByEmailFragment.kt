package com.ibeor.ibeorchattingapp.modules.troubleSigning.signInByEmail

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentSiginnByEmailBinding
import java.util.regex.Matcher
import java.util.regex.Pattern


class SiginnByEmailFragment : Fragment() {
private var fragmentSiginnByEmailFragment:FragmentSiginnByEmailBinding?=null
    var f:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_siginn_by_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding=FragmentSiginnByEmailBinding.bind(view)
        fragmentSiginnByEmailFragment=binding
        fragmentSiginnByEmailFragment!!.apply {
            backBtnByEmail.setOnClickListener {
                activity?.onBackPressed()
            }
            sendEmail.setOnClickListener {
                if(!TextUtils.isEmpty(etEnterEmail.text.toString())){
                    f=emailValidator(etEnterEmail.text.toString())?:false
                    if(f){
                        etEnterEmail.text!!.clear()
                        findNavController().navigate(R.id.action_siginnByEmailFragment_to_signinByEmailSuccessFragment)
                    }
                    else{
                        f=false
                        etEnterEmail.error="Please enter valid mail."
//                        Toast.makeText(requireContext(),"Please enter valid email",Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    etEnterEmail.error="Please enter valid email"
                }
            }
        }
    }

    override fun onDestroy() {
        fragmentSiginnByEmailFragment=null
        super.onDestroy()
    }
    fun emailValidator(email: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }

}