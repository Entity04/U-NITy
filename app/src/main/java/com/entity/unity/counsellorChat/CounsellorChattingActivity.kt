package com.entity.unity.counsellorChat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.entity.unity.R
import com.entity.unity.Vc2
import com.entity.unity.VcActivity
import com.entity.unity.videocall.VideoActivity
import com.entity.unity.adapter.CounsellorMessageAdapter
import com.entity.unity.model.MessageData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CounsellorChattingActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sentButton: ImageView
    private lateinit var messageAdapter: CounsellorMessageAdapter
    private lateinit var messageList: ArrayList<MessageData>
    private lateinit var mDbRef: DatabaseReference
    var recieverRoom: String? = null
    var senderRoom: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counsellor_chatting)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        val email= intent.getStringExtra("email")
        val recieverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = recieverUid + senderUid
        recieverRoom = senderUid + recieverUid

        supportActionBar?.title = email

        chatRecyclerView = findViewById(R.id.chatRecyclerView)

        messageBox = findViewById(R.id.messageBox)

        sentButton = findViewById(R.id.counsellorSentButton)
        messageList = ArrayList()
        messageAdapter = CounsellorMessageAdapter(this, messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                //@SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(MessageData::class.java)

                        messageList.add(message!!)
                    }
                    chatRecyclerView.smoothScrollToPosition(messageAdapter.itemCount)
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

        sentButton.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObject = MessageData(message, senderUid)
            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(recieverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.vc,menu)
        return super.onCreateOptionsMenu(menu)
    }
    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            android.R.id.home ->
            {
                finish()
                return true
            }
            R.id.vc->{
                startActivity(Intent(this, Vc2::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
