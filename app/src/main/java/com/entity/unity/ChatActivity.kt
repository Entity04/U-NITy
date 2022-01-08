package com.entity.unity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.entity.unity.adapter.ChatUserAdapter
import com.entity.unity.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chatUserRecyclerView: RecyclerView
    private lateinit var userList:ArrayList<User>
    private lateinit var adapter: ChatUserAdapter
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var pbChat:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        pbChat=findViewById(R.id.pbChat)
        mAuth= FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        userList=ArrayList()
        adapter= ChatUserAdapter(this,userList)
        chatUserRecyclerView=findViewById(R.id.rvChat)
        chatUserRecyclerView.layoutManager=LinearLayoutManager(this)
        chatUserRecyclerView.adapter=adapter

        mDbRef.child("user").addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser= postSnapshot.getValue(User::class.java)
                    if (mAuth.currentUser?.uid!= currentUser?.uid){
                        userList.add(currentUser!!)
                        //Log.i("Data",currentUser.email.toString())
                    }
                }
                adapter.notifyDataSetChanged()
                pbChat.visibility= View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }
}