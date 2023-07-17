package com.ibeor.ibeorchattingapp.modules.troubleSigning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentTroubleSigningWelcomeBinding
import com.ibeor.ibeorchattingapp.databinding.FragmentWelcomeBinding


class TroubleSigningWelcomeFragment : Fragment(){
    private  var troubleSigningWelcomeBinding: FragmentTroubleSigningWelcomeBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trouble_signing_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            val binding = FragmentTroubleSigningWelcomeBinding.bind(view)
            troubleSigningWelcomeBinding = binding
        troubleSigningWelcomeBinding!!.apply {
            backBtnTrouble.setOnClickListener {
                activity?.onBackPressed()
            }
            signInWithEmail.setOnClickListener {
                findNavController().navigate(R.id.action_troubleSigningWelcomeFragment_to_siginnByEmailFragment)
            }

        }
    }

    override fun onDestroy() {
        troubleSigningWelcomeBinding=null
        super.onDestroy()
    }



}