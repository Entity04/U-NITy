package com.entity.unity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import com.entity.unity.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SplashActivity : AppCompatActivity() {
    private lateinit var mDbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mDbRef=FirebaseDatabase.getInstance().getReference()

        Thread {

                val currentUser= FirebaseAuth.getInstance().currentUser
                var currentUserId= ""
                if(currentUser!=null){
                    currentUserId=currentUser!!.uid
                }
                if(currentUserId.isNotEmpty()){
                    mDbRef.child("user").addValueEventListener(object : ValueEventListener {
                        //@SuppressLint("NotifyDataSetChanged")
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for(postSnapshot in snapshot.children){
                                val current= postSnapshot.getValue(User::class.java)
                                if(currentUserId == current?.uid){
                                    val intent=Intent(this@SplashActivity,CounsellorHome::class.java)
                                    startActivity(intent)
                                }
                            }
                            startActivity(Intent(this@SplashActivity,MainActivity2::class.java))
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })

                }
                else{
                    startActivity(Intent(this,ChosserActivity::class.java))
                }
                finish()
        }.start()
    }
}