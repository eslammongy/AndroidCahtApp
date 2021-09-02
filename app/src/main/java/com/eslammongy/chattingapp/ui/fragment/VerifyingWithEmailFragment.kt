package com.eslammongy.chattingapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eslammongy.chattingapp.R
import com.eslammongy.chattingapp.databinding.FragmentVerifyingWithEmailBinding

class VerifyingWithEmailFragment : Fragment() {

    private var _binding:FragmentVerifyingWithEmailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
       _binding = FragmentVerifyingWithEmailBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoToSignInNumber.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.main_container , GetUserPhoneNumber())
                .commit()
        }
    }

}