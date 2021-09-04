package com.eslammongy.chattingapp.ui.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.eslammongy.chattingapp.databinding.UpdateStatusDialogBinding
import com.eslammongy.chattingapp.viewModels.ProfileViewModel

class UpdateUserInfo(private val name:String?, private val email:String?, private val phone:String?, val status:String?): DialogFragment() {

    private var _binding: UpdateStatusDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var updateUserViewModel: ProfileViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UpdateStatusDialogBinding.inflate(inflater, container, false)
        displayCurrentUserInfo()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUserViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(ProfileViewModel::class.java)

        binding.btnUpdateStatusDialog.setOnClickListener {
            val name = binding.tvUpdateUserName.text.toString()
            val email = binding.tvUpdateUserEmail.text.toString()
            val phone = binding.tvUpdateUserPhone.text.toString()
            val status = binding.tvUpdateUserStatus.text.toString()
            updateUserViewModel.updateUserInfo(name, email, phone, status)
            dialog!!.dismiss()
        }

        binding.btnExitStatusDialog.setOnClickListener {
            dialog!!.dismiss()
        }
    }

    private fun displayCurrentUserInfo(){
        binding.tvUpdateUserName.setText(name!!)
        binding.tvUpdateUserEmail.setText(email!!)
        binding.tvUpdateUserPhone.setText(phone!!)
        binding.tvUpdateUserStatus.setText(status!!)
    }

    override fun onStart() {
        super.onStart()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        sheetContainer.layoutParams.width = width
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        sheetContainer.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
    }
}