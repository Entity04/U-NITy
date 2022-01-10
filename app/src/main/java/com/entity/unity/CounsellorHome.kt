package com.entity.unity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.entity.unity.adapter.CounsellorChatAdapter
import com.entity.unity.model.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CounsellorHome : AppCompatActivity() {
    private lateinit var counsellorChatRecyclerView: RecyclerView
    private lateinit var userList:ArrayList<Student>
    private lateinit var adapter: CounsellorChatAdapter
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var pbChat: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counsellor_home)
        Log.d("Back","OnCreate")
        pbChat=findViewById(R.id.pbChat)
        mAuth= FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()
        userList=ArrayList()
        adapter= CounsellorChatAdapter(this,userList)
        counsellorChatRecyclerView=findViewById(R.id.rvChat)
        counsellorChatRecyclerView.layoutManager= LinearLayoutManager(this)
        counsellorChatRecyclerView.adapter=adapter
        mDbRef.child("student").addValueEventListener(object : ValueEventListener {
            //@SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser= postSnapshot.getValue(Student::class.java)
                    if(mAuth.currentUser!!.uid.toString() == currentUser!!.counselloruid.toString())
                        userList.add(currentUser)
                }
                adapter.notifyDataSetChanged()
                pbChat.visibility= View.GONE
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.counsellor_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.menusignOut -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, ChosserActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
