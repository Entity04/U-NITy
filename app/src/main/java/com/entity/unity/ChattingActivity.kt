package com.entity.unity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.entity.unity.model.MessageData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
@ExperimentalUnsignedTypes
class ChattingActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sentButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<MessageData>
    private lateinit var mDbRef: DatabaseReference

    var recieverRoom: String?=null
    var senderRoom: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        val name= intent.getStringExtra("name")
        val recieverUid= intent.getStringExtra("uid")

        val senderUid= FirebaseAuth.getInstance().currentUser?.uid
        mDbRef= FirebaseDatabase.getInstance().getReference()

        senderRoom= recieverUid+senderUid
        recieverRoom=senderUid+recieverUid

        supportActionBar?.title= name

        chatRecyclerView=findViewById(R.id.chatRecyclerView)

        messageBox=findViewById(R.id.messageBox)

        sentButton=findViewById(R.id.sentButton)
        messageList= ArrayList()
        messageAdapter= MessageAdapter(this,messageList)

        chatRecyclerView.layoutManager= LinearLayoutManager(this)
        chatRecyclerView.adapter=messageAdapter

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

        val back:ImageView=findViewById(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this,ChatActivity::class.java))
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.vc,menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.vc->{
                startActivity(Intent(this,VideoActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}