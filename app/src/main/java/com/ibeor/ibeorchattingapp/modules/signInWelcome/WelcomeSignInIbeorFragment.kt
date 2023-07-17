package com.ibeor.ibeorchattingapp.modules.signInWelcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibeor.ibeorchattingapp.R
import com.ibeor.ibeorchattingapp.databinding.FragmentWelcomeSignInIbeorBinding


class WelcomeSignInIbeorFragment : Fragment() {
    private var binding:FragmentWelcomeSignInIbeorBinding?=null
    private var list:ArrayList<WelcomeIbeorDataModel>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentWelcomeSignInIbeorBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setList()
        binding!!.apply {
            imBackBtnWecomeSigned.setOnClickListener {
                activity?.onBackPressed()
            }
            btnContinueSigned.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeSignInIbeorFragment_to_signinFinishedFragment)
            }

            rvWelcomeRuleSigned.layoutManager=LinearLayoutManager(requireContext())
            val adapter=RvWelcomeIbeorSigninAdapter(requireContext(),list!!)
            rvWelcomeRuleSigned.adapter=adapter
        }
    }
    private fun setList(){
        list=ArrayList<WelcomeIbeorDataModel>()
        list!!.add(WelcomeIbeorDataModel("Be yourself","Make sure your photos, age, and bio\n" +
                "are true to who you are."))
        list!!.add(WelcomeIbeorDataModel("Stay safe","Don’t be too quick to give out personal\n" +
                "information. Date Safely"))
        list!!.add(WelcomeIbeorDataModel("Play it cool","Respect others and treat them as you\n" +
                "would like to be treated."))
        list!!.add(WelcomeIbeorDataModel("Be Proactive","Always report bad behavior."))
    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }

}