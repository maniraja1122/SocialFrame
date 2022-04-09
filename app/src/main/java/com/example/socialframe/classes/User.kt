package com.example.socialframe.classes

import android.app.Notification

class User(var Name:String="NA",var Email:String="NA",var Password:String="NA") {
    var key:String=""
    var Description:String = ""
    var Followed:MutableList<String> = mutableListOf<String>()
    var Followers:MutableList<String> = mutableListOf<String>()
    var MyPosts:MutableList<String> = mutableListOf<String>()
    var MyNotifications:MutableList<Notifications> = mutableListOf<Notifications>()
    var MyPICUrl:String=""
}