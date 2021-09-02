package com.eslammongy.chattingapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.eslammongy.chattingapp.ui.activities.DashBoardActivity
import com.eslammongy.chattingapp.ui.activities.HomeActivity
import com.eslammongy.chattingapp.util.Utils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {
    private var firebaseAuth: FirebaseAuth? = null
    private lateinit var appUtil: Utils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()
        appUtil = Utils()
        Handler(Looper.getMainLooper()).postDelayed({
            if (firebaseAuth!!.currentUser == null){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                FirebaseInstallations.getInstance().id
                    .addOnCompleteListener {task ->
                        if (task.isSuccessful) {
                            var token: String?
                            FirebaseMessaging.getInstance().token.addOnCompleteListener { taskToken: Task<String> ->
                                println("Token : " + taskToken.result)
                                token = taskToken.result
                                val databaseReference =
                                    FirebaseDatabase.getInstance().getReference("Users")
                                        .child(appUtil.getUserUID()!!)
                                val map: MutableMap<String, Any> = HashMap()
                                map["token"] = token!!
                                databaseReference.updateChildren(map)
                            }

                        }
                    }
                startActivity(Intent(this, DashBoardActivity::class.java))
                finish()

            }

        }, 2000)
    }
}