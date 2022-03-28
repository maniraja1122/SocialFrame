package com.example.socialframe.classes

class User(var Name:String="NA",var Email:String="NA",var Password:String="NA") {
    var key:String=""
    var Description:String = ""
    var Followed:MutableList<String> = mutableListOf<String>()
    var Followers:MutableList<String> = mutableListOf<String>()
    var MyPosts:MutableList<String> = mutableListOf<String>()
    var MyPICUrl:String=""

    fun Equal(other:User):Boolean {
        if (this.Name != other.Name) {
            return false
        } else if (this.Description != other.Description) {
            return false
        } else if (this.Followed.size != other.Followed.size) {
            return false
        } else if (this.Followers.size != other.Followers.size) {
            return false
        } else if (this.MyPosts.size != other.MyPosts.size) {
            return false
        } else if (this.MyPICUrl != other.MyPICUrl) {
            return false
        }
        return true
    }
}