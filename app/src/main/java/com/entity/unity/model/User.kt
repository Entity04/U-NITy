package com.entity.unity.model

class User {
    var name: String?="Anonymous"
    var email: String?=null
    var uid: String?=null
    var isCounsellor: Boolean=false

    constructor(){}

    constructor(name: String?, email:String?,uid:String?){
        this.name=name
        this.email=email
        this.uid=uid
    }
}