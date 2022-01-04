package com.entity.unity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnRegister: Button =findViewById(R.id.btnRegister)
        val tvLogin: TextView =findViewById(R.id.tvLogin)
        val etRegisterEmail: EditText =findViewById(R.id.etRegisterEmail)
        val etRegisterPassword: EditText =findViewById(R.id.etRegisterPassword)

        tvLogin.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, EmailPassLogin::class.java))
        }

        btnRegister.setOnClickListener{
            when {
                TextUtils.isEmpty(etRegisterEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this@RegisterActivity, "Please enter Email.", Toast.LENGTH_SHORT)
                        .show()
                }

                TextUtils.isEmpty(etRegisterPassword.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this@RegisterActivity, "Please enter Password.", Toast.LENGTH_SHORT)
                        .show()
                }

                !etRegisterEmail.text.toString().contains("@nitj.ac.in") -> {
                    Toast.makeText(this@RegisterActivity, "Please enter Valid NITJ College Email.", Toast.LENGTH_SHORT)
                        .show()
                }


                else -> {
                    val email: String = etRegisterEmail.text.toString().trim { it <= ' ' }
                    val password: String = etRegisterPassword.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener({ task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!

                                Toast.makeText(
                                    this@RegisterActivity,
                                    "You are registered successfully",
                                    Toast.LENGTH_SHORT
                                ).show()


                                val intent = Intent(this@RegisterActivity, MainActivity2::class.java)

                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", firebaseUser.uid)
                                intent.putExtra("email_id", email)
                                startActivity(intent)

                                finish()
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        })
                }
            }

        }

    }
}