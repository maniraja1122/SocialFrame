package com.example.socialframe.classes

class Post(var title:String="") {
    var imagelink:String=""
    var Comments:List<Comment> = listOf()
    var Likes:List<User> = listOf()
}