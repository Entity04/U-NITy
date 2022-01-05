package com.entity.unity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Thread {
                val currentUser= FirebaseAuth.getInstance().currentUser
                var currentUserId= ""
                if(currentUser!=null){
                    currentUserId=currentUser.uid
                }
                if(currentUserId.isNotEmpty()){
                    startActivity(Intent(this,MainActivity2::class.java))
                }
                else{
                    startActivity(Intent(this,EmailPassLogin::class.java))
                }

                finish()
        }.start()
    }
}