package com.eslammongy.chattingapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eslammongy.chattingapp.R
import com.eslammongy.chattingapp.databinding.FragmentContactBinding

class ContactFragment : Fragment() {

    private var _binding:FragmentContactBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentContactBinding.inflate(inflater , container , false)
        return binding.root
    }


}