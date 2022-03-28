package com.example.socialframe.classes

class User(var Name:String="NA",var Email:String="NA",var Password:String="NA") {
    var key:String=""
    var Description:String = ""
    var Followed:MutableList<String> = mutableListOf<String>()
    var Followers:MutableList<String> = mutableListOf<String>()
    var MyPosts:MutableList<String> = mutableListOf<String>()
    var MyPICUrl:String=""
}