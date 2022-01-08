package com.entity.unity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent

import androidx.core.app.ActivityCompat.startActivityForResult
import android.provider.MediaStore

import android.graphics.Bitmap
import android.net.Uri
import java.io.IOException
import com.google.firebase.storage.StorageReference

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnFailureListener

import com.google.android.gms.tasks.OnSuccessListener

import android.app.ProgressDialog
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.OnProgressListener
import java.util.*




class CreateFeed : AppCompatActivity() {
    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 71
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    val postid: String =UUID.randomUUID().toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_feed)
        val mauth=FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        val desc: EditText=findViewById(R.id.feedDescription)
        val postImage :ImageView=findViewById(R.id.postImage)
        val post: TextView=findViewById(R.id.post)
        val db=FirebaseFirestore.getInstance()
        post.setOnClickListener {
            val mp= HashMap<String,String>()
            mp["description"]=desc.text.toString()
            mp["likes"]= 0.toString()
            db.collection("Feed").document(postid).set(mp)
                .addOnSuccessListener {
                uploadImage()
                val intent=Intent(this,MainActivity2::class.java)
                startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show()
            }
        }
        postImage.setOnClickListener {
            chooseImage()
        }
    }
    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
        }
    }
    private fun uploadImage() {
        if (filePath != null) {
            val ref = storageReference!!.child("images/$postid")
            ref.putFile(filePath!!)
                .addOnSuccessListener {
                    Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed " + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }
}