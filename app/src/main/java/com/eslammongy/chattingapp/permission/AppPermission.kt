package com.eslammongy.chattingapp.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.eslammongy.chattingapp.constants.Constants

class AppPermission {

    fun checkUserStoragePermission(context: Context):Boolean{
        return ContextCompat.checkSelfPermission(context , android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    fun checkUserContactPermission(context: Context):Boolean{
        return ContextCompat.checkSelfPermission(context , android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
    }

    fun requestUserPermission(activity: Activity){
        ActivityCompat.requestPermissions(activity , arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE) , Constants.STORAGE_PERMISSION)
    }

    fun requestUserContactPermission(activity: Activity){
        ActivityCompat.requestPermissions(activity , arrayOf(android.Manifest.permission.READ_CONTACTS) , Constants.CONTACT_PERMISSION)
    }
}