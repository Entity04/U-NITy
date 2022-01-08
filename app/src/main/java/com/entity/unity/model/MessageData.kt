package com.entity.unity.model

class MessageData {
    var message: String?=null
    var senderId: String?=null

    constructor(){}

    constructor( message: String?, senderId: String?){
        this.message=message
        this.senderId=senderId
    }
}