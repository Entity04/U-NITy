package com.entity.unity.model

data class Post(
    val desc: String,
    val like: String = 0.toString(),
    val gref: String? = null
)
