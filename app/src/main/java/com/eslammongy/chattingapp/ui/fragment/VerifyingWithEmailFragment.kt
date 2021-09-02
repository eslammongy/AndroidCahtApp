package com.eslammongy.chattingapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.eslammongy.chattingapp.R
import com.eslammongy.chattingapp.databinding.FragmentVerifyingWithEmailBinding

class VerifyingWithEmailFragment : Fragment() {

    private var _binding:FragmentVerifyingWithEmailBinding? = null
    private val binding get() = _binding!!
    private lateinit var userName:String
    private lateinit var userPassword:String
    private lateinit var userEmail:String

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

        binding.btnSignInWithEmail.setOnClickListener {
            if (checkInfoValidation()){
                Toast.makeText(requireContext(), "Login Success ",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "Login Error ",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkInfoValidation():Boolean{
        userName = binding.tvUserName.text.toString().trim()
        userEmail = binding.tvUserEmail.text.toString().trim()
        userPassword = binding.tvUserPassword.text.toString().trim()
        when {
            userName.isEmpty() -> {
                binding.tvUserName.error = "Error Field is required"
                return false
            }
            userEmail.isEmpty() -> {
                binding.tvUserEmail.error = "Error Field is required"
                return false
            }
            userPassword.isEmpty() -> {
                binding.tvUserPassword.error = "Error Field is required"
                return false
            }

            else -> return true
        }
    }

}