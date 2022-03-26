package com.example.socialframe.classes

class User(var Name:String="NA",var Email:String="NA",var Password:String="NA") {
    var key:String=""
    var Description:String = ""
    var Followed:List<String> = listOf<String>()
    var Followers:List<String> = listOf<String>()
    var MyPosts:List<String> = listOf()
}