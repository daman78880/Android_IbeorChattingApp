package com.ibeor.ibeorchattingapp.modules.troubleSigning.signInByEmail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentSigninByEmailSuccessBinding


class SigninByEmailSuccessFragment : Fragment() {
private var fragmentSigninByEmailSuccessFragment:FragmentSigninByEmailSuccessBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin_by_email_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding=FragmentSigninByEmailSuccessBinding.bind(view)
        fragmentSigninByEmailSuccessFragment=binding
        fragmentSigninByEmailSuccessFragment!!.apply {
            backBtnSuccess.setOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    override fun onDestroyView() {
        fragmentSigninByEmailSuccessFragment=null
        super.onDestroyView()
    }
}