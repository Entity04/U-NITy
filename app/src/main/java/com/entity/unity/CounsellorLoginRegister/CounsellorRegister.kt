package com.entity.unity.CounsellorLoginRegister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.entity.unity.*
import com.entity.unity.constants.Constants
import com.entity.unity.counsellorChat.CounsellorHome
import com.entity.unity.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CounsellorRegister : AppCompatActivity() {
    private lateinit var mDbRef: DatabaseReference

    companion object {
        const val NAME = "Counsellor"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counsellor_register)

        val secretCode: String="UNITE"

        val etCode: EditText=findViewById(R.id.etRegisterCode)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        val tvLogin: TextView = findViewById(R.id.tvLogin)
        val etRegisterEmail: EditText = findViewById(R.id.etRegisterEmail)
        val etRegisterPassword: EditText = findViewById(R.id.etRegisterPassword)

        tvLogin.setOnClickListener {
            startActivity(Intent(this, CounsellorLogin::class.java))
        }

        btnRegister.setOnClickListener {
            when {
                TextUtils.isEmpty(etRegisterEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter Email.", Toast.LENGTH_SHORT)
                        .show()
                }

                TextUtils.isEmpty(etRegisterPassword.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter Password.", Toast.LENGTH_SHORT)
                        .show()
                }
                TextUtils.isEmpty(etCode.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter Code.", Toast.LENGTH_SHORT)
                        .show()
                }
                !etRegisterEmail.text.toString().contains("@nitj.ac.in") -> {
                    Toast.makeText(
                        this,
                        "Please enter Valid NITJ College Email.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                (etCode.text.toString()!=secretCode) -> {
                    Toast.makeText(this, "Please Enter a Valid Code", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    val email: String = etRegisterEmail.text.toString().trim { it <= ' ' }
                    val password: String = etRegisterPassword.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                addUserToDatabase(
                                    NAME,
                                    email,
                                    FirebaseAuth.getInstance().currentUser?.uid!!
                                )
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Constants.hashmap[FirebaseAuth.getInstance().currentUser?.uid!!]=true
                                Toast.makeText(
                                    this,
                                    "You are registered successfully",
                                    Toast.LENGTH_SHORT
                                ).show()


                                val intent =
                                    Intent(this, CounsellorHome::class.java)

                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", firebaseUser.uid)
                                intent.putExtra("email_id", email)
                                startActivity(intent)

                                finish()
                            } else {
                                Toast.makeText(
                                    this,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                }
            }

        }
    }
    private fun addUserToDatabase(name: String, email: String, uid: String?) {
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid!!).setValue(User(name, email, uid))
    }
}
