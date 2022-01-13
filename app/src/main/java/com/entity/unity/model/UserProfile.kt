package com.entity.unity.model

import com.google.firebase.storage.StorageReference

data class UserProfile(
    var name: String?="Anonymous",
    var email: String?=null,
    var uid: String?=null,
    val gref: StorageReference? = null,
    var role:String
)
