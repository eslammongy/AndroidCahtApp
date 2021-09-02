package com.eslammongy.chattingapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.eslammongy.chattingapp.R
import com.eslammongy.chattingapp.databinding.ActivityDashBoardBinding
import com.eslammongy.chattingapp.ui.fragment.ChatFragment
import com.eslammongy.chattingapp.ui.fragment.ContactFragment
import com.eslammongy.chattingapp.ui.fragment.ProfileFragment

class DashBoardActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDashBoardBinding
    private var selectingFragment:Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction().replace(R.id.dashboardContainer , ChatFragment()).commit()

            binding.bottomChip.setItemSelected(R.id.btnChat , true)
        }
        binding.bottomChip.setOnItemSelectedListener {
            when(it){
                R.id.btnChat ->{
                    selectingFragment = ChatFragment()
                }
                R.id.btnContact ->{
                    selectingFragment = ContactFragment()
                }
                R.id.btnProfile -> {
                    selectingFragment = ProfileFragment()
                }
            }

            selectingFragment!!.let {
                supportFragmentManager.beginTransaction().replace(R.id.dashboardContainer , selectingFragment!!).commit()
            }
        }
    }
}