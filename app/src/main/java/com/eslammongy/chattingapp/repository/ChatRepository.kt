package com.eslammongy.chattingapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eslammongy.chattingapp.data.model.UserModel
import com.eslammongy.chattingapp.util.Utils
import com.google.firebase.database.*

class ChatRepository {

    private var liveData: MutableLiveData<UserModel>? = null
    private lateinit var databaseReference: DatabaseReference
    private val utils = Utils()

    object StaticFunRepoInstance {
        private var instance: ChatRepository? = null
        fun getInstance(): ChatRepository {
            if (instance == null)
                instance = ChatRepository()

            return instance!!
        }
    }

    fun getUser(): LiveData<UserModel> {
        if (liveData == null)
            liveData = MutableLiveData()
        databaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(utils.getUserUID()!!)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userModel = snapshot.getValue(UserModel::class.java)
                    liveData!!.postValue(userModel)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return liveData!!
    }

    fun updateUserInfo(name: String, email: String, phone: String, status: String) {

        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(utils.getUserUID()!!)
        val mapUser: Map<String, Any> = mapOf(
            "name" to name,
            "email" to email,
            "phone" to phone,
            "number" to phone,
            "status" to status)

        databaseReference.updateChildren(mapUser)
    }

    fun updateUserImage(imagePath:String){
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(utils.getUserUID()!!)
        val mapUser: Map<String, Any> = mapOf("image" to imagePath)

        databaseReference.updateChildren(mapUser)
    }
}