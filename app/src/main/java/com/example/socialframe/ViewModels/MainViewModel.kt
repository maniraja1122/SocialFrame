package com.example.socialframe.ViewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.socialframe.Activities.OpenModel
import com.example.socialframe.AuthFunctions.AuthHelper
import com.example.socialframe.classes.Post
import com.example.socialframe.classes.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception


class MainViewModel:ViewModel() {
    var CurrentUser:MutableLiveData<User> = MutableLiveData()
    var AllPosts:MutableLiveData<List<Post>> = MutableLiveData()
    var AllUsers:MutableLiveData<List<User>> = MutableLiveData()
    var VisitedUser:MutableLiveData<User> = MutableLiveData()
    var OpenedCommentPost:MutableLiveData<String> =MutableLiveData()
    var mycontext:Context?=null
    //Functions
    fun UpdateUI(){
        UpdateUser()
        UpdateAllPosts()
        UpdateAllUsers()
    }
    fun SetUI(){
        SetUser()
        SetAllUsers()
    }
    fun SetAllUsers(){
        //Single Time
        AuthHelper.manager.db.getReference().child("Users").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                AllUsers.value=(snapshot.children.mapNotNull {
                    it.getValue(User::class.java)
                }.toList()).reversed()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun UpdateAllUsers(){
        //Every Time
        AuthHelper.manager.db.getReference().child("Users").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var arr=snapshot.children.mapNotNull {
                    it.getValue(User::class.java)
                }.toList().reversed()
                if(arr.size!=AllUsers.value!!.size){
                    AllUsers.value=arr
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun SetAllPosts(){
        //Single Time
        AuthHelper.manager.db.getReference().child("Posts").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                AllPosts.value=(snapshot.children.mapNotNull {
                    it.getValue(Post::class.java)
                }.toList()).reversed().filter {
                    CurrentUser.value!!.Followed.contains(it.author)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun UpdateAllPosts(){
        //Every Time
        AuthHelper.manager.db.getReference().child("Posts").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var arr=(snapshot.children.mapNotNull {
                    it.getValue(Post::class.java)
                }.toList()).reversed().filter {
                    CurrentUser.value!!.Followed.contains(it.author)
                }
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
                var updated =snapshot.getValue(User::class.java)
                    CurrentUser.value=updated
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun SetUser(){
        //Single Time
        CoroutineScope(Dispatchers.IO).launch{
            async{
                AuthHelper.manager.db.getReference().child("Users").child(AuthHelper.manager.auth.uid.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        CurrentUser.value=snapshot.getValue(User::class.java)
                        SetAllPosts()
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }

    }
    fun ChangePic(s: Uri?){
        CoroutineScope(Dispatchers.IO).launch{
            async{
                var myref = AuthHelper.manager.storage.reference.child("ProfilePhotos").child(CurrentUser.value!!.key)
                myref.putFile(s!!)
                    .addOnSuccessListener {
                        myref.downloadUrl.addOnSuccessListener {
                            CurrentUser.value!!.MyPICUrl=it.toString()
                            AuthHelper.AddUser(CurrentUser.value!!)
                        }
                    }
            }
        }
    }
    init{
        AllPosts.value= mutableListOf()
        AllUsers.value= mutableListOf()
        CurrentUser.value=User()
        VisitedUser.value=User()
        OpenedCommentPost.value=""
        CoroutineScope(Dispatchers.IO).launch{
            async{
                SetUI()
            }
        }
    }
}
