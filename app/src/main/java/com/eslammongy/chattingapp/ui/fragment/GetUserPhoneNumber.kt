package com.eslammongy.chattingapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.eslammongy.chattingapp.R
import com.eslammongy.chattingapp.databinding.FragmentGetUserPhoneNumberBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class GetUserPhoneNumber : Fragment() {

    private var _binding: FragmentGetUserPhoneNumberBinding? = null
    private val binding get() = _binding!!
    private var phoneNumber:String? = null
    private lateinit var callbacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var verificationCode:String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGetUserPhoneNumberBinding.inflate(inflater , container , false)

       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnVerifyingPhoneNum.setOnClickListener {
            binding.verProgressBar.visibility = View.VISIBLE
            binding.linearLayout.alpha = 0.5F
            if (checkNumberValidation()){
                val phoneNumberPicker = binding.countryCodePicker.selectedCountryCodeWithPlus + phoneNumber
                sendVerifyingCode(phoneNumberPicker)
            }
        }
        binding.btnBackToEmailLogin.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container , VerifyingWithEmailFragment()).commit()
        }
        callbacks = object:PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            }
            override fun onVerificationFailed(exception: FirebaseException) {
                when (exception) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        Toast.makeText(requireActivity(), "${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                    is FirebaseTooManyRequestsException -> {
                        Toast.makeText(requireActivity(), "${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(requireActivity(), "${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onCodeSent(verifiyingCode: String, p1: PhoneAuthProvider.ForceResendingToken) {
                 verificationCode = verifiyingCode
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container , VerifyingNumberFragment.newInstance(
                        verificationCode!!
                    )).commit()

            }
        }
    }



    private fun checkNumberValidation():Boolean{

        phoneNumber = binding.tvPhoneNumber.text.toString().trim()
        return when {
            phoneNumber!!.isEmpty() -> {
                binding.tvPhoneNumber.error = "Error Field is required"
                false
            }
            phoneNumber!!.length <10 -> {
                binding.tvPhoneNumber.error = "Phone Number Must be in 10 Length"
                false
            }
            else -> true
        }
    }

    private fun sendVerifyingCode(phoneNumber:String){

        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        binding.verProgressBar.visibility = View.GONE

    }

}