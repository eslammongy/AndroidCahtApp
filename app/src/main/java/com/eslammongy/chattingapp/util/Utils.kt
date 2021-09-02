package com.eslammongy.chattingapp.util

import com.google.firebase.auth.FirebaseAuth

class Utils {

    fun getUserUID(): String? {
        val firebaseAuth = FirebaseAuth.getInstance()
        return firebaseAuth.uid
    }
}