package com.entity.unity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import com.entity.unity.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch
import kotlin.properties.Delegates

class SplashActivity : AppCompatActivity() {
    private lateinit var mDbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mDbRef = FirebaseDatabase.getInstance().getReference()
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser!!.uid
        }
        if (currentUserId.isNotEmpty()) {
            var isCounsellor: Boolean=false
            mDbRef.child("user").addValueEventListener(object : ValueEventListener {
                //@SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postSnapshot in snapshot.children) {
                        val current = postSnapshot.getValue(User::class.java)
                        if (currentUserId == current?.uid) {
                            isCounsellor=true
                            val intent =
                                Intent(this@SplashActivity, CounsellorHome::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    Log.d("Reached","Here")
                    if(!isCounsellor) {
                        startActivity(Intent(this@SplashActivity, MainActivity2::class.java))
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        } else {
            startActivity(Intent(this, ChosserActivity::class.java))
        }
    }
}