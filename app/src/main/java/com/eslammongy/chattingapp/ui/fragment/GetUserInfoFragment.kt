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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.eslammongy.chattingapp.constants.Constants
import com.eslammongy.chattingapp.databinding.FragmentGetUserInfoBinding
import com.eslammongy.chattingapp.ui.activities.DashBoardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class GetUserInfoFragment : Fragment() {

    private var _binding:FragmentGetUserInfoBinding? = null
    private val binding get() = _binding!!
    private var userImage:Uri? =null
    private lateinit var userName:String
    private lateinit var userStatus:String
    private lateinit var userEmail:String
    private lateinit var imageUrl:String
    private var firebaseAuth: FirebaseAuth? = null
    private var databaseReference: DatabaseReference? = null
    private var firebaseStorage:StorageReference? = null
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentGetUserInfoBinding.inflate(inflater , container , false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDataDone.setOnClickListener {
            if (checkUserInfo()){
               uploadUerInfo(userName , userEmail , userStatus , userImage!!)
            }
        }
        binding.imgPickImage.setOnClickListener {
            if (checkUserStoragePermission()){
                pickUserImage()
            }else{
                permReqLauncher.launch(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))
            }
        }
    }

    private fun checkUserInfo():Boolean{
        userName = binding.edtUserName.text.toString().trim()
        userStatus = binding.edtUserStatus.text.toString().trim()
        userEmail = binding.edtUserEmail.text.toString().trim()

        when {
            userName.isEmpty() -> {
                binding.edtUserName.error = "Error Field is required"
                return false
            }
            userEmail.isEmpty() -> {
                binding.edtUserEmail.error = "Error Field is required"
                return false
            }
            userStatus.isEmpty() -> {
                binding.edtUserEmail.error = "Error Field is required"
                return false
            }
            userImage == null -> {
                Toast.makeText(requireContext(), "Image Required !!", Toast.LENGTH_SHORT).show()
                return false
            }
            else -> return true
        }
    }

    private fun uploadUerInfo(name:String , email:String , status:String , image:Uri)= kotlin.run{

         firebaseStorage!!.child(firebaseAuth!!.uid + Constants.path).putFile(image)
             .addOnSuccessListener{
                 val task = it.storage.downloadUrl
                 task.addOnCompleteListener { uri ->
                     imageUrl = uri.result.toString()
                     val mapUserInfo = mapOf(
                         "name" to name,
                         "email" to email,
                         "status" to status,
                         "image" to imageUrl
                     )
                     databaseReference!!.child(firebaseAuth!!.uid!!).updateChildren(mapUserInfo)
                     startActivity(Intent(requireActivity() , DashBoardActivity::class.java))
                    requireActivity().finish()

                 }
             }
    }

    private fun checkUserStoragePermission():Boolean{
        return ContextCompat.checkSelfPermission(requireContext() , android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
/*
    private fun selectAndCropImage(){
        cropActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val cropResult = CropImage.getActivityResult(result.data)
            if (result.resultCode == Activity.RESULT_OK) {
              userImage = cropResult.uri
                binding.imgUser.setImageURI(userImage)

            }
        }
    }

 */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    userImage = result.uri
                    binding.imgUser.setImageURI(userImage)
                }
            }


        }
    }


    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                Toast.makeText(requireContext(), "gallery permission granted", Toast.LENGTH_SHORT).show()
                pickUserImage()
            }else{
                Toast.makeText(requireContext(), "gallery permission refused", Toast.LENGTH_SHORT).show()
            }
        }


    private fun pickUserImage(){
        CropImage.activity()
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(requireActivity() , this)
    }



}