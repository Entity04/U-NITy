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
import com.entity.unity.studentRegLogIn.ForgotPasswordActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class CounsellorLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counsellor_login)

        val secretCode: String="UNITE"

        val tvRegister: TextView = findViewById(R.id.tvRegister)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etRegisterPassword)
        val tvForgotPass: TextView =findViewById(R.id.tvForgotPass)
        val etCode:EditText =findViewById(R.id.etRegisterCode)
        tvRegister.setOnClickListener {
            val intent= Intent(this, CounsellorRegister::class.java)
            startActivity(intent)
        }

        tvForgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        btnLogin.setOnClickListener {
            when {
                TextUtils.isEmpty(etEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter Email.", Toast.LENGTH_SHORT)
                        .show()
                }
                TextUtils.isEmpty(etPassword.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter Password.", Toast.LENGTH_SHORT)
                        .show()
                }
                TextUtils.isEmpty(etCode.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter Code.", Toast.LENGTH_SHORT)
                        .show()
                }
                !etEmail.text.toString().contains("@nitj.ac.in") -> {
                    Toast.makeText(this, "Please enter Valid NITJ College Email.", Toast.LENGTH_SHORT)
                        .show()
                }
                (etCode.text.toString()!=secretCode) -> {
                    Toast.makeText(this, "Please Enter a Valid Code", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    val email: String = etEmail.text.toString().trim { it <= ' ' }
                    val password: String = etPassword.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Constants.hashmap[FirebaseAuth.getInstance().currentUser?.uid!!]=true
                                Toast.makeText(
                                    this,
                                    "You have logged in successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent = Intent(this, CounsellorHome::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra(
                                    "user_id",
                                    FirebaseAuth.getInstance().currentUser!!.uid
                                )
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
}