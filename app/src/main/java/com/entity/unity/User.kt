package com.entity.unity

data class User(
    var name:String ?= "Anonymous",
    var email:String ?=null,
    var uid:String ?=null
)