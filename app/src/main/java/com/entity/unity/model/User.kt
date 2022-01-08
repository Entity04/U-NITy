package com.entity.unity.model

data class User(
    var name:String ?= "Anonymous",
    var email:String ?=null,
    var uid:String ?=null
)