package com.entity.unity.model

class Student{
    var email: String?=null
    var counselloruid: String?=null
    var studentuid: String?= null
    constructor(){}
    constructor(email: String?, counselloruid:String?,studentuid:String?){
        this.email=email
        this.counselloruid=counselloruid
        this.studentuid=studentuid
    }
}

