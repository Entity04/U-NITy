package com.entity.unity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import com.entity.unity.CounsellorLoginRegister.CounsellorLogin
import com.entity.unity.studentRegLogIn.EmailPassLogin

class ChosserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chosser)
        val radioGroup=findViewById<RadioGroup>(R.id.radio)
        val next=findViewById<Button>(R.id.btnNext)
        next.setOnClickListener {
            when(radioGroup.checkedRadioButtonId)
            {
                R.id.btn_counsellor -> {
                    val intent= Intent(this,CounsellorLogin::class.java)
                    startActivity(intent)
                }
                R.id.btn_student -> {
                    val intent= Intent(this, EmailPassLogin::class.java)
                    startActivity(intent)
                }
                else -> Toast.makeText(this,"Please Select a Role",Toast.LENGTH_SHORT).show()
            }
        }
    }
}