package com.example.socialframe.classes

class Chat(var ChatUser:User=User(),var MyMessage:MessageModel=MessageModel()) {


    fun NotEqual(other:Chat):Boolean{
        if(this.ChatUser.key!=other.ChatUser.key){
            return true
        }
        else if(this.MyMessage.messageTime.time!=other.MyMessage.messageTime.time){
            return true
        }
        return false
    }
}