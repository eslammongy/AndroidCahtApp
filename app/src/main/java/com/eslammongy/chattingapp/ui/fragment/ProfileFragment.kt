package com.eslammongy.chattingapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eslammongy.chattingapp.R
import com.eslammongy.chattingapp.databinding.ActivityDashBoardBinding
import com.eslammongy.chattingapp.databinding.FragmentProfileBinding
import com.eslammongy.chattingapp.viewModels.ProfileViewModel


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileViewModel:ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = DataBindingUtil.inflate(inflater , R.layout.fragment_profile , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ProfileViewModel::class.java)
          fetchData()

    }

    private fun fetchData(){
        profileViewModel.getUser().observe(viewLifecycleOwner , Observer{ userModel ->
            binding.userModel = userModel
//            if (userModel.name.contains(" ")){
//                val split = userModel.name.split(" ")
//                binding.tvUserEmail.text = split[0]
//                binding.tvUserName.text = split[1]
//
//            }
        })
    }

}