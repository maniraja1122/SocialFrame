package com.example.socialframe.classes

class Post(var title:String="",var author:String = "") {
    var imagelink:String=""
    var Comments:MutableList<Comment> = mutableListOf()
    var Likes:MutableList<String> = mutableListOf()
    var key:String=""
}