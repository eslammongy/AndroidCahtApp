package com.eslammongy.chattingapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.eslammongy.chattingapp.R
import com.eslammongy.chattingapp.data.model.UserMode
import com.eslammongy.chattingapp.databinding.FragmentVerifingNumberBinding
import com.eslammongy.chattingapp.ui.fragment.VerifyingNumberFragment.Companion.newInstance
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class VerifyingNumberFragment : Fragment() {

    private var _binding:FragmentVerifingNumberBinding? = null
    private val binding get() = _binding!!
    private var code: String? = null
    private lateinit var pinCode:String
    private var firebaseAuth:FirebaseAuth? = null
    private var databaseReference:DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            code = it.getString("Code")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
       _binding = FragmentVerifingNumberBinding.inflate(inflater , container , false)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
   binding.btnVerifyOtpCode.setOnClickListener {
       if (checkPinCode()){
           val credentials = PhoneAuthProvider.getCredential(code!! , pinCode)
           userSignIn(credentials)

       }
   }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(code:String ) =
            VerifyingNumberFragment().apply {
                arguments = Bundle().apply {
                    putString("Code", code)

                }
            }
    }

    private fun checkPinCode():Boolean{
        pinCode = binding.otpCodeText.text.toString()
        return when {
            pinCode.isEmpty() -> {
                binding.otpCodeText.error = "Error Filed is Required !!"
                false
            }
            pinCode.length <6 -> {
                binding.otpCodeText.error = "You Entered Valid Code"
                false
            }
            else -> true
        }
    }

    private fun userSignIn(credential: AuthCredential){

        firebaseAuth!!.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                val userModel = UserMode("" , "","" , "" , firebaseAuth!!.currentUser!!.phoneNumber!! ,
                firebaseAuth!!.uid!!)
                databaseReference!!.child(firebaseAuth?.uid!!).setValue(userModel)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_container , GetUserInfoFragment()).commit()
            }else{
                Toast.makeText(requireActivity(), "Error happening when signIn with Phone Number", Toast.LENGTH_SHORT).show()
            }
        }

    }
}