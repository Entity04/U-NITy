package com.entity.unity.model

import com.google.firebase.storage.StorageReference

data class Post(
    val desc: String,
    val like: String = 0.toString(),
    val gref: StorageReference? = null
)
