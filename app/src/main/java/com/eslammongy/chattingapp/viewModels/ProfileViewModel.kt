package com.eslammongy.chattingapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.eslammongy.chattingapp.data.model.UserModel
import com.eslammongy.chattingapp.repository.ChatRepository

class ProfileViewModel:ViewModel() {

    private var chatRepository = ChatRepository.StaticFunRepoInstance.getInstance()

    fun getUser():LiveData<UserModel>{
        return chatRepository.getUser()
    }
}