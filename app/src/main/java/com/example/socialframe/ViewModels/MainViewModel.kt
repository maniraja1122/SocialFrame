package com.example.socialframe.ViewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.socialframe.AuthFunctions.AuthHelper
import com.example.socialframe.classes.Post
import com.example.socialframe.classes.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.lang.Exception


class MainViewModel:ViewModel() {
    var CurrentUser:MutableLiveData<User> = MutableLiveData()
    var AllPosts:MutableLiveData<List<Post>> = MutableLiveData()
    var mycontext:Context?=null
    //Functions
    fun UpdateUI(){
        UpdateUser()
        UpdateAllPosts()
    }
    fun SetUI(){
        SetUser()
        SetAllPosts()
    }
    fun SetAllPosts(){
        //Single Time
        AuthHelper.manager.db.getReference().child("Posts").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                AllPosts.value=(snapshot.children.mapNotNull {
                    it.getValue(Post::class.java)
                }.toList()).reversed()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun UpdateAllPosts(){
        //Every Time
        AuthHelper.manager.db.getReference().child("Posts").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var arr=snapshot.children.mapNotNull {
                    it.getValue(Post::class.java)
                }.toList().reversed()
                if(arr.size!=AllPosts.value!!.size){
                    AllPosts.value=arr
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun UpdateUser(){
        //Every Time
        AuthHelper.manager.db.getReference().child("Users").child(AuthHelper.manager.auth.uid.toString()).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                CurrentUser.value=snapshot.getValue(User::class.java)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun SetUser(){
        //Single Time
        AuthHelper.manager.db.getReference().child("Users").child(AuthHelper.manager.auth.uid.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                CurrentUser.value=snapshot.getValue(User::class.java)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun ChangePic(s: Uri?){
        var myref = AuthHelper.manager.storage.reference.child("ProfilePhotos").child(CurrentUser.value!!.key)
            myref.putFile(s!!)
                .addOnSuccessListener {
                    myref.downloadUrl.addOnSuccessListener {
                        CurrentUser.value!!.MyPICUrl=it.toString()
                        AuthHelper.AddUser(CurrentUser.value!!)
                    }
                }
    }
    init{
        SetUI()
    }
}
