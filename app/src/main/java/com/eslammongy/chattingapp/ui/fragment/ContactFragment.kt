package com.eslammongy.chattingapp.ui.fragment

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.eslammongy.chattingapp.adapter.FriendsAdapter
import com.eslammongy.chattingapp.constants.Constants
import com.eslammongy.chattingapp.data.model.UserModel
import com.eslammongy.chattingapp.databinding.FragmentContactBinding
import com.eslammongy.chattingapp.permission.AppPermission
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ContactFragment : Fragment() {

    private var _binding:FragmentContactBinding? = null
    private val binding get() = _binding!!
    private lateinit var appPermission: AppPermission
    private lateinit var mobileContactsList:ArrayList<UserModel>
    private lateinit var appContactsList:ArrayList<UserModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentContactBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appPermission = AppPermission()
        if (appPermission.checkUserContactPermission(requireContext())){
            getMobileContactList()
        }else{
            appPermission.requestUserContactPermission(requireActivity())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            Constants.CONTACT_PERMISSION ->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getMobileContactList()
                }else{
                    Toast.makeText(requireContext(), "Contact permission refused", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    @SuppressLint("Recycle")
    private fun getMobileContactList(){

        val projection = arrayOf(
            ContactsContract.Data.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val contentResolver = requireContext().contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        if (cursor != null) {
            mobileContactsList = ArrayList()
            while (cursor.moveToNext()) {

                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var number =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                number = number.replace("\\s".toRegex(), "")
                val num = number.elementAt(0).toString()
                if (num == "0")
                    number = number.replaceFirst("(?:0)+".toRegex(), "+92")
                val userModel = UserModel()
                userModel.name = name
                userModel.number = number
                mobileContactsList.add(userModel)
            }
            cursor.close()
            getAppContactsList(mobileContactsList)
        }
    }

    private fun getAppContactsList(contactList:ArrayList<UserModel>){

        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val query = databaseReference.orderByChild("number")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    appContactsList = ArrayList()
                    for (data in snapshot.children) {

                        val number = data.child("number").value.toString()
                        for (mobileModel in contactList) {
                            if (mobileModel.number == number) {
                                val userModel = data.getValue(UserModel::class.java)
                                appContactsList.add(userModel!!)
                            }
                        }
                    }

                    binding.friendsRecyclerView.apply {
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(true)
                        adapter = FriendsAdapter(appContactsList)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }



}