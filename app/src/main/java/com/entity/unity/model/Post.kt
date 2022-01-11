package com.entity.unity.model

import android.text.BoringLayout
import com.google.firebase.storage.StorageReference

data class Post(
    val id: String,
    val uid: String,
    val desc: String,
    val like: String = 0.toString(),
    val gref: StorageReference? = null,
    val likedBy: HashMap<String,Boolean> = hashMapOf()
)
