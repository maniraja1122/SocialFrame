package com.example.socialframe.classes

class Post(var title:String="") {
    var imagelink:String=""
    var Comments:MutableList<String> = mutableListOf()
    var Likes:MutableList<String> = mutableListOf()
    var key:String=""
}