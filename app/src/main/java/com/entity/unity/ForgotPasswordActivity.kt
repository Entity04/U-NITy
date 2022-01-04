package com.entity.unity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val tvLogin: TextView = findViewById(R.id.tvLogin)
        val etForgotPassEmail: EditText = findViewById(R.id.etEmail)
        val btnReset: Button = findViewById(R.id.btnReset)

        btnReset.setOnClickListener {
            val email: String = etForgotPassEmail.text.toString().trim { it <= ' ' }

            when {
                email.isEmpty() -> {
                    Toast.makeText(
                        this,
                        "Please enter email address",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                !email.contains("@nitj.ac.in") -> {
                    Toast.makeText(
                        this,
                        "Please enter Valid NITJ College Email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }


                else -> {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener({ task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Email sent successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                finish()

                            } else {
                                Toast.makeText(
                                    this,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        })
                }
            }
        }

        tvLogin.setOnClickListener {
            startActivity(Intent(this@ForgotPasswordActivity, EmailPassLogin::class.java))
        }
    }
}