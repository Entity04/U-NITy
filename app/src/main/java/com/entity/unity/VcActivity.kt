package com.entity.unity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.jitsi.meet.sdk.JitsiMeetActivity

import org.jitsi.meet.sdk.JitsiMeetConferenceOptions

import android.view.View

import android.widget.EditText
import java.net.MalformedURLException
import java.net.URL


class VcActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vc)

        // using try catch block to handle exceptions
        try {
            // object creation of JitsiMeetConferenceOptions
            // class by the name of options
            val options = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL("https://meet.jit.si"))

                .build()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
    }

    // we have declared the name of onButtonClick() method
    // in our xml file  now we are going to define it.
    fun onButtonClick(v: View?) {
        // initialize editText with method findViewById()
        // here editText will hold the name of room which is given by user
        val editText = findViewById<EditText>(R.id.conferenceName)

        // store the string input by user in editText in
        // an local variable named text of string type
        val text = editText.text.toString()

        // if user has typed some text in
        // EditText then only room will create
        if (text.length > 0) {
            // creating a room using  JitsiMeetConferenceOptions class
            // here .setRoom() method will set the text in room name
            // here launch method with launch a new room to user where
            // they can invite others too.
            val options = JitsiMeetConferenceOptions.Builder()
                .setRoom(text)
                .build()
            JitsiMeetActivity.launch(this, options)
        }
    }
}