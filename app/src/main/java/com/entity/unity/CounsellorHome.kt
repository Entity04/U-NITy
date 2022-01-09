package com.entity.unity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.entity.unity.adapter.ChatUserAdapter
import com.entity.unity.adapter.CounsellorChatAdapter
import com.entity.unity.model.Student
import com.entity.unity.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CounsellorHome : AppCompatActivity() {

    private lateinit var chatUserRecyclerView: RecyclerView
    private lateinit var userList:ArrayList<Student>
    private lateinit var adapter: ChatUserAdapter
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var pbChat: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counsellor_home)
        pbChat=findViewById(R.id.pbChat)
        mAuth= FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()
        userList=ArrayList()
        adapter= CounsellorChatAdapter(this,userList)
        chatUserRecyclerView=findViewById(R.id.rvChat)
        chatUserRecyclerView.layoutManager= LinearLayoutManager(this)
        chatUserRecyclerView.adapter=adapter
        mDbRef.child("student").addValueEventListener(object : ValueEventListener {
            //@SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(postSnapshot in snapshot.children){

                }
                adapter.notifyDataSetChanged()
                pbChat.visibility= View.GONE
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}