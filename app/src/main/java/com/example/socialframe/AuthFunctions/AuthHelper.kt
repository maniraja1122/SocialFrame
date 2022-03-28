package com.example.socialframe.AuthFunctions

import android.app.ProgressDialog
import android.net.Uri
import android.widget.Toast
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.Repository.FirebaseManager
import com.example.socialframe.classes.Post
import com.example.socialframe.classes.User


object AuthHelper {
    var manager = FirebaseManager()
    fun AddUser(user:User){
        manager.db.getReference().child("Users").child(user.key).setValue(user)
    }
    fun CreatePost(post: Post,uri: Uri):String{
        val dialog = ProgressDialog(OpenModel.mycontext)
        dialog.setMessage("Posting.....")
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        var key = manager.db.reference.child("Posts").push().key
        post.key=key!!
        var myref = manager.storage.reference.child("PostImages").child(key!!)
            myref.putFile(uri).addOnSuccessListener {
            myref.downloadUrl.addOnSuccessListener {
                post.imagelink=it.toString()
                manager.db.reference.child("Posts").child(key).setValue(post)
                dialog.dismiss()
                Toast.makeText(OpenModel.mycontext, "Post Published Successfully...", Toast.LENGTH_SHORT).show()
            }
        }
        return key
    }
    fun UpdatePost(post: Post){
        manager.db.reference.child("Posts").child(post.key).setValue(post)
    }
}