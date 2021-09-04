package com.eslammongy.chattingapp.ui.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eslammongy.chattingapp.R
import com.eslammongy.chattingapp.constants.Constants
import com.eslammongy.chattingapp.databinding.FragmentProfileBinding
import com.eslammongy.chattingapp.permission.AppPermission
import com.eslammongy.chattingapp.util.Utils
import com.eslammongy.chattingapp.viewModels.ProfileViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    private var firebaseStorage: StorageReference? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(ProfileViewModel::class.java)
        fetchData()

        binding.btnUpdateProfileInfo.setOnClickListener {
            UpdateUserInfo(
                binding.tvUserName.text.toString(),
                binding.tvUserEmail.text.toString(),
                binding.tvUserPhoneNum.text.toString(),
                binding.tvUserStatus.text.toString()
            ).show(requireActivity().supportFragmentManager, "TAG")
        }

        binding.imgPickImage.setOnClickListener {
            if (AppPermission().checkUserStoragePermission(requireContext())){
               pickUserImage()
            }else{
                AppPermission().requestUserPermission(requireActivity())
            }
        }

    }

    private fun fetchData() {
        profileViewModel.getUser().observe(viewLifecycleOwner, Observer { userModel ->
            binding.userModel = userModel
        })
    }

    private fun updateUserImage(imageUri:Uri){
        firebaseStorage = FirebaseStorage.getInstance().reference
        firebaseStorage!!.child(Utils().getUserUID()!! + Constants.PATH).putFile(imageUri)
            .addOnSuccessListener { snapshot->
                val task = snapshot.storage.downloadUrl
                task.addOnCompleteListener {
                    if (it.isSuccessful){
                        val imagePath = it.result.toString()
                        profileViewModel.updateUserImage(imagePath)
                    }
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            Constants.STORAGE_PERMISSION ->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickUserImage()
                }else{
                    Toast.makeText(requireContext(), "gallery permission refused", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val userImage = result.uri
                     updateUserImage(userImage)
                }
            }


        }
    }

    private fun pickUserImage(){
        CropImage.activity()
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(requireActivity() , this)
    }




}