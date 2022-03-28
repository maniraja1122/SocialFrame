package com.example.socialframe.AuthFunctions

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.socialframe.Repository.FirebaseManager
import com.example.socialframe.ViewModels.MainViewModel
import com.example.socialframe.classes.Post
import com.example.socialframe.classes.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

object AuthHelper {
    var manager = FirebaseManager()
    fun AddUser(user:User){
        manager.db.getReference().child("Users").child(user.key).setValue(user)
    }
    fun CreatePost(post: Post,uri: Uri):String{
        var key = manager.db.reference.child("Posts").push().key
        var myref = manager.storage.reference.child("PostImages").child(key!!)
            myref.putFile(uri).addOnSuccessListener {
            myref.downloadUrl.addOnSuccessListener {
                post.imagelink=it.toString()
                manager.db.reference.child("Posts").child(key).setValue(post)
            }
        }
        return key
    }
}