package com.entity.unity.ui.counsellor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CounsellorViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Exercise Fragment"
    }
    val text: LiveData<String> = _text
}