package com.eslammongy.chattingapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eslammongy.chattingapp.R
import com.eslammongy.chattingapp.databinding.ActivityHomeBinding
import com.eslammongy.chattingapp.databinding.FragmentVerifingNumberBinding
import com.eslammongy.chattingapp.ui.fragment.GetUserPhoneNumber
import com.eslammongy.chattingapp.ui.fragment.VerifyingWithEmailFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .add(R.id.main_container, VerifyingWithEmailFragment())
            .commit()
    }
}